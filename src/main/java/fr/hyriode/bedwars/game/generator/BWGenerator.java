package fr.hyriode.bedwars.game.generator;

import fr.hyriode.api.language.HyriLanguageMessage;
import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.shop.ItemMoney;
import fr.hyriode.bedwars.host.BWGameValues;
import fr.hyriode.hyrame.generator.HyriGenerator;
import fr.hyriode.hyrame.generator.IHyriGeneratorTier;
import fr.hyriode.hyrame.item.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BWGenerator {

    private final String name;
    private final ItemStack header;
    private final Supplier<List<Tier>> tiers;

    public BWGenerator(String name, ItemStack header, Supplier<List<Tier>> tiers) {
        this.name = name;
        this.header = header;
        this.tiers = tiers;
    }

    public String getName() {
        return name;
    }

    public List<Tier> getTiers() {
        return tiers.get();
    }

    public Tier getTier(int tier) {
        return getTiers().get(tier);
    }

    public ItemStack getHeader() {
        return header;
    }

    public static class Tier {

        private final int tier;
        private final String name;
        private final Supplier<BWGenerator> generator;
        private final List<Supplier<Drop>> drops = new ArrayList<>();

        @SafeVarargs
        public Tier(int tier, String name, Supplier<Drop>... drops) {
            this(tier, name, Arrays.asList(drops));
        }

        public Tier(int tier, String name, List<Supplier<Drop>> drops) {
            this.tier = tier;
            this.name = name;
            this.drops.addAll(drops);
            this.generator = () -> HyriBedWars.getGeneratorManager().getGeneratorByName(this.name);
        }

        public int getTier() {
            return tier;
        }

        public String getName() {
            return this.name;
        }

        public String getKeyName() {
            return "generator.tier." + tier + "." + name;
        }

        public List<IHyriGeneratorTier> getTierGenerator() {
            return this.drops.stream().map(drop -> drop.get().getTier()).collect(Collectors.toList());
        }

        public List<Supplier<Drop>> getDrops() {
            return this.drops;
        }

        public Supplier<Drop> getDrop(String dropName) {
            return this.drops.stream().filter(dropSupplier -> dropSupplier.get().getDropName().equals(dropName)).collect(Collectors.toList()).get(0);
        }

        public Map<String, HyriGenerator> getGenerators(HyriBedWars plugin, Location loc) {
            Map<String, HyriGenerator> generators = new HashMap<>();
            BWGenerator originGenerator = this.generator.get();
            int amount = this.getName().equals(StandardGenerator.DIAMOND.name().toLowerCase())
                    ? BWGameValues.DIAMOND_GENERATOR_RATE.get()
                    : this.getName().equals(StandardGenerator.EMERALD.name().toLowerCase())
                    ? BWGameValues.EMERALD_GENERATOR_RATE.get()
                    : 1;
            this.drops.forEach(drop -> {
                Drop originDrop = drop.get();
                HyriGenerator.Builder generator = new HyriGenerator.Builder(plugin, loc, originDrop.getTier())
                        .withItem(originDrop.getDrop().getAsItemStack(amount));
                if(originGenerator.getHeader() != null) {
                    generator.withDefaultHeader(originGenerator.getHeader(), (player) -> ChatColor.AQUA + "" + ChatColor.BOLD + HyriLanguageMessage.get("generator." + originGenerator.getName()).getValue(player))
                            .withDefaultAnimation();
                }
                generators.put(originDrop.getDropName(), generator.build());
            });
            return generators;
        }

        public void removeDrop(String name) {
            this.drops.remove(name);
        }

        @Override
        public String toString() {
            return "Tier{" +
                    "tier=" + tier +
                    ", name='" + name + '\'' +
                    ", generator=" + generator.get() +
                    ", drops=" + drops +
                    '}';
        }

        public static class Drop {

            private final Function<Player, String> name;
            private int spawnLimit;
            private long timeBetweenSpawns;
            private boolean splitting;
            private final ItemMoney drop;

            public Drop(int spawnLimit, long timeBetweenSpawns, boolean splitting, ItemMoney drop) {
                this((__) -> "", spawnLimit, timeBetweenSpawns, splitting, drop);
            }

            public Drop(Function<Player, String> name, int spawnLimit, long timeBetweenSpawns, boolean splitting, ItemMoney drop) {
                this.name = name;
                this.spawnLimit = spawnLimit;
                this.timeBetweenSpawns = timeBetweenSpawns;
                this.splitting = splitting;
                this.drop = drop;
            }

            public void setSpawnLimit(int spawnLimit){
                this.spawnLimit = spawnLimit;
            }

            public void setTimeBetweenSpawns(long timeBetweenSpawns){
                this.timeBetweenSpawns = timeBetweenSpawns;
            }

            public void setSplitting(boolean splitting) {
                this.splitting = splitting;
            }

            public int getSpawnLimit() {
                return spawnLimit;
            }

            public long getTimeBetweenSpawns() {
                return timeBetweenSpawns;
            }

            public boolean isSplitting() {
                return splitting;
            }

            public Function<Player, String> getName() {
                return name;
            }

            public IHyriGeneratorTier getTier() {
                IHyriGeneratorTier.Builder builder = new IHyriGeneratorTier.Builder();
                if(this.splitting) {
                    builder.withSplitting();
                }
                if(this.name != null) {
                    builder.withName(this.name);
                }
                return builder.withSpawnLimit(this.spawnLimit).withTimeBetweenSpawns(this.timeBetweenSpawns).build();
            }

            public ItemMoney getDrop() {
                return this.drop;
            }

            public String getDropName() {
                return this.drop.getName();
            }

            @Override
            public String toString() {
                return "Drop{" +
                        "name=" + getDropName() +
                        ", spawnLimit=" + spawnLimit +
                        ", timeBetweenSpawns=" + timeBetweenSpawns +
                        ", splitting=" + splitting +
                        ", drop=" + drop +
                        '}';
            }
        }

    }

}

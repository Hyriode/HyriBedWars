package fr.hyriode.bedwars.game.generator;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.language.HyriLanguageMessage;
import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.BWGame;
import fr.hyriode.bedwars.game.shop.ItemMoney;
import fr.hyriode.bedwars.game.type.BWGameType;
import fr.hyriode.hyrame.HyrameLoader;
import fr.hyriode.hyrame.generator.HyriGenerator;
import fr.hyriode.hyrame.generator.IHyriGeneratorTier;
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
        private final Map<String, Supplier<Drop>> drops = new HashMap<>();

        public Tier(int tier, String name, Drop... drops) {
            this(tier, name, Arrays.asList(drops));
        }

        public Tier(int tier, String name, List<Drop> drops) {
            this.tier = tier;
            this.name = name;
            drops.forEach(drop -> this.drops.put(drop.getName(), () -> drop));
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
            return this.drops.values().stream().map(drop -> drop.get().getTier()).collect(Collectors.toList());
        }

        public Map<String, Supplier<Drop>> getDrops() {
            return this.drops;
        }

        public Map<String, HyriGenerator> getGenerators(HyriBedWars plugin, Location loc) {
            Map<String, HyriGenerator> generators = new HashMap<>();
            BWGenerator originGenerator = this.generator.get();
            System.out.println(this.drops);
            System.out.println(originGenerator.getTier(this.getTier()).getDrops());
            this.drops.forEach((name, drop) -> {
                Drop originDrop = drop.get();
                HyriGenerator.Builder generator = new HyriGenerator.Builder(plugin, loc, originDrop.getTier())
                        .withItem(originDrop.getDrop().getAsItemStack());
                if(originGenerator.getHeader() != null) {
                    generator.withDefaultHeader(originGenerator.getHeader(), (player) -> ChatColor.AQUA + "" + ChatColor.BOLD + HyriLanguageMessage.get("generator.diamond").getValue(player))
                            .withDefaultAnimation();
                }
                generators.put(originDrop.getName(), generator.build());
            });
            return generators;
        }

        public void removeDrop(String name) {
            this.drops.remove(name);
        }

        public static class Drop {

            private final Function<Player, String> name;
            private int spawnLimit;
            private long timeBetweenSpawns;
            private boolean splitting;
            private final ItemMoney drop;

            public Drop(int spawnLimit, long timeBetweenSpawns, boolean splitting, ItemMoney drop) {
                this(null, spawnLimit, timeBetweenSpawns, splitting, drop);
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

            public String getName() {
                return this.drop.getName();
            }
        }

    }

}

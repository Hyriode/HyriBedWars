package fr.hyriode.bedwars.game.generator;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.bedwars.config.BWConfiguration;
import fr.hyriode.bedwars.game.shop.ItemMoney;
import fr.hyriode.bedwars.game.type.BWGameType;
import fr.hyriode.bedwars.host.BWForgeValues;
import fr.hyriode.hyggdrasil.api.server.HyggServer;
import fr.hyriode.hyrame.generator.IHyriGeneratorTier;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class GeneratorManager {

    private final List<BWGenerator> generators = new ArrayList<>();

    public static final String FORGE = "forge";
    public static final String DIAMOND = "diamond";
    public static final String EMERALD = "emerald";
    public static final String IRON = ItemMoney.IRON.getName();
    public static final String GOLD = ItemMoney.GOLD.getName();

    public GeneratorManager(BWConfiguration config) {
        boolean isHost = HyriAPI.get().getServer().getAccessibility() == HyggServer.Accessibility.HOST;

        this.add(FORGE, () -> {
            if(isHost) {
                return Arrays.asList(
                        new BWGenerator.Tier(0, FORGE,
                                this.getDrop(FORGE, 0, ItemMoney.IRON),
                                this.getDrop(FORGE, 0, ItemMoney.GOLD)
                        ), new BWGenerator.Tier(1, FORGE,
                                this.getDrop(FORGE, 1, ItemMoney.IRON),
                                this.getDrop(FORGE, 1, ItemMoney.GOLD)
                        ), new BWGenerator.Tier(2, FORGE,
                                this.getDrop(FORGE, 2, ItemMoney.IRON),
                                this.getDrop(FORGE, 2, ItemMoney.GOLD),
                                this.getDrop(FORGE, 2, ItemMoney.EMERALD)
                        ), new BWGenerator.Tier(3, FORGE,
                                this.getDrop(FORGE, 3, ItemMoney.IRON),
                                this.getDrop(FORGE, 3, ItemMoney.GOLD),
                                this.getDrop(FORGE, 3, ItemMoney.EMERALD)
                        )
                );
            }
            return config.getGeneratorsBase().stream().map(BWConfiguration.Generator::toTier).collect(Collectors.toList());
        });

        this.add(DIAMOND, new ItemStack(Material.DIAMOND_BLOCK), () -> {
            if(isHost) {
                return Arrays.asList(
                        new BWGenerator.Tier(0, DIAMOND, this.getDrop(__ -> "I", DIAMOND, 0, ItemMoney.DIAMOND)),
                        new BWGenerator.Tier(1, DIAMOND, this.getDrop(__ -> "I", DIAMOND, 1, ItemMoney.DIAMOND)),
                        new BWGenerator.Tier(2, DIAMOND, this.getDrop(__ -> "I", DIAMOND, 2, ItemMoney.DIAMOND))
                );
            }
            return config.getGeneratorsDiamond().stream().map(BWConfiguration.Generator::toTier).collect(Collectors.toList());
        });

        this.add(EMERALD, new ItemStack(Material.EMERALD_BLOCK), () -> {
            if(isHost) {
                return Arrays.asList(
                        new BWGenerator.Tier(0, EMERALD, this.getDrop(__ -> "I", EMERALD, 0, ItemMoney.EMERALD)),
                        new BWGenerator.Tier(1, EMERALD, this.getDrop(__ -> "I", EMERALD, 1, ItemMoney.EMERALD)),
                        new BWGenerator.Tier(2, EMERALD, this.getDrop(__ -> "I", EMERALD, 2, ItemMoney.EMERALD))
                );
            }
            return config.getGeneratorsEmerald().stream().map(BWConfiguration.Generator::toTier).collect(Collectors.toList());
        });
    }

    private void add(String name, ItemStack header, Supplier<List<BWGenerator.Tier>> tiers) {
        this.generators.add(new BWGenerator(name, header, tiers));
    }

    private void add(String name, Supplier<List<BWGenerator.Tier>> tiers) {
        this.add(name, null, tiers);
    }

    public BWGenerator.Tier.Drop getDrop(String generatorName, int tier, ItemMoney drop) {
        String dropName = drop.getName();
        return new BWGenerator.Tier.Drop(BWForgeValues.getSpawnLimit(generatorName, tier, dropName), BWForgeValues.getSpawnBetween(generatorName, tier, dropName), BWForgeValues.getSplitting(generatorName, tier, dropName), drop);
    }

    public BWGenerator.Tier.Drop getDrop(Function<Player, String> name, String generatorName, int tier, ItemMoney drop) {
        String dropName = drop.getName();
        return new BWGenerator.Tier.Drop(name, BWForgeValues.getSpawnLimit(generatorName, tier, dropName), BWForgeValues.getSpawnBetween(generatorName, tier, dropName), BWForgeValues.getSplitting(generatorName, tier, dropName), drop);
    }

    public List<BWGenerator> getGenerators() {
        return generators;
    }

    public BWGenerator getGeneratorByName(String name) {
        return this.generators.stream().filter(g -> g.getName().equals(name)).findFirst().orElse(null);
    }

    private IHyriGeneratorTier createGenerator(boolean splitting, int spawnLimit, int timeBetweenSpawns) {
        return this.createGenerator(null, splitting, spawnLimit, timeBetweenSpawns);
    }

    private IHyriGeneratorTier createGenerator(String name, boolean splitting, int spawnLimit, int timeBetweenSpawns) {
        IHyriGeneratorTier.Builder builder = new IHyriGeneratorTier.Builder();
        if(splitting){
            builder.withSplitting();
        }
        if(name != null) {
            builder.withName(player -> name);
        }
        return builder.withSpawnLimit(spawnLimit).withTimeBetweenSpawns(timeBetweenSpawns).build();
    }
}

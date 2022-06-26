package fr.hyriode.bedwars.game.generator;

import fr.hyriode.hyrame.generator.IHyriGeneratorTier;
import org.bukkit.entity.Player;

import java.util.function.Function;

public enum BWDiamondGenerator implements IHyriGeneratorTier {
    DIAMOND_TIER_I("I", 30*20),
    DIAMOND_TIER_II("II", 23*20),
    DIAMOND_TIER_III("III", 12*20),
    ;

    private final String name;
    private final long timeBetweenSpawns;

    BWDiamondGenerator(String name, long timeBetweenSpawns){
        this.name = name;
        this.timeBetweenSpawns = timeBetweenSpawns;
    }

    @Override
    public Function<Player, String> getName() {
        return (player) -> this.name;
    }

    @Override
    public int getSpawnLimit() {
        return 6;
    }

    @Override
    public long getTimeBetweenSpawns() {
        return this.timeBetweenSpawns;
    }

    @Override
    public boolean isSplitting() {
        return false;
    }
}

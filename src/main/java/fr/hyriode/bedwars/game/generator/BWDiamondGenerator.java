package fr.hyriode.bedwars.game.generator;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.hyrame.generator.IHyriGeneratorTier;
import org.bukkit.entity.Player;

import java.util.function.Function;

public enum BWDiamondGenerator implements IHyriGeneratorTier {
    DIAMOND_TIER_I("I", false, 8, 160),
    DIAMOND_TIER_II("II", false, 8, 200),
    DIAMOND_TIER_III("III", false, 8, 260),

    ;

    private final String name;
    private final boolean splitting;
    private final int spawnLimit;
    private final long timeBetweenSpawns;

    BWDiamondGenerator(String name, boolean splitting, int spawnLimit, long timeBetweenSpawns){
        this.name = name;
        this.splitting = splitting;
        this.spawnLimit = spawnLimit;
        this.timeBetweenSpawns = timeBetweenSpawns;
    }

    @Override
    public Function<Player, String> getName() {
        return (player) -> this.name;
    }

    @Override
    public int getSpawnLimit() {
        return this.spawnLimit;
    }

    @Override
    public long getTimeBetweenSpawns() {
        return this.timeBetweenSpawns;
    }

    @Override
    public boolean isSplitting() {
        return this.splitting;
    }
}

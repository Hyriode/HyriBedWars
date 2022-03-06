package fr.hyriode.bedwars.game.generator;

import fr.hyriode.hyrame.generator.IHyriGeneratorTier;
import org.bukkit.entity.Player;

import java.util.function.Function;

public enum BWBaseIronGenerator implements IHyriGeneratorTier {

    BASE_I(true, 48, 30),
    BASE_II(true, 48, 30),
    BASE_III(true, 48, 30),
    BASE_IV(true, 48, 30),

    ;

    private final boolean splitting;
    private final int spawnLimit;
    private final long timeBetweenSpawns;

    BWBaseIronGenerator(boolean splitting, int spawnLimit, long timeBetweenSpawns){
        this.splitting = splitting;
        this.spawnLimit = spawnLimit;
        this.timeBetweenSpawns = timeBetweenSpawns;
    }

    @Override
    public Function<Player, String> getName() {
        return null;
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

package fr.hyriode.bedwars.game.generator;

import fr.hyriode.bedwars.game.type.BWGameType;
import fr.hyriode.hyrame.generator.IHyriGeneratorTier;
import org.bukkit.entity.Player;

import java.util.function.Function;

public enum BWBaseGenerator implements IHyriGeneratorTier {
    TIER_IRON_SOLO_I(48, 30),
    TIER_IRON_SOLO_II(48, 23),
    TIER_IRON_SOLO_III(48, 18),
    TIER_IRON_SOLO_IV(48, 15),

    TIER_IRON_TRIO_I(48, 30),
    TIER_IRON_TRIO_II(48, 23),
    TIER_IRON_TRIO_III(48, 18),
    TIER_IRON_TRIO_IV(48, 15),

    TIER_IRON_ONE_I(48, 30),
    TIER_IRON_ONE_II(48, 23),
    TIER_IRON_ONE_III(48, 18),
    TIER_IRON_ONE_IV(48, 15),

    //gold
    TIER_GOLD_SOLO_I(16, 100), // 5s
    TIER_GOLD_SOLO_II(16, 60), // 3s
    TIER_GOLD_SOLO_III(16, 40),
    TIER_GOLD_SOLO_IV(16, 30),

    TIER_GOLD_TRIO_I(16, 100),
    TIER_GOLD_TRIO_II(16, 60),
    TIER_GOLD_TRIO_III(16, 40),
    TIER_GOLD_TRIO_IV(16, 30),

    TIER_GOLD_ONE_I(16, 100),
    TIER_GOLD_ONE_II(16, 60),
    TIER_GOLD_ONE_III(16, 40),
    TIER_GOLD_ONE_IV(16, 30),

    //emerald
    TIER_EMERALD(4, 20*25),
    ;

    private final long timeBetweenSpawns;
    private final int spawnLimit;

    BWBaseGenerator(int spawnLimit, long timeBetweenSpawns){
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
        return true;
    }

    public static BWBaseGenerator get(BWGameType gameType, Type type, int tier){
        if (type == Type.GOLD) {
            switch (gameType) {
                case SOLO:
                case DOUBLES:
                    switch (tier) {
                        case 2: return TIER_GOLD_SOLO_II;
                        case 3: return TIER_GOLD_SOLO_III;
                        case 4: return TIER_GOLD_SOLO_IV;
                    }
                    return TIER_GOLD_SOLO_I;
                case TRIO:
                case SQUAD:
                    switch (tier) {
                        case 2: return TIER_GOLD_TRIO_II;
                        case 3: return TIER_GOLD_TRIO_III;
                        case 4: return TIER_GOLD_TRIO_IV;
                    }
                    return TIER_GOLD_TRIO_I;
                case ONE_ONE:
                    switch (tier) {
                        case 2: return TIER_GOLD_ONE_II;
                        case 3: return TIER_GOLD_ONE_III;
                        case 4: return TIER_GOLD_ONE_IV;
                    }
            }
            return TIER_IRON_ONE_I;
        }
        switch (gameType) {
            case SOLO:
            case DOUBLES:
                switch (tier) {
                    case 2: return TIER_IRON_SOLO_II;
                    case 3: return TIER_IRON_SOLO_III;
                    case 4: return TIER_IRON_SOLO_IV;
                }
                return TIER_IRON_SOLO_I;
            case TRIO:
            case SQUAD:
                switch (tier) {
                    case 2: return TIER_IRON_TRIO_II;
                    case 3: return TIER_IRON_TRIO_III;
                    case 4: return TIER_IRON_TRIO_IV;
                }
                return TIER_IRON_TRIO_I;
            case ONE_ONE:
                switch (tier) {
                    case 2: return TIER_IRON_ONE_II;
                    case 3: return TIER_IRON_ONE_III;
                    case 4: return TIER_IRON_ONE_IV;
                }
        }
        return TIER_IRON_ONE_I;
    }

    public enum Type {
        IRON, GOLD
    }
}

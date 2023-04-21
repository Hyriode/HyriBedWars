package fr.hyriode.bedwars.host;

import fr.hyriode.bedwars.game.type.BWGameType;
import fr.hyriode.hyrame.game.util.value.HostValueModifier;
import fr.hyriode.hyrame.game.util.value.ValueProvider;

public class BWUpgradeValues {

    public static final ValueProvider<Boolean> UPGRADE_ENABLED = new ValueProvider<>(true).addModifiers(new HostValueModifier<>(1, Boolean.class, "upgrades-enabled"));
    public static final ValueProvider<Integer> UPGRADE_PRICE = new ValueProvider<>(100).addModifiers(new HostValueModifier<>(1, Integer.class, "upgrades-price"));

    public static final ValueProvider<Boolean> SHARPNESS_ENABLED = new ValueProvider<>(true).addModifiers(new HostValueModifier<>(1, Boolean.class, "upgrade-sharpness-enabled"));
    public static ValueProvider<Integer> SHARPNESS_PRICE_TIER_0;

    public static final ValueProvider<Boolean> PROTECTION_ENABLED = new ValueProvider<>(true).addModifiers(new HostValueModifier<>(1, Boolean.class, "upgrade-protection-enabled"));
    public static ValueProvider<Integer> PROTECTION_PRICE_TIER_0;
    public static ValueProvider<Integer> PROTECTION_PRICE_TIER_1;
    public static ValueProvider<Integer> PROTECTION_PRICE_TIER_2;
    public static ValueProvider<Integer> PROTECTION_PRICE_TIER_3;

    public static final ValueProvider<Boolean> MINER_ENABLED = new ValueProvider<>(true).addModifiers(new HostValueModifier<>(1, Boolean.class, "upgrade-miner-enabled"));
    public static ValueProvider<Integer> MINER_PRICE_TIER_0;
    public static ValueProvider<Integer> MINER_PRICE_TIER_1;

    public static final ValueProvider<Boolean> FORGE_ENABLED = new ValueProvider<>(true).addModifiers(new HostValueModifier<>(1, Boolean.class, "upgrade-forge-enabled"));
    public static ValueProvider<Integer> FORGE_PRICE_TIER_0;
    public static ValueProvider<Integer> FORGE_PRICE_TIER_1;
    public static ValueProvider<Integer> FORGE_PRICE_TIER_2;
    public static ValueProvider<Integer> FORGE_PRICE_TIER_3;

    public static final ValueProvider<Boolean> HEAL_POOL_ENABLED = new ValueProvider<>(true).addModifiers(new HostValueModifier<>(1, Boolean.class, "upgrade-heal-pool-enabled"));
    public static ValueProvider<Integer> HEAL_POOL_PRICE_TIER_0;

    public static final ValueProvider<Boolean> DRAGON_BUFF_ENABLED = new ValueProvider<>(true).addModifiers(new HostValueModifier<>(1, Boolean.class, "upgrade-dragon-buff-enabled"));
    public static ValueProvider<Integer> DRAGON_BUFF_PRICE_TIER_0;

    public static void init(BWGameType gameType) {
        SHARPNESS_PRICE_TIER_0 = new ValueProvider<>(gameType.isForTrioSquad() ? 8 : 4).addModifiers(new HostValueModifier<>(1, Integer.class, "upgrade-sharpness-price-tier-0"));

        PROTECTION_PRICE_TIER_0 = new ValueProvider<>(gameType.isForTrioSquad() ? 5 : 2).addModifiers(new HostValueModifier<>(1, Integer.class, "upgrade-protection-price-tier-0"));
        PROTECTION_PRICE_TIER_1 = new ValueProvider<>(gameType.isForTrioSquad() ? 10 : 4).addModifiers(new HostValueModifier<>(1, Integer.class, "upgrade-protection-price-tier-1"));
        PROTECTION_PRICE_TIER_2 = new ValueProvider<>(gameType.isForTrioSquad() ? 20 : 8).addModifiers(new HostValueModifier<>(1, Integer.class, "upgrade-protection-price-tier-2"));
        PROTECTION_PRICE_TIER_3 = new ValueProvider<>(gameType.isForTrioSquad() ? 30 : 16).addModifiers(new HostValueModifier<>(1, Integer.class, "upgrade-protection-price-tier-3"));

        MINER_PRICE_TIER_0 = new ValueProvider<>(gameType.isForTrioSquad() ? 4 : 2).addModifiers(new HostValueModifier<>(1, Integer.class, "upgrade-miner-price-tier-0"));
        MINER_PRICE_TIER_1 = new ValueProvider<>(gameType.isForTrioSquad() ? 6 : 4).addModifiers(new HostValueModifier<>(1, Integer.class, "upgrade-miner-price-tier-1"));

        FORGE_PRICE_TIER_0 = new ValueProvider<>(gameType.isForTrioSquad() ? 4 : 2).addModifiers(new HostValueModifier<>(1, Integer.class, "upgrade-forge-price-tier-0"));
        FORGE_PRICE_TIER_1 = new ValueProvider<>(gameType.isForTrioSquad() ? 8 : 4).addModifiers(new HostValueModifier<>(1, Integer.class, "upgrade-forge-price-tier-1"));
        FORGE_PRICE_TIER_2 = new ValueProvider<>(gameType.isForTrioSquad() ? 12 : 6).addModifiers(new HostValueModifier<>(1, Integer.class, "upgrade-forge-price-tier-2"));
        FORGE_PRICE_TIER_3 = new ValueProvider<>(gameType.isForTrioSquad() ? 16 : 8).addModifiers(new HostValueModifier<>(1, Integer.class, "upgrade-forge-price-tier-3"));

        HEAL_POOL_PRICE_TIER_0 = new ValueProvider<>(gameType.isForTrioSquad() ? 3 : 1).addModifiers(new HostValueModifier<>(1, Integer.class, "upgrade-heal-pool-price-tier-0"));

        DRAGON_BUFF_PRICE_TIER_0 = new ValueProvider<>(gameType.isForTrioSquad() ? 5 : 5).addModifiers(new HostValueModifier<>(1, Integer.class, "upgrade-dragon-buff-price-tier-0"));
    }
}

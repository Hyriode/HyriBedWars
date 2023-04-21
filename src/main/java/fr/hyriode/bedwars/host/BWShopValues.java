package fr.hyriode.bedwars.host;

import fr.hyriode.hyrame.game.util.value.HostValueModifier;
import fr.hyriode.hyrame.game.util.value.ValueProvider;

public class BWShopValues {

    public static final ValueProvider<Integer> PRICE_GLOBAL = new ValueProvider<>(100)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "shop-prices"));
    public static final ValueProvider<Boolean> POTIONS_ENABLE = new ValueProvider<>(true)
            .addModifiers(new HostValueModifier<>(1, Boolean.class, "shop-potion-enabled"));

    public static final ValueProvider<Integer> WOOL_PRICE = new ValueProvider<>(4)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "shop-item-wool-price"));
    public static final ValueProvider<Integer> HARD_CLAY_PRICE = new ValueProvider<>(12)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "shop-item-hard-clay-price"));
    public static final ValueProvider<Integer> GLASS_PRICE = new ValueProvider<>(12)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "shop-item-glass-price"));
    public static final ValueProvider<Integer> END_STONE_PRICE = new ValueProvider<>(24)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "shop-item-end-stone-price"));
    public static final ValueProvider<Integer> LADDER_PRICE = new ValueProvider<>(4)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "shop-item-ladder-price"));
    public static final ValueProvider<Integer> WOOD_PRICE = new ValueProvider<>(4)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "shop-item-wood-price"));
    public static final ValueProvider<Integer> OBSIDIAN_PRICE = new ValueProvider<>(4)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "shop-item-obsidian-price"));

    public static final ValueProvider<Integer> STONE_SWORD_PRICE = new ValueProvider<>(10)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "shop-item-stone-sword-price"));
    public static final ValueProvider<Integer> IRON_SWORD_PRICE = new ValueProvider<>(7)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "shop-item-iron-sword-price"));
    public static final ValueProvider<Integer> DIAMOND_SWORD_PRICE = new ValueProvider<>(4)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "shop-item-diamond-sword-price"));
    public static final ValueProvider<Integer> KNOCKBACK_STICK_PRICE = new ValueProvider<>(7)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "shop-item-stick-price"));

    public static final ValueProvider<Integer> CHAINMAIL_ARMOR_PRICE = new ValueProvider<>(30)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "shop-item-chainmail-armor-price"));
    public static final ValueProvider<Integer> IRON_ARMOR_PRICE = new ValueProvider<>(12)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "shop-item-iron-armor-price"));
    public static final ValueProvider<Integer> DIAMOND_ARMOR_PRICE = new ValueProvider<>(6)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "shop-item-diamond-armor-price"));

    public static final ValueProvider<Integer> SHEARS_PRICE = new ValueProvider<>(20)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "shop-item-shears-price"));
    public static final ValueProvider<Integer> PICKAXE_0_PRICE = new ValueProvider<>(10)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "shop-item-pickaxe-0-price"));
    public static final ValueProvider<Integer> PICKAXE_1_PRICE = new ValueProvider<>(10)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "shop-item-pickaxe-1-price"));
    public static final ValueProvider<Integer> PICKAXE_2_PRICE = new ValueProvider<>(3)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "shop-item-pickaxe-2-price"));
    public static final ValueProvider<Integer> PICKAXE_3_PRICE = new ValueProvider<>(6)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "shop-item-pickaxe-3-price"));
    public static final ValueProvider<Integer> AXE_0_PRICE = new ValueProvider<>(10)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "shop-item-axe-0-price"));
    public static final ValueProvider<Integer> AXE_1_PRICE = new ValueProvider<>(10)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "shop-item-axe-1-price"));
    public static final ValueProvider<Integer> AXE_2_PRICE = new ValueProvider<>(3)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "shop-item-axe-2-price"));
    public static final ValueProvider<Integer> AXE_3_PRICE = new ValueProvider<>(6)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "shop-item-axe-3-price"));

    public static final ValueProvider<Integer> ARROW_PRICE = new ValueProvider<>(2)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "shop-item-arrow-price"));
    public static final ValueProvider<Integer> BOW_PRICE = new ValueProvider<>(12)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "shop-item-bow-price"));
    public static final ValueProvider<Integer> BOW_POWER_PRICE = new ValueProvider<>(20)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "shop-item-bow-power-price"));
    public static final ValueProvider<Integer> BOW_PUNCH_PRICE = new ValueProvider<>(6)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "shop-item-bow-punch-price"));

    public static final ValueProvider<Integer> POTION_SPEED_PRICE = new ValueProvider<>(1)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "shop-item-potion-speed-price"));
    public static final ValueProvider<Integer> POTION_JUMP_PRICE = new ValueProvider<>(1)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "shop-item-potion-jump-price"));
    public static final ValueProvider<Integer> POTION_INVISIBILITY_PRICE = new ValueProvider<>(2)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "shop-item-potion-invisibility-price"));

    public static final ValueProvider<Integer> GOLDEN_APPLE_PRICE = new ValueProvider<>(3)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "shop-item-golden-apple-price"));
    public static final ValueProvider<Integer> BEDBUG_PRICE = new ValueProvider<>(30)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "shop-item-bedbug-price"));
    public static final ValueProvider<Integer> DREAM_DEFENDER_PRICE = new ValueProvider<>(120)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "shop-item-dream-defender-price"));
    public static final ValueProvider<Integer> FIREBALL_PRICE = new ValueProvider<>(40)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "shop-item-fireball-price"));
    public static final ValueProvider<Integer> TNT_PRICE = new ValueProvider<>(4)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "shop-item-tnt-price"));
    public static final ValueProvider<Integer> ENDER_PEARL_PRICE = new ValueProvider<>(4)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "shop-item-ender-pearl-price"));
    public static final ValueProvider<Integer> WATER_BUCKET_PRICE = new ValueProvider<>(3)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "shop-item-water-bucket-price"));
    public static final ValueProvider<Integer> BRIDGE_EGG_PRICE = new ValueProvider<>(1)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "shop-item-bridge-egg-price"));
    public static final ValueProvider<Integer> MAGIC_MILK_PRICE = new ValueProvider<>(4)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "shop-item-magic-milk-price"));
    public static final ValueProvider<Integer> SPONGE_PRICE = new ValueProvider<>(3)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "shop-item-sponge-price"));
    public static final ValueProvider<Integer> COMPACT_TOWER_PRICE = new ValueProvider<>(24)
            .addModifiers(new HostValueModifier<>(1, Integer.class, "shop-item-compact-tower-price"));

    public static final ValueProvider<Boolean> WOOL_ENABLE = new ValueProvider<>(true)
            .addModifiers(new HostValueModifier<>(1, Boolean.class, "shop-item-wool-enabled"));
    public static final ValueProvider<Boolean> HARD_CLAY_ENABLE = new ValueProvider<>(true)
            .addModifiers(new HostValueModifier<>(1, Boolean.class, "shop-item-hard-clay-enabled"));
    public static final ValueProvider<Boolean> GLASS_ENABLE = new ValueProvider<>(true)
            .addModifiers(new HostValueModifier<>(1, Boolean.class, "shop-item-glass-enabled"));
    public static final ValueProvider<Boolean> END_STONE_ENABLE = new ValueProvider<>(true)
            .addModifiers(new HostValueModifier<>(1, Boolean.class, "shop-item-end-stone-enabled"));
    public static final ValueProvider<Boolean> LADDER_ENABLE = new ValueProvider<>(true)
            .addModifiers(new HostValueModifier<>(1, Boolean.class, "shop-item-ladder-enabled"));
    public static final ValueProvider<Boolean> WOOD_ENABLE = new ValueProvider<>(true)
            .addModifiers(new HostValueModifier<>(1, Boolean.class, "shop-item-wood-enabled"));
    public static final ValueProvider<Boolean> OBSIDIAN_ENABLE = new ValueProvider<>(true)
            .addModifiers(new HostValueModifier<>(1, Boolean.class, "shop-item-obsidian-enabled"));

    public static final ValueProvider<Boolean> STONE_SWORD_ENABLE = new ValueProvider<>(true)
            .addModifiers(new HostValueModifier<>(1, Boolean.class, "shop-item-stone-sword-enabled"));
    public static final ValueProvider<Boolean> IRON_SWORD_ENABLE = new ValueProvider<>(true)
            .addModifiers(new HostValueModifier<>(1, Boolean.class, "shop-item-iron-sword-enabled"));
    public static final ValueProvider<Boolean> DIAMOND_SWORD_ENABLE = new ValueProvider<>(true)
            .addModifiers(new HostValueModifier<>(1, Boolean.class, "shop-item-diamond-sword-enabled"));
    public static final ValueProvider<Boolean> KNOCKBACK_STICK_ENABLE = new ValueProvider<>(true)
            .addModifiers(new HostValueModifier<>(1, Boolean.class, "shop-item-stick-enabled"));

    public static final ValueProvider<Boolean> CHAINMAIL_ARMOR_ENABLE = new ValueProvider<>(true)
            .addModifiers(new HostValueModifier<>(1, Boolean.class, "shop-item-chainmail-armor-enabled"));
    public static final ValueProvider<Boolean> IRON_ARMOR_ENABLE = new ValueProvider<>(true)
            .addModifiers(new HostValueModifier<>(1, Boolean.class, "shop-item-iron-armor-enabled"));
    public static final ValueProvider<Boolean> DIAMOND_ARMOR_ENABLE = new ValueProvider<>(true)
            .addModifiers(new HostValueModifier<>(1, Boolean.class, "shop-item-diamond-armor-enabled"));

    public static final ValueProvider<Boolean> SHEARS_ENABLE = new ValueProvider<>(true)
            .addModifiers(new HostValueModifier<>(1, Boolean.class, "shop-item-shears-enabled"));
    public static final ValueProvider<Boolean> PICKAXE_ENABLE = new ValueProvider<>(true)
            .addModifiers(new HostValueModifier<>(1, Boolean.class, "shop-item-pickaxe-enabled"));
    public static final ValueProvider<Boolean> AXE_ENABLE = new ValueProvider<>(true)
            .addModifiers(new HostValueModifier<>(1, Boolean.class, "shop-item-axe-enabled"));

    public static final ValueProvider<Boolean> ARROW_ENABLE = new ValueProvider<>(true)
            .addModifiers(new HostValueModifier<>(1, Boolean.class, "shop-item-arrow-enabled"));
    public static final ValueProvider<Boolean> BOW_ENABLE = new ValueProvider<>(true)
            .addModifiers(new HostValueModifier<>(1, Boolean.class, "shop-item-bow-enabled"));
    public static final ValueProvider<Boolean> BOW_POWER_ENABLE = new ValueProvider<>(true)
            .addModifiers(new HostValueModifier<>(1, Boolean.class, "shop-item-bow-power-enabled"));
    public static final ValueProvider<Boolean> BOW_PUNCH_ENABLE = new ValueProvider<>(true)
            .addModifiers(new HostValueModifier<>(1, Boolean.class, "shop-item-bow-punch-enabled"));

    public static final ValueProvider<Boolean> POTION_SPEED_ENABLE = new ValueProvider<>(true)
            .addModifiers(new HostValueModifier<>(1, Boolean.class, "shop-item-potion-speed-enabled"));
    public static final ValueProvider<Boolean> POTION_JUMP_ENABLE = new ValueProvider<>(true)
            .addModifiers(new HostValueModifier<>(1, Boolean.class, "shop-item-potion-jump-enabled"));
    public static final ValueProvider<Boolean> POTION_INVISIBILITY_ENABLE = new ValueProvider<>(true)
            .addModifiers(new HostValueModifier<>(1, Boolean.class, "shop-item-potion-invisibility-enabled"));

    public static final ValueProvider<Boolean> GOLDEN_APPLE_ENABLE = new ValueProvider<>(true)
            .addModifiers(new HostValueModifier<>(1, Boolean.class, "shop-item-golden-apple-enabled"));
    public static final ValueProvider<Boolean> BEDBUG_ENABLE = new ValueProvider<>(true)
            .addModifiers(new HostValueModifier<>(1, Boolean.class, "shop-item-bedbug-enabled"));
    public static final ValueProvider<Boolean> DREAM_DEFENDER_ENABLE = new ValueProvider<>(true)
            .addModifiers(new HostValueModifier<>(1, Boolean.class, "shop-item-dream-defender-enabled"));
    public static final ValueProvider<Boolean> FIREBALL_ENABLE = new ValueProvider<>(true)
            .addModifiers(new HostValueModifier<>(1, Boolean.class, "shop-item-fireball-enabled"));
    public static final ValueProvider<Boolean> TNT_ENABLE = new ValueProvider<>(true)
            .addModifiers(new HostValueModifier<>(1, Boolean.class, "shop-item-tnt-enabled"));
    public static final ValueProvider<Boolean> ENDER_PEARL_ENABLE = new ValueProvider<>(true)
            .addModifiers(new HostValueModifier<>(1, Boolean.class, "shop-item-ender-pearl-enabled"));
    public static final ValueProvider<Boolean> WATER_BUCKET_ENABLE = new ValueProvider<>(true)
            .addModifiers(new HostValueModifier<>(1, Boolean.class, "shop-item-water-bucket-enabled"));
    public static final ValueProvider<Boolean> BRIDGE_EGG_ENABLE = new ValueProvider<>(true)
            .addModifiers(new HostValueModifier<>(1, Boolean.class, "shop-item-bridge-egg-enabled"));
    public static final ValueProvider<Boolean> MAGIC_MILK_ENABLE = new ValueProvider<>(true)
            .addModifiers(new HostValueModifier<>(1, Boolean.class, "shop-item-magic-milk-enabled"));
    public static final ValueProvider<Boolean> SPONGE_ENABLE = new ValueProvider<>(true)
            .addModifiers(new HostValueModifier<>(1, Boolean.class, "shop-item-sponge-enabled"));
    public static final ValueProvider<Boolean> COMPACT_TOWER_ENABLE = new ValueProvider<>(true)
            .addModifiers(new HostValueModifier<>(1, Boolean.class, "shop-item-compact-tower-enabled"));

}

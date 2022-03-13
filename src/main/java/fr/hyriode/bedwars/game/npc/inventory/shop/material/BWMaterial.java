package fr.hyriode.bedwars.game.npc.inventory.shop.material;

import fr.hyriode.bedwars.game.npc.inventory.shop.*;
import fr.hyriode.bedwars.game.npc.inventory.shop.utility.BridgeEgg;
import fr.hyriode.hyrame.item.HyriItem;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.bedwars.game.BWGameOre;
import fr.hyriode.bedwars.game.npc.inventory.shop.material.upgradable.ArmorBW;
import fr.hyriode.bedwars.game.npc.inventory.shop.material.upgradable.ItemBWAxe;
import fr.hyriode.bedwars.game.npc.inventory.shop.material.upgradable.ItemBWPickaxe;
import fr.hyriode.bedwars.game.npc.inventory.shop.utility.DreamDefenderEgg;
import fr.hyriode.bedwars.game.npc.inventory.shop.utility.FireballBW;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

import java.util.Arrays;
import java.util.List;

public enum BWMaterial{

    WOOL(BWShopCategory.BLOCKS, "wool", Material.WOOL, 16, false, new OreStack(BWGameOre.IRON, 4)),
    HARD_CLAY(BWShopCategory.BLOCKS, "hard_clay", Material.HARD_CLAY, 16, false, new OreStack(BWGameOre.IRON, 12)),
    GLASS(BWShopCategory.BLOCKS, "glass", Material.GLASS, 4, false, new OreStack(BWGameOre.IRON, 12)),
    END_STONE(BWShopCategory.BLOCKS, "end_stone", Material.ENDER_STONE, 12, false, new OreStack(BWGameOre.IRON, 24)),
    LADDER(BWShopCategory.BLOCKS, "ladder", Material.LADDER, 8, false, new OreStack(BWGameOre.IRON, 4)),
    PLANKS(BWShopCategory.BLOCKS, "wood", Material.WOOD, 16, false, new OreStack(BWGameOre.GOLD, 4)),
    OBSIDIAN(BWShopCategory.BLOCKS, "obsidian", Material.OBSIDIAN, 4, false, new OreStack(BWGameOre.EMERALD, 4)),

    STONE_SWORD(BWShopCategory.MELEE, "stone_sword", Material.STONE_SWORD, 1, false, new OreStack(BWGameOre.IRON, 10)),
    IRON_SWORD(BWShopCategory.MELEE, "iron_sword", Material.IRON_SWORD, 1, false, new OreStack(BWGameOre.GOLD, 7)),
    DIAMOND_SWORD(BWShopCategory.MELEE, "diamond_sword", Material.DIAMOND_SWORD, 1, false, new OreStack(BWGameOre.EMERALD, 4)),
    STICK(BWShopCategory.MELEE, "stick", new ItemBuilder(Material.STICK).withEnchant(Enchantment.KNOCKBACK).build(), false, new OreStack(BWGameOre.GOLD, 5)),

    CHAINMAIL_ARMOR(new ArmorBW(0, Material.CHAINMAIL_BOOTS, "chainmail_armor", new OreStack(BWGameOre.IRON, 30)).setLeggings(Material.CHAINMAIL_LEGGINGS).setBoots(Material.CHAINMAIL_BOOTS)),
    IRON_ARMOR(new ArmorBW(1, Material.IRON_BOOTS, "iron_armor", new OreStack(BWGameOre.GOLD, 12)).setLeggings(Material.IRON_LEGGINGS).setBoots(Material.IRON_BOOTS)),
    DIAMOND_ARMOR(new ArmorBW(2, Material.DIAMOND_BOOTS, "diamond_armor", new OreStack(BWGameOre.EMERALD, 6)).setLeggings(Material.DIAMOND_LEGGINGS).setBoots(Material.DIAMOND_BOOTS)),

    SHEARS(BWShopCategory.TOOLS, "shears", Material.SHEARS, 1, true, new OreStack(BWGameOre.IRON, 20)),
    PICKAXE(new ItemBWPickaxe()),
    AXE(new ItemBWAxe()),

    ARROW(BWShopCategory.RANGED, "arrow", Material.ARROW, 6, false, new OreStack(BWGameOre.GOLD, 2)),
    BOW(BWShopCategory.RANGED, "bow", Material.BOW, 1, false, new OreStack(BWGameOre.GOLD, 12)),
    BOW_POWER(BWShopCategory.RANGED, "bow_power", new ItemBuilder(Material.BOW).withEnchant(Enchantment.ARROW_DAMAGE).build(), false, new OreStack(BWGameOre.GOLD, 20)),
    BOW_PUNCH(BWShopCategory.RANGED, "bow_punch", new ItemBuilder(Material.BOW).withEnchant(Enchantment.ARROW_DAMAGE).withEnchant(Enchantment.ARROW_KNOCKBACK).build(), false, new OreStack(BWGameOre.EMERALD, 6)),

    POTION_SPEED(BWShopCategory.POTIONS, "potion_speed", new ItemPotionBuilder(PotionType.SPEED, 20*45, 1).build(), false, new OreStack(BWGameOre.EMERALD)),
    POTION_JUMP(BWShopCategory.POTIONS, "potion_jump", new ItemPotionBuilder(PotionType.JUMP, 20*45, 4).build(), false, new OreStack(BWGameOre.EMERALD)),
    POTION_INVISIBILITY(BWShopCategory.POTIONS, "potion_invisibility", new ItemPotionBuilder(PotionType.INVISIBILITY, 20*30, 0).build(), false, new OreStack(BWGameOre.EMERALD, 2)),

    GOLDEN_APPLE(BWShopCategory.UTILITY, "golden_apple", Material.GOLDEN_APPLE, 1, false, new OreStack(BWGameOre.GOLD, 3)),
    BEDBUG(BWShopCategory.UTILITY, "bedbug", Material.SNOW_BALL, 1, false, new OreStack(BWGameOre.IRON, 30)),
    DREAM_DEFENDER(BWShopCategory.UTILITY, "dream_defender", new ItemBuilder(Material.MONSTER_EGG).build(), new OreStack(BWGameOre.IRON, 120), DreamDefenderEgg.class),
    FIREBALL(BWShopCategory.UTILITY, "fireball", new ItemBuilder(Material.FIREBALL).build(), new OreStack(BWGameOre.IRON, 40), FireballBW.class),
    TNT(BWShopCategory.UTILITY, "tnt", Material.TNT, 1, false, new OreStack(BWGameOre.GOLD, 4)),
    ENDER_PEARL(BWShopCategory.UTILITY, "ender_pearl", Material.ENDER_PEARL, 1, false, new OreStack(BWGameOre.EMERALD, 4)),
    WATER(BWShopCategory.UTILITY, "water_bucket", Material.WATER_BUCKET, 1, false, new OreStack(BWGameOre.GOLD, 3)),
    BRIDGE_EGG(BWShopCategory.UTILITY, "bridge_egg", new ItemBuilder(Material.EGG).nbt().setBoolean("BridgeEgg", true).build(), new OreStack(BWGameOre.EMERALD, 1), BridgeEgg.class),
    MAGIC_MILK(BWShopCategory.UTILITY, "magic_milk", Material.MILK_BUCKET, 1, false, new OreStack(BWGameOre.GOLD, 4)),
    SPONGE(BWShopCategory.UTILITY, "sponge", Material.SPONGE, 1, false, new OreStack(BWGameOre.GOLD, 3)),
    COMPACT_POP_UP_TOWER(BWShopCategory.UTILITY, "compact_tower", Material.CHEST, 1, false, new OreStack(BWGameOre.IRON, 24)),
    ;

    private final String name;
    private final ItemShop item;
    private ItemShopUpgradable itemUpgrade;
    private Class<? extends HyriItem<?>> hyriItem;

    BWMaterial(BWShopCategory category, String keyName, ItemStack item, boolean permanent, OreStack... price){
        this.name = keyName;
        this.item = new ItemShop(keyName, item, category, permanent, price).setHyriMaterial(this);
    }

    BWMaterial(BWShopCategory category, String keyName, Material item, int amount, boolean permanent, OreStack... price){
        this(category, keyName, new ItemStack(item, amount), permanent, price);
    }

    BWMaterial(ArmorBW armor){
        this.name = armor.getKeyName();
        this.item = armor.setHyriMaterial(this);
    }

    BWMaterial(ItemShopUpgradable itemUpgrade){
        this.name = itemUpgrade.getTierItem(0).getKeyName();
        this.itemUpgrade = itemUpgrade;
        this.item = itemUpgrade.getTierItem(0).setHyriMaterial(this);
    }

    BWMaterial(BWShopCategory category, String name, ItemStack itemStack, OreStack price, Class<? extends HyriShopItem<?>> hyriItem){
        this(category, name, itemStack, false, price);
        this.hyriItem = hyriItem;
    }

    public ItemShop getItemShop() {
        return item;
    }

    public boolean isArmor(){
        return this.item instanceof ArmorBW;
    }

    public ArmorBW getArmor(){
        return (ArmorBW) this.item;
    }

    public boolean isItemUpgradable(){
        return this.itemUpgrade != null;
    }

    public ItemShopUpgradable getItemUpgradable(){
        return this.itemUpgrade;
    }

    public boolean isHyriItem(){
        return this.hyriItem != null;
    }

    public Class<? extends HyriItem<?>> getHyriItem() {
        return hyriItem;
    }
}

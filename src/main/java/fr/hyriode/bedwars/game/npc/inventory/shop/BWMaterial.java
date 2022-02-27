package fr.hyriode.bedwars.game.npc.inventory.shop;

import fr.hyriode.hyrame.item.HyriItem;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.bedwars.game.BWGameOre;
import fr.hyriode.bedwars.game.npc.inventory.EHyriBWShopNavBar;
import fr.hyriode.bedwars.game.npc.inventory.shop.material.upgradable.ArmorBW;
import fr.hyriode.bedwars.game.npc.inventory.shop.material.upgradable.ItemBWAxe;
import fr.hyriode.bedwars.game.npc.inventory.shop.material.upgradable.ItemBWPickaxe;
import fr.hyriode.bedwars.game.npc.inventory.shop.utility.DreamDefenderEgg;
import fr.hyriode.bedwars.game.npc.inventory.shop.utility.FireballBW;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

public enum BWMaterial {

    WOOL(EHyriBWShopNavBar.BLOCKS, Material.WOOL, "wool", 16, false, new ItemShopStack(BWGameOre.IRON, 4)),
    HARD_CLAY(EHyriBWShopNavBar.BLOCKS, Material.HARD_CLAY, "hard_clay", 16, false, new ItemShopStack(BWGameOre.IRON, 12)),
    GLASS(EHyriBWShopNavBar.BLOCKS, Material.GLASS, "glass", 4, false, new ItemShopStack(BWGameOre.IRON, 12)),
    END_STONE(EHyriBWShopNavBar.BLOCKS, Material.ENDER_STONE, "end_stone", 12, false, new ItemShopStack(BWGameOre.IRON, 24)),
    LADDER(EHyriBWShopNavBar.BLOCKS, Material.LADDER, "ladder", 8, false, new ItemShopStack(BWGameOre.IRON, 4)),
    PLANKS(EHyriBWShopNavBar.BLOCKS, Material.WOOD, "wood", 16, false, new ItemShopStack(BWGameOre.GOLD, 4)),
    OBSIDIAN(EHyriBWShopNavBar.BLOCKS, Material.OBSIDIAN, "obsidian", 4, false, new ItemShopStack(BWGameOre.EMERALD, 4)),

    STONE_SWORD(EHyriBWShopNavBar.MELEE, Material.STONE_SWORD, "stone_sword", 1, false, new ItemShopStack(BWGameOre.IRON, 10)),
    IRON_SWORD(EHyriBWShopNavBar.MELEE, Material.IRON_SWORD, "iron_sword", 1, false, new ItemShopStack(BWGameOre.GOLD, 7)),
    DIAMOND_SWORD(EHyriBWShopNavBar.MELEE, Material.DIAMOND_SWORD, "diamond_sword", 1, false, new ItemShopStack(BWGameOre.EMERALD, 4)),
    STICK(EHyriBWShopNavBar.MELEE, new ItemBuilder(Material.STICK).withEnchant(Enchantment.KNOCKBACK).build(), "stick", false, new ItemShopStack(BWGameOre.GOLD, 5)),

    CHAINMAIL_ARMOR(EHyriBWShopNavBar.ARMOR, new ArmorBW(0, Material.CHAINMAIL_BOOTS, "chainmail_armor", true, new ItemShopStack(BWGameOre.IRON, 30)).setLeggings(new ItemBuilder(Material.CHAINMAIL_LEGGINGS).withEnchant(Enchantment.PROTECTION_ENVIRONMENTAL).build()).setBoots(new ItemBuilder(Material.CHAINMAIL_BOOTS).withEnchant(Enchantment.PROTECTION_ENVIRONMENTAL).build())),
    IRON_ARMOR(EHyriBWShopNavBar.ARMOR, new ArmorBW(1, Material.IRON_BOOTS, "iron_armor", true, new ItemShopStack(BWGameOre.GOLD, 12)).setLeggings(new ItemBuilder(Material.IRON_LEGGINGS).withEnchant(Enchantment.PROTECTION_ENVIRONMENTAL).build()).setBoots(new ItemBuilder(Material.IRON_BOOTS).withEnchant(Enchantment.PROTECTION_ENVIRONMENTAL).build())),
    DIAMOND_ARMOR(EHyriBWShopNavBar.ARMOR, new ArmorBW(2, Material.DIAMOND_BOOTS, "diamond_armor", true, new ItemShopStack(BWGameOre.EMERALD, 6)).setLeggings(new ItemBuilder(Material.DIAMOND_LEGGINGS).withEnchant(Enchantment.PROTECTION_ENVIRONMENTAL).build()).setBoots(new ItemBuilder(Material.DIAMOND_BOOTS).withEnchant(Enchantment.PROTECTION_ENVIRONMENTAL).build())),

    SHEARS(EHyriBWShopNavBar.TOOLS, Material.SHEARS, "shears", 1, true, new ItemShopStack(BWGameOre.IRON, 20)),
    PICKAXE(EHyriBWShopNavBar.TOOLS, new ItemBWPickaxe()),
    AXE(EHyriBWShopNavBar.TOOLS, new ItemBWAxe()),

    ARROW(EHyriBWShopNavBar.RANGED, Material.ARROW, "arrow", 6, false, new ItemShopStack(BWGameOre.GOLD, 2)),
    BOW(EHyriBWShopNavBar.RANGED, Material.BOW, "bow", 1, false, new ItemShopStack(BWGameOre.GOLD, 12)),
    BOW_POWER(EHyriBWShopNavBar.RANGED, new ItemBuilder(Material.BOW).withEnchant(Enchantment.ARROW_DAMAGE).build(), "bow_power", false, new ItemShopStack(BWGameOre.GOLD, 20)),
    BOW_PUNCH(EHyriBWShopNavBar.RANGED, new ItemBuilder(Material.BOW).withEnchant(Enchantment.ARROW_DAMAGE).withEnchant(Enchantment.ARROW_KNOCKBACK).build(), "bow_punch", false, new ItemShopStack(BWGameOre.EMERALD, 6)),

    POTION_SPEED(EHyriBWShopNavBar.POTIONS, new ItemPotionBuilder(PotionType.SPEED, 20*45, 1).build(), "potion_speed", false, new ItemShopStack(BWGameOre.EMERALD)),
    POTION_JUMP(EHyriBWShopNavBar.POTIONS, new ItemPotionBuilder(PotionType.JUMP, 20*45, 4).build(), "potion_jump", false, new ItemShopStack(BWGameOre.EMERALD)),
    POTION_INVISIBILITY(EHyriBWShopNavBar.POTIONS, new ItemPotionBuilder(PotionType.INVISIBILITY, 20*30, 0).build(), "potion_invisibility", false, new ItemShopStack(BWGameOre.EMERALD, 2)),

    GOLDEN_APPLE(EHyriBWShopNavBar.UTILITY, Material.GOLDEN_APPLE, "golden_apple", 1, false, new ItemShopStack(BWGameOre.GOLD, 3)),
    BEDBUG(EHyriBWShopNavBar.UTILITY, Material.SNOW_BALL, "bedbug", 1, false, new ItemShopStack(BWGameOre.IRON, 30)),
    DREAM_DEFENDER(EHyriBWShopNavBar.UTILITY, new ItemStack(Material.MONSTER_EGG), DreamDefenderEgg.class, "dream_defender", new ItemShopStack(BWGameOre.IRON, 120)),
    FIREBALL(EHyriBWShopNavBar.UTILITY, new ItemStack(Material.FIREBALL), FireballBW.class, "fireball", new ItemShopStack(BWGameOre.IRON, 40)),
    TNT(EHyriBWShopNavBar.UTILITY, Material.TNT, "tnt", 1, false, new ItemShopStack(BWGameOre.GOLD, 4)),
    ENDER_PEARL(EHyriBWShopNavBar.UTILITY, Material.ENDER_PEARL, "ender_pearl", 1, false, new ItemShopStack(BWGameOre.EMERALD, 4)),
    WATER(EHyriBWShopNavBar.UTILITY, Material.WATER_BUCKET, "water_bucket", 1, false, new ItemShopStack(BWGameOre.GOLD, 3)),
    BRIDGE_EGG(EHyriBWShopNavBar.UTILITY, Material.EGG, "bridge_egg", 1, false, new ItemShopStack(BWGameOre.EMERALD, 1)),
    MAGIC_MILK(EHyriBWShopNavBar.UTILITY, Material.MILK_BUCKET, "magic_milk", 1, false, new ItemShopStack(BWGameOre.GOLD, 4)),
    SPONGE(EHyriBWShopNavBar.UTILITY, Material.SPONGE, "sponge", 1, false, new ItemShopStack(BWGameOre.GOLD, 3)),
    COMPACT_POP_UP_TOWER(EHyriBWShopNavBar.UTILITY, Material.CHEST, "compact_tower", 1, false, new ItemShopStack(BWGameOre.IRON, 24)),
    ;

    private final ItemShopStack itemShop;
    private final EHyriBWShopNavBar category;
    private ItemShopUpgradable itemUpgradable;

    BWMaterial(EHyriBWShopNavBar category, ItemShopStack itemShop){
        itemShop.getItem().setHyriMaterial(this);
        this.itemShop = itemShop;
        this.category = category;
    }

    BWMaterial(EHyriBWShopNavBar category, ArmorBW armorBW){
        armorBW.setHyriMaterial(this);
        this.itemShop = new ItemShopStack(armorBW);
        this.category = category;
    }

    BWMaterial(EHyriBWShopNavBar category, Material material, String keyName, int amount, boolean permanent, ItemShopStack... price){
        this.itemShop = new ItemShopStack(new ItemShop(new ItemStack(material), keyName, category, permanent, null, price).setHyriMaterial(this), amount);
        this.category = category;
    }

    BWMaterial(EHyriBWShopNavBar category, ItemStack itemStack, String keyName, boolean permanent, ItemShopStack... price){
        this.itemShop = new ItemShopStack(new ItemShop(itemStack, keyName, category, permanent, null, price).setHyriMaterial(this), itemStack.getAmount());
        this.category = category;
    }

    BWMaterial(EHyriBWShopNavBar category, ItemShopUpgradable itemShopUpgradable){
        this.itemUpgradable = itemShopUpgradable;
        this.itemShop = new ItemShopStack(itemShopUpgradable.getTierItem(0).setUpgradable(true).setHyriMaterial(this));
        this.category = category;
    }

    BWMaterial(EHyriBWShopNavBar category, ItemStack itemStack, Class<? extends HyriItem<?>> hyriItem, String keyName, ItemShopStack... price){
        this.category = category;
        this.itemShop = new ItemShopStack(new ItemShop(itemStack, hyriItem, keyName, category, false, null, price), 1);
    }

    public ItemShopStack getItemShop() {
        return itemShop;
    }

    public boolean isUpgradable() {
        return itemUpgradable != null;
    }

    public EHyriBWShopNavBar getCategory() {
        return category;
    }

    public ItemShopUpgradable getItemUpgradable(){
        return this.itemUpgradable;
    }


}

package fr.hyriode.bedwars.game.shop;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.api.player.style.HyriGameStyle;
import fr.hyriode.bedwars.game.shop.material.MaterialArmorShop;
import fr.hyriode.bedwars.game.shop.material.MaterialShop;
import fr.hyriode.bedwars.host.BWShopValues;
import fr.hyriode.bedwars.utils.ItemPotionBuilder;
import fr.hyriode.bedwars.utils.MetadataReferences;
import fr.hyriode.hyrame.game.HyriGameState;
import fr.hyriode.hyrame.game.util.value.ValueProvider;
import fr.hyriode.hyrame.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

import java.util.*;
import java.util.stream.Collectors;

public class ShopManager {

    private final List<MaterialShop> itemShops = new ArrayList<>();
    private final HyriBedWars plugin;

    public ShopManager(HyriBedWars plugin) {
        this.plugin = plugin;
        this.add(BWShopValues.WOOL_ENABLE, "wool", ShopCategory.BLOCKS, new ItemShop(new ItemBuilder(Material.WOOL, 16), new ItemPrice(ItemMoney.IRON, BWShopValues.WOOL_PRICE)));
        this.add(BWShopValues.HARD_CLAY_ENABLE, "hard-clay", ShopCategory.BLOCKS, new ItemShop(new ItemBuilder(Material.STAINED_CLAY, 16), new ItemPrice(ItemMoney.IRON, BWShopValues.HARD_CLAY_PRICE)));
        this.add(BWShopValues.GLASS_ENABLE, "glass", ShopCategory.BLOCKS, new ItemShop(new ItemBuilder(Material.GLASS, 4), new ItemPrice(ItemMoney.IRON, BWShopValues.GLASS_PRICE)));
        this.add(BWShopValues.END_STONE_ENABLE, "end-stone", ShopCategory.BLOCKS, new ItemShop(new ItemBuilder(Material.ENDER_STONE, 12), new ItemPrice(ItemMoney.IRON, BWShopValues.END_STONE_PRICE)));
        this.add(BWShopValues.LADDER_ENABLE, "ladder", ShopCategory.BLOCKS, new ItemShop(new ItemBuilder(Material.LADDER, 8), new ItemPrice(ItemMoney.IRON, BWShopValues.LADDER_PRICE)));
        this.add(BWShopValues.WOOD_ENABLE, "wood", ShopCategory.BLOCKS, new ItemShop(new ItemBuilder(Material.WOOD, 16), new ItemPrice(ItemMoney.GOLD, BWShopValues.WOOD_PRICE)));
        this.add(BWShopValues.OBSIDIAN_ENABLE, "obsidian", ShopCategory.BLOCKS, new ItemShop(new ItemBuilder(Material.OBSIDIAN, 4), new ItemPrice(ItemMoney.EMERALD, BWShopValues.OBSIDIAN_PRICE)));

        this.add(BWShopValues.STONE_SWORD_ENABLE, "stone-sword", ShopCategory.MELEE, new ItemShop(new ItemBuilder(Material.STONE_SWORD), new ItemPrice(ItemMoney.IRON, BWShopValues.STONE_SWORD_PRICE)));
        this.add(BWShopValues.IRON_SWORD_ENABLE, "iron-sword", ShopCategory.MELEE, new ItemShop(new ItemBuilder(Material.IRON_SWORD), new ItemPrice(ItemMoney.GOLD, BWShopValues.IRON_SWORD_PRICE)));
        this.add(BWShopValues.DIAMOND_SWORD_ENABLE, "diamond-sword", ShopCategory.MELEE, new ItemShop(new ItemBuilder(Material.DIAMOND_SWORD), new ItemPrice(ItemMoney.EMERALD, BWShopValues.DIAMOND_SWORD_PRICE)));
        this.add(BWShopValues.KNOCKBACK_STICK_ENABLE, "stick", ShopCategory.MELEE, new ItemShop(new ItemBuilder(Material.STICK).withEnchant(Enchantment.KNOCKBACK), new ItemPrice(ItemMoney.GOLD, BWShopValues.KNOCKBACK_STICK_PRICE)));

        this.addArmor(BWShopValues.CHAINMAIL_ARMOR_ENABLE, "chainmail-armor", 0, ShopCategory.ARMOR, new ItemShop(new ItemBuilder(Material.CHAINMAIL_BOOTS), new ItemPrice(ItemMoney.IRON, BWShopValues.CHAINMAIL_ARMOR_PRICE)),
                new ItemStack(Material.CHAINMAIL_LEGGINGS), new ItemStack(Material.CHAINMAIL_BOOTS));
        this.addArmor(BWShopValues.IRON_ARMOR_ENABLE, "iron-armor", 1, ShopCategory.ARMOR, new ItemShop(new ItemBuilder(Material.IRON_BOOTS), new ItemPrice(ItemMoney.GOLD, BWShopValues.IRON_ARMOR_PRICE)),
                new ItemStack(Material.IRON_LEGGINGS), new ItemStack(Material.IRON_BOOTS));
        this.addArmor(BWShopValues.DIAMOND_ARMOR_ENABLE, "diamond-armor", 2, ShopCategory.ARMOR, new ItemShop(new ItemBuilder(Material.DIAMOND_BOOTS), new ItemPrice(ItemMoney.EMERALD, BWShopValues.DIAMOND_ARMOR_PRICE)),
                new ItemStack(Material.DIAMOND_LEGGINGS), new ItemStack(Material.DIAMOND_BOOTS));

        this.add(BWShopValues.SHEARS_ENABLE, "shears", ShopCategory.TOOLS, true, new ItemShop(new ItemBuilder(Material.SHEARS), new ItemPrice(ItemMoney.IRON, BWShopValues.SHEARS_PRICE)));
        this.add(BWShopValues.PICKAXE_ENABLE, "pickaxe", ShopCategory.TOOLS, true,
                new ItemShop(new ItemBuilder(Material.WOOD_PICKAXE).withEnchant(Enchantment.DIG_SPEED), new ItemPrice(ItemMoney.IRON, BWShopValues.PICKAXE_0_PRICE)),
                new ItemShop(new ItemBuilder(Material.IRON_PICKAXE).withEnchant(Enchantment.DIG_SPEED, 2), new ItemPrice(ItemMoney.IRON, BWShopValues.PICKAXE_1_PRICE)),
                new ItemShop(new ItemBuilder(Material.GOLD_PICKAXE).withEnchant(Enchantment.DIG_SPEED, 3)/*.withEnchant(Enchantment.DAMAGE_ALL, 2)*/, new ItemPrice(ItemMoney.GOLD, BWShopValues.PICKAXE_2_PRICE)),
                new ItemShop(new ItemBuilder(Material.DIAMOND_PICKAXE).withEnchant(Enchantment.DIG_SPEED, 3), new ItemPrice(ItemMoney.GOLD, BWShopValues.PICKAXE_3_PRICE))
        );
        this.add(BWShopValues.AXE_ENABLE, "axe", ShopCategory.TOOLS, true,
                new ItemShop(new ItemBuilder(Material.WOOD_AXE).withEnchant(Enchantment.DIG_SPEED).withEnchant(Enchantment.DIG_SPEED, 1), new ItemPrice(ItemMoney.IRON, BWShopValues.AXE_0_PRICE)),
                new ItemShop(new ItemBuilder(Material.IRON_AXE).withEnchant(Enchantment.DIG_SPEED, 2).withEnchant(Enchantment.DIG_SPEED, 1), new ItemPrice(ItemMoney.IRON, BWShopValues.AXE_1_PRICE)),
                new ItemShop(new ItemBuilder(Material.GOLD_AXE).withEnchant(Enchantment.DIG_SPEED, 3).withEnchant(Enchantment.DIG_SPEED, 2).withEnchant(Enchantment.DAMAGE_ALL, 2), new ItemPrice(ItemMoney.GOLD, BWShopValues.AXE_2_PRICE)),
                new ItemShop(new ItemBuilder(Material.DIAMOND_AXE).withEnchant(Enchantment.DIG_SPEED, 3).withEnchant(Enchantment.DIG_SPEED, 3), new ItemPrice(ItemMoney.GOLD, BWShopValues.AXE_3_PRICE))
        );

        this.add(BWShopValues.ARROW_ENABLE, "arrow", ShopCategory.RANGED, new ItemShop(new ItemBuilder(Material.ARROW, 6), new ItemPrice(ItemMoney.GOLD, BWShopValues.ARROW_PRICE)));
        this.add(BWShopValues.BOW_ENABLE, "bow", ShopCategory.RANGED, new ItemShop(new ItemBuilder(Material.BOW), new ItemPrice(ItemMoney.GOLD, BWShopValues.BOW_PRICE)));
        this.add(BWShopValues.BOW_POWER_ENABLE, "bow-power", ShopCategory.RANGED, new ItemShop(new ItemBuilder(Material.BOW).withEnchant(Enchantment.ARROW_DAMAGE), new ItemPrice(ItemMoney.GOLD, BWShopValues.BOW_POWER_PRICE)));
        this.add(BWShopValues.BOW_PUNCH_ENABLE, "bow-punch", ShopCategory.RANGED, new ItemShop(new ItemBuilder(Material.BOW).withEnchant(Enchantment.ARROW_DAMAGE).withEnchant(Enchantment.ARROW_KNOCKBACK), new ItemPrice(ItemMoney.EMERALD, BWShopValues.BOW_PUNCH_PRICE)));

        this.add(BWShopValues.POTION_SPEED_ENABLE, "potion-speed", ShopCategory.POTIONS, new ItemShop(new ItemPotionBuilder(PotionType.SPEED, 20*45, 1), new ItemPrice(ItemMoney.EMERALD, BWShopValues.POTION_SPEED_PRICE)));
        this.add(BWShopValues.POTION_JUMP_ENABLE, "potion-jump", ShopCategory.POTIONS, new ItemShop(new ItemPotionBuilder(PotionType.JUMP, 20*45, 4), new ItemPrice(ItemMoney.EMERALD, BWShopValues.POTION_JUMP_PRICE)));
        this.add(BWShopValues.POTION_INVISIBILITY_ENABLE, "potion-invisibility", ShopCategory.POTIONS, new ItemShop(new ItemPotionBuilder(PotionType.INVISIBILITY, 20*30, 0), new ItemPrice(ItemMoney.EMERALD, BWShopValues.POTION_INVISIBILITY_PRICE)));

        this.add(BWShopValues.GOLDEN_APPLE_ENABLE, "golden-apple", ShopCategory.UTILITY, new ItemShop(new ItemBuilder(Material.GOLDEN_APPLE), new ItemPrice(ItemMoney.GOLD, BWShopValues.GOLDEN_APPLE_PRICE)));
        this.add(BWShopValues.BEDBUG_ENABLE, "bedbug", ShopCategory.UTILITY, new ItemShop(new ItemBuilder(Material.SNOW_BALL).nbt().setBoolean(MetadataReferences.BEDBUG, true).toBuilder(), new ItemPrice(ItemMoney.IRON, BWShopValues.BEDBUG_PRICE)));
        this.add(BWShopValues.DREAM_DEFENDER_ENABLE, "dream-defender", ShopCategory.UTILITY, new ItemShop(new ItemBuilder(Material.MONSTER_EGG).nbt().setBoolean(MetadataReferences.DREAM_DEFENDER, true).toBuilder(), new ItemPrice(ItemMoney.IRON, BWShopValues.DREAM_DEFENDER_PRICE)));
        this.add(BWShopValues.FIREBALL_ENABLE, "fireball", ShopCategory.UTILITY, new ItemShop(new ItemBuilder(Material.FIREBALL).nbt().setBoolean(MetadataReferences.FIREBALL, true).toBuilder(), new ItemPrice(ItemMoney.IRON, BWShopValues.FIREBALL_PRICE)));
        this.add(BWShopValues.TNT_ENABLE, "tnt", ShopCategory.UTILITY, new ItemShop(new ItemBuilder(Material.TNT), new ItemPrice(ItemMoney.GOLD, BWShopValues.TNT_PRICE)));
        this.add(BWShopValues.ENDER_PEARL_ENABLE, "ender-pearl", ShopCategory.UTILITY, new ItemShop(new ItemBuilder(Material.ENDER_PEARL), new ItemPrice(ItemMoney.EMERALD, BWShopValues.ENDER_PEARL_PRICE)));
        this.add(BWShopValues.WATER_BUCKET_ENABLE, "water-bucket", ShopCategory.UTILITY, new ItemShop(new ItemBuilder(Material.WATER_BUCKET), new ItemPrice(ItemMoney.GOLD, BWShopValues.WATER_BUCKET_PRICE)));
        this.add(BWShopValues.BRIDGE_EGG_ENABLE, "bridge-egg", ShopCategory.UTILITY, new ItemShop(new ItemBuilder(Material.EGG).nbt().setBoolean(MetadataReferences.BRIDGE_EGG, true).toBuilder(), new ItemPrice(ItemMoney.EMERALD, BWShopValues.BRIDGE_EGG_PRICE)));
        this.add(BWShopValues.MAGIC_MILK_ENABLE, "magic-milk", ShopCategory.UTILITY, new ItemShop(new ItemBuilder(Material.MILK_BUCKET).nbt().setBoolean(MetadataReferences.MAGIC_MILK, true).toBuilder(), new ItemPrice(ItemMoney.GOLD, BWShopValues.MAGIC_MILK_PRICE)));
        this.add(BWShopValues.SPONGE_ENABLE, "sponge", ShopCategory.UTILITY, new ItemShop(new ItemBuilder(Material.SPONGE, 4).nbt().setBoolean(MetadataReferences.SPONGE, true).toBuilder(), new ItemPrice(ItemMoney.GOLD, BWShopValues.SPONGE_PRICE)));
        this.add(BWShopValues.COMPACT_TOWER_ENABLE, "compact-tower", ShopCategory.UTILITY, new ItemShop(new ItemBuilder(Material.CHEST, 1).nbt().setBoolean(MetadataReferences.POPUP_TOWER, true).toBuilder(), new ItemPrice(ItemMoney.IRON, BWShopValues.COMPACT_TOWER_PRICE)));
    }

    private void add(ValueProvider<Boolean> enable, String name, ShopCategory category, ItemShop... itemShops){
        this.add(enable, name, category, false, itemShops);
    }

    private void add(ValueProvider<Boolean> enable, String name, ShopCategory category, boolean permanent, ItemShop... itemShops){
        this.itemShops.add(new MaterialShop(enable, name, category, permanent, Arrays.asList(itemShops)));
    }

    private void addArmor(ValueProvider<Boolean> enable, String name, int level, ShopCategory category, ItemShop icon, ItemStack helmet, ItemStack chestplate){
        this.itemShops.add(new MaterialArmorShop(enable, name, category, icon, level, true, helmet, chestplate));
    }

    public boolean isArmor(MaterialShop materialShop){
        return materialShop instanceof MaterialArmorShop;
    }

    public MaterialArmorShop getArmorByName(boolean withEnableCheck, String name){
        return this.getItemsShop(withEnableCheck).stream().filter(materialShop -> materialShop.getName().equals(name) && this.isArmor(materialShop))
                .map(materialShop -> (MaterialArmorShop) materialShop).findFirst().orElse(null);
    }

    public ItemPrice getPriceByName(boolean withEnableCheck, int tier, String name) {
        final ItemShop itemShop = this.getItemShopByName(withEnableCheck, name);
        if(itemShop != null) {
            return itemShop.getPrice();
        }
        return null;
    }

    public ItemPrice getPriceByTierName(boolean withEnableCheck, int tier, String name){
        return this.getItemShopByNameTier(withEnableCheck, tier, name).getPrice();
    }

    public List<MaterialShop> getItemShopByCategory(boolean withEnableCheck, ShopCategory category) {
        return this.getItemsShop(withEnableCheck).stream().filter(itemShop -> itemShop.getCategory() == category).collect(Collectors.toList());
    }

    private List<MaterialShop> getItemsShop(boolean withEnableCheck) {
        if (withEnableCheck) {
            HyriGameState gameState = this.plugin.getGame().getState();
            if(gameState == HyriGameState.WAITING || gameState == HyriGameState.READY) {
                return this.itemShops.stream().filter(materialShop -> materialShop.isEnable().getDefaultValue()).collect(Collectors.toList());
            }
            return this.itemShops.stream().filter(materialShop -> materialShop.isEnable().get()).collect(Collectors.toList());
        }
        return this.itemShops;
    }

    public Map<Integer, MaterialShop> getItemShopByCategoryToMap(boolean withEnableCheck, int base, ShopCategory category) {
        Map<Integer, MaterialShop> materialsShop = new HashMap<>();
        int i = base;
        for (MaterialShop materialShop : this.getItemShopByCategory(withEnableCheck, category)) {
            materialsShop.put(i++, materialShop);
        }
        return materialsShop;
    }

    public ItemShop getItemShopByNameTier(boolean withEnableCheck, int tier, String name){
        MaterialShop material = this.getItemsShop(withEnableCheck).stream().filter(item -> item.getName().equals(name)).findFirst().orElse(null);

        if(material != null) {
            return material.getItem(tier);
        }
        return null;
    }

    public ItemShop getItemShopByName(boolean withEnableCheck, String name){
        return this.getItemShopByNameTier(withEnableCheck, 0, name);
    }

    public MaterialShop getMaterialShopByName(boolean withEnableCheck, String name){
        return this.getItemsShop(withEnableCheck).stream().filter(materialShop -> materialShop.getName().toLowerCase().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    public MaterialShop getMaterialByItemShop(boolean withEnableCheck, ItemShop itemShop) {
        return this.getItemsShop(withEnableCheck).stream().filter(materialShop -> materialShop.getItems().stream().filter(itemShop1 -> itemShop.getName().equals(itemShop1.getName())).findFirst().orElse(null) != null).findFirst().orElse(null);
    }
}

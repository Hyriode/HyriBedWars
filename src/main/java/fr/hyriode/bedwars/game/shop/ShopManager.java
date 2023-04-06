package fr.hyriode.bedwars.game.shop;

import fr.hyriode.bedwars.game.shop.material.MaterialArmorShop;
import fr.hyriode.bedwars.game.shop.material.MaterialShop;
import fr.hyriode.bedwars.utils.ItemPotionBuilder;
import fr.hyriode.bedwars.utils.MetadataReferences;
import fr.hyriode.hyrame.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

import java.util.*;
import java.util.stream.Collectors;

public class ShopManager {

    private final List<MaterialShop> itemShops = new ArrayList<>();

    public ShopManager() {
        this.add("wool", ShopCategory.BLOCKS, new ItemShop(new ItemBuilder(Material.WOOL, 16), new ItemPrice(ItemMoney.IRON, 4)));
        this.add("hard-clay", ShopCategory.BLOCKS, new ItemShop(new ItemBuilder(Material.STAINED_CLAY, 16), new ItemPrice(ItemMoney.IRON, 12)));
        this.add("glass", ShopCategory.BLOCKS, new ItemShop(new ItemBuilder(Material.GLASS, 4), new ItemPrice(ItemMoney.IRON, 12)));
        this.add("end-stone", ShopCategory.BLOCKS, new ItemShop(new ItemBuilder(Material.ENDER_STONE, 12), new ItemPrice(ItemMoney.IRON, 24)));
        this.add("ladder", ShopCategory.BLOCKS, new ItemShop(new ItemBuilder(Material.LADDER, 8), new ItemPrice(ItemMoney.IRON, 4)));
        this.add("wood", ShopCategory.BLOCKS, new ItemShop(new ItemBuilder(Material.WOOD, 16), new ItemPrice(ItemMoney.GOLD, 4)));
        this.add("obsidian", ShopCategory.BLOCKS, new ItemShop(new ItemBuilder(Material.OBSIDIAN, 4), new ItemPrice(ItemMoney.EMERALD, 4)));

        this.add("stone-sword", ShopCategory.MELEE, new ItemShop(new ItemBuilder(Material.STONE_SWORD), new ItemPrice(ItemMoney.IRON, 10)));
        this.add("iron-sword", ShopCategory.MELEE, new ItemShop(new ItemBuilder(Material.IRON_SWORD), new ItemPrice(ItemMoney.GOLD, 7)));
        this.add("diamond-sword", ShopCategory.MELEE, new ItemShop(new ItemBuilder(Material.DIAMOND_SWORD), new ItemPrice(ItemMoney.EMERALD, 4)));
        this.add("stick", ShopCategory.MELEE, new ItemShop(new ItemBuilder(Material.STICK).withEnchant(Enchantment.KNOCKBACK), new ItemPrice(ItemMoney.GOLD, 7)));

        this.addArmor("chainmail-armor", 0, ShopCategory.ARMOR, new ItemShop(new ItemBuilder(Material.CHAINMAIL_BOOTS), new ItemPrice(ItemMoney.IRON, 30)),
                new ItemStack(Material.CHAINMAIL_LEGGINGS), new ItemStack(Material.CHAINMAIL_BOOTS));
        this.addArmor("iron-armor", 1, ShopCategory.ARMOR, new ItemShop(new ItemBuilder(Material.IRON_BOOTS), new ItemPrice(ItemMoney.GOLD, 12)),
                new ItemStack(Material.IRON_LEGGINGS), new ItemStack(Material.IRON_BOOTS));
        this.addArmor("diamond-armor", 2, ShopCategory.ARMOR, new ItemShop(new ItemBuilder(Material.DIAMOND_BOOTS), new ItemPrice(ItemMoney.EMERALD, 6)),
                new ItemStack(Material.DIAMOND_LEGGINGS), new ItemStack(Material.DIAMOND_BOOTS));

        this.add("shears", ShopCategory.TOOLS, true, new ItemShop(new ItemBuilder(Material.SHEARS), new ItemPrice(ItemMoney.IRON, 20)));
        this.add("pickaxe", ShopCategory.TOOLS, true,
                new ItemShop(new ItemBuilder(Material.WOOD_PICKAXE).withEnchant(Enchantment.DIG_SPEED), new ItemPrice(ItemMoney.IRON, 10)),
                new ItemShop(new ItemBuilder(Material.IRON_PICKAXE).withEnchant(Enchantment.DIG_SPEED, 2), new ItemPrice(ItemMoney.IRON, 10)),
                new ItemShop(new ItemBuilder(Material.GOLD_PICKAXE).withEnchant(Enchantment.DIG_SPEED, 3).withEnchant(Enchantment.DAMAGE_ALL, 2), new ItemPrice(ItemMoney.GOLD, 3)),
                new ItemShop(new ItemBuilder(Material.DIAMOND_PICKAXE).withEnchant(Enchantment.DIG_SPEED, 3), new ItemPrice(ItemMoney.GOLD, 6))
        );
        this.add("axe", ShopCategory.TOOLS, true,
                new ItemShop(new ItemBuilder(Material.WOOD_AXE).withEnchant(Enchantment.DIG_SPEED).withEnchant(Enchantment.DIG_SPEED, 1), new ItemPrice(ItemMoney.IRON, 10)),
                new ItemShop(new ItemBuilder(Material.IRON_AXE).withEnchant(Enchantment.DIG_SPEED, 2).withEnchant(Enchantment.DIG_SPEED, 1), new ItemPrice(ItemMoney.IRON, 10)),
                new ItemShop(new ItemBuilder(Material.GOLD_AXE).withEnchant(Enchantment.DIG_SPEED, 3).withEnchant(Enchantment.DIG_SPEED, 2).withEnchant(Enchantment.DAMAGE_ALL, 2), new ItemPrice(ItemMoney.GOLD, 3)),
                new ItemShop(new ItemBuilder(Material.DIAMOND_AXE).withEnchant(Enchantment.DIG_SPEED, 3).withEnchant(Enchantment.DIG_SPEED, 3), new ItemPrice(ItemMoney.GOLD, 6))
        );

        this.add("arrow", ShopCategory.RANGED, new ItemShop(new ItemBuilder(Material.ARROW, 6), new ItemPrice(ItemMoney.GOLD, 2)));
        this.add("bow", ShopCategory.RANGED, new ItemShop(new ItemBuilder(Material.BOW), new ItemPrice(ItemMoney.GOLD, 12)));
        this.add("bow-power", ShopCategory.RANGED, new ItemShop(new ItemBuilder(Material.BOW).withEnchant(Enchantment.ARROW_DAMAGE), new ItemPrice(ItemMoney.GOLD, 20)));
        this.add("bow-punch", ShopCategory.RANGED, new ItemShop(new ItemBuilder(Material.BOW).withEnchant(Enchantment.ARROW_DAMAGE).withEnchant(Enchantment.ARROW_KNOCKBACK), new ItemPrice(ItemMoney.EMERALD, 6)));

        this.add("potion-speed", ShopCategory.POTIONS, new ItemShop(new ItemPotionBuilder(PotionType.SPEED, 20*45, 1), new ItemPrice(ItemMoney.EMERALD)));
        this.add("potion-jump", ShopCategory.POTIONS, new ItemShop(new ItemPotionBuilder(PotionType.JUMP, 20*45, 4), new ItemPrice(ItemMoney.EMERALD)));
        this.add("potion-invisibility", ShopCategory.POTIONS, new ItemShop(new ItemPotionBuilder(PotionType.INVISIBILITY, 20*30, 0), new ItemPrice(ItemMoney.EMERALD, 2)));

        this.add("golden-apple", ShopCategory.UTILITY, new ItemShop(new ItemBuilder(Material.GOLDEN_APPLE), new ItemPrice(ItemMoney.GOLD, 3)));
        this.add("bedbug", ShopCategory.UTILITY, new ItemShop(new ItemBuilder(Material.SNOW_BALL).nbt().setBoolean(MetadataReferences.BEDBUG, true).toBuilder(), new ItemPrice(ItemMoney.IRON, 30)));
        this.add("dream-defender", ShopCategory.UTILITY, new ItemShop(new ItemBuilder(Material.MONSTER_EGG).nbt().setBoolean(MetadataReferences.DREAM_DEFENDER, true).toBuilder(), new ItemPrice(ItemMoney.IRON, 120)));
        this.add("fireball", ShopCategory.UTILITY, new ItemShop(new ItemBuilder(Material.FIREBALL).nbt().setBoolean(MetadataReferences.FIREBALL, true).toBuilder(), new ItemPrice(ItemMoney.IRON, 40)));
        this.add("tnt", ShopCategory.UTILITY, new ItemShop(new ItemBuilder(Material.TNT), new ItemPrice(ItemMoney.GOLD, 4)));
        this.add("ender-pearl", ShopCategory.UTILITY, new ItemShop(new ItemBuilder(Material.ENDER_PEARL), new ItemPrice(ItemMoney.EMERALD, 4)));
        this.add("water-bucket", ShopCategory.UTILITY, new ItemShop(new ItemBuilder(Material.WATER_BUCKET), new ItemPrice(ItemMoney.GOLD, 3)));
        this.add("bridge-egg", ShopCategory.UTILITY, new ItemShop(new ItemBuilder(Material.EGG).nbt().setBoolean(MetadataReferences.BRIDGE_EGG, true).toBuilder(), new ItemPrice(ItemMoney.EMERALD)));
        this.add("magic-milk", ShopCategory.UTILITY, new ItemShop(new ItemBuilder(Material.MILK_BUCKET).nbt().setBoolean(MetadataReferences.MAGIC_MILK, true).toBuilder(), new ItemPrice(ItemMoney.GOLD, 4)));
        this.add("sponge", ShopCategory.UTILITY, new ItemShop(new ItemBuilder(Material.SPONGE, 4).nbt().setBoolean(MetadataReferences.SPONGE, true).toBuilder(), new ItemPrice(ItemMoney.GOLD, 3)));
        this.add("compact-tower", ShopCategory.UTILITY, new ItemShop(new ItemBuilder(Material.CHEST, 1).nbt().setBoolean(MetadataReferences.POPUP_TOWER, true).toBuilder(), new ItemPrice(ItemMoney.IRON, 24)));

//        this.setPriceToFree();
    }

    private void setPriceToFree() {
        this.itemShops.forEach(materialShop -> {
            materialShop.getItems().forEach(itemShop -> {
                itemShop.getPrice().setAmount(0);
            });
        });
    }

    private void add(String name, ShopCategory category, ItemShop... itemShops){
        this.add(name, category, false, itemShops);
    }

    private void add(String name, ShopCategory category, boolean permanent, ItemShop... itemShops){
        this.itemShops.add(new MaterialShop(name, category, permanent, Arrays.asList(itemShops)));
    }

    private void addArmor(String name, int level, ShopCategory category, ItemShop icon, ItemStack helmet, ItemStack chestplate){
        this.itemShops.add(new MaterialArmorShop(name, category, icon, level, true, helmet, chestplate));
    }

    public boolean isArmor(MaterialShop materialShop){
        return materialShop instanceof MaterialArmorShop;
    }

    public MaterialArmorShop getArmorByName(String name){
        return this.itemShops.stream().filter(materialShop -> materialShop.getName().equals(name) && this.isArmor(materialShop))
                .map(materialShop -> (MaterialArmorShop) materialShop).findFirst().orElse(null);
    }

    public ItemPrice getPriceByName(int tier, String name) {
        final ItemShop itemShop = this.getItemShopByName(name);
        if(itemShop != null) {
            return itemShop.getPrice();
        }
        return null;
    }

    public ItemPrice getPriceByTierName(int tier, String name){
        return this.getItemShopByNameTier(tier, name).getPrice();
    }

    public List<MaterialShop> getItemShopByCategory(ShopCategory category) {
        return this.itemShops.stream().filter(itemShop -> itemShop.getCategory() == category).collect(Collectors.toList());
    }

    public Map<Integer, MaterialShop> getItemShopByCategoryToMap(int base, ShopCategory category) {
        Map<Integer, MaterialShop> materialsShop = new HashMap<>();
        int i = base;
        for (MaterialShop materialShop : this.getItemShopByCategory(category)) {
            materialsShop.put(i++, materialShop);
        }
        return materialsShop;
    }

    public ItemShop getItemShopByNameTier(int tier, String name){
        MaterialShop material = this.itemShops.stream().filter(item -> item.getName().equals(name)).findFirst().orElse(null);

        if(material != null) {
            return material.getItem(tier);
        }
        return null;
    }

    public ItemShop getItemShopByName(String name){
        return this.getItemShopByNameTier(0, name);
    }

    public MaterialShop getMaterialShopByName(String name){
        return this.itemShops.stream().filter(materialShop -> materialShop.getName().toLowerCase().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    public MaterialShop getMaterialByItemShop(ItemShop itemShop) {
        return this.itemShops.stream().filter(materialShop -> materialShop.getItems().stream().filter(itemShop1 -> itemShop.getName().equals(itemShop1.getName())).findFirst().orElse(null) != null).findFirst().orElse(null);
    }
}

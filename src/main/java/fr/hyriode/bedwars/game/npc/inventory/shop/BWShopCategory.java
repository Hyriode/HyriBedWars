package fr.hyriode.bedwars.game.npc.inventory.shop;

import fr.hyriode.bedwars.api.player.HyriGameStyle;
import fr.hyriode.bedwars.api.shop.HyriHotbarCategory;
import fr.hyriode.bedwars.game.material.ItemPotionBuilder;
import fr.hyriode.bedwars.game.npc.inventory.shop.pages.hotbar.BWHotbarItems;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.language.HyriLanguageMessage;
import fr.hyriode.bedwars.HyriBedWars;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

public enum BWShopCategory {

    QUICK_BUY(0, "quick_buy", new ItemStack(Material.NETHER_STAR), null),
    BLOCKS(1, "blocks", new ItemStack(Material.HARD_CLAY), HyriHotbarCategory.BLOCKS),
    MELEE(2, "melee", new ItemStack(Material.GOLD_SWORD), new ItemStack(Material.DIAMOND_SWORD), HyriHotbarCategory.MELEE),
    ARMOR(3, "armor", new ItemStack(Material.CHAINMAIL_BOOTS), new ItemStack(Material.DIAMOND_BOOTS), null),
    TOOLS(4, "tools", new ItemStack(Material.STONE_PICKAXE), new ItemStack(Material.DIAMOND_PICKAXE), HyriHotbarCategory.TOOLS),
    RANGED(5, "ranged", new ItemStack(Material.BOW), HyriHotbarCategory.RANGED),
    POTIONS(6, "potions", new ItemStack(Material.BREWING_STAND_ITEM), new ItemPotionBuilder(PotionType.REGEN, 0, 0).withSplash().build(), HyriHotbarCategory.POTIONS),
    UTILITY(7, "utility", new ItemStack(Material.TNT), HyriHotbarCategory.UTILITY),

    ;

    private final int id;
    private final String titleKey;
    private final ItemStack hypixel;
    private final ItemStack hyriode;
    private final HyriHotbarCategory category;

    BWShopCategory(int id, String titleKey, ItemStack hypixel, ItemStack hyriode, HyriHotbarCategory category){
        this.id = id;
        this.titleKey = getTitle(titleKey);
        this.hypixel = hypixel;
        this.hyriode = hyriode;
        this.category = category;
    }

    BWShopCategory(int id, String titleKey, ItemStack hypixel, HyriHotbarCategory category){
        this(id, titleKey, hypixel, hypixel, category);
    }

    public int getId() {
        return id;
    }

    public String getTitleKey() {
        return titleKey;
    }

    public ItemStack getItemStack(HyriGameStyle gameStyle) {
        switch (gameStyle){
            case HYRIODE:
                return this.hyriode.clone();
            case HYPIXEL:
                return this.hypixel.clone();
        }
        return hyriode.clone();
    }

    public ItemStack getItemStack(boolean isHyriode){
        return this.getItemStack(isHyriode ? HyriGameStyle.HYRIODE : HyriGameStyle.HYPIXEL);
    }

    public ItemStack getItemStack(Player player, boolean active, boolean isHyriode) {
        ItemBuilder itemBuilder = new ItemBuilder(this.getItemStack(isHyriode))
                .withName((this == QUICK_BUY ? ChatColor.AQUA : ChatColor.GREEN) + HyriBedWars.getLanguageManager().getValue(player, titleKey) + (this == MELEE && isHyriode ? " & " + RANGED.getTitleLanguage().getForPlayer(player) : ""));
        if(!active)
            itemBuilder.withLore(ChatColor.YELLOW + HyriBedWars.getLanguageManager().getValue(player, "inv.shop.click.view"));

        return itemBuilder.withAllItemFlags().build();
    }

    public HyriLanguageMessage getTitleLanguage(){
        return HyriBedWars.getLanguageManager().getMessage(getTitleKey());
    }

    public HyriHotbarCategory getHotbar() {
        return category;
    }

    private static String getTitle(String title){
        return "inv.shop.navbar."+title+".title";
    }

}

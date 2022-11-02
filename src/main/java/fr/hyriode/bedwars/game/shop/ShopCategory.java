package fr.hyriode.bedwars.game.shop;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.bedwars.api.player.HyriBWPlayer;
import fr.hyriode.bedwars.api.player.style.HyriGameStyle;
import fr.hyriode.bedwars.game.player.hotbar.HotbarCategory;
import fr.hyriode.bedwars.utils.ItemPotionBuilder;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.api.language.HyriLanguageMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

public enum ShopCategory {

    QUICK_BUY(0, "quick-buy", new ItemStack(Material.NETHER_STAR), null),
    BLOCKS(1, "blocks", new ItemStack(Material.HARD_CLAY), HotbarCategory.BLOCKS),
    MELEE(2, "melee", new ItemStack(Material.GOLD_SWORD), new ItemStack(Material.DIAMOND_SWORD), HotbarCategory.MELEE),
    ARMOR(3, "armor", new ItemStack(Material.CHAINMAIL_BOOTS), new ItemStack(Material.DIAMOND_BOOTS), null),
    TOOLS(4, "tools", new ItemStack(Material.STONE_PICKAXE), new ItemStack(Material.DIAMOND_PICKAXE), HotbarCategory.TOOLS),
    RANGED(5, "ranged", new ItemStack(Material.BOW), HotbarCategory.RANGED),
    POTIONS(6, "potions", new ItemStack(Material.BREWING_STAND_ITEM), new ItemPotionBuilder(PotionType.SPEED, 0, 0).build(), HotbarCategory.POTIONS),
    UTILITY(7, "utility", new ItemStack(Material.TNT), HotbarCategory.UTILITY),

    ;

    private final int id;
    private final String titleKey;
    private final ItemStack hypixel;
    private final ItemStack hyriode;
    private final HotbarCategory category;

    ShopCategory(int id, String titleKey, ItemStack hypixel, ItemStack hyriode, HotbarCategory category){
        this.id = id;
        this.titleKey = getTitle(titleKey);
        this.hypixel = hypixel;
        this.hyriode = hyriode;
        this.category = category;
    }

    ShopCategory(int id, String titleKey, ItemStack hypixel, HotbarCategory category){
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
                return new ItemBuilder(this.hyriode.clone()).withAllItemFlags().build();
            case HYPIXEL:
                return new ItemBuilder(this.hypixel.clone()).withAllItemFlags().build();
        }
        return new ItemBuilder(this.hyriode.clone()).withAllItemFlags().build();
    }

    public ItemStack getItemStack(boolean isHyriode){
        return this.getItemStack(isHyriode ? HyriGameStyle.HYRIODE : HyriGameStyle.HYPIXEL);
    }

    public ItemStack getItemStack(Player player, boolean active, boolean isHyriode) {
        ItemBuilder itemBuilder = new ItemBuilder(this.getItemStack(isHyriode))
                .withName((this == QUICK_BUY ? ChatColor.AQUA : ChatColor.GREEN) + this.getTitleLanguage(isHyriode, player));
        if(!active)
            itemBuilder.withLore(ChatColor.YELLOW + HyriLanguageMessage.get("inv.shop.click.view").getValue(player));

        return itemBuilder.withAllItemFlags().build();
    }

    public String getTitleLanguage(boolean isHyriode, Player player){
        return HyriLanguageMessage.get(getTitleKey()).getValue(player)
                + (this == MELEE && isHyriode ? " & " + RANGED.getTitleLanguage(true, player) : "");
    }

    public String getTitleLanguage(Player player){
        HyriBWPlayer bwPlayer = HyriAPI.get().getPlayerManager().getPlayer(player.getUniqueId())
                .getData("bedwars", HyriBWPlayer.class);
        if(bwPlayer != null) {
            return this.getTitleLanguage(bwPlayer.getGameStyle() == HyriGameStyle.HYRIODE, player);
        }
        return this.getTitleLanguage(false, player);
    }

    public HotbarCategory getHotbar() {
        return category;
    }

    private static String getTitle(String title){
        return "shop.inventory."+title+".title";
    }
}

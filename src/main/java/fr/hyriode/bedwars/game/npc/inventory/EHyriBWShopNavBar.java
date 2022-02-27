package fr.hyriode.bedwars.game.npc.inventory;

import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.language.HyriLanguageMessage;
import fr.hyriode.bedwars.HyriBedWars;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public enum EHyriBWShopNavBar {

    QUICK_BUY(0, getTitle("quick_buy"), new ItemBuilder(Material.NETHER_STAR).build()),
    BLOCKS(1, getTitle("blocks"), new ItemBuilder(Material.HARD_CLAY).build()),
    MELEE(2, getTitle("melee"), new ItemBuilder(Material.GOLD_SWORD).build()),
    ARMOR(3, getTitle("armor"), new ItemBuilder(Material.CHAINMAIL_BOOTS).build()),
    TOOLS(4, getTitle("tools"), new ItemBuilder(Material.STONE_PICKAXE).build()),
    RANGED(5, getTitle("ranged"), new ItemBuilder(Material.BOW).build()),
    POTIONS(6, getTitle("potions"), new ItemBuilder(Material.BREWING_STAND_ITEM).build()),
    UTILITY(7, getTitle("utility"), new ItemBuilder(Material.TNT).build()),

    ;

    private final int id;
    private final String titleKey;
    private final ItemStack itemStack;

    EHyriBWShopNavBar(int id, String titleKey, ItemStack itemStack){
        this.id = id;
        this.titleKey = titleKey;
        this.itemStack = itemStack;
    }

    public int getId() {
        return id;
    }

    public String getTitleKey() {
        return titleKey;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public ItemStack getItemStack(Player player, boolean active) {
        ItemBuilder itemBuilder = new ItemBuilder(itemStack.getType()).withName((this == QUICK_BUY ? ChatColor.AQUA : ChatColor.GREEN) + HyriBedWars.getLanguageManager().getValue(player, titleKey));
        if(!active)
            itemBuilder.withLore(ChatColor.YELLOW + HyriBedWars.getLanguageManager().getValue(player, "inv.shop.click.view"));

        return itemBuilder.withItemFlags(ItemFlag.HIDE_ATTRIBUTES).build();
    }

    public HyriLanguageMessage getTitleLanguage(){
        return HyriBedWars.getLanguageManager().getMessage(getTitleKey());
    }

    private static String getTitle(String title){
        return "inv.shop.navbar."+title+".title";
    }

}

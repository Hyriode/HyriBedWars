package fr.hyriode.bedwars.game.npc.inventory.shop.pages.hotbar;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.api.shop.HyriHotbarCategory;
import fr.hyriode.bedwars.game.npc.inventory.shop.BWShopCategory;
import fr.hyriode.bedwars.utils.StringBWUtils;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.language.HyriLanguageMessage;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public enum BWHotbarItems {

    BLOCKS(0, BWShopCategory.BLOCKS),
    MELEE(1, BWShopCategory.MELEE),
    TOOLS(2, BWShopCategory.TOOLS),
    RANGED(3, BWShopCategory.RANGED),
    POTIONS(4, BWShopCategory.POTIONS),
    UTILITY(5, BWShopCategory.UTILITY),
    COMPASS(6, "inv.hotbar.compass.name", "inv.hotbar.compass.lore", new ItemStack(Material.COMPASS), null)
    ;

    private final int id;
    private final String name;
    private final String lore;
    private final ItemStack itemStack;
    private final BWShopCategory category;

    BWHotbarItems(int id, BWShopCategory category){
        this(id, category.getTitleKey(), "inv.hotbar.navbar.lore", category.getItemStack(false), category);
    }

    BWHotbarItems(int id, String name, String lore, ItemStack itemStack, BWShopCategory category){
        this.id = id;
        this.name = name;
        this.lore = lore;
        this.itemStack = itemStack;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public String getKeyName() {
        return name;
    }

    public HyriLanguageMessage getName(){
        return HyriBedWars.getLanguageManager().getMessage(this.name);
    }

    public String getKeyLore() {
        return lore;
    }

    public List<String> getLore(Player player){
        return StringBWUtils.loreToList(HyriBedWars.getLanguageManager().getValue(player, this.lore));
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public ItemStack getItemStack(Player player){
        return new ItemBuilder(this.itemStack.clone())
                .withName(ChatColor.GREEN + this.getName().getForPlayer(player))
                .withLore(this.getLore(player)).build();
    }

    public BWShopCategory getCategory() {
        return category;
    }

    public static BWHotbarItems getById(int id){
        return Arrays.stream(BWHotbarItems.values()).filter(hotbarItems -> hotbarItems.getId() == id).findFirst().orElse(null);
    }

    public HyriHotbarCategory fromAPI() {
        return Arrays.stream(HyriHotbarCategory.values()).filter(hotbarItems -> hotbarItems.getId() == this.id).findFirst().orElse(null);
    }
}

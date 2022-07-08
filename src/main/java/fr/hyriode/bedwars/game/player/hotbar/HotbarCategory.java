package fr.hyriode.bedwars.game.player.hotbar;

import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.bedwars.api.player.HyriBWPlayer;
import fr.hyriode.bedwars.api.player.style.HyriGameStyle;
import fr.hyriode.bedwars.game.shop.ShopCategory;
import fr.hyriode.bedwars.utils.ItemPotionBuilder;
import fr.hyriode.bedwars.utils.StringUtils;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.language.HyriLanguageMessage;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

import java.util.Arrays;
import java.util.List;

public enum HotbarCategory {

    BLOCKS(0, "shop.inventory.blocks.title", "hotbar.inventory.blocks.lore", new ItemStack(Material.HARD_CLAY), true),
    MELEE(1, "shop.inventory.melee.title", "hotbar.inventory.melee.lore", new ItemStack(Material.GOLD_SWORD), new ItemStack(Material.GOLD_SWORD), true),
    TOOLS(2, "shop.inventory.tools.title", "hotbar.inventory.tools.lore", new ItemStack(Material.STONE_PICKAXE), new ItemStack(Material.DIAMOND_PICKAXE), true),
    RANGED(3, "shop.inventory.ranged.title", "hotbar.inventory.ranged.lore", new ItemStack(Material.BOW), true),
    POTIONS(4, "shop.inventory.potions.title", "hotbar.inventory.potions.lore", new ItemStack(Material.BREWING_STAND_ITEM), new ItemPotionBuilder(PotionType.SPEED, 0, 0).build(), true),
    UTILITY(5, "shop.inventory.utility.title", "hotbar.inventory.utility.lore", new ItemStack(Material.TNT), true),
    COMPASS(6, "hotbar.inventory.compass.title", "hotbar.inventory.compass.lore", new ItemStack(Material.COMPASS), false)
    ;

    private final int id;
    private final String name;
    private final String lore;
    private final ItemStack hypixel;
    private final ItemStack hyriode;
    private final boolean canDuplicate;

    HotbarCategory(int id, String name, ItemStack hypixel, boolean duplicate) {
        this(id, name, "inv.hotbar.navbar.lore", hypixel, hypixel, duplicate);
    }

    HotbarCategory(int id, String name, ItemStack hypixel, ItemStack hyriode, boolean duplicate){
        this(id, name, "inv.hotbar.navbar.lore", hypixel, hyriode, duplicate);
    }

    HotbarCategory(int id, String name, String lore, ItemStack hypixel, boolean duplicate){
        this(id, name, lore, hypixel, hypixel, duplicate);
    }

    HotbarCategory(int id, String name, String lore, ItemStack hypixel, ItemStack hyriode, boolean duplicate){
        this.id = id;
        this.name = name;
        this.lore = lore;
        this.hypixel = hypixel;
        this.hyriode = hyriode;
        this.canDuplicate = duplicate;
    }

    public int getId() {
        return id;
    }

    public String getKeyName() {
        return name;
    }

    public HyriLanguageMessage getName(){
        return HyriLanguageMessage.get(this.name);
    }

    public String getKeyLore() {
        return lore;
    }

    public List<String> getLore(IHyriPlayer player){
        return StringUtils.loreToList(HyriLanguageMessage.get(this.lore).getForPlayer(player));
    }

    public ItemStack getItemHypixel() {
        return hypixel.clone();
    }

    public ItemStack getItemHyriode() {
        return hyriode.clone();
    }

    public boolean isCanDuplicate() {
        return canDuplicate;
    }

    public ItemStack getItemStack(IHyriPlayer player){
        HyriBWPlayer hyriPlayer = player.getData("bedwars", HyriBWPlayer.class);
        ItemStack item = hyriPlayer.getGameStyle() == HyriGameStyle.HYPIXEL ? this.hypixel.clone() : this.hyriode.clone();
        return new ItemBuilder(item)
                .withName(ChatColor.GREEN + this.getName().getForPlayer(player))
                .withLore(this.getLore(player)).withAllItemFlags().build();
    }

    public ShopCategory getCategory() {
        try {
            return ShopCategory.valueOf(this.name());
        }catch (Exception e){
            return null;
        }
    }

    public static HotbarCategory getById(int id){
        return Arrays.stream(HotbarCategory.values()).filter(hotbarItems -> hotbarItems.getId() == id).findFirst().orElse(null);
    }
}

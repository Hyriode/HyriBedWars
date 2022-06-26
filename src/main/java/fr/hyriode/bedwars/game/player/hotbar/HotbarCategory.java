package fr.hyriode.bedwars.game.player.hotbar;

import fr.hyriode.api.HyriAPI;
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
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public enum HotbarCategory {

    BLOCKS(0, "blocks", new ItemStack(Material.HARD_CLAY)),
    MELEE(1, "melee", new ItemStack(Material.GOLD_SWORD), new ItemStack(Material.GOLD_SWORD)),
    TOOLS(2, "tools", new ItemStack(Material.STONE_PICKAXE), new ItemStack(Material.DIAMOND_PICKAXE)),
    RANGED(3, "ranged", new ItemStack(Material.BOW)),
    POTIONS(4, "potions", new ItemStack(Material.BREWING_STAND_ITEM), new ItemPotionBuilder(PotionType.REGEN, 0, 0).withSplash().build()),
    UTILITY(5, "utility", new ItemStack(Material.TNT)),
    COMPASS(6, "inv.hotbar.compass.name", "inv.hotbar.compass.lore", new ItemStack(Material.COMPASS))
    ;

    private final int id;
    private final String name;
    private final String lore;
    private final ItemStack hypixel;
    private final ItemStack hyriode;

    HotbarCategory(int id, String name, ItemStack hypixel){
        this(id, name, "inv.hotbar.navbar.lore", hypixel, hypixel);
    }

    HotbarCategory(int id, String name, ItemStack hypixel, ItemStack hyriode){
        this(id, name, "inv.hotbar.navbar.lore", hypixel, hyriode);
    }

    HotbarCategory(int id, String name, String lore, ItemStack hypixel){
        this(id, name, lore, hypixel, hypixel);
    }

    HotbarCategory(int id, String name, String lore, ItemStack hypixel, ItemStack hyriode){
        this.id = id;
        this.name = name;
        this.lore = lore;
        this.hypixel = hypixel;
        this.hyriode = hyriode;
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

    public List<String> getLore(Player player){
        return StringUtils.loreToList(HyriLanguageMessage.get(this.lore).getForPlayer(player));
    }

    public List<String> getLore(UUID uuid){
        return this.getLore(uuid);
    }

    public ItemStack getItemHypixel() {
        return hypixel.clone();
    }

    public ItemStack getItemHyriode() {
        return hyriode.clone();
    }

    public ItemStack getItemStack(IHyriPlayer player){
        HyriBWPlayer hyriPlayer = player.getData("bedwars", HyriBWPlayer.class);
        ItemStack item = hyriPlayer.getGameStyle() == HyriGameStyle.HYPIXEL ? this.hypixel.clone() : this.hyriode.clone();
        return new ItemBuilder(item)
                .withName(ChatColor.GREEN + this.getName().getForPlayer(player))
                .withLore(this.getLore(player.getUniqueId())).build();
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

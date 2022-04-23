package fr.hyriode.bedwars.game.npc.inventory.shop.pages;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.api.player.HyriBWPlayer;
import fr.hyriode.bedwars.api.shop.HyriHotbarCategory;
import fr.hyriode.bedwars.game.npc.inventory.shop.BWShopCategory;
import fr.hyriode.bedwars.game.npc.inventory.shop.pages.hotbar.BWHotbarChoiceGUI;
import fr.hyriode.bedwars.game.npc.inventory.shop.pages.hotbar.BWHotbarItems;
import fr.hyriode.bedwars.utils.StringBWUtils;
import fr.hyriode.hyrame.inventory.HyriInventory;
import fr.hyriode.hyrame.item.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class BWHotbarManagerGUI extends HyriInventory {

    private final HyriBedWars plugin;

    public BWHotbarManagerGUI(HyriBedWars plugin, Player owner) {
        super(owner, "Hotbar Manager", 5*9);
        this.plugin = plugin;
        this.initCategories();
//        this.initGui();
    }

    private void initCategories() {
        HyriBWPlayer player = this.plugin.getGame().getPlayer(this.owner).getAccount();

        this.setItem(0, new ItemBuilder(Material.ARROW)
                .withName(ChatColor.GREEN + HyriBedWars.getLanguageManager().getValue(this.owner, "inv.back"))
                .withLore(ChatColor.GRAY + HyriBedWars.getLanguageManager().getValue(this.owner, "inv.hotbar.back.lore"))
                .build(),
                event -> BWShopQuickBuy.open(this.plugin, this.owner));
        this.setItem(8, new ItemBuilder(Material.BARRIER)
                .withName(ChatColor.RED + HyriBedWars.getLanguageManager().getValue(this.owner, "inv.hotbar.reset.name"))
                .withLore(StringBWUtils.loreToList(HyriBedWars.getLanguageManager().getValue(this.owner, "inv.hotbar.reset.lore")))
                .build(),
                event -> {
                    player.resetHotbar();
                    player.update(this.owner.getUniqueId());
                    open(this.plugin, this.owner);
                });

        int i = 10;
        for (BWHotbarItems category : BWHotbarItems.values()) {
            this.setItem(i, category.getItemStack(this.owner), event -> new BWHotbarChoiceGUI(this.owner, this.plugin, category).open());
            ++i;
        }

        int slotAdd = 27;

        for (int j = 0 ; j < 9 ; ++j){
            this.setItem(j + slotAdd, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 11));
        }

        final Map<HyriHotbarCategory, Integer> hotbar = player.getHotBar();

        hotbar.forEach((category, slot) -> this.setItem(slotAdd + slot, BWHotbarItems.getById(category.getId()).getItemStack(this.owner), event -> {
            player.removeMaterialHotBar(slot);
            player.update(this.owner.getUniqueId());
            open(this.plugin, this.owner);
        }));

//        this.initLine();
    }

    private void initLine(){
        for(int j = 27 ; j < 36 ; ++j){
            byte data = 7;
            this.setItem(j, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, data)
                    .withName(ChatColor.DARK_GRAY + "⬆" + ChatColor.GRAY + " " +
                            HyriBedWars.getLanguageManager().getValue(this.owner, "inv.hotbar.separator.title"))
                    .withLore(ChatColor.DARK_GRAY + "⬇" + ChatColor.GRAY + " " +
                            HyriBedWars.getLanguageManager().getValue(this.owner, "inv.hotbar.separator.lore"))
                    .build());
        }
    }

    public static void open(HyriBedWars plugin, Player player){
        new BWHotbarManagerGUI(plugin, player).open();
    }


}

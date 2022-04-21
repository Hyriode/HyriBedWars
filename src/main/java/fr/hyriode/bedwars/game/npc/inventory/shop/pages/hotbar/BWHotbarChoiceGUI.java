package fr.hyriode.bedwars.game.npc.inventory.shop.pages.hotbar;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.api.player.HyriBWPlayer;
import fr.hyriode.bedwars.api.shop.HyriHotbarCategory;
import fr.hyriode.bedwars.game.npc.inventory.shop.pages.BWHotbarManagerGUI;
import fr.hyriode.hyrame.inventory.HyriInventory;
import fr.hyriode.hyrame.item.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.function.Consumer;

public class BWHotbarChoiceGUI extends HyriInventory {

    private final BWHotbarItems hotbarItems;
    private final HyriBedWars plugin;

    public BWHotbarChoiceGUI(Player owner, HyriBedWars plugin, BWHotbarItems hotbarItems) {
        super(owner, "Hotbar Manager", 45);
        this.hotbarItems = hotbarItems;
        this.plugin = plugin;
        this.initGui();
    }

    private void initGui(){
        HyriBWPlayer player = this.plugin.getGame().getPlayer(this.owner).getAccount();

        final Map<HyriHotbarCategory, Integer> hotbar = player.getHotBar();

        int slotAdd = 27;

        for (int i = 0 ; i < 9 ; ++i){
            this.setItem(i + slotAdd, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 11), this.getClick(player, i));
        }

        hotbar.forEach((category, slot) -> this.setItem(slotAdd + slot, BWHotbarItems.getById(category.getId()).getItemStack(this.owner), this.getClick(player, slot)));

        this.setItem(0, new ItemBuilder(Material.ARROW)
                        .withName(ChatColor.GREEN + HyriBedWars.getLanguageManager().getValue(this.owner, "inv.back"))
                        .withLore(ChatColor.GRAY + HyriBedWars.getLanguageManager().getValue(this.owner, "inv.hotbar.back.lore"))
                        .build(),
                event -> BWHotbarManagerGUI.open(this.plugin, this.owner));
        this.setItem(13, hotbarItems.getItemStack(this.owner));
    }

    private Consumer<InventoryClickEvent> getClick(HyriBWPlayer player, int slot){
        return event -> {
            player.putMaterialHotBar(slot, this.hotbarItems.fromAPI());
            player.update();
            new BWHotbarManagerGUI(this.plugin, this.owner).open();
        };
    }
}

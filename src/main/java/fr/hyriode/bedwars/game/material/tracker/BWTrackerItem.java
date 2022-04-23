package fr.hyriode.bedwars.game.material.tracker;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.utils.MetadataReferences;
import fr.hyriode.bedwars.utils.StringBWUtils;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.item.HyriItem;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.language.HyriLanguageMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BWTrackerItem extends HyriItem<HyriBedWars> {

    public BWTrackerItem(HyriBedWars plugin) {
        super(plugin, "tracker", () -> new HyriLanguageMessage("item.tracker.name"), Material.COMPASS);
    }

    public static ItemStack getItemGUI(Player player) {
        return new ItemBuilder(Material.COMPASS)
                .withName(ChatColor.GREEN + HyriBedWars.getLanguageManager().getValue(player, "inv.shop.quick_buy.tracker.name"))
                .withLore(StringBWUtils.loreToList(HyriBedWars.getLanguageManager().getValue(player, "inv.shop.quick_buy.tracker.lore")))
                .build();
    }

    @Override
    public void onRightClick(IHyrame hyrame, PlayerInteractEvent event) {
        BWTrackerItemGUI.open(event.getPlayer(), this.plugin);
    }

    @Override
    public ItemStack onPreGive(IHyrame hyrame, Player player, int slot, ItemStack itemStack) {
        return new ItemBuilder(super.onPreGive(hyrame, player, slot, itemStack)).withName(HyriBedWars.getLanguageManager().getValue(player, "item.tracker.name")).nbt().setBoolean(MetadataReferences.ISPERMANENT, true).build();
    }
}

package fr.hyriode.bedwars.game.npc.inventory.shop.pages;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.BWGameOre;
import fr.hyriode.bedwars.game.material.OreStack;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import fr.hyriode.bedwars.utils.InventoryBWUtils;
import fr.hyriode.bedwars.utils.StringBWUtils;
import fr.hyriode.hyrame.game.team.HyriGameTeam;
import fr.hyriode.hyrame.inventory.HyriInventory;
import fr.hyriode.hyrame.item.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;

public class BWTrackerGUI extends HyriInventory {

    private final HyriBedWars plugin;
    private final HyriInventory inventoryFrom;

    public BWTrackerGUI(HyriBedWars plugin, Player owner, HyriInventory inventoryFrom) {
        super(owner, HyriBedWars.getLanguageManager().getValue(owner, "inv.tracker.title"), 36);
        this.plugin = plugin;
        this.inventoryFrom = inventoryFrom;
        this.initGui();
    }

    @SuppressWarnings("deprecation")
    private void initGui(){
        int slot = 10;
        for (BWGameTeam team : this.plugin.getGame().getTeamsTracker(this.plugin.getGame().getPlayer(this.owner).getHyriTeam())) {
            if(slot == 17) slot = 19;
            boolean hasPrice = InventoryBWUtils.hasPrice(this.owner, new OreStack(BWGameOre.EMERALD, 2));
            this.setItem(slot, new ItemBuilder(Material.WOOL, 1, team.getColor().getDyeColor().getWoolData())
                    .withName((hasPrice ? ChatColor.GREEN : ChatColor.RED) + HyriBedWars.getLanguageManager().getValue(this.owner, "inv.tracker.name") + " " + team.getDisplayName().getForPlayer(this.owner))
                    .withLore(StringBWUtils.loreToList(HyriBedWars.getLanguageManager().getValue(this.owner, "inv.tracker.lore"))).build(),
                    this.getClick(team));
            ++slot;
        }
        this.setItem(31, new ItemBuilder(Material.ARROW)
                        .withName(ChatColor.GREEN + HyriBedWars.getLanguageManager().getValue(this.owner, "inv.back"))
                        .withLore(ChatColor.GRAY + HyriBedWars.getLanguageManager().getValue(this.owner, "inv.tracker.back.lore")).build(),
                event -> {
                    if (inventoryFrom instanceof BWShopQuickBuy) {
                        BWShopQuickBuy.open(this.plugin, this.owner);
                    }else{
                        this.owner.closeInventory();
                    }
                });
    }

    private Consumer<InventoryClickEvent> getClick(BWGameTeam team){
        return event -> {
            if(this.plugin.getGame().teamsHasBed()) {
                if (InventoryBWUtils.hasPrice(this.owner, new OreStack(BWGameOre.EMERALD, 2))) {
                    owner.playSound(owner.getLocation(), Sound.NOTE_PLING, 0.8F, 2.0F);
                    owner.sendMessage(ChatColor.GREEN + this.getValue(owner, "purchased") + " " + ChatColor.GOLD + "");
                    InventoryBWUtils.removeItem(this.owner, new ItemStack(BWGameOre.EMERALD.getItemStack().getType(), 2));
                    return;
                }
                //TODO l'ajouter au membre
                owner.sendMessage(String.format(ChatColor.RED + this.getValue(owner, "purchased.missing"), StringBWUtils.getPriceAsString(owner, new OreStack(BWGameOre.EMERALD, 2)), StringBWUtils.getCountPriceMissing(owner, new OreStack(BWGameOre.EMERALD, 2))));
                owner.playSound(owner.getLocation(), Sound.ENDERMAN_TELEPORT, 0.8F, 0.1F);
                return;
            }
            this.owner.sendMessage(ChatColor.RED + this.getValue(this.owner, "tracker.bed-dont-destroy"));
        };
    }

    private String getValue(Player player, String key){
        return HyriBedWars.getLanguageManager().getValue(player, "shop." + key);
    }
}

package fr.hyriode.bedwars.game.material.tracker;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.npc.inventory.shop.pages.BWTrackerGUI;
import fr.hyriode.hyrame.inventory.HyriInventory;
import org.bukkit.entity.Player;

public class BWTrackerItemGUI extends HyriInventory {

    public BWTrackerItemGUI(Player owner, HyriBedWars plugin) {
        super(owner, "Tracker & Communcation", 9*3);

        this.setItem(13, BWTrackerItem.getItemGUI(owner), event -> new BWTrackerGUI(plugin, owner, this).open());
    }

    public static void open(Player player, HyriBedWars plugin){
        new BWTrackerItemGUI(player, plugin).open();
    }

}

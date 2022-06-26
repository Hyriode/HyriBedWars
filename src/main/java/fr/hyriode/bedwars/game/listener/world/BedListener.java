package fr.hyriode.bedwars.game.listener.world;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import fr.hyriode.hyrame.listener.HyriListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class BedListener extends HyriListener<HyriBedWars> {

    public BedListener(HyriBedWars plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBedBreak(BlockBreakEvent event) {
        BWGamePlayer player = this.plugin.getGame().getPlayer(event.getPlayer());

        if(event.getBlock().getType() == Material.BED_BLOCK) {
            for (BWGameTeam team : this.plugin.getGame().getBWTeams()) {
                if (team.getConfig().getBaseArea().isInArea(event.getBlock().getLocation())) {
                    event.setCancelled(false);
                    team.breakBed();
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onPickupItemBed(PlayerPickupItemEvent event) {
        if(event.getItem().getItemStack().getType() == Material.BED) {
            event.setCancelled(true);
        }
    }
}

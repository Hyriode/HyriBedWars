package fr.hyriode.bedwars.game.listener.world.team;

import fr.hyriode.api.language.HyriLanguageMessage;
import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import fr.hyriode.hyrame.listener.HyriListener;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class BedListener extends HyriListener<HyriBedWars> {

    public BedListener(HyriBedWars plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBedBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if(player == null || !player.isOnline()) {
            event.setCancelled(true);
            return;
        }
        BWGamePlayer bwPlayer = this.plugin.getGame().getPlayer(player);
        if(bwPlayer == null) {
            event.setCancelled(true);
            return;
        }

        if(event.getBlock().getType() == Material.BED_BLOCK) {
            if(!this.plugin.getGame().isCanBreakBed()) {
                event.setCancelled(true);
                player.sendMessage(HyriLanguageMessage.get("game.bed.break.not-allowed").getValue(player));
                return;
            }

            for (BWGameTeam team : this.plugin.getGame().getBWTeams()) {
                if (team.getConfig().getBaseArea().isInArea(event.getBlock().getLocation())) {
                    if(bwPlayer.getBWTeam().equals(team)){
                        event.setCancelled(true);
                        player.sendMessage(ChatColor.RED + "Vous ne pouvez pas détruire votre lit !");
                        break;
                    }
                    event.setCancelled(false);
                    team.breakBed(bwPlayer);
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

    @EventHandler
    public void onClick(PlayerInteractEvent event){
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.BED_BLOCK
                && !event.getPlayer().isSneaking()
                && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            event.setCancelled(true);
        }
    }
}

package fr.hyriode.bedwars.game.listener.world.team;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import fr.hyriode.hyrame.listener.HyriListener;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

public class ChestListener extends HyriListener<HyriBedWars> {

    public ChestListener(HyriBedWars plugin) {
        super(plugin);
    }

    @EventHandler
    public void onOpenChest(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.CHEST) {
            BWGameTeam playerTeam = this.plugin.getGame().getPlayer(event.getPlayer()).getBWTeam();
            this.plugin.getGame().getBWTeams().forEach(team -> {
                if (team.equals(playerTeam) || team.isEliminated()) return;

                if (team.getConfig().getBaseArea().isInArea(event.getPlayer().getLocation())) {
                    event.setCancelled(true);
                }
            });
        }
    }
}

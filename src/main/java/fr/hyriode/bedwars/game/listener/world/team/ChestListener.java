package fr.hyriode.bedwars.game.listener.world.team;

import fr.hyriode.api.language.HyriLanguageMessage;
import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import fr.hyriode.hyrame.listener.HyriListener;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

public class ChestListener extends HyriListener<HyriBedWars> {

    public ChestListener(HyriBedWars plugin) {
        super(plugin);
    }

    @EventHandler
    public void onOpenChest(PlayerInteractEvent event) {
        Player pl = event.getPlayer();
        if(event.getClickedBlock() != null) {
            BWGamePlayer player = this.plugin.getGame().getPlayer(pl);
            if(player == null) return;

            switch (event.getClickedBlock().getType()){
                case ENDER_CHEST:
                    if(player.isSpectator() || player.isDead()) {
                        event.setCancelled(true);
                    }
                    return;
                case CHEST:
                    BWGameTeam playerTeam = player.getBWTeam();
                    this.plugin.getGame().getBWTeams().forEach(team -> {
                        if (team.equals(playerTeam) || team.isEliminated()) return;

                        if (team.getConfig().getBaseArea().isInArea(event.getPlayer().getLocation())) {
                            pl.sendMessage(ChatColor.RED + HyriLanguageMessage.get("chest.cant-open.team").getValue(player));
                            event.setCancelled(true);
                        }
                    });
            }
        }
    }
}

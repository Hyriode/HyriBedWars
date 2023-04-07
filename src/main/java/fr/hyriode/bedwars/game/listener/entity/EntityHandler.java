package fr.hyriode.bedwars.game.listener.entity;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.manager.pnj.EntityInteractManager;
import fr.hyriode.bedwars.manager.pnj.PNJ;
import fr.hyriode.hyrame.listener.HyriListener;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class EntityHandler extends HyriListener<HyriBedWars> {

    public EntityHandler(HyriBedWars plugin) {
        super(plugin);
    }

//    @EventHandler
//    public void onMove(PlayerMoveEvent event) {
//        final Player player = event.getPlayer();
//
//        this.checkDistance(player, event.getFrom(), event.getTo());
//    }
//
//    @EventHandler(priority = EventPriority.MONITOR)
//    public void onTeleport(PlayerTeleportEvent event) {
//        final Player player = event.getPlayer();
//
//        this.checkDistance(player, event.getFrom(), event.getTo());
//    }
//
//    @EventHandler(priority = EventPriority.MONITOR)
//    public void onRespawn(PlayerRespawnEvent event) {
//        final Player player = event.getPlayer();
//
//        this.checkDistance(player, player.getLocation(), event.getRespawnLocation());
//    }
//
//    @EventHandler(priority = EventPriority.MONITOR)
//    public void onJoin(PlayerJoinEvent event) {
//        final Player player = event.getPlayer();
//
////        for (PNJ npc : EntityInteractManager.getEntities().keySet()) {
////            EntityInteractManager.addPNJ(player, npc);
////        }
//    }
//
//    @EventHandler(priority = EventPriority.MONITOR)
//    public void onQuit(PlayerQuitEvent event) {
//        final Player player = event.getPlayer();
//
//        for (PNJ npc : EntityInteractManager.getEntities().keySet()) {
//            EntityInteractManager.removeNPC(player, npc);
//
//            npc.removePlayer(player);
//        }
//    }

    private void checkDistance(Player player, Location from, Location to) {
        if (from == null || to == null) {
            return;
        }

        final String fromWorld = from.getWorld().getName();
        final String toWorld = to.getWorld().getName();

        if (!fromWorld.equals(toWorld)) {
            return;
        }

        for (PNJ npc : EntityInteractManager.getEntities().keySet()) {
            final Location location = npc.getLocation();
            final String world = location.getWorld().getName();

            if (!world.equals(fromWorld)) {
                continue;
            }

            if (from.distanceSquared(location) > 2500 && to.distanceSquared(location) < 2500) {
                npc.spawn();
            }
        }
    }

}

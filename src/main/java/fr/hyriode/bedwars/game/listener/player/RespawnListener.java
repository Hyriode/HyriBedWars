package fr.hyriode.bedwars.game.listener.player;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.hyrame.listener.HyriListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class RespawnListener extends HyriListener<HyriBedWars> {

    public RespawnListener(HyriBedWars plugin) {
        super(plugin);
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event){
        if(!(event.getEntity() instanceof Player)) return;
        if(!(event.getDamager() instanceof Player)) return;
        BWGamePlayer victim = this.plugin.getGame().getPlayer((Player) event.getEntity());
        BWGamePlayer attacker = this.plugin.getGame().getPlayer((Player) event.getDamager());

        if(attacker.isCountdown(BWGamePlayer.RESPAWN_COUNTDOWN)){
            attacker.removeCountdown(BWGamePlayer.RESPAWN_COUNTDOWN);
        }

        if(victim.isCountdown(BWGamePlayer.RESPAWN_COUNTDOWN)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event){
        if(!(event.getEntity() instanceof Player)) return;
        BWGamePlayer player = this.plugin.getGame().getPlayer((Player) event.getEntity());

        if(player.isCountdown(BWGamePlayer.RESPAWN_COUNTDOWN)) {
            event.setCancelled(true);
        }
    }
}

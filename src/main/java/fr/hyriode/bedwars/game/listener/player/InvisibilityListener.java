package fr.hyriode.bedwars.game.listener.player;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.BWGame;
import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.game.HyriGameState;
import fr.hyriode.hyrame.listener.HyriListener;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

public class InvisibilityListener extends HyriListener<HyriBedWars> {

    public InvisibilityListener(HyriBedWars plugin) {
        super(plugin);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();
        if(from.getX() == to.getX() && from.getY() == to.getY() && from.getZ() == to.getZ()) return;
        Player player = event.getPlayer();
        BWGame game = this.plugin.getGame();
        if(game == null) return;
        BWGamePlayer bwPlayer = game.getPlayer(player);
        if(bwPlayer == null || game.getState() != HyriGameState.PLAYING ||
                (bwPlayer.isSpectator() || bwPlayer.isDead() || !player.hasPotionEffect(PotionEffectType.INVISIBILITY))) return;
        if(!bwPlayer.hasCountdown(BWGamePlayer.INVISIBILITY)) {
            bwPlayer.addCountdown(BWGamePlayer.INVISIBILITY, 10);
            IHyrame.WORLD.get().playEffect(player.getLocation().add(0, 0.05, 0), Effect.FOOTSTEP, 2);
        }
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) return;
        Player playerVictim = (Player) event.getEntity();

        if(playerVictim.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
            playerVictim.removePotionEffect(PotionEffectType.INVISIBILITY);
        }
    }
}
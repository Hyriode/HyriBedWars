package fr.hyriode.bedwars.game.listener.player;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.game.HyriGameState;
import fr.hyriode.hyrame.listener.HyriListener;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
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
        BWGamePlayer bwPlayer = this.plugin.getGame().getPlayer(player);
        if(this.plugin.getGame().getState() != HyriGameState.PLAYING ||
                bwPlayer != null && (bwPlayer.isSpectator() || bwPlayer.isDead() || !player.hasPotionEffect(PotionEffectType.INVISIBILITY))) return;
        if(!bwPlayer.hasCountdown("invisibility")) {
            bwPlayer.addCountdown("invisibility", 10);
            IHyrame.WORLD.get().playEffect(player.getLocation().add(0, 0.05, 0), Effect.FOOTSTEP, 2);
        }

    }
}

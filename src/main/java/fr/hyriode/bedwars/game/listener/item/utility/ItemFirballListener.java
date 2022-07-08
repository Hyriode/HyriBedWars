package fr.hyriode.bedwars.game.listener.item.utility;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.bedwars.utils.InventoryUtils;
import fr.hyriode.bedwars.utils.MetadataReferences;
import fr.hyriode.hyrame.listener.HyriListener;
import net.minecraft.server.v1_8_R3.EntityFireball;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftFireball;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class ItemFirballListener extends HyriListener<HyriBedWars> {

    public ItemFirballListener(HyriBedWars plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onClick(PlayerInteractEvent event){
        ItemStack itemStack = event.getItem();
        if(!MetadataReferences.isMetaItem(MetadataReferences.FIREBALL, itemStack)) return;
        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){
            event.setCancelled(true);

            final Player player = event.getPlayer();
            final BWGamePlayer bwPlayer = this.plugin.getGame().getPlayer(player);

            if(!bwPlayer.hasCountdown(BWGamePlayer.FIREBALL_COUNTDOWN)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10, 1));
                Fireball fireball = player.launchProjectile(Fireball.class);

                fireball.setYield(5F);

                final Vector direction = player.getEyeLocation().getDirection();

                fireball = this.setFireballDirection(fireball, direction);
                fireball.setVelocity(fireball.getDirection().multiply(2));
                if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                    InventoryUtils.removeInHand(event.getPlayer());
                }
                bwPlayer.addCountdown(BWGamePlayer.FIREBALL_COUNTDOWN, 10);
            }
        }
    }

    public Fireball setFireballDirection(Fireball fireball, Vector vector) {
        final EntityFireball fb = ((CraftFireball) fireball).getHandle();

        fb.dirX = vector.getX() * 0.1D;
        fb.dirY = vector.getY() * 0.1D;
        fb.dirZ = vector.getZ() * 0.1D;

        return (Fireball) fb.getBukkitEntity();
    }
}

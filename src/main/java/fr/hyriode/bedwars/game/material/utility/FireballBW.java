package fr.hyriode.bedwars.game.material.utility;

import fr.hyriode.bedwars.game.BWGamePlayer;
import fr.hyriode.bedwars.game.material.BWMaterial;
import fr.hyriode.bedwars.game.material.HyriShopItem;
import fr.hyriode.bedwars.utils.InventoryBWUtils;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.bedwars.HyriBedWars;
import net.minecraft.server.v1_8_R3.EntityFireball;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftFireball;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class FireballBW extends HyriShopItem<HyriBedWars> {

    public FireballBW(HyriBedWars plugin) {
        super(plugin, BWMaterial.FIREBALL);
    }

    @Override
    public void onRightClick(IHyrame hyrame, PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final BWGamePlayer bwPlayer = this.plugin.getGame().getPlayer(player);

        if(!bwPlayer.isCooldownFireball()) {
            bwPlayer.setCooldownFireball(true);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10, 1));
            Fireball fireball = player.launchProjectile(Fireball.class);

            fireball.setYield(5F);

            final Vector direction = player.getEyeLocation().getDirection();

            fireball = this.setFireballDirection(fireball, direction);
            fireball.setVelocity(fireball.getDirection().multiply(2));
            if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                ItemStack item = new ItemStack(event.getItem());
                item.setAmount(1);
                InventoryBWUtils.removeItem(event.getPlayer(), item);
            }
            Bukkit.getScheduler().runTaskLater(this.plugin, () -> bwPlayer.setCooldownFireball(false), 10L);
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

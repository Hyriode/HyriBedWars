package fr.hyriode.bedwars.game.npc.inventory.shop.utility;

import fr.hyriode.bedwars.game.BWGameOre;
import fr.hyriode.bedwars.game.npc.inventory.shop.BWShopCategory;
import fr.hyriode.bedwars.game.npc.inventory.shop.material.HyriShopItem;
import fr.hyriode.bedwars.game.npc.inventory.shop.material.OreStack;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.bedwars.HyriBedWars;
import net.minecraft.server.v1_8_R3.EntityFireball;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftFireball;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class FireballBW extends HyriShopItem<HyriBedWars> {

    public FireballBW(HyriBedWars plugin) {
        super(plugin, "fireball", Material.FIREBALL, BWShopCategory.UTILITY, new OreStack(BWGameOre.IRON, 40));
    }

    @Override
    public void onRightClick(IHyrame hyrame, PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Fireball fireball = player.launchProjectile(Fireball.class);
        fireball.setYield(2F);
        Vector direction = player.getEyeLocation().getDirection();
        fireball = this.setFireballDirection(fireball, direction);
        fireball.setVelocity(fireball.getDirection().multiply(2));
    }

    public Fireball setFireballDirection(Fireball fireball, Vector vector) {
        EntityFireball fb = ((CraftFireball) fireball).getHandle();
        fb.dirX = vector.getX() * 0.1D;
        fb.dirY = vector.getY() * 0.1D;
        fb.dirZ = vector.getZ() * 0.1D;
        return (Fireball) fb.getBukkitEntity();
    }
}

package fr.hyriode.bedwars.game.npc.inventory.shop.utility;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.item.HyriItem;
import fr.hyriode.bedwars.HyriBedWars;
import net.minecraft.server.v1_8_R3.EntityFireball;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftFireball;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class FireballBW extends HyriItem<HyriBedWars> {

    public FireballBW(HyriBedWars plugin) {
        super(plugin, "fireball", () -> HyriBedWars.getLanguageManager().getMessage("shop.item.fireball.name"), Material.FIREBALL);
    }

    @Override
    public void onRightClick(IHyrame hyrame, PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Location location = player.getEyeLocation();
        Fireball fireball = location.getWorld().spawn(location, Fireball.class);

        fireball.setYield(1F);
        this.setFireballDirection(fireball, location.getDirection());
    }

    public Fireball setFireballDirection(Fireball fireball, Vector vector) {
        EntityFireball fb = ((CraftFireball) fireball).getHandle();
        fb.dirX = vector.getX() * 0.2D;
        fb.dirY = vector.getY() * 0.2D;
        fb.dirZ = vector.getZ() * 0.2D;
        return (Fireball) fb.getBukkitEntity();
    }
}

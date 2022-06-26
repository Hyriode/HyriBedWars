package fr.hyriode.bedwars.manager.pnj;

import fr.hyriode.hyrame.IHyrame;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;

public class PNJ {

    private Location location;
    private Class<? extends Entity> classEntity;
    private Entity entity;

    public PNJ(Location location, Class<? extends Entity> classEntity){
        this.location = location;
        this.classEntity = classEntity;
    }

    public PNJ spawn(){
        this.entity = IHyrame.WORLD.get().spawn(location, this.classEntity);
        EntityLiving handle = ((CraftLivingEntity) this.entity).getHandle();

        handle.getDataWatcher().watch(15, (byte) 1);
        return this;
    }

    public Entity getEntity() {
        return entity;
    }

}

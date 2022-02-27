package fr.hyriode.bedwars.game.npc.inventory.shop.utility.entity;

import fr.hyriode.hyrame.game.team.HyriGameTeam;
import fr.hyriode.bedwars.HyriBedWars;
import net.minecraft.server.v1_8_R3.EntityIronGolem;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Location;

import java.lang.reflect.Field;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;


public class DreamDefenderEntity extends EntityIronGolem {

    private final HyriBedWars plugin;
    private HyriGameTeam team;

    @SuppressWarnings("ALL")
    public DreamDefenderEntity(HyriBedWars plugin, HyriGameTeam team, World world){
        super(world);
        this.plugin = plugin;
        this.team = team;
        try {
            Field bField = PathfinderGoalSelector.class.getDeclaredField("b");
            bField.setAccessible(true);
            Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
            cField.setAccessible(true);
            bField.set(this.goalSelector, new UnsafeList());
            bField.set(this.targetSelector, new UnsafeList());
            cField.set(this.goalSelector, new UnsafeList());
            cField.set(this.targetSelector, new UnsafeList());
        } catch (IllegalAccessException | NoSuchFieldException e1) {
            e1.printStackTrace();
        }
        ((Navigation) this.getNavigation()).a(true);
        this.goalSelector.a(1, new PathfinderGoalFloat(this));
        this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, 1.5D, false));
        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
        this.goalSelector.a(3, new PathfinderGoalRandomStroll(this, 1D));
        this.goalSelector.a(4, new PathfinderGoalRandomLookaround(this));
//        HyriBWConfiguration.Team teamConfig = this.plugin.getConfiguration().getTeam(this.team.getName());
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, 20, true, false, player -> {
            if (player == null) return false;
            return ((EntityHuman)player).isAlive() && !team.contains(((EntityHuman)player).getUniqueID())
//                    && !team.getArena().isReSpawning(((EntityHuman)player).getUniqueID())
//                    && !teamConfig.getBaseArea().isSpectator(((EntityHuman)player).getUniqueID())
                    ;
        }));
        this.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget(this, DreamDefenderEntity.class, 20, true, false, golem -> {
            if (golem == null) return false;
            return ((DreamDefenderEntity)golem).getTeam() != team;
        }));
//        this.targetSelector.a(4, new PathfinderGoalNearestAttackableTarget(this, Silverfish.class, 20, true, false, sf -> {
//            if (sf == null) return false;
//            return ((Silverfish)sf).getTeam() != team;
//        }));
    }

    public static LivingEntity spawn(HyriBedWars plugin, Location loc, HyriGameTeam bedWarsTeam, double speed, double health, int despawn) {
        WorldServer mcWorld = ((CraftWorld) loc.getWorld()).getHandle();
        DreamDefenderEntity customEnt = new DreamDefenderEntity(plugin, bedWarsTeam, mcWorld);

        customEnt.getAttributeInstance(GenericAttributes.maxHealth).setValue(health);
        customEnt.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(speed);
        customEnt.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        ((CraftLivingEntity) customEnt.getBukkitEntity()).setRemoveWhenFarAway(false);
        customEnt.setCustomNameVisible(true);

        customEnt.setCustomName("bilabilou");
        mcWorld.addEntity(customEnt, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return (LivingEntity) customEnt.getBukkitEntity();
    }

    @Override
    protected void dropDeathLoot(boolean flag, int i) {

    }

    @Override
    public void die() {
        super.die();
        team = null;
//        VersionCommon.api.getVersionSupport().getDespawnablesList().remove(this.getUniqueID());
    }

    @Override
    public void die(DamageSource damagesource) {
        super.die(damagesource);
        team = null;
//        VersionCommon.api.getVersionSupport().getDespawnablesList().remove(this.getUniqueID());
    }

    public HyriGameTeam getTeam() {
        return team;
    }

}

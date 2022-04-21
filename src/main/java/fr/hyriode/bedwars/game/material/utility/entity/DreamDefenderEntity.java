package fr.hyriode.bedwars.game.material.utility.entity;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.BWGamePlayer;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import fr.hyriode.hyrame.game.team.HyriGameTeam;
import net.minecraft.server.v1_8_R3.EntityIronGolem;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Location;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Silverfish;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.lang.reflect.Field;


public class DreamDefenderEntity extends EntityIronGolem {

    private BWGameTeam team;

    @SuppressWarnings("ALL")
    public DreamDefenderEntity(BWGameTeam team, HyriBedWars plugin, World world){
        super(world);
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

        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, 20, true, false, player -> {
            if (player == null) return false;
            BWGamePlayer bwPlayer = plugin.getGame().getPlayer(((EntityHuman)player).getUniqueID());
            return ((EntityHuman)player).isAlive() && !team.contains(((EntityHuman)player).getUniqueID())
                    && !bwPlayer.isSpectator() && !bwPlayer.isDead()
//                    && !team.getArena().isReSpawning(((EntityHuman)player).getUniqueID())
//                    && !teamConfig.getBaseArea().isSpectator(((EntityHuman)player).getUniqueID())
                    ;
        }));
        this.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget(this, DreamDefenderEntity.class, 20, true, false, golem -> {
            if (golem == null) return false;
            return ((DreamDefenderEntity)golem).getTeam() != team;
        }));
        this.targetSelector.a(4, new PathfinderGoalNearestAttackableTarget(this, BedBugEntity.class, 20, true, false, sf -> {
            if (sf == null) return false;
            return ((BedBugEntity)sf).getTeam() != team;
        }));
    }

    public static void spawn(Location loc, HyriBedWars plugin, BWGameTeam team) {
        WorldServer mcWorld = ((CraftWorld) loc.getWorld()).getHandle();
        DreamDefenderEntity customEnt = new DreamDefenderEntity(team, plugin, mcWorld);

        customEnt.getAttributeInstance(GenericAttributes.maxHealth).setValue(120);
        customEnt.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.2D);
        customEnt.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        ((CraftLivingEntity) customEnt.getBukkitEntity()).setRemoveWhenFarAway(false);
        customEnt.setCustomNameVisible(true);

        customEnt.setCustomName(customEnt.getHealth() + " PV");
        mcWorld.addEntity(customEnt, CreatureSpawnEvent.SpawnReason.CUSTOM);
        new Despawnable((LivingEntity) customEnt.getBukkitEntity(),
                plugin, team, 240, "").startTime();
        plugin.getGame().addDreamDefender(customEnt);
    }

    @Override
    protected void dropDeathLoot(boolean flag, int i) {}

    @Override
    public void die() {
        super.die();
        this.team = null;
    }

    @Override
    public void die(DamageSource damagesource) {
        super.die(damagesource);
        this.team = null;
    }

    public BWGameTeam getTeam() {
        return team;
    }

}

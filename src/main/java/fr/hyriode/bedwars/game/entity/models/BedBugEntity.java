package fr.hyriode.bedwars.game.entity.models;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.entity.Despawnable;
import fr.hyriode.bedwars.game.entity.EntityCustom;
import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import fr.hyriode.bedwars.host.BWEntityValues;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.lang.reflect.Field;

public class BedBugEntity extends EntitySilverfish {

    private BWGameTeam team;

    @SuppressWarnings("ALL")
    public BedBugEntity(World world, HyriBedWars plugin, BWGameTeam team) {
        super(world);
        if (team == null) return;
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
        this.team = team;
        this.goalSelector.a(1, new PathfinderGoalFloat(this));
        this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this,1.9D, false));
        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
        this.goalSelector.a(3, new PathfinderGoalRandomStroll(this, 2D));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, 20, true, false, player -> {
            if (player == null) return false;
            BWGamePlayer bwPlayer = plugin.getGame().getPlayer(((EntityHuman)player).getUniqueID());
            return ((EntityHuman)player).isAlive() && !team.contains(((EntityHuman)player).getUniqueID())
                    && !bwPlayer.isSpectator() && !bwPlayer.isDead();
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
        BedBugEntity customEnt = new BedBugEntity(mcWorld, plugin, team);

        customEnt.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        customEnt.getAttributeInstance(GenericAttributes.maxHealth).setValue(BWEntityValues.BED_BUG_MAX_HEALTH.get());
        customEnt.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.2D);
        customEnt.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(0.5D);
        ((CraftLivingEntity)customEnt.getBukkitEntity()).setRemoveWhenFarAway(false);
        customEnt.setCustomName("");
        customEnt.setCustomNameVisible(true);
        mcWorld.addEntity(customEnt, CreatureSpawnEvent.SpawnReason.CUSTOM);

        new Despawnable((LivingEntity) customEnt.getBukkitEntity(), plugin, team, BWEntityValues.BED_BUG_TIME_ALIVE.get()).startTime();
        HyriBedWars.getEntityManager().addEntity(customEnt);
    }

    public static EntityCustom getEntityCustom() {
        return new EntityCustom() {
            @Override
            public Material getIcon() {
                return Material.SNOW_BALL;
            }

            @Override
            public String getEntityName() {
                return "bed-bug";
            }
        };
    }

    public BWGameTeam getTeam() {
        return team;
    }

    @Override
    public void die(DamageSource damagesource) {
        super.die(damagesource);
        team = null;
    }

    @Override
    public void die() {
        super.die();
        team = null;
    }

}

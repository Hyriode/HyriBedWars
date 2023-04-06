package fr.hyriode.bedwars.manager.pnj;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.hologram.Hologram;
import fr.hyriode.hyrame.npc.NPCInteractCallback;
import fr.hyriode.hyrame.packet.PacketUtil;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PNJ {

    private final Location location;
    private final Class<? extends EntityCreature> classEntity;
    private final List<Player> players = new ArrayList<>();
    private final Hologram hologram;
    private boolean visibleAll = true;
    private EntityCreature entity;
    private NPCInteractCallback callback;

    public PNJ(Location location, Class<? extends EntityCreature> classEntity, Hologram hologram){
        this.location = location;
        this.classEntity = classEntity;
        this.hologram = hologram;
    }

    public PNJ setVisibleAll(boolean visible) {
        this.visibleAll = visible;
        return this;
    }

    public boolean isVisibleAll() {
        return visibleAll;
    }

    public PNJ removePlayer(Player player){
        this.players.remove(player);
        return this;
    }

    public PNJ addPlayer(Player player){
        this.players.add(player);
        return this;
    }

    public PNJ setInteractCallback(NPCInteractCallback callback) {
        this.callback = callback;
        return this;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public PNJ spawn(Player player){
        try {
            this.entity = this.classEntity.getConstructor(World.class).newInstance(((CraftWorld) IHyrame.WORLD.get()).getHandle());
        } catch (Exception e) {
            e.printStackTrace();
            return this;
        }
        this.entity.setPositionRotation(this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(), this.location.getPitch());
        this.entity.getDataWatcher().watch(15, (byte) 1);
        NBTTagCompound nbt = new NBTTagCompound();
        this.entity.c(nbt);
        nbt.setBoolean("Silent", true);
        this.entity.f(nbt);

        List<Packet<?>> packets = new ArrayList<>();

        packets.add(new PacketPlayOutSpawnEntityLiving(this.entity));
        packets.add(new PacketPlayOutEntityHeadRotation(this.entity, (byte) (this.location.getYaw() * 256.0F / 360.0F)));

        for (Packet<?> packet : packets) {
            PacketUtil.sendPacket(player, packet);
        }
        this.hologram.addReceiver(player);

        return this;
    }

    public List<Packet<?>> getDestroyPacket() {
        return Collections.singletonList(new PacketPlayOutEntityDestroy(this.getId()));
    }

    public void spawn() {
        if(!visibleAll) {
            for (Player player : players) {
                spawn(player);
            }
        } else {
            for (Player player : Bukkit.getOnlinePlayers()) {
                spawn(player);
            }
        }
    }

    public EntityCreature getEntity() {
        return entity;
    }

    public NPCInteractCallback getInteractCallback() {
        return this.callback;
    }

    public Hologram getHologram() {
        return this.hologram;
    }

    public int getId() {
        return this.entity.getId();
    }

    public Location getLocation() {
        return this.entity.getBukkitEntity().getLocation();
    }

    public enum Type {
        NPC(null),
        VILLAGER(EntityVillager.class),
        BLAZE(EntityBlaze.class),
        ;

        private final Class<? extends EntityCreature> classEntity;

        Type(Class<? extends EntityCreature> classEntity) {
            this.classEntity = classEntity;
        }

        public Class<? extends EntityCreature> getClassEntity() {
            return classEntity;
        }
    }
}

package fr.hyriode.bedwars.game.entity;

import fr.hyriode.bedwars.game.entity.models.BedBugEntity;
import fr.hyriode.bedwars.game.entity.models.DreamDefenderEntity;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityTypes;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class EntityManager {

    private final List<EntityLiving> entities = new ArrayList<>();

    public EntityManager(){
        this.registerEntity("BedBug", 60, BedBugEntity.class);
        this.registerEntity("DreamDefender", 99, DreamDefenderEntity.class);
    }

    private void registerEntity(String name, int id, Class<?> customClass) {
        try {
            ArrayList<Map<?, ?>> dataMap = new ArrayList<>();
            for (Field f : EntityTypes.class.getDeclaredFields()) {
                if (!f.getType().getSimpleName().equals(Map.class.getSimpleName())) continue;
                f.setAccessible(true);
                dataMap.add((Map<?, ?>) f.get(null));
            }
            if (dataMap.get(2).containsKey(id)) {
                dataMap.get(0).remove(name);
                dataMap.get(2).remove(id);
            }
            Method method = EntityTypes.class.getDeclaredMethod("a", Class.class, String.class, Integer.TYPE);
            method.setAccessible(true);
            method.invoke(null, customClass, name, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<EntityLiving> getEntities() {
        return entities;
    }

    public List<EntityLiving> getEntitiesByClass(Class<? extends EntityLiving> clazz){
        return this.entities.stream().filter(entityLiving -> entityLiving.getClass() == clazz).collect(Collectors.toList());
    }

    public List<DreamDefenderEntity> getDreamDefenders(){
        return this.getEntitiesByClass(DreamDefenderEntity.class).stream()
                .map(entityLiving -> (DreamDefenderEntity) entityLiving).collect(Collectors.toList());
    }

    public List<BedBugEntity> getBedBugs(){
        return this.getEntitiesByClass(BedBugEntity.class).stream()
                .map(entityLiving -> (BedBugEntity) entityLiving).collect(Collectors.toList());
    }

    public EntityLiving getEntityByUUID(UUID uuid){
        return this.getEntities().stream().filter(entityLiving -> entityLiving.getUniqueID().equals(uuid))
                .findFirst().orElse(null);
    }

    public DreamDefenderEntity getDreamDefenderByUUID(UUID uuid){
        return this.getDreamDefenders().stream().filter(entityLiving -> entityLiving.getUniqueID().equals(uuid))
                .findFirst().orElse(null);
    }

    public BedBugEntity getBedBugByUUID(UUID uuid){
        return this.getBedBugs().stream().filter(entityLiving -> entityLiving.getUniqueID().equals(uuid))
                .findFirst().orElse(null);
    }

    public void addEntity(EntityLiving entity) {
        this.entities.add(entity);
    }
}

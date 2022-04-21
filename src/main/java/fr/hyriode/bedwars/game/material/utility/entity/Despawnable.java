package fr.hyriode.bedwars.game.material.utility.entity;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;

import java.util.Arrays;

public class Despawnable {

    private LivingEntity entity;
    private String namePath;
    private BWGameTeam team;
    private int despawn = 250;
    private HyriBedWars plugin;

    public Despawnable(LivingEntity entity, HyriBedWars plugin, BWGameTeam team, int despawn, String namePath) {
        this.entity = entity;
        this.plugin = plugin;
        if (entity == null) return;
        this.team = team;
        this.namePath = namePath;
        if (despawn != 0) {
            this.despawn = despawn;
        }

        entity.setHealth((float) entity.getMaxHealth());
        this.setName();
    }

    public void refresh(int taskId) {
        if (entity.isDead() || entity == null || team == null || team.getBaseArea() == null) {
            if (entity != null){
                entity.damage(entity.getHealth()+100);
                Bukkit.getScheduler().cancelTask(taskId);
            }
            return;
        }
        this.setName();
        despawn--;
        if (despawn == 0) {
            entity.damage(entity.getHealth()+100);
            Bukkit.getScheduler().cancelTask(taskId);
        }
    }

    private void setName() {
        int length = 30;
        int percentage = (int) (this.getEntity().getHealth()*100/this.getEntity().getMaxHealth());
        int health = percentage * length / 100;
        String name = despawn + "s " + ChatColor.GRAY + "[" + (percentage > 60 ? ChatColor.GREEN : percentage > 30 ? ChatColor.GOLD : ChatColor.RED) + this.getHealthBar(length, "|") + ChatColor.GRAY + "]";
        if (team != null) {
            name = this.team.getColor().getChatColor().toString() + name;
        }
        this.entity.setCustomName(name);
    }

    private String getHealthBar(int length, String symbol){
        int percentage = (int) (this.getEntity().getHealth()*100/this.getEntity().getMaxHealth());
        int health = percentage * length / 100;

        StringBuilder result = new StringBuilder();
        for(int i = 0; i < length;++i){
            if(i == health)
                result.append(ChatColor.WHITE);
            result.append(symbol);
        }
        return result.toString();
    }

//    private String getHealthBar(){
//        String[] point = "|||||||||| ".split("");
//        int health = (int) (this.getEntity().getHealth() / 10);
//        point[health] = ChatColor.WHITE + point[health];
//        StringBuilder result = new StringBuilder();
//        for (String s : point) {
//            result.append(s);
//        }
//        return result.substring(0, result.length() - 1);
//    }

    public LivingEntity getEntity() {
        return entity;
    }

    public BWGameTeam getTeam() {
        return team;
    }

    public int getDespawn() {
        return despawn;
    }

    public void destroy(){
        if (getEntity() != null){
            getEntity().damage(Integer.MAX_VALUE);
        }
        this.team = null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LivingEntity) return ((LivingEntity) obj).getUniqueId().equals(entity.getUniqueId());
        return false;
    }

    public void startTime(){
        int[] taskIds = {0};
        taskIds[0] = Bukkit.getScheduler().runTaskTimer(this.plugin, () -> this.refresh(taskIds[0]), 0L, 20).getTaskId();
    }
}

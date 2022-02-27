package fr.hyriode.bedwars.game.listener;

import fr.hyriode.hyrame.game.util.HyriDeadScreen;
import fr.hyriode.hyrame.item.ItemNBT;
import fr.hyriode.hyrame.listener.HyriListener;
import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.BWGamePlayer;
import fr.hyriode.bedwars.game.npc.inventory.shop.BWMaterial;
import net.minecraft.server.v1_8_R3.ItemArmor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class BWPlayerListener extends HyriListener<HyriBedWars> {
    public BWPlayerListener(HyriBedWars plugin) {
        super(plugin);
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onDrinkPotion(PlayerItemConsumeEvent event){
        if(event.getItem().getType() == Material.POTION){
            System.out.println("true");
            PotionMeta meta = (PotionMeta) event.getItem().getItemMeta();
            System.out.println(meta);
            System.out.println(meta.getCustomEffects().get(0));
            if(meta.getCustomEffects().get(0).getType().getName().equals(PotionEffectType.INVISIBILITY.getName())){
                System.out.println("OUI");
                this.plugin.getGame().getPlayer(event.getPlayer().getUniqueId()).clearArmor();
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        plugin.getGame().getPlayer(event.getPlayer().getUniqueId()).giveArmor();
                    }
                }.runTaskLater(this.plugin, meta.getCustomEffects().get(0).getDuration());
            }
//            switch (name){
//                case PotionEffectType.JUMP.getName():
//                    break;
//            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event){
        System.out.println("Drop");
        Item item = event.getItemDrop();
        for (BWMaterial material : BWMaterial.values()) {
            if(material.isUpgradable()){
                for(int i = 0 ; i < material.getItemUpgradable().getMaxTier(); ++i){
                    ItemStack itemUpgradable = material.getItemUpgradable().getTierItem(i).getItemStack();
                    if(item.getItemStack().getType() == itemUpgradable.getType()) {
                        event.setCancelled(true);
                        return;
                    }
                }
            }

            if(item.getItemStack().getType() == material.getItemShop().getItem().getItemStack().getType() && material.getItemShop().getItem().isPermanent()){
                event.setCancelled(true);
                return;
            }
        }
        ItemNBT nbt = new ItemNBT(item.getItemStack());

        if(event.getItemDrop() instanceof ItemArmor){
            event.setCancelled(true);
        }

        if(nbt.hasTag("IsPermanent") && nbt.getBoolean("IsPermanent")){
            System.out.println("Drop Perma");
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onBreakBed(BlockBreakEvent event){
        if(event.getBlock().getType() == Material.BED_BLOCK){
            System.out.println("WA");
        }
    }

    @EventHandler
    public void onInteract(InventoryClickEvent event){
        if(event.getSlotType() == InventoryType.SlotType.ARMOR){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event){
        if(!(event.getEntity() instanceof Player)) return;
        Player victim = (Player) event.getEntity();
        if(event.getCause() == EntityDamageEvent.DamageCause.VOID){
            event.setCancelled(true);
            this.kill(victim);
        }
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event){
        if(!(event.getEntity() instanceof Player)) return;
        Player victim = (Player) event.getEntity();

        if(event.getDamager() instanceof Player) {
            Player damager = (Player) event.getDamager();
            if (victim.getHealth() < 0){
                event.setCancelled(true);
                this.kill(victim);
            }
        }
    }

    private void kill(Player victim){
        victim.teleport(this.plugin.getConfiguration().getKillLoc());
        victim.getInventory().clear();
        victim.setAllowFlight(true);
        victim.setFlying(true);
        victim.setGameMode(GameMode.ADVENTURE);
        victim.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20*5, 0, true, false));
        HyriDeadScreen.create(this.plugin, victim, 5, () -> {
            victim.setFallDistance(0);
            victim.setFlying(false);
            victim.setAllowFlight(false);
            victim.getActivePotionEffects().forEach(potionEffect -> victim.removePotionEffect(potionEffect.getType()));
            victim.teleport(this.plugin.getConfiguration().getTeam(this.getPlayer(victim).getTeam().getName()).getRespawnLocation());
        });

    }

    private BWGamePlayer getPlayer(Player player){
        return this.plugin.getGame().getPlayer(player.getUniqueId());
    }
}

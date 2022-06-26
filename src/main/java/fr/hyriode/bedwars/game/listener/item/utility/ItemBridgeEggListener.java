package fr.hyriode.bedwars.game.listener.item.utility;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.entity.models.BedBugEntity;
import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.bedwars.utils.MetadataReferences;
import fr.hyriode.hyrame.item.ItemNBT;
import fr.hyriode.hyrame.listener.HyriListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class ItemBridgeEggListener extends HyriListener<HyriBedWars> {

    public ItemBridgeEggListener(HyriBedWars plugin) {
        super(plugin);
    }

    @EventHandler
    public void onLaunch(ProjectileLaunchEvent event){
        if(event.getEntity() instanceof Egg){
            Egg egg = (Egg) event.getEntity();
            if(!(egg.getShooter() instanceof Player)) return;
            Player player = (Player) egg.getShooter();
            ItemStack itemEgg = player.getItemInHand();
            boolean isBridgeEgg = MetadataReferences.isMetaItem(MetadataReferences.BRIDGE_EGG, itemEgg);

            if(!isBridgeEgg){
                event.setCancelled(true);
                return;
            }

            int[] taskId = {0};
            taskId[0] = Bukkit.getScheduler().runTaskTimer(this.plugin, () -> {
                if(egg.isDead()) {
                    Bukkit.getScheduler().cancelTask(taskId[0]);
                    return;
                }

                final Location loc = egg.getLocation().clone();

                Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                    final Block block = loc.clone().getBlock();
                    if(this.placeBlock(block.getRelative(0, -2, 0), player, itemEgg)
                            && this.placeBlock(block.getRelative(-1, -2, 0), player, itemEgg)
                            && this.placeBlock(block.getRelative(0, -2, -1), player, itemEgg)){
                        //Play sound
                    }
                }, 1L);
            }, 3L, 1L).getTaskId();
            Bukkit.getScheduler().runTaskLater(this.plugin, egg::remove, 20L);
        }
    }

    @EventHandler
    public void onHitEgg(PlayerEggThrowEvent event){
        event.setHatching(false);
    }

    @SuppressWarnings("deprecation")
    private boolean placeBlock(Block b, Player player, ItemStack itemEgg){
        if(b.getType() == Material.AIR) {
            b.setType(Material.WOOL);
            BlockPlaceEvent event = new BlockPlaceEvent(b, b.getState(), b, itemEgg, player, true);
            Bukkit.getPluginManager().callEvent(event);
            if(!event.isCancelled()) {
                b.setData(this.getPlayer(player).getTeam().getColor().getDyeColor().getWoolData());
                b.getWorld().playSound(b.getLocation(), Sound.CHICKEN_EGG_POP, 2.0F, 1.0F);
                return true;
            }
            b.setType(Material.AIR);
        }
        return false;
    }

    private BWGamePlayer getPlayer(Player player){
        return this.plugin.getGame().getPlayer(player.getUniqueId());
    }



}

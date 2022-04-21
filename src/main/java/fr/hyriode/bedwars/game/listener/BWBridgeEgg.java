package fr.hyriode.bedwars.game.listener;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.BWGamePlayer;
import fr.hyriode.hyrame.item.ItemNBT;
import fr.hyriode.hyrame.listener.HyriListener;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;

public class BWBridgeEgg extends HyriListener<HyriBedWars> {

    public BWBridgeEgg(HyriBedWars plugin) {
        super(plugin);
    }

    @EventHandler
    public void onLaunch(ProjectileLaunchEvent event){
        if(event.getEntity() instanceof Egg){
            Egg egg = (Egg) event.getEntity();
            if(!(egg.getShooter() instanceof Player)) return;
            Player player = (Player) egg.getShooter();
            ItemStack itemEgg = player.getItemInHand();
            boolean isBridgeEgg = new ItemNBT(itemEgg).hasTag("BridgeEgg");

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
                    if (player.getLocation().distance(loc) <= 4.0D) return;

                    final Block block = loc.clone().getBlock();

                    this.placeBlock(block.getRelative(0, -1, 0), player, itemEgg);
                    this.placeBlock(block.getRelative(-1, -1, 0), player, itemEgg);
                    this.placeBlock(block.getRelative(0, -1, -1), player, itemEgg);
                }, 2L);

            }, 2L, 1L).getTaskId();
            Bukkit.getScheduler().runTaskLater(this.plugin, egg::remove, 20L);
        }
    }

    @SuppressWarnings("deprecation")
    private void placeBlock(Block b, Player player, ItemStack itemEgg){
        if(b.getType() == Material.AIR) {
            BlockPlaceEvent event = new BlockPlaceEvent(b, b.getState(), b, itemEgg, player, true);
            Bukkit.getPluginManager().callEvent(event);
            if(!event.isCancelled()) {
                b.setType(Material.WOOL);
                b.setData(this.getPlayer(player).getTeam().getColor().getDyeColor().getWoolData());
                b.getWorld().playSound(b.getLocation(), Sound.CHICKEN_EGG_POP, 2.0F, 1.0F);
            }
        }
    }

    private BWGamePlayer getPlayer(Player player){
        return this.plugin.getGame().getPlayer(player.getUniqueId());
    }

}

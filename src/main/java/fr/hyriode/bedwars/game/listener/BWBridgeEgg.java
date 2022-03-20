package fr.hyriode.bedwars.game.listener;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.BWGamePlayer;
import fr.hyriode.hyrame.item.ItemNBT;
import fr.hyriode.hyrame.listener.HyriListener;
import io.netty.channel.local.LocalAddress;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class BWBridgeEgg extends HyriListener<HyriBedWars> {

    public BWBridgeEgg(HyriBedWars plugin) {
        super(plugin);
    }

    @EventHandler
    public void onMove(ProjectileLaunchEvent event){

        if(event.getEntity() instanceof Egg){
            Egg egg = (Egg) event.getEntity();
            Player player = (Player) egg.getShooter();
            ItemStack itemEgg = player.getItemInHand();
            boolean isBridgeEgg = new ItemNBT(itemEgg).hasTag("BridgeEgg");
            if(!isBridgeEgg){
                event.setCancelled(true);
            }else{
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        if(egg.isDead()){
                            cancel();
                        }else{
                            Location loc = egg.getLocation().clone();
                            new BukkitRunnable(){
                                @Override
                                public void run() {
                                    if (player.getLocation().distance(loc) > 4.0D) {

                                        Block b2 = loc.clone().subtract(0.0D, 1.0D, 0.0D).getBlock();
                                        if (b2.getType() == Material.AIR) {
                                            b2.setType(Material.WOOL);
                                            Bukkit.getPluginManager().callEvent(new BlockPlaceEvent(b2, b2.getState(), b2, itemEgg, player, true));
                                            b2.setData(getPlayer(player).getTeam().getColor().getDyeColor().getWoolData());
//                                               getArena().addPlacedBlock(b2);
                                            loc.getWorld().playSound(b2.getLocation(), Sound.CHICKEN_EGG_POP, 2.0F, 1.0F);
                                        }

                                        Block b3 = loc.clone().subtract(1.0D, 1.0D, 0.0D).getBlock();
                                        if (b3.getType() == Material.AIR) {
                                            b3.setType(Material.WOOL);
                                            Bukkit.getPluginManager().callEvent(new BlockPlaceEvent(b3, b3.getState(), b3, itemEgg, player, true));
                                            b3.setData(getPlayer(player).getTeam().getColor().getDyeColor().getWoolData());
//                                                nms.setBlockTeamColor(b3, getTeamColor());
//                                                getArena().addPlacedBlock(b3);
//                                                Bukkit.getPluginManager().callEvent(new EggBridgeBuildEvent(getTeamColor(), getArena(), b3));
//                                                loc.getWorld().playEffect(b3.getLocation(), nms.eggBridge(), 3);
//                                                Sounds.playSound("egg-bridge-block", getPlayer());
                                        }

                                        Block b4 = loc.clone().subtract(0.0D, 1.0D, 1.0D).getBlock();
                                        if (b4.getType() == Material.AIR) {
                                            b4.setType(Material.WOOL);
                                            Bukkit.getPluginManager().callEvent(new BlockPlaceEvent(b4, b4.getState(), b4, itemEgg, player, true));

                                            b4.setData(getPlayer(player).getTeam().getColor().getDyeColor().getWoolData());
                                        }
                                    }
                                }
                            }.runTaskLater(plugin, 2L);
                        }
                    }
                }.runTaskTimer(this.plugin, 2L, 1L);
            }

        }

    }

    private BWGamePlayer getPlayer(Player player){
        return this.plugin.getGame().getPlayer(player.getUniqueId());
    }

}

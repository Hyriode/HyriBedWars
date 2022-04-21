package fr.hyriode.bedwars.game.listener;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.hyrame.listener.HyriListener;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

public class BWPlaceWaterListener extends HyriListener<HyriBedWars> {

    public BWPlaceWaterListener(HyriBedWars plugin) {
        super(plugin);
    }

    @EventHandler
    public void onBucket(PlayerBucketEmptyEvent event){
        Block block = event.getBlockClicked().getLocation().add(0, 1, 0).getBlock();
        BlockPlaceEvent e = new BlockPlaceEvent(block, block.getState(), block, event.getItemStack(), event.getPlayer(), true);
        Bukkit.getPluginManager().callEvent(e);
        event.setCancelled(e.isCancelled());
        if(!e.isCancelled()) {
            Player player = event.getPlayer();
            Bukkit.getScheduler().runTaskLater(this.plugin, () -> player.getInventory().setItem(player.getInventory().getHeldItemSlot(), null), 1L);
        }
    }

    @EventHandler
    public void onFill(PlayerBucketFillEvent event){
        event.setCancelled(true);
    }
}

package fr.hyriode.bedwars.game.listener.world;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import fr.hyriode.bedwars.utils.MetadataReferences;
import fr.hyriode.hyrame.listener.HyriListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class WorldListener extends HyriListener<HyriBedWars> {

    public WorldListener(HyriBedWars plugin) {
        super(plugin);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event){

        if(!event.getBlock().hasMetadata(MetadataReferences.PLACEBYPLAYER)){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event){
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Location loc = block.getLocation();

        for (BWGameTeam team : this.plugin.getGame().getBWTeams()) {
            if(team.getConfig().getBaseProtectArea().isInArea(loc)){
                event.setCancelled(true);
                return;
            }
        }

        if(MetadataReferences.isMetaItem(MetadataReferences.SPONGE, player.getItemInHand())) return;

        if(!event.isCancelled() && block.getType() != Material.AIR) {
            block.setMetadata(MetadataReferences.PLACEBYPLAYER, new FixedMetadataValue(this.plugin, player.getUniqueId()));
        }
    }

    @EventHandler
    public void onPlaceWater(PlayerBucketEmptyEvent event){
        Block block = event.getBlockClicked().getRelative(event.getBlockFace());
        BlockPlaceEvent e = new BlockPlaceEvent(block, block.getState(), event.getBlockClicked(), event.getItemStack(), event.getPlayer(), true);
        Bukkit.getPluginManager().callEvent(e);
        if(e.isCancelled()){
            event.setCancelled(true);
            return;
        }
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> event.getPlayer().getInventory().setItemInHand(null), 1L);
    }
}

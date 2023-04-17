package fr.hyriode.bedwars.game.listener.world;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.config.BWConfiguration;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import fr.hyriode.bedwars.host.BWMapValues;
import fr.hyriode.bedwars.utils.MetadataReferences;
import fr.hyriode.bedwars.utils.NBTGetter;
import fr.hyriode.hyrame.item.ItemNBT;
import fr.hyriode.hyrame.listener.HyriListener;
import fr.hyriode.hyrame.utils.Area;
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

import java.util.ArrayList;
import java.util.List;

public class WorldListener extends HyriListener<HyriBedWars> {

    public WorldListener(HyriBedWars plugin) {
        super(plugin);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event){
        if(BWMapValues.BREAK_WORLD.get()) {
            Location loc = event.getBlock().getLocation();
            BWConfiguration config = this.plugin.getConfiguration();
            List<Location> generators = new ArrayList<>(config.getDiamondGeneratorLocations());

            generators.addAll(config.getEmeraldGeneratorLocations());

            for (BWGameTeam team : this.plugin.getGame().getBWTeams()) {
                if (team.getConfig().getBaseAreaProtection().isInArea(loc)) {
                    event.setCancelled(true);
                    break;
                }
            }
            for (Area area : config.getProtectionArea()) {
                if(area.isInArea(loc)){
                    event.setCancelled(true);
                    return;
                }
            }

            for (Location locGenerator : generators) {
                if(new Area(locGenerator.clone().subtract(3, 3, 3), locGenerator.clone().add(2, 3, 2)).isInArea(loc)){
                    event.setCancelled(true);
                    return;
                }
            }
            return;
        }
        if(!event.getBlock().hasMetadata(MetadataReferences.PLACEBYPLAYER)){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event){
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Location loc = block.getLocation();
        BWConfiguration config = this.plugin.getConfiguration();

        for (BWGameTeam team : this.plugin.getGame().getBWTeams()) {
            if(team.getConfig().getBaseAreaProtection().isInArea(loc)){
                event.setCancelled(true);
                return;
            }
        }
        List<Location> generators = new ArrayList<>(config.getDiamondGeneratorLocations());
        generators.addAll(config.getEmeraldGeneratorLocations());

        for (Location locGenerator : generators) {
            if(new Area(locGenerator.clone().subtract(3, 3, 3), locGenerator.clone().add(2, 3, 2)).isInArea(loc)){
                event.setCancelled(true);
                return;
            }
        }

        for (Area area : this.plugin.getConfiguration().getProtectionArea()) {
            if(area.isInArea(loc)){
                event.setCancelled(true);
                return;
            }
        }

        if(!this.plugin.getConfiguration().getGameArea().asArea().isInArea(loc)){
            event.setCancelled(true);
            return;
        }

        if(MetadataReferences.isMetaItem(MetadataReferences.SPONGE, player.getItemInHand())) return;

        if(!event.isCancelled() && block.getType() != Material.AIR) {
            block.setMetadata(MetadataReferences.PLACEBYPLAYER, new FixedMetadataValue(this.plugin, player.getUniqueId()));
            NBTGetter nbt = new NBTGetter(event.getItemInHand());
            nbt.getNBTMap().forEach((tag, value) -> block.setMetadata(tag, new FixedMetadataValue(this.plugin, value)));
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

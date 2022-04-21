package fr.hyriode.bedwars.game.material.utility.popuptower;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;

public class Tower {

    private final Block chest;
    private final Player player;

    protected Tower(Block block, Player player){
        this.chest = block;
        this.player = player;
    }

    @SuppressWarnings("deprecation")
    protected void placeBlock(Material material, String xyz, int data){
        int x = Integer.parseInt(xyz.split(", ")[0]);
        int y = Integer.parseInt(xyz.split(", ")[1]);
        int z = Integer.parseInt(xyz.split(", ")[2]);
        Location loc = this.chest.getRelative(x, y, z).getLocation();
        BlockPlaceEvent event = new BlockPlaceEvent(loc.getBlock(), loc.getBlock().getState(), loc.getBlock(), null, this.player, true);
        Bukkit.getPluginManager().callEvent(event);
        if(!event.isCancelled() && loc.getBlock().getType() == Material.AIR) {
            loc.getBlock().setType(material);
            loc.getBlock().setData((byte) data);
        }
    }

}

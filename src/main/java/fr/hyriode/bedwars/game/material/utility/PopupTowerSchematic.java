package fr.hyriode.bedwars.game.material.utility;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.BWGamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class PopupTowerSchematic {

    private final HyriBedWars plugin;
    private final BWGamePlayer player;
    private final Location origin;
    private final ItemStack itemStack;

    public PopupTowerSchematic(HyriBedWars plugin, BWGamePlayer player, Location origin, ItemStack itemStack){
        this.plugin = plugin;
        this.player = player;
        this.origin = origin;
        this.itemStack = itemStack;
    }

    public void placeTower(){
        for(int y = 0 ; y < 5 ;++y){
            placeSquare(y);
        }
    }

    private void placeSquare(int y){
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> placeLine(origin.getBlockX() - 2, origin.getBlockY(), origin.getBlockZ() + 2, 4, y, 0), 1L);
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> placeLine(origin.getBlockX() + 2, origin.getBlockY(), origin.getBlockZ() + 2, 0, y, -4), 5L);
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> placeLine(origin.getBlockX() + 2, origin.getBlockY(), origin.getBlockZ() - 2, -4, y, 0), 10L);
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> placeLine(origin.getBlockX() - 2, origin.getBlockY(), origin.getBlockZ() - 2, 0, y, 4), 15L);
    }

    private void placeLine(int oX, int oY, int oZ, int sX, int sY, int sZ){
        int i = 0;
        for(int y = oY; sY < 0 ? y >= oY + sY : y <= oY + sY;){
            for(int x = oX ; sX < 0 ? x >= oX + sX : x <= oX + sX;){
                for(int z = oZ ; sZ < 0 ? z >= oZ + sZ : z <= oZ + sZ;){
                    ++i;
                    if(i > 40){
                        break;
                    }
                    int finalX = x;
                    int finalY = y;
                    int finalZ = z;
                    Bukkit.getScheduler().runTaskLater(this.plugin, () -> placeBlock(Material.WOOL, new Location(origin.getWorld(), finalX, finalY, finalZ)), 2L + ((i & 0x1) == 0 ? i - 1 : i));
                    if(sZ < 0) --z; else ++z;
                }
                if(sX < 0) --x; else ++x;
            }
            if(sY < 0) --y; else ++y;
        }
    }

    @SuppressWarnings("ALL")
    private void placeBlock(Material material, Location loc){
        BlockPlaceEvent event = new BlockPlaceEvent(loc.getBlock(), loc.getBlock().getState(), loc.getBlock(), this.itemStack, this.player.getPlayer(), true);
        Bukkit.getPluginManager().callEvent(event);
        if(!event.isCancelled()) {
            loc.getBlock().setType(material);
            loc.getBlock().setData(this.player.getTeam().getColor().getDyeColor().getWoolData());
        }
    }

}

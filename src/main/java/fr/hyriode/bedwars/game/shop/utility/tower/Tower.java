package fr.hyriode.bedwars.game.shop.utility.tower;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.utils.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;

public abstract class Tower {

    protected List<Pair<Location, MaterialData>> blocks = new ArrayList<>();
    protected final HyriBedWars plugin;
    protected final Block origin;
    protected final BWGamePlayer player;

    public Tower(HyriBedWars plugin, Block origin, BWGamePlayer player){
        this.plugin = plugin;
        this.origin = origin;
        this.player = player;
    }

    public abstract int getDataLadder();

    protected void add(int x, int y, int z, MaterialData material){
        this.blocks.add(new Pair<>(new Location(IHyrame.WORLD.get(), x, y, z), material));
    }

    @SuppressWarnings("deprecation")
    protected void add(int x, int y, int z){
        this.add(x, y, z, new MaterialData(Material.WOOL, this.player.getTeam().getColor().getDyeColor().getWoolData()));
    }

    @SuppressWarnings("deprecation")
    protected void addLadder(int x, int y, int z){
        this.add(x, y, z, new MaterialData(Material.LADDER, (byte) this.getDataLadder()));
    }

    protected void playSound(Location loc){
        loc.getWorld().playSound(loc, Sound.CHICKEN_EGG_POP, 0.5F, 1.0F);
    }

    @SuppressWarnings("deprecation")
    protected boolean placeBlock(Location loc, MaterialData data){
        Block block = loc.getBlock();
        Player player = this.player.getPlayer();
        if(block.getType() != Material.AIR) return false;
        block.setType(data.getItemType());

        BlockPlaceEvent event = new BlockPlaceEvent(block, block.getState(), block, null, player, true);
        Bukkit.getPluginManager().callEvent(event);

        if(!event.isCancelled()) {
            block.setData(data.getData());
            return true;
        }
        block.setType(Material.AIR);
        return false;
    }

    public void placeTower() {
//        this.origin.setType(Material.AIR);
        int i = 0;
        boolean skip = false;
        for (Pair<Location, MaterialData> blockData : this.blocks) {
            Location loc = this.origin.getRelative(
                    blockData.getKey().getBlockX(),
                    blockData.getKey().getBlockY(),
                    blockData.getKey().getBlockZ()).getLocation();
            skip = !skip;
            Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                if(this.placeBlock(loc, blockData.getValue())){
                    this.playSound(loc);
                }
            }, skip ? i : --i);
            ++i;

        }
    }
}

package fr.hyriode.bedwars.game.material.utility.popuptower;

import fr.hyriode.bedwars.HyriBedWars;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TowerSouth extends Tower{

    private BukkitTask task;
    private final Player player;

    public TowerSouth(Location loc, Block chest, int color, Player p, HyriBedWars plugin) {
        super(chest, p);
        this.player = p;
        ItemStack itemStack = p.getInventory().getItemInHand();
        if (itemStack.getAmount() > 1) {
            itemStack.setAmount(itemStack.getAmount() - 1);
        } else {
            p.getInventory().setItemInHand(null);
        }

        HashMap<Location, Boolean> locations = new HashMap<>();

        List<String> relloc = new ArrayList<>();
        relloc.add("1, 0, 2");
        relloc.add("2, 0, 1");
        relloc.add("2, 0, 0");
        relloc.add("1, 0, -1");
        relloc.add("0, 0, -1");
        relloc.add("-1, 0, -1");
        relloc.add("-2, 0, 0");
        relloc.add("-2, 0, 1");
        relloc.add("-1, 0, 2");
        relloc.add("0, 0, 0, ladder3");
        relloc.add("1, 1, 2");
        relloc.add("2, 1, 1");
        relloc.add("2, 1, 0");
        relloc.add("1, 1, -1");
        relloc.add("0, 1, -1");
        relloc.add("-1, 1, -1");
        relloc.add("-2, 1, 0");
        relloc.add("-2, 1, 1");
        relloc.add("-1, 1, 2");
        relloc.add("0, 1, 0, ladder3");
        relloc.add("1, 2, 2");
        relloc.add("2, 2, 1");
        relloc.add("2, 2, 0");
        relloc.add("1, 2, -1");
        relloc.add("0, 2, -1");
        relloc.add("-1, 2, -1");
        relloc.add("-2, 2, 0");
        relloc.add("-2, 2, 1");
        relloc.add("-1, 2, 2");
        relloc.add("0, 2, 0, ladder3");
        relloc.add("0, 3, 2");
        relloc.add("1, 3, 2");
        relloc.add("2, 3, 1");
        relloc.add("2, 3, 0");
        relloc.add("1, 3, -1");
        relloc.add("0, 3, -1");
        relloc.add("-1, 3, -1");
        relloc.add("-2, 3, 0");
        relloc.add("-2, 3, 1");
        relloc.add("-1, 3, 2");
        relloc.add("0, 3, 0, ladder3");
        relloc.add("0, 4, 2");
        relloc.add("1, 4, 2");
        relloc.add("2, 4, 1");
        relloc.add("2, 4, 0");
        relloc.add("1, 4, -1");
        relloc.add("0, 4, -1");
        relloc.add("-1, 4, -1");
        relloc.add("-2, 4, 0");
        relloc.add("-2, 4, 1");
        relloc.add("-1, 4, 2");
        relloc.add("0, 4, 0, ladder3");
        relloc.add("2, 5, -1");
        relloc.add("2, 5, 0");
        relloc.add("2, 5, 1");
        relloc.add("2, 5, 2");
        relloc.add("1, 5, -1");
        relloc.add("1, 5, 0");
        relloc.add("1, 5, 1");
        relloc.add("1, 5, 2");
        relloc.add("0, 5, -1");
        relloc.add("0, 5, 1");
        relloc.add("0, 5, 2");
        relloc.add("-1, 5, -1");
        relloc.add("0, 5, 0, ladder3");
        relloc.add("-1, 5, 0");
        relloc.add("-1, 5, 1");
        relloc.add("-1, 5, 2");
        relloc.add("-2, 5, -1");
        relloc.add("-2, 5, 0");
        relloc.add("-2, 5, 1");
        relloc.add("-2, 5, 2");
        relloc.add("3, 5, 2");
        relloc.add("3, 6, 2");
        relloc.add("3, 7, 2");
        relloc.add("3, 6, 1");
        relloc.add("3, 6, 0");
        relloc.add("3, 5, -1");
        relloc.add("3, 6, -1");
        relloc.add("3, 7, -1");
        relloc.add("2, 5, -2");
        relloc.add("2, 6, -2");
        relloc.add("2, 7, -2");
        relloc.add("1, 6, -2");
        relloc.add("0, 5, -2");
        relloc.add("0, 6, -2");
        relloc.add("0, 7, -2");
        relloc.add("-1, 6, -2");
        relloc.add("-2, 5, -2");
        relloc.add("-2, 6, -2");
        relloc.add("-2, 7, -2");
        relloc.add("-3, 5, 2");
        relloc.add("-3, 6, 2");
        relloc.add("-3, 7, 2");
        relloc.add("-3, 6, 1");
        relloc.add("-3, 6, 0");
        relloc.add("-3, 5, -1");
        relloc.add("-3, 6, -1");
        relloc.add("-3, 7, -1");
        relloc.add("2, 5, 3");
        relloc.add("2, 6, 3");
        relloc.add("2, 7, 3");
        relloc.add("1, 6, 3");
        relloc.add("0, 5, 3");
        relloc.add("0, 6, 3");
        relloc.add("0, 7, 3");
        relloc.add("-1, 6, 3");
        relloc.add("-2, 5, 3");
        relloc.add("-2, 6, 3");
        relloc.add("-2, 7, 3");

//        System.out.println("TowerSouth");
//        StringBuilder output = new StringBuilder();
//
//        for (String s : relloc) {
//            output.append("relloc.put(new Location(IHyrame.WORLD.get(), ").append(s.replace(", ladder3", "")).append("), " + s.contains("ladder") + ");\n");
//        }
//        System.out.println(output);

        int[] i = { 0 };
        this.task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            loc.getWorld().playSound(loc, Sound.CHICKEN_EGG_POP, 1.0F, 0.5F);
            if (relloc.size() + 1 == i[0] + 1) {
                this.task.cancel();
                return;
            }
            String c1 = relloc.get(i[0]);
            if (c1.contains("ladder")) {
                int ldata = Integer.parseInt(c1.split("ladder")[1]);
                this.placeBlock(Material.LADDER, c1, ldata);
            } else {
                this.placeBlock(Material.WOOL, c1, color);
            }
            if (relloc.size() + 1 == i[0] + 2) {
                this.task.cancel();
                return;
            }
            String c2 = relloc.get(i[0] + 1);
            if (c2.contains("ladder")) {
                int ldata = Integer.parseInt(c2.split("ladder")[1]);
                this.placeBlock(Material.LADDER, c2, ldata);
            } else {
                this.placeBlock(Material.WOOL, c2, color);
            }
            i[0] = i[0] + 2;
        }, 0L, 1L);
    }

}

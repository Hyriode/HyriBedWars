package fr.hyriode.bedwars.game.shop.utility.tower.models;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.bedwars.game.shop.utility.tower.Tower;
import org.bukkit.block.Block;

public class TowerNorth extends Tower {
    public TowerNorth(HyriBedWars plugin, Block origin, BWGamePlayer player) {
        super(plugin, origin, player);

        this.add(-1, 0, -2);
        this.add(-2, 0, -1);
        this.add(-2, 0, 0);
        this.add(-1, 0, 1);
        this.add(0, 0, 1);
        this.add(1, 0, 1);
        this.add(2, 0, 0);
        this.add(2, 0, -1);
        this.add(1, 0, -2);
        this.addLadder(0, 0, 0);
        this.add(-1, 1, -2);
        this.add(-2, 1, -1);
        this.add(-2, 1, 0);
        this.add(-1, 1, 1);
        this.add(0, 1, 1);
        this.add(1, 1, 1);
        this.add(2, 1, 0);
        this.add(2, 1, -1);
        this.add(1, 1, -2);
        this.addLadder(0, 1, 0);
        this.add(-1, 2, -2);
        this.add(-2, 2, -1);
        this.add(-2, 2, 0);
        this.add(-1, 2, 1);
        this.add(0, 2, 1);
        this.add(1, 2, 1);
        this.add(2, 2, 0);
        this.add(2, 2, -1);
        this.add(1, 2, -2);
        this.addLadder(0, 2, 0);
        this.add(0, 3, -2);
        this.add(-1, 3, -2);
        this.add(-2, 3, -1);
        this.add(-2, 3, 0);
        this.add(-1, 3, 1);
        this.add(0, 3, 1);
        this.add(1, 3, 1);
        this.add(2, 3, 0);
        this.add(2, 3, -1);
        this.add(1, 3, -2);
        this.addLadder(0, 3, 0);
        this.add(0, 4, -2);
        this.add(-1, 4, -2);
        this.add(-2, 4, -1);
        this.add(-2, 4, 0);
        this.add(-1, 4, 1);
        this.add(0, 4, 1);
        this.add(1, 4, 1);
        this.add(2, 4, 0);
        this.add(2, 4, -1);
        this.add(1, 4, -2);
        this.addLadder(0, 4, 0);
        this.add(-2, 5, 1);
        this.add(-2, 5, 0);
        this.add(-2, 5, -1);
        this.add(-2, 5, -2);
        this.add(-1, 5, 1);
        this.add(-1, 5, 0);
        this.add(-1, 5, -1);
        this.add(-1, 5, -2);
        this.add(0, 5, 1);
        this.add(0, 5, -1);
        this.add(0, 5, -2);
        this.add(1, 5, 1);
        this.addLadder(0, 5, 0);
        this.add(1, 5, 0);
        this.add(1, 5, -1);
        this.add(1, 5, -2);
        this.add(2, 5, 1);
        this.add(2, 5, 0);
        this.add(2, 5, -1);
        this.add(2, 5, -2);
        this.add(-3, 5, -2);
        this.add(-3, 6, -2);
        this.add(-3, 7, -2);
        this.add(-3, 6, -1);
        this.add(-3, 6, 0);
        this.add(-3, 5, 1);
        this.add(-3, 6, 1);
        this.add(-3, 7, 1);
        this.add(-2, 5, 2);
        this.add(-2, 6, 2);
        this.add(-2, 7, 2);
        this.add(-1, 6, 2);
        this.add(0, 5, 2);
        this.add(0, 6, 2);
        this.add(0, 7, 2);
        this.add(1, 6, 2);
        this.add(2, 5, 2);
        this.add(2, 6, 2);
        this.add(2, 7, 2);
        this.add(3, 5, -2);
        this.add(3, 6, -2);
        this.add(3, 7, -2);
        this.add(3, 6, -1);
        this.add(3, 6, 0);
        this.add(3, 5, 1);
        this.add(3, 6, 1);
        this.add(3, 7, 1);
        this.add(-2, 5, -3);
        this.add(-2, 6, -3);
        this.add(-2, 7, -3);
        this.add(-1, 6, -3);
        this.add(0, 5, -3);
        this.add(0, 6, -3);
        this.add(0, 7, -3);
        this.add(1, 6, -3);
        this.add(2, 5, -3);
        this.add(2, 6, -3);
        this.add(2, 7, -3);
    }

    @Override
    public int getDataLadder() {
        return 2;
    }
}
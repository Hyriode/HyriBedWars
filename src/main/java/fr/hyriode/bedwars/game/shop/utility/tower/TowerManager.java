package fr.hyriode.bedwars.game.shop.utility.tower;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.bedwars.game.shop.utility.tower.models.TowerEast;
import fr.hyriode.bedwars.game.shop.utility.tower.models.TowerNorth;
import fr.hyriode.bedwars.game.shop.utility.tower.models.TowerSouth;
import fr.hyriode.bedwars.game.shop.utility.tower.models.TowerWest;
import org.bukkit.block.Block;

import java.util.Arrays;

public class TowerManager {

    public static void placeTower(HyriBedWars plugin, Block origin, BWGamePlayer player, Direction direction){
        try {
            Tower tower = direction.getTower().getConstructor(HyriBedWars.class, Block.class, BWGamePlayer.class)
                    .newInstance(plugin, origin, player);
            tower.placeTower();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public enum Direction{
        NORTH(2, TowerNorth.class),
        SOUTH(3, TowerSouth.class),
        WEST(4, TowerWest.class),
        EAST(5, TowerEast.class),

        ;

        private final Class<? extends Tower> tower;
        private final int direction;

        Direction(int direction, Class<? extends Tower> tower){
            this.direction = direction;
            this.tower = tower;
        }

        public int getDirection() {
            return direction;
        }

        public Class<? extends Tower> getTower() {
            return tower;
        }

        public static Direction getDirection(int data) {
            return Arrays.stream(Direction.values()).filter(direction1 -> direction1.getDirection() == data).findFirst().orElse(null);
        }
    }

}

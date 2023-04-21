package fr.hyriode.bedwars.game.test;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.config.BWConfiguration;
import fr.hyriode.bedwars.game.shop.ItemMoney;
import fr.hyriode.bedwars.game.team.BWGameTeamColor;
import fr.hyriode.hyrame.game.waitingroom.HyriWaitingRoom;
import fr.hyriode.hyrame.utils.AreaWrapper;
import fr.hyriode.hyrame.utils.LocationWrapper;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Arrays;

public class TestConfiguration {

    public static BWConfiguration getPoseidonTrio(){
            HyriWaitingRoom.Config wr = new HyriWaitingRoom.Config(
                    new LocationWrapper(0.5, 190, 0.5, 90, 0),
                    new LocationWrapper(24, 250, -54),
                    new LocationWrapper(-78, 180, 50),
                    new LocationWrapper(-9.5D, 190, 5.5D, -117.5F, 0));
            wr.addLeaderboard(new HyriWaitingRoom.Config.Leaderboard(HyriBedWars.ID, "bedwars-experience", new LocationWrapper(-5.5, 189, -12.5)));
            wr.addLeaderboard(new HyriWaitingRoom.Config.Leaderboard(HyriBedWars.ID, "kills", new LocationWrapper(-1.5, 189, -6.5)));
            wr.addLeaderboard(new HyriWaitingRoom.Config.Leaderboard(HyriBedWars.ID, "victories", new LocationWrapper(-1.5, 189, 7.5)));
            wr.addLeaderboard(new HyriWaitingRoom.Config.Leaderboard(HyriBedWars.ID, "beds-destroyed", new LocationWrapper(-5.5, 189, 13.5)));
            return new BWConfiguration(
                    wr,
                    new AreaWrapper(
                            new LocationWrapper(-89, 145, 89),
                            new LocationWrapper(89, 43, -89)),
                    new ArrayList<>(), // Protect Areas
                    Arrays.asList(//diamond
                            new LocationWrapper(0.5, 101, -75.5),
                            new LocationWrapper(76.5, 101, 0.5),
                            new LocationWrapper(0.5, 101, 76.5),
                            new LocationWrapper(-75.5, 101, 0.5)
                    ),
                    Arrays.asList(
                            new LocationWrapper(-8.5, 101, 9.5),
                            new LocationWrapper(-8.5, 101, -8.5),
                            new LocationWrapper(9.5, 101, -8.5),
                            new LocationWrapper(9.5, 101, 9.5)
                    ),
                    Arrays.asList(
                            new BWConfiguration.Team(
                                    BWGameTeamColor.BLUE.getName(),
                                    new AreaWrapper(new LocationWrapper(-84, 117, -27),
                                            new LocationWrapper(-66, 87, -44)), //base2
                                    new AreaWrapper(new LocationWrapper(-83, 98, -32),
                                            new LocationWrapper(-73, 104, -44)), //protect2
                                    new LocationWrapper(-75.5, 99, -41.5), //generator
                                    new LocationWrapper(-79.5, 99, -33.5, -90, 0), //shop
                                    new LocationWrapper(-79.5, 99, -35.5, -90, 0), //upgrade
                                    new LocationWrapper(-75.5, 99, -37.5, 0, 0) //spawn
                            ),
                            new BWConfiguration.Team(
                                    BWGameTeamColor.RED.getName(),
                                    new AreaWrapper(new LocationWrapper(-85, 117, 26),
                                            new LocationWrapper(-67, 87, 44)), //base2
                                    new AreaWrapper(new LocationWrapper(-83, 98, 32),
                                            new LocationWrapper(-73, 104, 143)), //protect2
                                    new LocationWrapper(-75.5, 99, 42.5), //generator
                                    new LocationWrapper(-79.5, 99, 36.5, -90, 0), //shop
                                    new LocationWrapper(-79.5, 99, 34.5, -90, 0), //upgrade
                                    new LocationWrapper(-75.5, 99, 38.5, 180, 0) //spawn
                            ),
                            new BWConfiguration.Team(
                                    BWGameTeamColor.GREEN.getName(),
                                    new AreaWrapper(new LocationWrapper(45, 116, 65),
                                            new LocationWrapper(24, 87, 86)),
                                    new AreaWrapper(new LocationWrapper(32, 98, 83),
                                            new LocationWrapper(44, 104, 73)), //protect2
                                    new LocationWrapper(42.5, 99, 76.5), //generator
                                    new LocationWrapper(36.5, 99, 80.5, -180, 0), //shop
                                    new LocationWrapper(34.5, 99, 80.5, -180, 0), //upgrade
                                    new LocationWrapper(38.5, 99, 76.5, 90, 0) //spawn
                            ),
                            new BWConfiguration.Team(
                                    BWGameTeamColor.YELLOW.getName(),
                                    new AreaWrapper(new LocationWrapper(65, 116, -46), //base
                                            new LocationWrapper(85, 87, -26)), //base2
                                    new AreaWrapper(new LocationWrapper(83, 98, -32), //protect
                                            new LocationWrapper(73, 104, -44)), //protect2
                                    new LocationWrapper(76.5, 99, -41.5), //generator
                                    new LocationWrapper(80.5, 99, -35.5, 90, 0), //shop
                                    new LocationWrapper(80.5, 99, -33.5, 90, 0), //upgrade
                                    new LocationWrapper(76.5, 99, -37.5, 0, 0) //spawn
                            ),
                            new BWConfiguration.Team(
                                    BWGameTeamColor.AQUA.getName(),
                                    new AreaWrapper(new LocationWrapper(65, 116, 46), //base
                                            new LocationWrapper(86, 87, 25)), //base2
                                    new AreaWrapper(new LocationWrapper(83, 98, 32), //protect
                                            new LocationWrapper(73, 104, 44)), //protect2
                                    new LocationWrapper(76.5, 99, 42.5), //generator
                                    new LocationWrapper(80.5, 99, 34.5, 90, 0), //shop
                                    new LocationWrapper(80.5, 99, 36.5, 90, 0), //upgrade
                                    new LocationWrapper(76.5, 99, 38.5, 180, 0) //spawn
                            ),
                            new BWConfiguration.Team(
                                    BWGameTeamColor.WHITE.getName(),
                                    new AreaWrapper(new LocationWrapper(-46, 87, -88), //base
                                            new LocationWrapper(-24, 116, -65)), //base2
                                    new AreaWrapper(new LocationWrapper(-32, 98, -83), //protect
                                            new LocationWrapper(-44, 104, -73)), //protect2
                                    new LocationWrapper(-41.5, 99, -75.5), //generator
                                    new LocationWrapper(-35.5, 99, -79.5, 0, 0), //shop
                                    new LocationWrapper(-33.5, 99, -79.5, 0, 0), //upgrade
                                    new LocationWrapper(-37.5, 99, -75.5, -90, 0) //spawn
                            ),
                            new BWConfiguration.Team(
                                    BWGameTeamColor.PINK.getName(),
                                    new AreaWrapper(new LocationWrapper(-45, 87, 66), //base
                                            new LocationWrapper(-25, 117, 86)), //base2
                                    new AreaWrapper(new LocationWrapper(-32, 98, 83), //protect
                                            new LocationWrapper(-44, 104, 73)), //protect2
                                    new LocationWrapper(-41.5, 99, 76.5), //generator
                                    new LocationWrapper(-35.5, 99, 80.5, 180, 0), //shop
                                    new LocationWrapper(-33.5, 99, 80.5, 180, 0), //upgrade
                                    new LocationWrapper(-37.5, 99, 76.5, -90, 0) //spawn
                            ),
                            new BWConfiguration.Team(
                                    BWGameTeamColor.GRAY.getName(),
                                    new AreaWrapper(new LocationWrapper(46, 87, -64), //base
                                            new LocationWrapper(24, 117, -87)), //base2
                                    new AreaWrapper(new LocationWrapper(32, 98, -83), //protect
                                            new LocationWrapper(44, 104, -73)), //protect2
                                    new LocationWrapper(42.5, 99, -75.5), //generator
                                    new LocationWrapper(36.5, 99, -79.5, 0, 0), //shop
                                    new LocationWrapper(34.5, 99, -79.5, 0, 0), //upgrade
                                    new LocationWrapper(38.5, 99, -75.5, 90, 0) //spawn
                            )
                    )
            );
    }

}

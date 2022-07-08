package fr.hyriode.bedwars.game.test;

import fr.hyriode.bedwars.config.BWConfiguration;
import fr.hyriode.bedwars.game.team.BWGameTeamColor;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.utils.LocationWrapper;

import java.util.ArrayList;
import java.util.Arrays;

public class TestConfiguration {

    public static BWConfiguration getPoseidonSolo(){
        return new BWConfiguration(
                new BWConfiguration.WaitingRoom(
                        new LocationWrapper(IHyrame.WORLD.get().getUID(), 0.5, 170, 0.5, -90, 0),
                        new LocationWrapper(IHyrame.WORLD.get().getUID(), 22, 184, -15),
                        new LocationWrapper(IHyrame.WORLD.get().getUID(), -14, 168, 16),
                        new LocationWrapper(IHyrame.WORLD.get().getUID(), 4.5, 170, 4.5, 130, 0)),

                new BWConfiguration.GameArea(
                        new LocationWrapper(IHyrame.WORLD.get().getUID(), -89, 145, 89),
                        new LocationWrapper(IHyrame.WORLD.get().getUID(), 89, 43, -89)),

                new ArrayList<>(), // Protect Areas

                Arrays.asList(//diamond
                        new LocationWrapper(IHyrame.WORLD.get().getUID(), 0.5, 101, -75.5),
                        new LocationWrapper(IHyrame.WORLD.get().getUID(), 76.5, 101, 0.5),
                        new LocationWrapper(IHyrame.WORLD.get().getUID(), 0.5, 101, 76.5),
                        new LocationWrapper(IHyrame.WORLD.get().getUID(), -75.5, 101, 0.5)
                ),
                Arrays.asList(
                        new LocationWrapper(IHyrame.WORLD.get().getUID(), -8.5, 101, 9.5),
                        new LocationWrapper(IHyrame.WORLD.get().getUID(), -8.5, 101, -8.5),
                        new LocationWrapper(IHyrame.WORLD.get().getUID(), 9.5, 101, -8.5),
                        new LocationWrapper(IHyrame.WORLD.get().getUID(), 9.5, 101, 9.5)
                ),
                Arrays.asList(
                        new BWConfiguration.Team(
                                BWGameTeamColor.BLUE.getName(),
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), -84, 117, -27), //base
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), -66, 87, -44), //base2
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), -83, 99, -32), //protect
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), -73, 104, -44), //protect2
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), -75.5, 99, -41.5), //generator
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), -79.5, 99, -33.5, -90, 0), //shop
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), -79.5, 99, -35.5, -90, 0), //upgrade
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), -75.5, 99, -37.5, 0, 0) //spawn
                        ),
                        new BWConfiguration.Team(
                                BWGameTeamColor.RED.getName(),
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), -85, 117, 26), //base
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), -67, 87, 44), //base2
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), -83, 99, 32), //protect
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), -73, 104, 143), //protect2
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), -75.5, 99, 42.5), //generator
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), -79.5, 99, 36.5, -90, 0), //shop
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), -79.5, 99, 34.5, -90, 0), //upgrade
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), -75.5, 99, 38.5, 180, 0) //spawn
                        ),
                        new BWConfiguration.Team(
                                BWGameTeamColor.GREEN.getName(),
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), 45, 116, 65), //base
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), 24, 87, 86), //base2
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), 32, 99, 83), //protect
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), 44, 104, 73), //protect2
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), 42.5, 99, 76.5), //generator
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), 36.5, 99, 80.5, -180, 0), //shop
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), 34.5, 99, 80.5, -180, 0), //upgrade
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), 38.5, 99, 76.5, 90, 0) //spawn
                        ),
                        new BWConfiguration.Team(
                                BWGameTeamColor.YELLOW.getName(),
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), 65, 116, -46), //base
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), 85, 87, -26), //base2
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), 83, 99, -32), //protect
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), 73, 104, -44), //protect2
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), 76.5, 99, -41.5), //generator
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), 80.5, 99, -35.5, 90, 0), //shop
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), 80.5, 99, -33.5, 90, 0), //upgrade
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), 76.5, 99, -37.5, 0, 0) //spawn
                        ),
                        new BWConfiguration.Team(
                                BWGameTeamColor.AQUA.getName(),
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), 65, 116, 46), //base
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), 86, 87, 25), //base2
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), 83, 99, 32), //protect
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), 73, 104, 44), //protect2
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), 76.5, 99, 42.5), //generator
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), 80.5, 99, 34.5, 90, 0), //shop
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), 80.5, 99, 36.5, 90, 0), //upgrade
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), 76.5, 99, 38.5, 180, 0) //spawn
                        ),
                        new BWConfiguration.Team(
                                BWGameTeamColor.WHITE.getName(),
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), -46, 87, -88), //base
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), -24, 116, -65), //base2
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), -32, 99, -83), //protect
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), -44, 104, -73), //protect2
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), -41.5, 99, -75.5), //generator
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), -35.5, 99, -79.5, 0, 0), //shop
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), -33.5, 99, -79.5, 0, 0), //upgrade
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), -37.5, 99, -75.5, -90, 0) //spawn
                        ),
                        new BWConfiguration.Team(
                                BWGameTeamColor.PINK.getName(),
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), -45, 87, 66), //base
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), -25, 117, 86), //base2
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), -32, 99, 83), //protect
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), -44, 104, 73), //protect2
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), -41.5, 99, 76.5), //generator
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), -35.5, 99, 80.5, 180, 0), //shop
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), -33.5, 99, 80.5, 180, 0), //upgrade
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), -37.5, 99, 76.5, -90, 0) //spawn
                        ),
                        new BWConfiguration.Team(
                                BWGameTeamColor.GRAY.getName(),
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), 46, 87, -64), //base
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), 24, 117, -87), //base2
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), 32, 99, -83), //protect
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), 44, 104, -73), //protect2
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), 42.5, 99, -75.5), //generator
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), 36.5, 99, -79.5, 0, 0), //shop
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), 34.5, 99, -79.5, 0, 0), //upgrade
                                new LocationWrapper(IHyrame.WORLD.get().getUID(), 38.5, 99, -75.5, 90, 0) //spawn
                        )
                ),
                80
        );
    }

}

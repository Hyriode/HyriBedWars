package fr.hyriode.bedwars;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.rank.type.HyriStaffRankType;
import fr.hyriode.api.server.IHyriServer;
import fr.hyriode.bedwars.game.BWGameType;
import fr.hyriode.bedwars.game.material.BWMaterials;
import fr.hyriode.bedwars.game.material.upgradable.ItemBWAxe;
import fr.hyriode.bedwars.game.material.upgradable.ItemBWPickaxe;
import fr.hyriode.bedwars.game.material.utility.entity.BedBugEntity;
import fr.hyriode.bedwars.game.material.utility.entity.DreamDefenderEntity;
import fr.hyriode.bedwars.game.team.EBWGameTeam;
import fr.hyriode.bedwars.game.team.upgrade.BWUpgrades;
import fr.hyriode.hyrame.HyrameLoader;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.language.IHyriLanguageManager;
import fr.hyriode.bedwars.configuration.HyriBWConfiguration;
import fr.hyriode.bedwars.game.BWGame;
import fr.hyriode.hyrame.utils.LocationWrapper;
import net.minecraft.server.v1_8_R3.EntityTypes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

public class HyriBedWars extends JavaPlugin {

    public static final String NAME = "BedWars";

    private static IHyriLanguageManager hyriLanguageManager;

    private IHyrame hyrame;
    private BWGame game;
    private HyriBWConfiguration configuration;

    @Override
    public void onEnable() {
        final ChatColor color = ChatColor.GOLD;
        final ConsoleCommandSender sender = Bukkit.getConsoleSender();

        sender.sendMessage(color + "  _    _            _ ____           ___          __            ");
        sender.sendMessage(color + " | |  | |          (_)  _ \\         | \\ \\        / /            ");
        sender.sendMessage(color + " | |__| |_   _ _ __ _| |_) | ___  __| |\\ \\  /\\  / /_ _ _ __ ___ ");
        sender.sendMessage(color + " |  __  | | | | '__| |  _ < / _ \\/ _` | \\ \\/  \\/ / _` | '__/ __|");
        sender.sendMessage(color + " | |  | | |_| | |  | | |_) |  __/ (_| |  \\  /\\  / (_| | |  \\__ \\");
        sender.sendMessage(color + " |_|  |_|\\__, |_|  |_|____/ \\___|\\__,_|   \\/  \\/ \\__,_|_|  |___/");
        sender.sendMessage(color + "          __/ |                                                 ");
        sender.sendMessage(color + "         |___/                                                  ");

        log("Starting " + NAME + "...");

        if(HyriAPI.get().getConfiguration().isDevEnvironment()) {
//            HyriAPI.get().getHystiaAPI().getWorldManager().saveWorld(IHyrame.WORLD.get().getUID(), "bedwars", BWGameType.DOUBLES.getName(), "Poseidon").whenComplete((aBoolean, throwable) -> System.out.println("world: " + aBoolean));
//            HyriAPI.get().getHystiaAPI().getConfigManager().saveConfig(
            this.configuration = new HyriBWConfiguration(
                    new HyriBWConfiguration.WaitingRoom(
                            new LocationWrapper(IHyrame.WORLD.get().getUID(), 0.5, 170, 0.5, -90, 0),
                            new LocationWrapper(IHyrame.WORLD.get().getUID(), 22, 184, -15),
                            new LocationWrapper(IHyrame.WORLD.get().getUID(), -14, 168, 16)),

                    new HyriBWConfiguration.GameArea(
                            new LocationWrapper(IHyrame.WORLD.get().getUID(), -89, 145, 89),
                            new LocationWrapper(IHyrame.WORLD.get().getUID(), 89, 43, -89)),

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
                            new HyriBWConfiguration.Team(
                                    EBWGameTeam.GRAY.getName(),
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 24, 115, -64), //base
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 44, 89, -85), //base2
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 32, 98, -74), //protect
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 43, 104, -78), //protect2
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 42.5, 101, -75.5), //generator
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 36.5, 99, -79.5, 0, 0), //shop
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 34.5, 99, -79.5, 0, 0), //upgrade
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 38.5, 99, -75.5, 90, 0) //spawn
                            ),
                            new HyriBWConfiguration.Team(
                                    EBWGameTeam.GREEN.getName(),
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 24, 115, 64), //base
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 44, 89, 85), //base2
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 32, 98, 78), //protect
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 43, 104, 74), //protect2
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 42.5, 101, 76.5), //generator
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 36.5, 99, 80.5, -180, 0), //shop
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 34.5, 99, 80.5, -180, 0), //upgrade
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 38.5, 99, 76.5, 90, 0) //spawn
                            ),
                            new HyriBWConfiguration.Team(
                                    EBWGameTeam.PINK.getName(),
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), -44, 89, 85), //base
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), -24, 115, 34), //base2
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), -32, 98, 74), //protect
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), -43, 104, 79), //protect2
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), -41.5, 101, 76.5), //generator
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), -35.5, 99, 80.5, 180, 0), //shop
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), -33.5, 99, 80.5, 180, 0), //upgrade
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), -37.5, 99, 76.5, -90, 0) //spawn
                            ),
                            new HyriBWConfiguration.Team(
                                    EBWGameTeam.BLUE.getName(),
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 64, 115, 24), //base
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 85, 89, 44), //base2
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 74, 98, 32), //protect
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 78, 104, 43), //protect2
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 76.5, 101, 42.5), //generator
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 80.5, 99, 36.5, 90, 0), //shop
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 80.5, 99, 34.5, 90, 0), //upgrade
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 76.5, 99, 38.5, 180, 0) //spawn
                            ),
                            new HyriBWConfiguration.Team(
                                    EBWGameTeam.AQUA.getName(),
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), -64, 115, 24), //base
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), -85, 89, 44), //base2
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), -78, 98, 32), //protect
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), -74, 104, 43), //protect2
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), -75.5, 101, 42.5), //generator
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), -79.5, 99, -35.5, -90, 0), //shop
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), -79.5, 99, -33.5, -90, 0), //upgrade
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), -75.5, 99, 38.5, -180, 0) //spawn
                            ),
                            new HyriBWConfiguration.Team(
                                    EBWGameTeam.WHITE.getName(),
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 68, 115, -25), //base
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 85, 89, -44), //base2
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 83, 104, -44), //protect
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 75, 99, -31), //protect2
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 76.5, 101, -41.5), //generator
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 80.5, 99, -35.5, 90, 0), //shop
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 80.5, 99, -33.5, 90, 0), //upgrade
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 76.5, 99, -37.5, 0, 0) //spawn
                            ),
                            new HyriBWConfiguration.Team(
                                    EBWGameTeam.YELLOW.getName(),
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), -24, 115, -64), //base
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), -44, 89, -85), //base2
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), -32, 98, -78), //protect
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), -43, 104, -74), //protect2
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), -41.5, 101, -75.5), //generator
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), -35.5, 99, -79.5, 0, 0), //shop
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), -33.5, 99, -79.5, 0, 0), //upgrade
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), -35.5, 99, -75.5, 0, 0) //spawn
                            ),
                            new HyriBWConfiguration.Team(
                                    EBWGameTeam.RED.getName(),
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 85, 89, -24), //base
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 64, 115, -44), //base2
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 78, 98, -32), //protect
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 74, 104, -43), //protect2
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), -76.5, 101, -41.5), //generator
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), -79.5, 99, 36.5, -90, 0), //shop
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), -79.5, 99, 34.5, -90, 0), //upgrade
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 76.5, 99, -37.5, 0, 0) //spawn
                            )
                    ),
                    80
            );//, "bedwars", BWGameType.SOLO.getName(), "Poseidon").whenComplete((aBoolean, throwable) -> System.out.println("config: " + aBoolean));
        } else this.configuration = HyriAPI.get().getServer().getConfig(HyriBWConfiguration.class);
        new ItemBWPickaxe().init();
        new ItemBWAxe().init();

        this.hyrame = HyrameLoader.load(new HyriBedWarsProvider(this));

        hyriLanguageManager = this.hyrame.getLanguageManager();

        this.game = new BWGame(this.hyrame, this);
        this.hyrame.getGameManager().registerGame(() -> this.game);
        BWMaterials.init((BWGameType) this.game.getType());
        BWUpgrades.init((BWGameType) this.game.getType());

//        PlaceholderAPI.registerHandler(new BWPlaceHolder(this.hyrame));

        this.registerEntity("BedBug", 60, BedBugEntity.class);
        this.registerEntity("DreamDefender", 99, DreamDefenderEntity.class);

        HyriAPI.get().getServer().setState(IHyriServer.State.READY);
    }

    @Override
    public void onDisable() {
        this.hyrame.getGameManager().unregisterGame(this.game);
    }

    public static void log(Level level, String message) {
        String prefix = ChatColor.GOLD + "[" + NAME + "] ";

        if (level == Level.SEVERE) {
            prefix += ChatColor.RED;
        } else if (level == Level.WARNING) {
            prefix += ChatColor.YELLOW;
        } else {
            prefix += ChatColor.RESET;
        }
        Bukkit.getConsoleSender().sendMessage(prefix + message);
    }

    public static void log(String msg) {
        log(Level.INFO, msg);
    }

    public static IHyriLanguageManager getLanguageManager() {
        return hyriLanguageManager;
    }

    public IHyrame getHyrame() {
        return hyrame;
    }

    public BWGame getGame() {
        return game;
    }

    public HyriBWConfiguration getConfiguration() {
        return configuration;
    }

    private void registerEntity(String name, int id, Class<?> customClass) {
        try {
            ArrayList<Map<?, ?>> dataMap = new ArrayList<>();
            for (Field f : EntityTypes.class.getDeclaredFields()) {
                if (!f.getType().getSimpleName().equals(Map.class.getSimpleName())) continue;
                f.setAccessible(true);
                dataMap.add((Map<?, ?>) f.get(null));
            }
            if (dataMap.get(2).containsKey(id)) {
                dataMap.get(0).remove(name);
                dataMap.get(2).remove(id);
            }
            Method method = EntityTypes.class.getDeclaredMethod("a", Class.class, String.class, Integer.TYPE);
            method.setAccessible(true);
            method.invoke(null, customClass, name, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

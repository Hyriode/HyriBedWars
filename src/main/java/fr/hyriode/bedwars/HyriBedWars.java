package fr.hyriode.bedwars;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.server.IHyriServer;
import fr.hyriode.bedwars.configuration.BWGeneratorConfiguration;
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
import fr.hyriode.hyrame.game.HyriGameType;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

public class HyriBedWars extends JavaPlugin {

    public static final String NAME = "BedWars";

    private static IHyriLanguageManager hyriLanguageManager;

    private IHyrame hyrame;
    private BWGame game;
    private HyriBWConfiguration configuration;
    private BWGeneratorConfiguration generatorConfiguration;

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
//                this.configuration = HyriAPI.get().getHystiaAPI().getConfigManager().getConfig(HyriBWConfiguration.class, "bedwars", BWGameType.TRIO.getName(), "Circus").get();
            this.configuration = new HyriBWConfiguration(
                    new HyriBWConfiguration.WaitingRoom(new LocationWrapper(IHyrame.WORLD.get().getUID(), 0.5, 170, 0.5, -90, 0),
                            new LocationWrapper(IHyrame.WORLD.get().getUID(), 22, 184, -15),
                            new LocationWrapper(IHyrame.WORLD.get().getUID(), -14, 168, 16)),
                    new HyriBWConfiguration.GameArea(new LocationWrapper(IHyrame.WORLD.get().getUID(), -89, 115, 89),
                            new LocationWrapper(IHyrame.WORLD.get().getUID(), 89, 35, -89)),
                    Arrays.asList(
                            new LocationWrapper(IHyrame.WORLD.get().getUID(), -50.5, 90, -51.5),
                            new LocationWrapper(IHyrame.WORLD.get().getUID(), 52.5, 90, -50.5),
                            new LocationWrapper(IHyrame.WORLD.get().getUID(), 51.5, 90, 52.5),
                            new LocationWrapper(IHyrame.WORLD.get().getUID(), -51.5, 90, 51.5)
                    ),
                    Arrays.asList(
                            new LocationWrapper(IHyrame.WORLD.get().getUID(), 13.5, 90, 13.5),
                            new LocationWrapper(IHyrame.WORLD.get().getUID(), -12.5, 90, -12.5)
                    ),
                    Arrays.asList(
                            new HyriBWConfiguration.Team(
                                    EBWGameTeam.RED.getName(),
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 15, 110, -66), //base
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), -15, 67, -97), //base2
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 11, 92, -81), //protect
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), -10, 88, -89), //protect2
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 0.5, 88, -87.5), //generator
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), -7.5, 89, -84.5, -90, 0), //shop
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), -7.5, 89, -82.5, -90, 0), //upgrade
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 8.5, 89, -83.5, 45, 0) //spawn
                            ),
                            new HyriBWConfiguration.Team(
                                    EBWGameTeam.GREEN.getName(),
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 66, 110, 15), //base
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 97, 67, -15), //base2
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 81, 92, 11), //protect
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 89, 88, -10), //protect2
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 88.5, 88, 0.5), //generator
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 85.5, 89, -7.5, 0, 0), //shop
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 83.5, 89, -7.5, 0, 0), //upgrade
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 84.5, 89, 8.5, 135, 0) //spawn
                            ),
                            new HyriBWConfiguration.Team(
                                    EBWGameTeam.YELLOW.getName(),
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), -15, 110, 66), //base
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 15, 67, 97), //base2
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), -11, 92, 81), //protect
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 10, 88, 89), //protect2
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 0.5, 88, 88.5), //generator
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 8.5, 89, 85.5, 90, 0), //shop
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), 8.5, 89, 83.5, 90, 0), //upgrade
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), -7.5, 89, 84.5, -135, 0) //spawn
                            ),
                            new HyriBWConfiguration.Team(
                                    EBWGameTeam.BLUE.getName(),
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), -66, 110, -15), //base
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), -97, 67, 15), //base2
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), -81, 92, -11), //protect
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), -89, 88, 10), //protect2
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), -87.5, 88, 0.5), //generator
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), -84.5, 89, 8.5, 180, 0), //shop
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), -82.5, 89, 8.5, 180, 0), //upgrade
                                    new LocationWrapper(IHyrame.WORLD.get().getUID(), -83.5, 89, -7.5, -45, 0) //spawn
                            )
                    ),
                    80
            );
        } else this.configuration = HyriAPI.get().getServer().getConfig(HyriBWConfiguration.class);
        new ItemBWPickaxe().init();
        new ItemBWAxe().init();

        this.generatorConfiguration = new BWGeneratorConfiguration(this);
        this.generatorConfiguration.create();
        this.generatorConfiguration.load();

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

    public BWGeneratorConfiguration getGeneratorConfiguration() {
        return generatorConfiguration;
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

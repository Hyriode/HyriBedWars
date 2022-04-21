package fr.hyriode.bedwars;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.bedwars.configuration.BWGeneratorConfiguration;
import fr.hyriode.bedwars.game.material.BWMaterials;
import fr.hyriode.bedwars.game.material.upgradable.ItemBWAxe;
import fr.hyriode.bedwars.game.material.upgradable.ItemBWPickaxe;
import fr.hyriode.bedwars.game.material.utility.entity.BedBugEntity;
import fr.hyriode.bedwars.game.material.utility.entity.DreamDefenderEntity;
import fr.hyriode.hyrame.HyrameLoader;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.language.IHyriLanguageManager;
import fr.hyriode.bedwars.api.HyriBedWarsAPI;
import fr.hyriode.bedwars.configuration.HyriBWConfiguration;
import fr.hyriode.bedwars.game.BWGame;
import fr.hyriode.hyrame.placeholder.PlaceholderAPI;
import net.minecraft.server.v1_8_R3.EntityTypes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;

public class HyriBedWars extends JavaPlugin {

    public static final String NAME = "BedWars";

    private static IHyriLanguageManager hyriLanguageManager;

    private IHyrame hyrame;
    private HyriBedWarsAPI api;
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

        this.configuration = new HyriBWConfiguration(this);
        this.configuration.create();
        this.configuration.load();
        new ItemBWPickaxe().init();
        new ItemBWAxe().init();
        BWMaterials.init(this.configuration);

        this.generatorConfiguration = new BWGeneratorConfiguration(this);
        this.generatorConfiguration.create();
        this.generatorConfiguration.load();

        this.hyrame = HyrameLoader.load(new HyriBedWarsProvider(this));

        hyriLanguageManager = this.hyrame.getLanguageManager();

        IHyriLanguageManager.Provider.registerInstance(() -> this.hyrame.getLanguageManager());

        this.api = new HyriBedWarsAPI(HyriAPI.get().getRedisConnection().getPool());
        this.api.start();
        this.game = new BWGame(this.hyrame, this);
        this.hyrame.getGameManager().registerGame(() -> this.game);

//        PlaceholderAPI.registerHandler(new BWPlaceHolder(this.hyrame));

        this.registerEntity("BedBug", 60, BedBugEntity.class);
        this.registerEntity("DreamDefender", 99, DreamDefenderEntity.class);
    }

    private void registerEntityCustom(){

    }

    @Override
    public void onDisable() {
        this.hyrame.getGameManager().unregisterGame(this.game);
        this.api.stop();
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

    public HyriBedWarsAPI getAPI() {
        return api;
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

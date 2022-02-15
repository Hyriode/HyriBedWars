package fr.hyriode.hyribedwars;

import fr.hyriode.hyrame.HyrameLoader;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.language.IHyriLanguageManager;
import fr.hyriode.hyribedwars.configuration.HyriBedWarsConfiguration;
import fr.hyriode.hyribedwars.game.HyriBedWarsGame;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class HyriBedWars extends JavaPlugin {

    public static String NAME = "BedWars";

    private static IHyriLanguageManager hyriLanguageManager;

    private IHyrame hyrame;
    private HyriBedWarsGame game;
    private HyriBedWarsConfiguration configuration;

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

        this.configuration = new HyriBedWarsConfiguration(this);
        this.configuration.create();
        this.configuration.load();
        this.hyrame = HyrameLoader.load(new HyriBedWarsProvider(this));

        hyriLanguageManager = this.hyrame.getLanguageManager();

        this.game = new HyriBedWarsGame(this.hyrame, this);
        this.hyrame.getGameManager().registerGame(this.game);
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

    public static IHyriLanguageManager getHyriLanguageManager() {
        return hyriLanguageManager;
    }

    public IHyrame getHyrame() {
        return hyrame;
    }

    public HyriBedWarsGame getGame() {
        return game;
    }

    public HyriBedWarsConfiguration getConfiguration() {
        return configuration;
    }
}

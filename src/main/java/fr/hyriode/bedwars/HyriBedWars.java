package fr.hyriode.bedwars;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.host.HostData;
import fr.hyriode.api.host.HostType;
import fr.hyriode.api.server.IHyriServer;
import fr.hyriode.bedwars.config.BWConfiguration;
import fr.hyriode.bedwars.game.BWGame;
import fr.hyriode.bedwars.game.entity.EntityManager;
import fr.hyriode.bedwars.game.generator.GeneratorManager;
import fr.hyriode.bedwars.game.shop.ShopManager;
import fr.hyriode.bedwars.game.test.TestConfiguration;
import fr.hyriode.bedwars.game.trap.TrapManager;
import fr.hyriode.bedwars.game.type.BWGameType;
import fr.hyriode.bedwars.game.upgrade.UpgradeManager;
import fr.hyriode.bedwars.host.BWHostManager;
import fr.hyriode.bedwars.manager.pnj.EntityInteractManager;
import fr.hyriode.hyggdrasil.api.server.HyggServer;
import fr.hyriode.hyrame.HyrameLoader;
import fr.hyriode.bedwars.host.BWForgeValues;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.reflection.Reflection;
import net.minecraft.server.v1_8_R3.Block;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.UUID;
import java.util.logging.Level;

public class HyriBedWars extends JavaPlugin {

    public static final String NAME = "BedWars";

    private IHyrame hyrame;
    private BWGame game;
    private BWConfiguration configuration;

    private static ShopManager shopManager;
    private static UpgradeManager upgradeManager;
    private static EntityManager entityManager;
    private static TrapManager trapManager;
    private static GeneratorManager generatorManager;
    private BWHostManager hostManager;

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

        this.registerBlockResistance();

        this.hyrame = HyrameLoader.load(new HyriBWProvider(this));
        EntityInteractManager.init(this);
        if(HyriAPI.get().getConfig().isDevEnvironment()) {
            this.configuration = TestConfiguration.getPoseidonTrio();
            Reflection.setField("hostData", HyriAPI.get().getServer(), new HostData(HostType.PUBLIC, UUID.fromString("b0bdcb68-a0f2-3bfb-9d4a-c7665fbb2da0"), "HostCool"));
        } else {
            this.configuration = HyriAPI.get().getServer().getConfig(BWConfiguration.class);
        }

        this.game = new BWGame(this);
        this.hyrame.getGameManager().registerGame(() -> this.game);
        this.initManager();

        this.hostManager = new BWHostManager();
        this.hostManager.attach();

        HyriAPI.get().getServer().setState(HyggServer.State.READY);
        System.out.println("Bedwars Ready");
    }

    @Override
    public void onDisable() {
        this.hyrame.getGameManager().unregisterGame(this.game);
    }

    public void initManager() {
        shopManager = new ShopManager();
        upgradeManager = new UpgradeManager();
        entityManager = new EntityManager();
        trapManager = new TrapManager();
        BWForgeValues.init();
        generatorManager = new GeneratorManager(this);
    }

    public IHyrame getHyrame() {
        return hyrame;
    }

    public BWGame getGame() {
        return game;
    }

    public static ShopManager getShopManager() {
        return shopManager;
    }

    public static UpgradeManager getUpgradeManager() {
        return upgradeManager;
    }

    public static EntityManager getEntityManager() {
        return entityManager;
    }

    public static TrapManager getTrapManager() {
        return trapManager;
    }

    public static GeneratorManager getGeneratorManager() {
        return generatorManager;
    }

    public BWConfiguration getConfiguration() {
        return HyriAPI.get().getServer().getAccessibility() == HyggServer.Accessibility.HOST
                ? HyriAPI.get().getServer().getConfig(BWConfiguration.class)
                : configuration;
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

    private void registerBlockResistance(){
        try {
            Field field = Block.class.getDeclaredField("durability");
            field.setAccessible(true);
            field.set(Block.getByName("glass"), 300f);
            field.set(Block.getByName("stained_glass"), 300f);
            field.set(Block.getByName("end_stone"), 69f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package fr.hyriode.bedwars.manager.pnj;

import fr.hyriode.hyrame.hologram.Hologram;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class PNJManager {

    private static Map<PNJ, Hologram> PNJS = new HashMap<>();

    private static JavaPlugin plugin;

    public static void init(JavaPlugin plugin){
        PNJManager.plugin = plugin;
    }

    /**
     * Create a PNJ
     *
     * @param location PNJ location
     * @param entity PNJ entity
     * @return - {@link PNJ} object
     */
    public static PNJ createPNJ(Location location, Class<? extends Entity> entity, String[] hologramLines){
        PNJ pnj = new PNJ(location, entity);
        return pnj;
    }

}

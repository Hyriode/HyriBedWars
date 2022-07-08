package fr.hyriode.bedwars.utils;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundUtils {

    public static void playBuy(Player player){
        Location location = player.getLocation();
        player.playSound(location, Sound.NOTE_PLING, 0.8F, 2.0F);
    }

    public static void playCantBuy(Player player){
        Location location = player.getLocation();
        player.playSound(location, Sound.ENDERMAN_TELEPORT, 0.8F, 0.1F);
    }

}

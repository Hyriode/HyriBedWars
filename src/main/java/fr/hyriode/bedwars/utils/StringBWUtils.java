package fr.hyriode.bedwars.utils;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.material.OreStack;
import org.bukkit.entity.Player;

import java.util.List;

public class StringBWUtils {

    public static String formatTime(long timeSecond){
        long sec = timeSecond % 60;
        long min = (timeSecond / 60) % 60;

        return min+":"+(String.valueOf(sec).length() < 2 ? "0" + sec : sec);
    }

    public static String getLevelLang(int i){
        switch (i){
            case 1:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            case 5:
                return "V";
            default:
                return "0";
        }
    }

    public static String getLevelLang(Player player, int i){
        return HyriBedWars.getLanguageManager().getValue(player, "tier." + i);
    }

    public static String getCountPriceAsString(Player player, List<OreStack> items){
        if(items.isEmpty())
            return "Nothing";
        StringBuilder finalStr = new StringBuilder();
        for (OreStack item : items) {
            finalStr.append(item.getItem().getColor() + "" + item.getAmount() + " " + item.getItem().getName().getForPlayer(player) + (item.getAmount() != 1 ? "s" : "") + ", ");
        }
        return finalStr.substring(0, finalStr.length() - 2);
    }

    public static String getPriceAsString(Player player, List<OreStack> items){
        if(items.isEmpty())
            return "Nothing";
        if(items.size() == 1)
            return items.get(0).getItem().getName().getForPlayer(player);
        StringBuilder finalStr = new StringBuilder();
        for (OreStack item : items) {
            finalStr.append(item.getItem().getName().getForPlayer(player) + (item.getAmount() != 1 ? "s" : "") + ", ");
        }
        return finalStr.substring(0, finalStr.length() - 2);
    }

}

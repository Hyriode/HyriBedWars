package fr.hyriode.bedwars.utils;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.material.OreStack;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

    public static String getCountPriceAsString(Player player, OreStack item){
        return item.getItem().getColor() + "" + item.getAmount() + " " + item.getItem().getName().getForPlayer(player) + (item.getAmount() != 1 ? "s" : "");
    }

    public static String getPriceAsString(Player player, OreStack item){
        return item.getItem().getName().getForPlayer(player) + (item.getAmount() != 1 ? "s" : "");
    }

    public static int getCountPriceMissing(Player player, OreStack items){
        return items.getAmount() - InventoryBWUtils.getAmountItems(player, items);
    }

    public static List<String> loreToList(String inputLore, String... inputOtherLore){
        List<String> outputLore = new ArrayList<>();
        String[] splitLore = inputLore.split("\n");
        for(String desc : splitLore){
            outputLore.add(ChatColor.GRAY + desc);
        }
        outputLore.addAll(Arrays.asList(inputOtherLore));
        return outputLore;
    }

}

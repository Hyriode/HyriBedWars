package fr.hyriode.bedwars.utils;

import fr.hyriode.bedwars.game.shop.ItemPrice;
import fr.hyriode.api.language.HyriLanguageMessage;
import fr.hyriode.bedwars.game.type.BWGameType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {

    public static List<String> loreToList(String inputLore){
        List<String> outputLore = new ArrayList<>();
        String[] splitLore = inputLore.split("\n");
        for(String desc : splitLore){
            outputLore.add(ChatColor.GRAY + desc);
        }
        return outputLore;
    }

    public static String getLevelLang(int i){
        switch (i){
            case 1: return "I";
            case 2: return "II";
            case 3: return "III";
            case 4: return "IV";
            case 5: return "V";
            default: return "0";
        }
    }

    public static String formatTime(long timeSecond){
        long sec = timeSecond % 60;
        long min = (timeSecond / 60) % 60;

        return min + ":" + (String.valueOf(sec).length() < 2 ? "0" + sec : sec);
    }

    public static String getDisplayCostPrice(BWGameType gameType, Player player, ItemPrice price) {
        return ChatColor.GRAY + HyriLanguageMessage.get("shop.inventory.item.cost").getValue(player)
                + getDisplayPrice(gameType, player, price);
    }

    public static String getDisplayPrice(BWGameType gameType, Player player, ItemPrice price) {
        return price.getColor() + "" + price.getAmount().apply(gameType).get() + " " + price.getName(player);
    }

    public static HyriLanguageMessage getWordNumber(int i) {
        switch (i){
            case 2: return HyriLanguageMessage.get("second.word");
            case 3: return HyriLanguageMessage.get("third.word");
            default: return HyriLanguageMessage.get("first.word");
        }
    }

    public static String getTitleBuy(boolean maxed, boolean canBuy){
        return (maxed ? ChatColor.GREEN + "" : canBuy ? ChatColor.GREEN + "✔ " : ChatColor.RED + "✘ ");
    }
}

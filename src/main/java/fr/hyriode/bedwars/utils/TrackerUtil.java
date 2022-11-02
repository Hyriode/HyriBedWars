package fr.hyriode.bedwars.utils;

import fr.hyriode.bedwars.game.shop.ItemMoney;
import fr.hyriode.bedwars.game.shop.ItemPrice;

public class TrackerUtil {

    private static ItemPrice price = new ItemPrice(ItemMoney.EMERALD, 2);

    public static ItemPrice getPrice(){
        return price;
    }

    public static void setPrice(ItemPrice price) {
        TrackerUtil.price = price;
    }
}

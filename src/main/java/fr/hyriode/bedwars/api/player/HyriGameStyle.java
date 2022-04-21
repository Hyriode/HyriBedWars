package fr.hyriode.bedwars.api.player;

import org.bukkit.ChatColor;

public enum HyriGameStyle {

    HYRIODE("Hyriode", "§3"),
    HYPIXEL("Hypixel", "§6"),

    ;

    private final String name;
    private final String color;

    HyriGameStyle(String name, String color){
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public String getNameColor(){
        return this.color + this.name;
    }
}

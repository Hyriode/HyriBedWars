package fr.hyriode.bedwars.api.player.style;

import net.md_5.bungee.api.ChatColor;

public enum HyriGameStyle {
    HYRIODE("Hyriode", ChatColor.DARK_AQUA),
    HYPIXEL("Hypixel", ChatColor.GOLD);

    private final String name;
    private final ChatColor color;
    
    HyriGameStyle(String name, ChatColor color){
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public ChatColor getColor() {
        return color;
    }

    public String getNameColor(){
        return this.color + this.name;
    }
}

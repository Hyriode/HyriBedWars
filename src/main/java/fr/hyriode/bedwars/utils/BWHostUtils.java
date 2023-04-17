package fr.hyriode.bedwars.utils;

import fr.hyriode.api.language.HyriLanguageMessage;
import fr.hyriode.hyrame.host.HostDisplay;
import fr.hyriode.hyrame.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public class BWHostUtils {

    public static HostDisplay optionDisplay(String name, String displayName, String description, ItemStack icon) {
        return new HostDisplay.Builder().withName(name)
                .withDisplayName(HyriLanguageMessage.get("host.option." + displayName + ".name"))
                .withDescription(HyriLanguageMessage.get("host.option." + description + ".description"))
                .withIcon(icon)
                .build();
    }

    public static HostDisplay optionDisplay(String name, String displayName, String description, Material icon) {
        return optionDisplay(name, displayName, description, new ItemStack(icon));
    }

    public static HostDisplay optionDisplay(String name, String displayName, Material icon) {
        return optionDisplay(name, displayName, new ItemStack(icon));
    }

    public static HostDisplay optionDisplay(String name, String displayName, ItemStack icon) {
        return optionDisplay(name, displayName, displayName, icon);
    }

    public static HostDisplay optionDisplay(String name, ItemStack icon) {
        return optionDisplay(name, name, name, icon);
    }

    public static HostDisplay optionDisplay(String name, Material icon) {
        return optionDisplay(name, new ItemStack(icon));
    }

    public static HostDisplay categoryDisplay(String name, ItemStack icon) {
        HostDisplay hostDisplay = new HostDisplay.Builder().withName(name)
                .withDisplayName(HyriLanguageMessage.get("host.category." + name + ".name"))
                .withDescription(HyriLanguageMessage.get("host.category." + name + ".description"))
                .withIcon(icon)
                .build();
        return hostDisplay;
    }

    public static HostDisplay categoryDisplay(String name, Material icon) {
        return categoryDisplay(name, new ItemStack(icon));
    }

    public static ItemStack createItem(Player player, HostDisplay hostDisplay) {
        return new ItemBuilder(hostDisplay.getIcon().clone())
                .withName(hostDisplay.getDisplayName().getValue(player))
                .withLore(hostDisplay.getDescription() != null
                        ? Arrays.asList(hostDisplay.getDescription().getValue(player).split("\n"))
                        : new ArrayList<>()).build();
    }
}

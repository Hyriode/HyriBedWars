package fr.hyriode.bedwars.game.team.upgrade.traps;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.BWGameOre;
import fr.hyriode.bedwars.game.BWGamePlayer;
import fr.hyriode.bedwars.game.material.OreStack;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import fr.hyriode.bedwars.utils.StringBWUtils;
import fr.hyriode.hyrame.language.HyriLanguageMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public interface BWTrap {

    String getKeyName();
    int getTime();
    Material getMaterial();
    void active(BWGamePlayer enemy, BWGameTeam team);
    OreStack getPrice(BWGameTeam team);

    default HyriLanguageMessage getName() {
        return HyriBedWars.getLanguageManager().getMessage("trap." + this.getKeyName() + ".name");
    }

    default HyriLanguageMessage getDescription() {
        return HyriBedWars.getLanguageManager().getMessage("trap." + this.getKeyName() + ".lore");
    }

    default List<String> getLore(Player player){
        return StringBWUtils.loreToList(this.getDescription().getForPlayer(player));
    }

}

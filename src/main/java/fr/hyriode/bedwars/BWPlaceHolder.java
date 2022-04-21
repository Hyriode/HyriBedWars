package fr.hyriode.bedwars;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.settings.IHyriPlayerSettings;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.language.IHyriLanguageManager;
import fr.hyriode.hyrame.placeholder.PlaceholderHandler;
import fr.hyriode.hyrame.placeholder.PlaceholderPrefixHandler;
import org.bukkit.entity.Player;

public class BWPlaceHolder extends PlaceholderPrefixHandler {

    private final IHyriLanguageManager lm;

    public BWPlaceHolder(IHyrame hyrame) {
        super("bedwars");

        this.lm = hyrame.getLanguageManager();
    }

    @Override
    public String handle(Player player, String placeholder) {
//        final IHyriPlayerSettings settings = HyriAPI.get().getPlayerSettingsManager().getPlayerSettings(player.getUniqueId());

//        switch (placeholder){
//            case "": return this.lm.getValue(player, "")
//        }

        return null;
    }
}

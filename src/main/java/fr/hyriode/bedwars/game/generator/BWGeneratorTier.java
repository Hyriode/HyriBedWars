package fr.hyriode.bedwars.game.generator;

import fr.hyriode.hyrame.language.HyriLanguageMessage;
import fr.hyriode.bedwars.game.generator.item.BWItemGenerator;
import org.bukkit.entity.Player;

import java.util.function.Function;

public class BWGeneratorTier {

    private final String keyLang;
    private final boolean isSplitting;
    private final BWItemGenerator[] itemsToGen;

    public BWGeneratorTier(String keyLang, boolean isSplitting, BWItemGenerator... itemsToGen){
        this.keyLang = keyLang;
        this.isSplitting = isSplitting;
        this.itemsToGen = itemsToGen;
    }

    public Function<Player, String> getName() {
        return player -> keyLang;
    }

    public HyriLanguageMessage getLanguageName(){
        return new HyriLanguageMessage(this.keyLang);
    }

    public boolean isSplitting() {
        return isSplitting;
    }

    public BWItemGenerator[] getItemsToGen() {
        return itemsToGen;
    }
}

package fr.hyriode.bedwars.api.player.cosmetic;

import fr.hyriode.hyrame.npc.NPCSkin;
import fr.hyriode.hyriapi.cosmetic.HyriCosmetic;
import fr.hyriode.hyriapi.cosmetic.HyriCosmeticRarity;
import fr.hyriode.hyriapi.cosmetic.HyriCosmeticType;
import fr.hyriode.hyriapi.player.IHyriPlayer;

public class HyriBWNPCCosmetic extends HyriCosmetic {

    private NPCSkin skin;

    public HyriBWNPCCosmetic(String name, HyriCosmeticRarity rarity) {
        super(name, HyriCosmeticType.TRAIL, rarity);
    }

    public void setSkin(NPCSkin skin) {
        this.skin = skin;
    }

    public NPCSkin getSkin() {
        return skin;
    }

    @Override
    public void start(IHyriPlayer iHyriPlayer) {}

    @Override
    public void stop(IHyriPlayer iHyriPlayer) {}

    @Override
    public void stop() {}
}

package fr.hyriode.bedwars.api.player.cosmetic;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.hyrame.npc.NPCSkin;

public class HyriBWNPCCosmetic  {

    private NPCSkin skin;

//    public HyriBWNPCCosmetic(String name, HyriCosmeticRarity rarity) {
//        super(name, HyriAPI.get().getCosmeticManager().getCosmetic(name).getType(), rarity);
//    }

    public void setSkin(NPCSkin skin) {
        this.skin = skin;
    }

    public NPCSkin getSkin() {
        return skin;
    }

}

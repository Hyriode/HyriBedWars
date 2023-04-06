package fr.hyriode.bedwars.game.player.cosmetic.npc;

import fr.hyriode.bedwars.manager.pnj.PNJ;
import org.bukkit.entity.EntityType;

public class NPCSkin {

    private final Skin skinShop;
    private final Skin skinUpgrade;

    public NPCSkin(Skin skinShop, Skin skinUpgrade) {
        this.skinShop = skinShop;
        this.skinUpgrade = skinUpgrade;
    }

    public Skin getShop() {
        return this.skinShop;
    }

    public Skin getUpgrade() {
        return this.skinUpgrade;
    }

    public static class Skin{

        private final PNJ.Type type;

        public Skin(PNJ.Type type) {
            this.type = type;
        }

        public PNJ.Type getSkinEntity() {
            return this.type;
        }
    }
}

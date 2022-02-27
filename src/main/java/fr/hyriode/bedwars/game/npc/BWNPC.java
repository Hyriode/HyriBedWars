package fr.hyriode.bedwars.game.npc;

import fr.hyriode.hyrame.npc.NPC;
import fr.hyriode.hyrame.npc.NPCSkin;
import org.bukkit.Location;

import java.util.List;

public class BWNPC {

    private Location location;
    private NPCSkin skin;
    private List<String> hologramLines;
    private NPC npc;

    public BWNPC(NPCSkin skin, List<String> hologramLines) {
        this.skin = skin;
        this.hologramLines = hologramLines;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public NPCSkin getSkin() {
        return skin;
    }

    public void setSkin(NPCSkin skin) {
        this.skin = skin;
    }

    public List<String> getHologramLines() {
        return hologramLines;
    }

    public void setHologramLines(List<String> hologramLines) {
        this.hologramLines = hologramLines;
    }
}

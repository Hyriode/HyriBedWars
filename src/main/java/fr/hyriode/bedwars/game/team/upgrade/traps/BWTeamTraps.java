package fr.hyriode.bedwars.game.team.upgrade.traps;

import fr.hyriode.bedwars.game.BWGameOre;
import fr.hyriode.bedwars.game.material.OreStack;
import fr.hyriode.bedwars.game.team.BWGameTeam;

import java.util.ArrayList;
import java.util.List;

public class BWTeamTraps {

    private final List<BWTrap> traps = new ArrayList<>();

    private final BWGameTeam team;
    private boolean canTrap = true;

    public BWTeamTraps(BWGameTeam team){
        this.team = team;
    }

    public List<BWTrap> getTraps() {
        return traps;
    }

    public boolean addTrap(BWTrap trap){
        if(this.size() < 3) {
            this.traps.add(trap);
            return true;
        }
        return false;
    }

    public BWTrap getFirstTrap(){
        return !this.isEmpty() ? this.traps.get(0) : null;
    }

    public void removeTrap(){
        if(this.traps.size() > 0)
            this.traps.remove(this.traps.get(0));
    }

    public OreStack getPrice(){
        return !this.isEmpty() ? this.traps.get(this.size() - 1).getPrice(this.team) : new OreStack(BWGameOre.DIAMOND);
    }

    public int size(){
        return this.traps.size();
    }

    public boolean isEmpty(){
        return this.traps.isEmpty();
    }

    public boolean canTrap(){
        return this.canTrap && !this.isEmpty();
    }

    public void setCanTrap(boolean canTrap) {
        this.canTrap = canTrap;
    }
}

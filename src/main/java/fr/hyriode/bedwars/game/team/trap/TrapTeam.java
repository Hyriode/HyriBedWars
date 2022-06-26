package fr.hyriode.bedwars.game.team.trap;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.shop.ItemMoney;
import fr.hyriode.bedwars.game.shop.ItemPrice;

import java.util.ArrayList;
import java.util.List;

public class TrapTeam {

    private final List<String> traps;

    public TrapTeam(){
        this.traps = new ArrayList<>();
    }

    public void addTrap(String name){
        this.traps.add(name);
    }

    public boolean removeTrap(String name){
        return this.traps.removeIf(s -> s.equals(name));
    }

    public List<String> getTraps() {
        return traps;
    }

    public ItemPrice getPrice() {
        int amount = this.traps.size() * 2;
        if(amount == 0) amount++;

        return new ItemPrice(ItemMoney.DIAMOND, amount);
    }

    public boolean isFull() {
        return this.traps.size() >= 3;
    }
}

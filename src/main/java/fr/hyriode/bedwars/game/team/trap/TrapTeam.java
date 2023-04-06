package fr.hyriode.bedwars.game.team.trap;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.bedwars.game.shop.ItemMoney;
import fr.hyriode.bedwars.game.shop.ItemPrice;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import fr.hyriode.bedwars.game.trap.Trap;
import fr.hyriode.api.language.HyriLanguageMessage;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class TrapTeam {

    private final List<String> traps;
    private final BWGameTeam team;

    public TrapTeam(BWGameTeam team){
        this.traps = new ArrayList<>();
        this.team = team;
    }

    public void addTrap(String name){
        this.traps.add(name);
    }

    public boolean removeTrap(Trap trap){
        return this.traps.remove(trap.getName());
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

    public void trap(BWGamePlayer player) {
        if(this.traps.isEmpty()) return;
        Trap trap = HyriBedWars.getTrapManager().getTrapByName(this.traps.get(0));

        trap.active(player, this.team);
        if(trap.isShowTitle()){
            this.team.sendTitle(p -> ChatColor.RED + HyriLanguageMessage.get("trap.title").getValue(p),
                    p -> ChatColor.RED + HyriLanguageMessage.get("trap.subtitle").getValue(p)
                            .replace("%trap%", trap.getDisplayName().getValue(p)),
                    10, 20, 10);
        }

        player.addCountdown(BWGamePlayer.TRAP_COUNTDOWN, 15*20);
        this.removeTrap(trap);
    }
}

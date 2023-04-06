package fr.hyriode.bedwars.game.trap;

import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.bedwars.game.shop.ItemMoney;
import fr.hyriode.bedwars.game.shop.ItemPrice;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import fr.hyriode.bedwars.game.team.trap.TrapTeam;
import fr.hyriode.bedwars.utils.TriConsumer;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.api.language.HyriLanguageMessage;
import fr.hyriode.hyrame.title.Title;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class TrapManager {

    private final List<Trap> traps = new ArrayList<>();

    public TrapManager(){
        this.add("blindness", new ItemBuilder(Material.IRON_TRAPDOOR).build(), 8, (enemy, team, trap) -> {
            final Player player = enemy.getPlayer();
            final int time = trap.getTimeSeconds() * 20;
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, time, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, time, 0));
        });
        this.add("counter-offensive", new ItemBuilder(Material.FEATHER).build(), 15, (enemy, team, trap) -> {
            team.getPlayers().forEach(player -> {
                final Player p = player.getPlayer();
                final int time = trap.getTimeSeconds() * 20;
                if(team.getConfig().getBaseArea().isInArea(p.getLocation())){
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, time, 1));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, time, 1));
                }
            });
        });
        this.add("alarm", new ItemBuilder(Material.REDSTONE_TORCH_ON).build(), 0, (enemy, team, trap) -> {
            enemy.getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
            team.getPlayers().forEach(player -> {
                Title.sendTitle(player.getPlayer(), ChatColor.RED + "ALARM", ChatColor.GRAY + "Alarm activate by " + enemy.getBWTeam().getColor().getChatColor() + enemy.getBWTeam().getName(), 10, 20*3, 10);
            });
            team.sendTitle(p -> ChatColor.RED + HyriLanguageMessage.get("trap.alarm.title").getValue(p),
                    p -> ChatColor.RED + HyriLanguageMessage.get("trap.alarm.subtitle").getValue(p)
                            .replace("%team%", team.getColor().getDyeColor() + team.getDisplayName().getValue(p) + ChatColor.RESET),
                    10, 20, 10);
        }, false);
        this.add("miner-fatigue", new ItemBuilder(Material.IRON_PICKAXE).withAllItemFlags().build(), 10, (enemy, team, trap) -> {
            enemy.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, trap.getTimeSeconds() * 20, 0));
        });
    }

    private void add(String name, ItemStack icon, int timeSeconds, TriConsumer<BWGamePlayer, BWGameTeam, Trap> action) {
        this.add(name, icon, timeSeconds, action, true);
    }

    private void add(String name, ItemStack icon, int timeSeconds, TriConsumer<BWGamePlayer, BWGameTeam, Trap> action, boolean showTitle){
        this.traps.add(new Trap(name, icon, timeSeconds, action, showTitle));
    }

    public List<Trap> getTraps() {
        return traps;
    }

    public Trap getTrapByName(String name){
        return this.traps.stream().filter(trap -> trap.getName().equals(name)).findFirst().orElse(null);
    }
}

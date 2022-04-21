package fr.hyriode.bedwars.game.team.upgrade.traps;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.BWGameOre;
import fr.hyriode.bedwars.game.BWGamePlayer;
import fr.hyriode.bedwars.game.material.ItemShop;
import fr.hyriode.bedwars.game.material.OreStack;
import fr.hyriode.bedwars.game.material.upgradable.ArmorBW;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import fr.hyriode.bedwars.utils.InventoryBWUtils;
import fr.hyriode.bedwars.utils.StringBWUtils;
import fr.hyriode.bedwars.utils.TriConsumer;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.language.HyriLanguageMessage;
import fr.hyriode.hyrame.title.Title;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public enum BWTraps implements BWTrap {

    BLINDNESS_SLOWNESS("blindness", Material.IRON_TRAPDOOR, 8, (enemy, team, time) -> {
        final Player player = enemy.getPlayer();
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, time, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, time, 0));
    }),
    COUNTER_OFFENSIVE("counter_offensive", Material.FEATHER, 15, (enemy, team, time) -> {
        team.getPlayers().forEach(player -> {
            final Player p = player.getPlayer();
            if(team.getBaseArea().isInArea(p.getLocation())){
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, time, 1));
                p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, time, 1));
            }
        });
    }),
    ALARM("alarm", Material.REDSTONE_TORCH_ON, 0, (enemy, team, time) -> {
        enemy.getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
    }, (player, enemy) -> ChatColor.RED + HyriBedWars.getLanguageManager().getValue(player.getPlayer(), "trap.alarm.title"),
       (player, enemy) -> String.format(HyriBedWars.getLanguageManager().getValue(player.getPlayer(), "trap.alarm.subtitle"),
               enemy.getTeam().getColor().getChatColor() + enemy.getTeam().getDisplayName().getForPlayer(player) + ChatColor.RESET)),
    MINER_FATIGUE("miner_fatigue", Material.IRON_PICKAXE, 10, (enemy, team, time) -> {
        enemy.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, time, 0));
    })
    ;

    private final String name;
    private final int time; //in seconds
    private final Material material;
    /** The player is the enemy, the team is the base penetrate */
    private final TriConsumer<BWGamePlayer, BWGameTeam, Integer> action;
    private final BiFunction<Player, BWGamePlayer, String> title;
    private final BiFunction<Player, BWGamePlayer, String> subtitle;

    BWTraps(String name, Material material, int time, TriConsumer<BWGamePlayer, BWGameTeam, Integer> action,
            BiFunction<Player, BWGamePlayer, String> title, BiFunction<Player, BWGamePlayer, String> subtitle){
        this.name = name;
        this.material = material;
        this.time = time;
        this.action = action;
        this.title = title;
        this.subtitle = subtitle;
    }

    BWTraps(String name, Material material, int time, TriConsumer<BWGamePlayer, BWGameTeam, Integer> action){
        this(name, material, time, action, null, null);
    }

    @Override
    public String getKeyName() {
        return this.name;
    }

    @Override
    public int getTime() {
        return this.time;
    }

    @Override
    public Material getMaterial() {
        return this.material;
    }

    @Override
    public void active(BWGamePlayer enemy, BWGameTeam team) {
        if(!enemy.isCooldownTrap()) {
            action.accept(enemy, team, this.time * 20);
            team.sendTitle(
                player -> {
                    if(this.title != null) {
                        return this.title.apply(player, enemy);
                    }
                    return ChatColor.RED + this.getValue(player, "title");
                },
                player -> {
                    if(this.subtitle != null) {
                        return this.subtitle.apply(player, enemy);
                    }
                    return String.format(this.getValue(player, "subtitle"), this.getName().getForPlayer(player));
                }, 0, 5 * 20, 0);
        }
    }

    @Override
    public OreStack getPrice(BWGameTeam team) {
        int price = team.getTraps().size() + 1 >= 3 ? 4 : team.getTraps().size() + 1;
        return new OreStack(BWGameOre.DIAMOND, price);
    }

    public ItemStack getItemShop(BWGamePlayer hyriPlayer){
        BWGameTeam team = hyriPlayer.getHyriTeam();
        Player player = hyriPlayer.getPlayer();
        OreStack price = this.getPrice(team);
        List<String> lore = new ArrayList<>();

        if(this.getDescription() != null) {
            lore.addAll(StringBWUtils.loreToList(this.getDescription().getForPlayer(player)));
            lore.add(" ");
        }

        lore.add(ChatColor.GRAY + String.format(HyriBedWars.getLanguageManager().getValue(player, "shop.cost"), StringBWUtils.getCountPriceAsString(player, price)));
        lore.add(" ");

        boolean hasItems = InventoryBWUtils.hasPrice(player, Collections.singletonList(price));

        lore.add((hasItems ? ChatColor.YELLOW + HyriBedWars.getLanguageManager().getValue(player, "inv.shop.click.purchase") :
                ChatColor.RED + HyriBedWars.getLanguageManager().getValue(player, "inv.shop.enough.item") + " " +
                        StringBWUtils.getPriceAsString(player, price)));

        return new ItemBuilder(this.material)
                .withName((hasItems ? ChatColor.GREEN : ChatColor.RED) + this.getName().getForPlayer(player))
                .withAllItemFlags().withLore(lore).build();
    }

    private String getValue(Player player, String key){
        return HyriBedWars.getLanguageManager().getValue(player, "trap." + key);
    }

}

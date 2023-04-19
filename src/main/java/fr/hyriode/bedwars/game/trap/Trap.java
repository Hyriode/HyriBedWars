package fr.hyriode.bedwars.game.trap;

import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import fr.hyriode.bedwars.game.team.trap.TrapTeam;
import fr.hyriode.bedwars.game.type.BWGameType;
import fr.hyriode.bedwars.utils.StringUtils;
import fr.hyriode.bedwars.utils.TriConsumer;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.api.language.HyriLanguageMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Trap {

    private final String name;
    private final ItemStack icon;
    private final int timeSeconds;
    private final boolean showTitle;
    private final TriConsumer<BWGamePlayer, BWGameTeam, Trap> action;

    public Trap(String name, ItemStack icon, int timeSeconds, TriConsumer<BWGamePlayer, BWGameTeam, Trap> action, boolean showTitle) {
        this.name = name;
        this.icon = icon;
        this.timeSeconds = timeSeconds;
        this.showTitle = showTitle;
        this.action = action;
    }

    public String getName() {
        return name;
    }

    public HyriLanguageMessage getDisplayName(){
        return HyriLanguageMessage.get("trap." + this.name + ".name");
    }

    public HyriLanguageMessage getDisplayLore(){
        return HyriLanguageMessage.get("trap." + this.name + ".lore");
    }

    public ItemStack getIcon() {
        return icon;
    }

    public boolean isShowTitle() {
        return showTitle;
    }

    public int getTimeSeconds() {
        return timeSeconds;
    }

    public void active(BWGamePlayer enemy, BWGameTeam team) {
        this.action.accept(enemy, team, this);
    }

    public ItemStack getIconForUpgrade(BWGameType gameType, Player player, TrapTeam trapTeam) {
        List<String> lore = new ArrayList<>(StringUtils.loreToList(this.getDisplayLore().getValue(player)));

        lore.add(" ");
        if(trapTeam.isFull())
            lore.add(ChatColor.RED + "Full");
        else
            lore.add(StringUtils.getDisplayCostPrice(gameType, player, trapTeam.getPrice()));

        return new ItemBuilder(this.icon.clone())
                .withName(StringUtils.getTitleBuy(trapTeam.isFull(), trapTeam.getPrice().hasPrice(gameType, player)) + this.getDisplayName().getValue(player))
                .withLore(lore)
                .build();
    }
}

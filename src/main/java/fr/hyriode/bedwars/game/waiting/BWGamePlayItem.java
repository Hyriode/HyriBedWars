package fr.hyriode.bedwars.game.waiting;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.api.player.BWPlayerData;
import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.bedwars.utils.BWItemBuilder;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.item.HyriItem;
import fr.hyriode.api.language.HyriLanguageMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class BWGamePlayItem extends HyriItem<HyriBedWars> {

    private final HyriBedWars plugin;

    public BWGamePlayItem(HyriBedWars plugin) {
        super(plugin, "gamestylemode", () -> HyriLanguageMessage.get("item.gamestyle.title"), null, Material.REDSTONE_COMPARATOR);
        this.plugin = plugin;
    }

    @Override
    public void onRightClick(IHyrame hyrame, PlayerInteractEvent event) {
        this.action(hyrame, event.getPlayer());
    }

    @Override
    public ItemStack onPreGive(IHyrame hyrame, Player player, int slot, ItemStack itemStack) {
        BWPlayerData account = this.plugin.getGame().getPlayer(player).getAccount();

        return new BWItemBuilder(itemStack)
                .withDescription(HyriLanguageMessage.get("item.gamestyle.lore").getValue(player))
                .withName(ChatColor.WHITE + HyriLanguageMessage.get("item.gamestyle.title").getValue(player) + ": " +
                    account.getGameStyle().getNameColor())
                .build();
    }

    @Override
    public void onGive(IHyrame hyrame, Player player, int slot, ItemStack itemStack) {
        player.getInventory().setItem(4, itemStack);
    }

    @Override
    public void onInventoryClick(IHyrame hyrame, InventoryClickEvent event) {
        this.action(hyrame, (Player) event.getWhoClicked());
    }

    private void action(IHyrame hyrame, Player player){
        System.out.println("Player: " + this.plugin.getGame().getPlayer(player));
        BWPlayerData account = this.plugin.getGame().getPlayer(player).getAccount();

        account.changeGamePlayStyle();

        hyrame.getItemManager().giveItem(player, 4, BWGamePlayItem.class);
    }

    public static ItemStack getItemStack(BWGamePlayer player){
        return new BWItemBuilder(Material.REDSTONE_COMPARATOR)
                .withDescription(HyriLanguageMessage.get("item.gamestyle.lore").getValue(player.getPlayer()))
                .withName(ChatColor.WHITE + HyriLanguageMessage.get("item.gamestyle.title").getValue(player.getPlayer()) + ": " + player.getAccount().getGameStyle().getNameColor())
                .build();
    }
}

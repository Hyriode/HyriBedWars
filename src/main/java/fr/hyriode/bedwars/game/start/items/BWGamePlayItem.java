package fr.hyriode.bedwars.game.start.items;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.api.player.HyriBWPlayer;
import fr.hyriode.bedwars.utils.StringBWUtils;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.item.HyriItem;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.language.HyriLanguageMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class BWGamePlayItem extends HyriItem<HyriBedWars> {

    private final HyriBedWars plugin;

    public BWGamePlayItem(HyriBedWars plugin) {
        super(plugin, "gamestylemode", () -> new HyriLanguageMessage(""), Material.REDSTONE_COMPARATOR);
        this.plugin = plugin;
    }

    @Override
    public void onRightClick(IHyrame hyrame, PlayerInteractEvent event) {
        this.action(hyrame, event.getPlayer());
    }

    @Override
    public ItemStack onPreGive(IHyrame hyrame, Player player, int slot, ItemStack itemStack) {
        HyriBWPlayer account = this.plugin.getGame().getPlayer(player).getAccount();

        return new ItemBuilder(itemStack)
                .withName(ChatColor.WHITE + HyriBedWars.getLanguageManager().getValue(player, "item.gamestyle.title") + ": " + account.getGameStyle().getNameColor())
                .withLore(StringBWUtils.loreToList(HyriBedWars.getLanguageManager().getValue(player, "item.gamestyle.lore")))
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
        HyriBWPlayer account = this.plugin.getGame().getPlayer(player).getAccount();

        account.changeGamePlayStyle();
        account.update();

        hyrame.getItemManager().giveItem(player, 4, BWGamePlayItem.class);
    }

    public static ItemStack getItemStack(Player player, HyriBWPlayer account){
        return new ItemBuilder(Material.REDSTONE_COMPARATOR)
                .withName(ChatColor.WHITE + HyriBedWars.getLanguageManager().getValue(player, "item.gamestyle.title") + ": " + account.getGameStyle().getNameColor())
                .withLore(StringBWUtils.loreToList(HyriBedWars.getLanguageManager().getValue(player, "item.gamestyle.lore")))
                .build();
    }
}

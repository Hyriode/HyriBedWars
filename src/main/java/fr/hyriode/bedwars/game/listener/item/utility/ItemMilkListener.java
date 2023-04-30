package fr.hyriode.bedwars.game.listener.item.utility;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.hyrame.listener.HyriListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

public class ItemMilkListener extends HyriListener<HyriBedWars> {

    public ItemMilkListener(HyriBedWars plugin) {
        super(plugin);
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event){
        BWGamePlayer player = this.plugin.getGame().getPlayer(event.getPlayer());
        ItemStack itemStack = event.getItem();

        if(itemStack.isSimilar(this.plugin.getShopManager().getItemShopByName(true, "magic-milk").getItem(this.plugin.getShopManager()))){
            player.addCountdown(BWGamePlayer.TRAP_COUNTDOWN, 30*20, "trap.timer.magic-milk");
        }
    }
}

package fr.hyriode.bedwars.game.material.utility;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.BWGameOre;
import fr.hyriode.bedwars.game.npc.inventory.shop.BWShopCategory;
import fr.hyriode.bedwars.game.material.HyriShopItem;
import fr.hyriode.bedwars.game.material.OreStack;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class BridgeEgg extends HyriShopItem<HyriBedWars> {

    public BridgeEgg(HyriBedWars plugin) {
        super(plugin, "bridge_egg", Material.EGG, BWShopCategory.UTILITY, new OreStack(BWGameOre.EMERALD, 1));
    }

    @Override
    public void onRightClick(IHyrame hyrame, PlayerInteractEvent event) {
    }

    @Override
    public void onGive(IHyrame hyrame, Player player, int slot, ItemStack itemStack) {
        super.onGive(hyrame, player, slot, itemStack);
        player.getInventory().setItem(slot, new ItemBuilder(itemStack).nbt().setBoolean("BridgeEgg", true).build());
    }
}

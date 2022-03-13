package fr.hyriode.bedwars.game.npc.inventory.shop.utility;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.BWGameOre;
import fr.hyriode.bedwars.game.npc.inventory.shop.BWShopCategory;
import fr.hyriode.bedwars.game.npc.inventory.shop.material.HyriShopItem;
import fr.hyriode.bedwars.game.npc.inventory.shop.material.OreStack;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.item.ItemNBT;
import org.bukkit.Material;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class BridgeEgg extends HyriShopItem<HyriBedWars> {

    public BridgeEgg(HyriBedWars plugin) {
        super(plugin, "bridge_egg", Material.EGG, BWShopCategory.UTILITY, new OreStack(BWGameOre.EMERALD, 1));
    }

    @Override
    public void onRightClick(IHyrame hyrame, PlayerInteractEvent event) {
    }

    @Override
    public void onGive(IHyrame hyrame, Player player, int slot, ItemStack itemStack) {
        player.getInventory().setItem(slot, new ItemBuilder(itemStack).nbt().setBoolean("BridgeEgg", true).build());

    }
}

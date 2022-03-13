package fr.hyriode.bedwars.game.npc.inventory.shop.material;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.BWGamePlayer;
import fr.hyriode.bedwars.game.npc.inventory.shop.BWShopInventory;
import fr.hyriode.hyrame.item.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class ItemEmptySlot extends ItemShop{
    public ItemEmptySlot() {
        super("empty_slot", new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14), null, false);
    }

    @Override
    public ItemStack getItemToReplace(BWGamePlayer hyriPlayer) {
        return new ItemBuilder(this.getItemStack())
                .withName(ChatColor.RED + this.getName().getForPlayer(hyriPlayer.getPlayer()))
                .withAllItemFlags()
                .withLore(ChatColor.YELLOW + HyriBedWars.getLanguageManager().getValue(hyriPlayer.getPlayer(), "inv.choice_slot.replace"))
                .build();
    }

    @Override
    public Consumer<InventoryClickEvent> getClick(HyriBedWars plugin, BWShopInventory inventory) {
        return event -> {
            inventory.getPlayer().getPlayer().sendMessage(ChatColor.RED + "This is a quick buy truc la");
            inventory.getPlayer().getPlayer().sendMessage(ChatColor.AQUA + "Sneak click pour truc tu sais quoi (j'ai" +
                    " fais l'effort d'écrire ce texte pour dire que j'ai fais l'effort de mettre de la couleur)");
        };
    }
}

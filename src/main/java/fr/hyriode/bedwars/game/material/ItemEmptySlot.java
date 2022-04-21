package fr.hyriode.bedwars.game.material;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.BWGamePlayer;
import fr.hyriode.bedwars.game.npc.inventory.shop.BWShopInventory;
import fr.hyriode.bedwars.utils.StringBWUtils;
import fr.hyriode.hyrame.item.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ItemEmptySlot extends ItemShop{
    public ItemEmptySlot() {
        super("empty_slot", new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14), null, false, null);
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

    @Override
    public ItemStack getItemForShop(BWShopInventory inventory, BWGamePlayer hyriPlayer) {
        Player player = hyriPlayer.getPlayer();
        List<String> lore = new ArrayList<>();
        if(this.getDescription() != null) {
            lore.addAll(StringBWUtils.loreToList(this.getDescription().getForPlayer(player)));
        }
        return new ItemBuilder(this.getItemStack())
                .withName(ChatColor.RED + this.getName().getForPlayer(player))
                .withAllItemFlags().withLore(lore).build();
    }
}

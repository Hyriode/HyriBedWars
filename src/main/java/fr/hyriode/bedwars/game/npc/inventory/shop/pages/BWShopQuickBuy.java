package fr.hyriode.bedwars.game.npc.inventory.shop.pages;

import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.npc.inventory.shop.BWShopCategory;
import fr.hyriode.bedwars.game.npc.inventory.shop.BWShopInventory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BWShopQuickBuy extends BWShopInventory {
    public BWShopQuickBuy(HyriBedWars plugin, Player owner) {
        super(plugin, owner, BWShopCategory.QUICK_BUY);
    }

    @Override
    protected void initGui() {
        super.initGui();

        this.setItem(19, this.getEmptySlot(), event -> owner.sendMessage("Ta cliquer"));
        this.setItem(45, new ItemBuilder(Material.COMPASS)
                .withName(ChatColor.GREEN + HyriBedWars.getLanguageManager().getValue(this.owner, "inv.shop.quick_buy.tracker.name"))
                .withLore(ChatColor.GRAY + HyriBedWars.getLanguageManager().getValue(this.owner, "inv.shop.quick_buy.tracker.lore1"),
                        ChatColor.GRAY + HyriBedWars.getLanguageManager().getValue(this.owner, "inv.shop.quick_buy.tracker.lore2"),
                        ChatColor.GRAY + HyriBedWars.getLanguageManager().getValue(this.owner, "inv.shop.quick_buy.tracker.lore3"),
                        ChatColor.GRAY + HyriBedWars.getLanguageManager().getValue(this.owner, "inv.shop.quick_buy.tracker.lore4"))
                .build());

        this.setItem(53, new ItemBuilder(Material.BLAZE_POWDER)
                .withName(ChatColor.GREEN + HyriBedWars.getLanguageManager().getValue(this.owner, "inv.shop.quick_buy.hotbar.name"))
                .withLore(ChatColor.GRAY + HyriBedWars.getLanguageManager().getValue(this.owner, "inv.shop.quick_buy.hotbar.lore1"),
                        ChatColor.GRAY + HyriBedWars.getLanguageManager().getValue(this.owner, "inv.shop.quick_buy.hotbar.lore2"),
                        " ",
                        ChatColor.GRAY + HyriBedWars.getLanguageManager().getValue(this.owner, "inv.shop.click.edit"))
                .build());
    }

    private ItemStack getEmptySlot(){
        return new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 14)
                .withName(ChatColor.RED + HyriBedWars.getLanguageManager().getValue(this.owner, "inv.shop.empty.name"))
                .withLore(ChatColor.GRAY + HyriBedWars.getLanguageManager().getValue(this.owner, "inv.shop.empty.lore1"),
                        ChatColor.AQUA + HyriBedWars.getLanguageManager().getValue(this.owner, "inv.shop.sneak.click") + " " + ChatColor.GRAY + HyriBedWars.getLanguageManager().getValue(this.owner, "inv.shop.empty.lore2"),
                        ChatColor.GRAY + HyriBedWars.getLanguageManager().getValue(this.owner, "inv.shop.empty.lore3"))
                .build();
    }
}

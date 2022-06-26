package fr.hyriode.bedwars.game.gui;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.bedwars.game.shop.FakeMaterialShop;
import fr.hyriode.bedwars.game.shop.ItemPrice;
import fr.hyriode.bedwars.game.shop.MaterialShop;
import fr.hyriode.hyrame.inventory.HyriInventory;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.language.HyriLanguageMessage;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public abstract class BWGui extends HyriInventory {

    protected HyriBedWars plugin;

    public BWGui(Player owner, HyriBedWars plugin, HyriLanguageMessage languageMessage, TypeSize size) {
        this(owner, plugin, languageMessage.getForPlayer(owner), size);

    }

    public BWGui(Player owner, HyriBedWars plugin, String name, TypeSize size) {
        super(owner, name, size.getSize());

        this.plugin = plugin;
    }

    protected abstract void initGui();

    protected abstract void refresh();

    public HyriBedWars getPlugin() {
        return plugin;
    }

    public BWGamePlayer getPlayer(){
        return this.plugin.getGame().getPlayer(this.owner);
    }

    public void setItem(int x /* max 9 */, int y /* max 6 */, ItemStack itemStack, Consumer<InventoryClickEvent> click){
        x -= 1;
        y -= 1;
        super.setItem(x+(y*9), itemStack, click);
    }

    public void setItem(int x /* max 9 */, int y /* max 6 */, ItemStack itemStack){
        this.setItem(x, y, itemStack, null);
    }

    protected ItemStack getItemDeco(){
        return new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 9).withName(" ").build();
    }

    public enum TypeSize{
        LINE_1(9),
        LINE_2(18),
        LINE_3(27),
        LINE_4(39),
        LINE_5(45),
        LINE_6(54),

        ;
        
        private final int size;
        
        TypeSize(int size){
            this.size = size;
        }

        public int getSize() {
            return size;
        }
    }
}

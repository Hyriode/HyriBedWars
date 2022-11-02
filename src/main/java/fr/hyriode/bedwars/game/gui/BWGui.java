package fr.hyriode.bedwars.game.gui;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.bedwars.utils.InventoryUtils;
import fr.hyriode.hyrame.inventory.HyriInventory;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.api.language.HyriLanguageMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public abstract class BWGui extends HyriInventory {

    protected HyriBedWars plugin;
    protected BWGui backGui;

    public BWGui(Player owner, HyriBedWars plugin, HyriLanguageMessage languageMessage, TypeSize size, BWGui backGui) {
        this(owner, plugin, languageMessage.getValue(owner), size, backGui, true);
    }

    public BWGui(Player owner, HyriBedWars plugin, HyriLanguageMessage languageMessage, TypeSize size, BWGui backGui, boolean autoInit) {
        this(owner, plugin, languageMessage.getValue(owner), size, backGui, autoInit);
    }

    public BWGui(Player owner, HyriBedWars plugin, String name, TypeSize size, BWGui backGui) {
        this(owner, plugin, name, size, backGui, true);
    }

    public BWGui(Player owner, HyriBedWars plugin, String name, TypeSize size, BWGui backGui, boolean autoInit) {
        super(owner, name, size.getSize());

        this.backGui = backGui;
        this.plugin = plugin;
        if(autoInit) this.initGui();
    }

    protected abstract void initGui();

    protected abstract void refresh();

    protected void back(){
        if(this.backGui != null) {
            this.backGui.open();
        }
    }

    public HyriBedWars getPlugin() {
        return plugin;
    }

    public BWGamePlayer getPlayer(){
        return this.plugin.getGame().getPlayer(this.owner);
    }

    public void setItem(int x /* max 9 */, int y /* max 6 */, ItemStack itemStack, Consumer<InventoryClickEvent> click){
        super.setItem(InventoryUtils.getSlotByXY(x, y), itemStack, click);
    }

    public void setItem(int x /* max 9 */, int y /* max 6 */, ItemStack itemStack){
        this.setItem(x, y, itemStack, null);
    }

    protected ItemStack getItemDeco(){
        return new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 9).withName(" ").build();
    }

    protected ItemStack getItemBack() {
        return new ItemBuilder(Material.ARROW)
                .withName(ChatColor.RED + HyriLanguageMessage.get("gui.back").getValue(this.owner))
                .withLore(this.backGui != null ? ChatColor.GRAY + HyriLanguageMessage.get("gui.back.lore").getValue(this.owner) + " " +  this.backGui.getName() : " ")
                .build();
    }

    public enum TypeSize{
        LINE_1(9),
        LINE_2(18),
        LINE_3(27),
        LINE_4(36),
        LINE_5(45),
        LINE_6(54);

        private final int size;
        
        TypeSize(int size){
            this.size = size;
        }

        public int getSize() {
            return size;
        }
    }
}

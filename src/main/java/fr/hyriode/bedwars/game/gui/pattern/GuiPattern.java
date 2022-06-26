package fr.hyriode.bedwars.game.gui.pattern;

import fr.hyriode.bedwars.game.gui.shop.ShopGui;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.function.Consumer;

public abstract class GuiPattern {

    protected final HashMap<Integer, ItemSlot> slots;
    protected final Size size;
    protected final ShopGui gui;

    public GuiPattern(ShopGui gui, Size size){
        this.gui = gui;
        this.size = size;
        this.slots = new HashMap<>();
    }

    public void initGui() {
        this.slots.forEach((slot, itemSlot) -> this.gui.setItem(slot, itemSlot.getItemStack(), itemSlot.getInteract()));
    }

    protected void setSlot(int slot, ItemStack itemStack, Consumer<InventoryClickEvent> interact){
        this.slots.put(slot, new ItemSlot(itemStack, interact));
    }

    public void setSlot(int x /* max 9 */, int y /* max 6 */, ItemStack itemStack, Consumer<InventoryClickEvent> click){
        x -= 1;
        y -= 1;
        this.setSlot(x+(y*9), itemStack, click);
    }

    protected void setLineVertical(int x, int y, int width, ItemStack itemStack){
        for(int w = 0; w < width ; ++w) {
            this.setSlot(x + w, y, itemStack);
        }
    }

    protected void setLineHorizontal(int x, int y, int height, ItemStack itemStack){
        for(int h = 0 ; h < height ; ++h){
            this.setSlot(x, y + h, itemStack);
        }
    }

    public void setSlot(int x /* max 9 */, int y /* max 6 */, ItemStack itemStack){
        this.setSlot(x, y, itemStack, null);
    }

    protected void setSlot(int slot, ItemStack itemStack){
        this.slots.put(slot, new ItemSlot(itemStack));
    }

    public HashMap<Integer, ItemSlot> getSlots() {
        return slots;
    }

    public Size getSize() {
        return size;
    }

    public static class Size{
        private final int startX;
        private final int startY;
        private final int width;
        private final int height;

        public Size(int startX, int startY, int width, int height) {
            this.startX = startX;
            this.startY = startY;
            this.width = width;
            this.height = height;
        }

        public int getStartX() {
            return startX;
        }

        public int getStartY() {
            return startY;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public int getSizeBox(){
            return this.width * this.height;
        }
    }

    static class ItemSlot{

        private final ItemStack itemStack;
        private final Consumer<InventoryClickEvent> interact;

        public ItemSlot(ItemStack itemStack, Consumer<InventoryClickEvent> interact){
            this.itemStack = itemStack;
            this.interact = interact;
        }

        public ItemSlot(ItemStack itemStack){
            this(itemStack, event -> {});
        }

        public ItemStack getItemStack() {
            return itemStack;
        }

        public Consumer<InventoryClickEvent> getInteract() {
            return interact;
        }
    }

}

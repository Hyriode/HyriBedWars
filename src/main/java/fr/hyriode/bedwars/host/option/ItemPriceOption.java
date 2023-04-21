package fr.hyriode.bedwars.host.option;

import fr.hyriode.bedwars.game.shop.ItemMoney;
import fr.hyriode.bedwars.game.shop.ItemPrice;
import fr.hyriode.bedwars.utils.InventoryUtils;
import fr.hyriode.hyrame.host.HostDisplay;
import fr.hyriode.hyrame.host.option.HostOption;
import fr.hyriode.hyrame.inventory.HyriInventory;
import fr.hyriode.hyrame.item.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class ItemPriceOption extends HostOption<ItemPrice> {
    public ItemPriceOption(HostDisplay hostDisplay, ItemPrice price) {
        super(hostDisplay, price);
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event) {
        new GUI(player).open();
    }

    private class GUI extends HyriInventory {
        public GUI(Player player) {
            super(player, displayName.getValue(player).length() > 30 ? displayName.getValue(player).substring(0, 29) + "..." : displayName.getValue(player), 6 * 9);

            this.setItem(InventoryUtils.getSlotByXY(3, 3), getValue().getItemStack(), event -> {
                new ItemGUI(player).open();
            });
        }

        private class ItemGUI extends HyriInventory {
            public ItemGUI(Player player) {
                super(player, "Item", 6 * 9);

                this.setMoney(4, 3, ItemMoney.IRON);
                this.setMoney(5, 3, ItemMoney.GOLD);
                this.setMoney(6, 3, ItemMoney.DIAMOND);
                this.setMoney(5, 4, ItemMoney.EMERALD);
            }

            private void setMoney(int x, int y, ItemMoney money){
                ItemStack itemStack = money.getAsItemStack();
                this.setItem(InventoryUtils.getSlotByXY(x, y), getValue().getItemStack().getType() == itemStack.getType()
                        ? new ItemBuilder(itemStack).withGlow().build() : itemStack, click(money));
            }

            private Consumer<InventoryClickEvent> click(ItemMoney money){
                return event -> {
                    setValue(getValue().setItemStack(money));

                };
            }
        }
    }
}

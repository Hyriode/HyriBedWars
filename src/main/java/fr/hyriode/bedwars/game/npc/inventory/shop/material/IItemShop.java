package fr.hyriode.bedwars.game.npc.inventory.shop.material;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.npc.inventory.shop.BWShopCategory;
import fr.hyriode.bedwars.utils.MetadataReferences;
import fr.hyriode.bedwars.utils.StringBWUtils;
import fr.hyriode.hyrame.item.HyriItem;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.language.HyriLanguageMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public interface IItemShop {

    String getKeyName();

    ItemStack getMaterial();

    List<OreStack> getPrice();

    default String getCountPriceAsString(Player player){
        return StringBWUtils.getCountPriceAsString(player, this.getPrice());
    }

    default String getPriceAsString(Player player){
        return StringBWUtils.getPriceAsString(player, this.getPrice());
    }

    BWShopCategory getCategory();

    boolean isPermanent();

    default HyriLanguageMessage getName(){
        return HyriBedWars.getLanguageManager().getMessage("shop.item." + this.getKeyName() + ".name");
    }

    default HyriLanguageMessage getDescription(){
        return HyriBedWars.getLanguageManager().getMessage("shop.item." + this.getKeyName() + ".lore");
    }

    default ItemStack getItemStack(){
        return new ItemBuilder(this.getItemStack()).nbt().setBoolean(MetadataReferences.ISPERMANENT, this.isPermanent()).build();
    }

    ChatColor getColor();

    Class<? extends HyriItem<?>> getHyriItem();

    IItemShop setUpgradable(boolean upgradable);

    boolean isUpgradable();

    IItemShop setHyriMaterial(BWMaterial material);

    BWMaterial getHyriMaterial();

}

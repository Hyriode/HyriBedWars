package fr.hyriode.bedwars.utils;

import fr.hyriode.hyrame.item.ItemNBT;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MetadataReferences {

    //TODO To change this for remove or move
    public static final String ISPERMANENT = "IsPermanent";
    public static final String PLACEBYPLAYER = "PlacedByPlayer";
    public static final String ISINVISIBLE = "IsInvisible";
    public static final String POPUP_TOWER = "PopupTower";
    public static final String BEDBUG = "Bedbug";
    public static final String DREAM_DEFENDER = "DreamDefender";
    public static final String FIREBALL = "Fireball";
    public static final String BRIDGE_EGG = "BridgeEgg";
    public static final String MAGIC_MILK = "MagikMilk";
    public static final String SPONGE = "Sponge";
    public static final String MATERIAL = "Material";
    public static final String COMPASS = "Compass";

    public static boolean isPermanent(ItemStack itemStack){
        if(itemStack != null && itemStack.getType() != Material.AIR){
            ItemNBT item = new ItemNBT(itemStack);
            return item.hasTag(ISPERMANENT) && item.getBoolean(ISPERMANENT);
        }
        return false;
    }

    public static boolean isInvisible(Player player){
        return player.hasMetadata(ISINVISIBLE) && player.getMetadata(ISINVISIBLE).get(0).asBoolean();
    }

    public static boolean isMetaItem(String metadata, ItemStack itemStack) {
        if(itemStack != null && itemStack.getType() != Material.AIR){
            ItemNBT item = new ItemNBT(itemStack);
            return item.hasTag(metadata) && item.getBoolean(metadata);
        }
        return false;
    }
}

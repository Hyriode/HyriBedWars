package fr.hyriode.bedwars.utils;

import fr.hyriode.hyrame.item.ItemNBT;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MetadataReferences {

    public static final String ISPERMANENT = "IsPermanent";
    public static final String PLACEBYPLAYER = "PlacedByPlayer";
    public static final String ISINVISIBLE = "IsInvisible";

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
}

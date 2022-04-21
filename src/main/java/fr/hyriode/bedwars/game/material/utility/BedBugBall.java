package fr.hyriode.bedwars.game.material.utility;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.material.BWMaterial;
import fr.hyriode.bedwars.game.material.BWMaterials;
import fr.hyriode.bedwars.game.material.HyriShopItem;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.item.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BedBugBall extends HyriShopItem<HyriBedWars> {

    public BedBugBall(HyriBedWars plugin) {
        super(plugin, BWMaterial.BEDBUG);
    }

    @Override
    public ItemStack onPreGive(IHyrame hyrame, Player player, int slot, ItemStack itemStack) {
        return new ItemBuilder(super.onPreGive(hyrame, player, slot, itemStack)).nbt().setBoolean("BedBug", true).build();
    }
}

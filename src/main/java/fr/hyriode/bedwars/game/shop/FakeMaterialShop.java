package fr.hyriode.bedwars.game.shop;

import fr.hyriode.hyrame.item.ItemBuilder;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.Collections;

public class FakeMaterialShop extends MaterialShop{
    public FakeMaterialShop() {
        super("fake", null, false, Collections.singletonList(new ItemShop(new ItemBuilder(Material.AIR), null)));
    }
}

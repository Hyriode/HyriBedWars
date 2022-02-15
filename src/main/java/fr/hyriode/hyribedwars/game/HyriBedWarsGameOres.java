package fr.hyriode.hyribedwars.game;

import org.bukkit.Material;

public enum HyriBedWarsGameOres {

    IRON("iron", 1, Material.IRON_INGOT),
    GOLD("gold", 2, Material.GOLD_INGOT),
    DIAMOND("diamond", 3, Material.DIAMOND),
    EMERALD("emerald", 4, Material.EMERALD),
    COAL("coal", 5, Material.COAL),

    ;

    private final String name;
    private final int id;
    private final Material material;

    HyriBedWarsGameOres(String name, int id, Material material) {
        this.name = name;
        this.id = id;
        this.material = material;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Material getMaterial() {
        return material;
    }
}

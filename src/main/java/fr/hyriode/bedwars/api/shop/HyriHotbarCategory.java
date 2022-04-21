package fr.hyriode.bedwars.api.shop;

public enum HyriHotbarCategory {

    BLOCKS(0),
    MELEE(1),
    TOOLS(2),
    RANGED(3),
    POTIONS(4),
    UTILITY(5),
    COMPASS(6),

    ;

    private final int id;

    HyriHotbarCategory(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }
}

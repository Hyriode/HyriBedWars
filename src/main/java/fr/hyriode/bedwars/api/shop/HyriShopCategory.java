package fr.hyriode.bedwars.api.shop;

public enum HyriShopCategory {

    QUICK_BUY(0),
    BLOCKS(1),
    MELEE(2),
    ARMOR(3),
    TOOLS(4),
    RANGED(5),
    POTIONS(6),
    UTILITY(7),
    ;

    private final int id;

    HyriShopCategory(int id){
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}

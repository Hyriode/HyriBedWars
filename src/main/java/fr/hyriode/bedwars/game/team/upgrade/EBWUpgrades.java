package fr.hyriode.bedwars.game.team.upgrade;

import fr.hyriode.bedwars.game.BWGameOre;
import fr.hyriode.bedwars.game.npc.inventory.shop.ItemShopStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum EBWUpgrades {

    SHARPNESS(new BWUpgrade("sharpness", new BWUpgradeTier(0, "sharpness", new ItemStack(Material.IRON_SWORD), new ItemShopStack(BWGameOre.DIAMOND, 4)))),
    PROTECTION_ARMOR(new BWUpgrade("reinforced_armor",
            new BWUpgradeTier(0, "protection1", new ItemStack(Material.IRON_CHESTPLATE), new ItemShopStack(BWGameOre.DIAMOND, 2)),
            new BWUpgradeTier(1, "protection2", new ItemStack(Material.IRON_CHESTPLATE, 2), new ItemShopStack(BWGameOre.DIAMOND, 4)),
            new BWUpgradeTier(2, "protection3", new ItemStack(Material.IRON_CHESTPLATE, 3), new ItemShopStack(BWGameOre.DIAMOND, 8)),
            new BWUpgradeTier(3, "protection4", new ItemStack(Material.IRON_CHESTPLATE, 4), new ItemShopStack(BWGameOre.DIAMOND, 16))
    )),
    HASTE(new BWUpgrade("maniac_miner",
            new BWUpgradeTier(0, "haste1", new ItemStack(Material.IRON_PICKAXE), new ItemShopStack(BWGameOre.DIAMOND, 2)),
            new BWUpgradeTier(1, "haste2", new ItemStack(Material.IRON_PICKAXE, 2), new ItemShopStack(BWGameOre.DIAMOND, 4))
    )),
    FORGE(new BWUpgrade("forge",
            new BWUpgradeTier(0, "forge1", new ItemStack(Material.FURNACE), new ItemShopStack(BWGameOre.DIAMOND, 2)),
            new BWUpgradeTier(1, "forge2", new ItemStack(Material.FURNACE, 2), new ItemShopStack(BWGameOre.DIAMOND, 4)),
            new BWUpgradeTier(2, "forge3", new ItemStack(Material.FURNACE, 3), new ItemShopStack(BWGameOre.DIAMOND, 6)),
            new BWUpgradeTier(3, "forge4", new ItemStack(Material.FURNACE, 4), new ItemShopStack(BWGameOre.DIAMOND, 8))
    )),
    HEAL_POOL(new BWUpgrade("heal_pool", new BWUpgradeTier(0, "heal_pool", new ItemStack(Material.BEACON), new ItemShopStack(BWGameOre.DIAMOND, 1)))),
    DRAGON(new BWUpgrade("dragon_buff", new BWUpgradeTier(0, "dragon_buff", new ItemStack(Material.DRAGON_EGG), new ItemShopStack(BWGameOre.DIAMOND, 5)))),
    ;

    private final BWUpgrade upgrade;

    EBWUpgrades(BWUpgrade upgrade){
        this.upgrade = upgrade;

    }

    public BWUpgrade getUpgrade() {
        return upgrade;
    }
}

package fr.hyriode.bedwars.game.upgrade;

import fr.hyriode.bedwars.config.BWConfiguration;
import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.bedwars.game.shop.ItemMoney;
import fr.hyriode.bedwars.game.shop.ItemPrice;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.utils.ItemUtil;
import fr.hyriode.hyrame.utils.block.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

public class UpgradeManager {

    private final List<Upgrade> upgrades = new ArrayList<>();

    public static final String SHARPNESS = "sharpness";
    public static final String PROTECTION = "protection";
    public static final String MINER = "miner";
    public static final String FORGE = "forge";
    public static final String HEAL_POOL = "heal-pool";
    public static final String DRAGON_BUFF = "dragon";

    public UpgradeManager(){
        this.add(SHARPNESS, true, (player, __) -> {
            player.getPlayer().getInventory().forEach(itemStack -> {
                if (itemStack != null && ItemUtil.isSword(itemStack)){
                    itemStack.addEnchantment(Enchantment.DAMAGE_ALL, 1);
                }
            });
        }, new Upgrade.Tier(0, new ItemBuilder(Material.DIAMOND_SWORD).build(), new ItemPrice(ItemMoney.DIAMOND, 4)));
        this.add(PROTECTION, true, (player, tier) -> {
            for (ItemStack armorContent : player.getPlayer().getInventory().getArmorContents()) {
                if(armorContent != null){
                    armorContent.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1 + tier);
                }
            }
        }, new Upgrade.Tier(0, new ItemBuilder(Material.DIAMOND_CHESTPLATE).build(), new ItemPrice(ItemMoney.DIAMOND, 2)),
                new Upgrade.Tier(1, new ItemBuilder(Material.DIAMOND_CHESTPLATE, 2).build(), new ItemPrice(ItemMoney.DIAMOND, 4)),
                new Upgrade.Tier(2, new ItemBuilder(Material.DIAMOND_CHESTPLATE, 3).build(), new ItemPrice(ItemMoney.DIAMOND, 8)),
                new Upgrade.Tier(3, new ItemBuilder(Material.DIAMOND_CHESTPLATE, 4).build(), new ItemPrice(ItemMoney.DIAMOND, 16)));
        this.add(MINER, true, (player, tier) -> {
            Player p = player.getPlayer();
            p.removePotionEffect(PotionEffectType.FAST_DIGGING);
            p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 999999, tier));
                    System.out.println("Tier miner : " + tier);
        },
                new Upgrade.Tier(0, new ItemBuilder(Material.DIAMOND_PICKAXE).withAllItemFlags().build(), new ItemPrice(ItemMoney.DIAMOND, 2)),
                new Upgrade.Tier(1, new ItemBuilder(Material.DIAMOND_PICKAXE, 2).withAllItemFlags().build(), new ItemPrice(ItemMoney.DIAMOND, 4)));
        this.add(FORGE, false, (player, tier) -> player.getBWTeam().upgradeGenerator(tier),
                new Upgrade.Tier(0, new ItemBuilder(Material.FURNACE).build(), new ItemPrice(ItemMoney.DIAMOND, 2)),
                new Upgrade.Tier(1, new ItemBuilder(Material.FURNACE, 2).build(), new ItemPrice(ItemMoney.DIAMOND, 4)),
                new Upgrade.Tier(2, new ItemBuilder(Material.FURNACE, 3).build(), new ItemPrice(ItemMoney.DIAMOND, 6)),
                new Upgrade.Tier(3, new ItemBuilder(Material.FURNACE, 4).build(), new ItemPrice(ItemMoney.DIAMOND, 8)));
        this.add(HEAL_POOL, false, (player, __) -> player.getBWTeam().startHealPool(),
                new Upgrade.Tier(0, new ItemBuilder(Material.BEACON).build(), new ItemPrice(ItemMoney.DIAMOND, 1)));
        this.add(DRAGON_BUFF, false, (___, __) -> {},
                new Upgrade.Tier(0, new ItemBuilder(Material.DRAGON_EGG).build(), new ItemPrice(ItemMoney.DIAMOND, 5)));
    }

    private void add(String name, boolean player, BiConsumer<BWGamePlayer, Integer> action, Upgrade.Tier... tiers){
        this.upgrades.add(new Upgrade(name, player, action, Arrays.asList(tiers)));
    }

    public List<Upgrade> getUpgrades() {
        return upgrades;
    }

    public Upgrade getUpgradeByName(String name){
        return this.upgrades.stream().filter(upgrade -> upgrade.getName().equals(name)).findFirst().orElse(null);
    }
}

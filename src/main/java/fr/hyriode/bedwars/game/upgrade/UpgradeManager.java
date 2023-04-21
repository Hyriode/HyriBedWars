package fr.hyriode.bedwars.game.upgrade;

import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.bedwars.game.shop.ItemMoney;
import fr.hyriode.bedwars.game.shop.ItemPrice;
import fr.hyriode.bedwars.host.BWUpgradeValues;
import fr.hyriode.hyrame.game.util.value.ValueProvider;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.utils.ItemUtil;
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
import java.util.function.Supplier;

public class UpgradeManager {

    private final List<Upgrade> upgrades = new ArrayList<>();

    public static final String SHARPNESS = "sharpness";
    public static final String PROTECTION = "protection";
    public static final String MINER = "miner";
    public static final String FORGE = "forge";
    public static final String HEAL_POOL = "heal-pool";
    public static final String DRAGON_BUFF = "dragon-buff";

    public UpgradeManager(){
        this.add(() -> BWUpgradeValues.SHARPNESS_ENABLED, SHARPNESS, true, (player, __) -> {
            player.getPlayer().getInventory().forEach(itemStack -> {
                if (itemStack != null && ItemUtil.isSword(itemStack)){
                    itemStack.addEnchantment(Enchantment.DAMAGE_ALL, 1);
                }
            });
        }, new Upgrade.Tier(0, new ItemBuilder(Material.DIAMOND_SWORD).build(), new ItemPrice(ItemMoney.DIAMOND, BWUpgradeValues.SHARPNESS_PRICE_TIER_0)));
        this.add(() -> BWUpgradeValues.PROTECTION_ENABLED, PROTECTION, true, (player, tier) -> {
            for (ItemStack armorContent : player.getPlayer().getInventory().getArmorContents()) {
                if(armorContent != null){
                    armorContent.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1 + tier);
                }
            }
        }, new Upgrade.Tier(0, new ItemBuilder(Material.DIAMOND_CHESTPLATE).build(), new ItemPrice(ItemMoney.DIAMOND, BWUpgradeValues.PROTECTION_PRICE_TIER_0)),
                new Upgrade.Tier(1, new ItemBuilder(Material.DIAMOND_CHESTPLATE, 2).build(), new ItemPrice(ItemMoney.DIAMOND, BWUpgradeValues.PROTECTION_PRICE_TIER_1)),
                new Upgrade.Tier(2, new ItemBuilder(Material.DIAMOND_CHESTPLATE, 3).build(), new ItemPrice(ItemMoney.DIAMOND, BWUpgradeValues.PROTECTION_PRICE_TIER_2)),
                new Upgrade.Tier(3, new ItemBuilder(Material.DIAMOND_CHESTPLATE, 4).build(), new ItemPrice(ItemMoney.DIAMOND, BWUpgradeValues.PROTECTION_PRICE_TIER_3)));
        this.add(() -> BWUpgradeValues.MINER_ENABLED, MINER, true, (player, tier) -> {
            Player p = player.getPlayer();
            p.removePotionEffect(PotionEffectType.FAST_DIGGING);
            p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 999999, tier));
        },
                new Upgrade.Tier(0, new ItemBuilder(Material.DIAMOND_PICKAXE).withAllItemFlags().build(), new ItemPrice(ItemMoney.DIAMOND, BWUpgradeValues.MINER_PRICE_TIER_0)),
                new Upgrade.Tier(1, new ItemBuilder(Material.DIAMOND_PICKAXE, 2).withAllItemFlags().build(), new ItemPrice(ItemMoney.DIAMOND, BWUpgradeValues.MINER_PRICE_TIER_1)));
        this.add(() -> BWUpgradeValues.FORGE_ENABLED, FORGE, false, (player, tier) -> player.getBWTeam().upgradeGenerator(tier),
                new Upgrade.Tier(0, new ItemBuilder(Material.FURNACE).build(), new ItemPrice(ItemMoney.DIAMOND, BWUpgradeValues.FORGE_PRICE_TIER_0)),
                new Upgrade.Tier(1, new ItemBuilder(Material.FURNACE, 2).build(), new ItemPrice(ItemMoney.DIAMOND, BWUpgradeValues.FORGE_PRICE_TIER_1)),
                new Upgrade.Tier(2, new ItemBuilder(Material.FURNACE, 3).build(), new ItemPrice(ItemMoney.DIAMOND, BWUpgradeValues.FORGE_PRICE_TIER_2)),
                new Upgrade.Tier(3, new ItemBuilder(Material.FURNACE, 4).build(), new ItemPrice(ItemMoney.DIAMOND, BWUpgradeValues.FORGE_PRICE_TIER_3)));
        this.add(() -> BWUpgradeValues.HEAL_POOL_ENABLED, HEAL_POOL, false, (player, __) -> player.getBWTeam().startHealPool(),
                new Upgrade.Tier(0, new ItemBuilder(Material.BEACON).build(), new ItemPrice(ItemMoney.DIAMOND, BWUpgradeValues.HEAL_POOL_PRICE_TIER_0)));
        this.add(() -> BWUpgradeValues.DRAGON_BUFF_ENABLED, DRAGON_BUFF, false, (___, __) -> {},
                new Upgrade.Tier(0, new ItemBuilder(Material.DRAGON_EGG).build(), new ItemPrice(ItemMoney.DIAMOND, BWUpgradeValues.DRAGON_BUFF_PRICE_TIER_0)));
    }

    private void add(Supplier<ValueProvider<Boolean>> enabled, String name, boolean player, BiConsumer<BWGamePlayer, Integer> action, Upgrade.Tier... tiers){
        this.upgrades.add(new Upgrade(enabled, name, player, action, Arrays.asList(tiers)));
    }

    public List<Upgrade> getUpgrades() {
        return upgrades;
    }

    public Upgrade getUpgradeByName(String name){
        return this.upgrades.stream().filter(upgrade -> upgrade.getName().equals(name)).findFirst().orElse(null);
    }
}

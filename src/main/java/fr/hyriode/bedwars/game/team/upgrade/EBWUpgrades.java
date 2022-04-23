package fr.hyriode.bedwars.game.team.upgrade;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.BWGameOre;
import fr.hyriode.bedwars.game.BWGamePlayer;
import fr.hyriode.bedwars.game.generator.BWBaseGoldGenerator;
import fr.hyriode.bedwars.game.generator.BWBaseIronGenerator;
import fr.hyriode.bedwars.game.generator.BWEmeraldGenerator;
import fr.hyriode.bedwars.game.material.BWMaterial;
import fr.hyriode.bedwars.game.material.OreStack;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import fr.hyriode.bedwars.utils.InventoryBWUtils;
import fr.hyriode.bedwars.utils.StringBWUtils;
import fr.hyriode.bedwars.utils.TriConsumer;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.generator.HyriGenerator;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.language.HyriLanguageMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public enum EBWUpgrades implements BWUpgrade{

    SHARPNESS("sharpness", false, (plugin, player, tier) -> {
        InventoryBWUtils.changeItemsSlot(player.getPlayer(),
                itemStack -> itemStack.addEnchantment(Enchantment.DAMAGE_ALL, 1),
                new ItemStack(Material.WOOD_SWORD),
                BWMaterial.STONE_SWORD.getItemShop().getItemStack(),
                BWMaterial.IRON_SWORD.getItemShop().getItemStack(),
                BWMaterial.DIAMOND_SWORD.getItemShop().getItemStack());
    }, new BWUpgradeTier(0, "sharpness", new ItemStack(Material.IRON_SWORD), new OreStack(BWGameOre.DIAMOND, 4))),
    PROTECTION_ARMOR("reinforced_armor", false, (plugin, player, tier) -> {
        for (ItemStack armor : player.getPlayer().getInventory().getArmorContents()) {
            armor.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, tier + 1);
        }
    },
            new BWUpgradeTier(0, "protection1", new ItemStack(Material.IRON_CHESTPLATE), new OreStack(BWGameOre.DIAMOND, 2)),
            new BWUpgradeTier(1, "protection2", new ItemStack(Material.IRON_CHESTPLATE, 2), new OreStack(BWGameOre.DIAMOND, 4)),
            new BWUpgradeTier(2, "protection3", new ItemStack(Material.IRON_CHESTPLATE, 3), new OreStack(BWGameOre.DIAMOND, 8)),
            new BWUpgradeTier(3, "protection4", new ItemStack(Material.IRON_CHESTPLATE, 4), new OreStack(BWGameOre.DIAMOND, 16))
    ),
    HASTE("maniac_miner", false, (plugin, player, tier) -> {
        Player p = player.getPlayer();
        if(p.hasPotionEffect(PotionEffectType.FAST_DIGGING))
            p.removePotionEffect(PotionEffectType.FAST_DIGGING);
        p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 99999 * 20, tier));
    },
            new BWUpgradeTier(0, "haste1", new ItemStack(Material.IRON_PICKAXE), new OreStack(BWGameOre.DIAMOND, 2)),
            new BWUpgradeTier(1, "haste2", new ItemStack(Material.IRON_PICKAXE, 2), new OreStack(BWGameOre.DIAMOND, 4))
    ),
    FORGE("forge", true, (plugin, player, tier) -> {
        switch (tier){
            case 0:
                player.getHyriTeam().getIronGenerator().upgrade(BWBaseIronGenerator.BASE_II);
                player.getHyriTeam().getGoldGenerator().upgrade(BWBaseGoldGenerator.BASE_II);
                break;
            case 1:
                player.getHyriTeam().getIronGenerator().upgrade(BWBaseIronGenerator.BASE_III);
                player.getHyriTeam().getGoldGenerator().upgrade(BWBaseGoldGenerator.BASE_III);
                break;
            case 2:
                HyriGenerator emeraldGenerator = new HyriGenerator.Builder(plugin, player.getHyriTeam().getGeneratorLocation(), BWEmeraldGenerator.BASE)
                        .withItem(BWGameOre.EMERALD.getItemStack()).build();
                emeraldGenerator.create();
                break;
            case 3:
                player.getHyriTeam().getIronGenerator().upgrade(BWBaseIronGenerator.BASE_IV);
                player.getHyriTeam().getGoldGenerator().upgrade(BWBaseGoldGenerator.BASE_IV);
                break;
        }
    },
            new BWUpgradeTier(0, "forge1", new ItemStack(Material.FURNACE), new OreStack(BWGameOre.DIAMOND, 2)),
            new BWUpgradeTier(1, "forge2", new ItemStack(Material.FURNACE, 2), new OreStack(BWGameOre.DIAMOND, 4)),
            new BWUpgradeTier(2, "forge3", new ItemStack(Material.FURNACE, 3), new OreStack(BWGameOre.DIAMOND, 6)),
            new BWUpgradeTier(3, "forge4", new ItemStack(Material.FURNACE, 4), new OreStack(BWGameOre.DIAMOND, 8))
    ),
    HEAL_POOL("heal_pool", true, (plugin, player, tier) -> {
        BWGameTeam team = player.getHyriTeam();
        new BukkitRunnable(){
            @Override
            public void run() {
                if(player.getHyriTeam() == null) {
                    this.cancel();
                    return;
                }
                player.getHyriTeam().baseArea(location -> {
                    Random random = new Random();
                    int rand = random.nextInt(500);
                    if(rand == 1)
                        IHyrame.WORLD.get().playEffect(location, Effect.HAPPY_VILLAGER, 10);
                });
                team.getPlayers().forEach(player -> {
                    if(team.getBaseArea().isInArea(player.getPlayer().getLocation())){
                        player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 2, 1));
                    }
                });
            }
        }.runTaskTimer(plugin, 0L, 40L);
    },
            new BWUpgradeTier(0, "heal_pool", new ItemStack(Material.BEACON), new OreStack(BWGameOre.DIAMOND, 1))),
    DRAGON("dragon_buff", false, (plugin, player, tier) -> {},
            new BWUpgradeTier(0, "dragon_buff", new ItemStack(Material.DRAGON_EGG), new OreStack(BWGameOre.DIAMOND, 5))),
    ;


    private EBWUpgrades eUpgrades;

    private final BWUpgradeTier[] upgradesTier;
    private final String keyName;
    private final boolean forBase;
    private final TriConsumer<HyriBedWars, BWGamePlayer, Integer> action;

    EBWUpgrades(String keyName, boolean forBase, TriConsumer<HyriBedWars, BWGamePlayer, Integer> action, BWUpgradeTier... upgradesTier){
        this.keyName = keyName;
        this.forBase = forBase;
        this.upgradesTier = upgradesTier;
        this.action = action;
    }

    public String getKeyName() {
        return keyName;
    }

    public BWUpgradeTier[] getUpgradesTier() {
        return upgradesTier;
    }

    public ItemStack getItemUpgrade(Player player, BWGameTeam team){
        List<String> lore = new ArrayList<>();
        final BWUpgradeTier currentTier = team.getUpgrades().containsUpgrade(this) ? team.getUpgrades().getCurrentUpgradeTier(this) : null;
        final BWUpgradeTier nextTier = !team.getUpgrades().containsUpgrade(this) ? this.getUpgradeTier(0) : this.getNextUpgradeTier(team.getUpgrades().getCurrentUpgradeTier(this) != null ? team.getUpgrades().getCurrentUpgradeTier(this).getTier() : 0);

        if(this.getDescription() != null) {
            String[] description = this.getDescription().getForPlayer(player).split("\n");
            for(String desc : description){
                lore.add(ChatColor.GRAY + desc);
            }
            lore.add(" ");
        }

        if(this.getMaxTier() == 0){
            lore.add(ChatColor.GRAY + "Cost: " + StringBWUtils.getCountPriceAsString(player, nextTier.getPrice()));
        }else{
            int j = 0;
            for(BWUpgradeTier tier : this.upgradesTier){
                boolean hasTier = currentTier != null && currentTier.getTier() >= j;
                lore.add((hasTier ? ChatColor.GREEN : ChatColor.GRAY) + "Tier " + (j + 1) + ": " + tier.getName().getForPlayer(player) + ", " + StringBWUtils.getCountPriceAsString(player, tier.getPrice()));
                ++j;
            }
        }
        if(currentTier != null && currentTier.getTier() == this.getMaxTier()) {
            lore.add(ChatColor.GREEN + "Unlocked");
        }
        boolean hasItems = InventoryBWUtils.hasPrice(player, nextTier.getPrice());
        return new ItemBuilder(currentTier != null ? currentTier.getItemStack() : nextTier.getItemStack())
                .withName((hasItems ? ChatColor.GREEN : ChatColor.RED) + this.getName().getForPlayer(player) + (this.getMaxTier() == 0 ? "" : " " + StringBWUtils.getLevelLang(team.getUpgrades().containsUpgrade(this) ? team.getUpgrades().getCurrentUpgradeTier(this).getTier() + 1 : 1)))
                .withLore(lore)
                .withAllItemFlags().build();
    }

    public void active(HyriBedWars plugin, BWGamePlayer player, int tier) {
        this.action.accept(plugin, player, tier);
    }

    public HyriLanguageMessage getName(){
        return HyriBedWars.getLanguageManager().getMessage("upgrade." + this.keyName + ".name");
    }

    public HyriLanguageMessage getDescription(){
        return HyriBedWars.getLanguageManager().getMessage("upgrade." + this.keyName + ".lore");
    }

    public BWUpgradeTier getUpgradeTier(int i){
        return Arrays.asList(this.upgradesTier).get(i);
    }

    public BWUpgradeTier getNextUpgradeTier(int i){
        if(i + 1 <= this.getMaxTier()) {
            return Arrays.asList(this.upgradesTier).get(i + 1);
        }
        return Arrays.asList(this.upgradesTier).get(i);
    }

    public boolean isUpgrading(){
        return upgradesTier.length > 1;
    }

    public int getMaxTier(){
        return upgradesTier.length - 1;
    }

    public boolean isForBase() {
        return forBase;
    }

    public EBWUpgrades getEUpgrade(){
        return this.eUpgrades;
    }
}

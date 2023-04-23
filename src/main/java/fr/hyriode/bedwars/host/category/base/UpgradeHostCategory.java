package fr.hyriode.bedwars.host.category.base;

import fr.hyriode.api.language.HyriLanguageMessage;
import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.shop.ItemPrice;
import fr.hyriode.bedwars.game.team.upgrade.UpgradeTeam;
import fr.hyriode.bedwars.game.type.BWGameType;
import fr.hyriode.bedwars.game.upgrade.Upgrade;
import fr.hyriode.bedwars.host.BWUpgradeValues;
import fr.hyriode.bedwars.host.category.BWHostCategory;
import fr.hyriode.bedwars.host.option.BetterIntegerOption;
import fr.hyriode.bedwars.option.PercentageOption;
import fr.hyriode.bedwars.utils.BWHostUtils;
import fr.hyriode.bedwars.utils.InventoryUtils;
import fr.hyriode.bedwars.utils.SoundUtils;
import fr.hyriode.hyrame.host.HostDisplay;
import fr.hyriode.hyrame.host.option.BooleanOption;
import fr.hyriode.hyrame.host.option.PreciseIntegerOption;
import fr.hyriode.hyrame.item.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class UpgradeHostCategory extends BWHostCategory {
    public UpgradeHostCategory() {
        super(BWHostUtils.categoryDisplay("upgrade",
                ItemBuilder.asHead(() -> "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubm" +
                        "V0L3RleHR1cmUvZjQxZjFlZjQzOWY5MTA2OWE0MzY3OGQyMjdhZDQ1OGQ2NjNlYzA0MzYzYmNlOWM3YzAxOWM1Njc5Z" +
                        "ThjZjAwNCJ9fX0=").build()));

        List<Upgrade> upgrades = HyriBedWars.getUpgradeManager().getUpgrades();

        this.addOption(slot(2, 3), new BooleanOption(BWHostUtils.optionDisplay("upgrades-enabled", Material.LEVER), BWUpgradeValues.UPGRADE_ENABLED.getDefaultValue()));
        this.addOption(slot(2, 4), new PercentageOption(BWHostUtils.optionDisplay("upgrades-price", Material.GOLD_INGOT), BWUpgradeValues.UPGRADE_PRICE.getDefaultValue(), 50, 1000, 50));

        int i = 0;
        for(int y = 0; y < 2; ++y) {
            for (int x = 0; x < 3; ++x) {
                if (upgrades.size() > i) {
                    Upgrade upgrade = upgrades.get(i++);
                    this.addSubCategory(slot(4 + x, 3 + y), new UpgradeGUI(upgrade));
                }
            }
        }
    }

    public static class UpgradeGUI extends BWHostCategory {

        public UpgradeGUI(Upgrade upgrade) {
            super(BWHostUtils.categoryDisplay("upgrade-" + upgrade.getName(), upgrade.getTier(0).getIcon()));

            this.addOption(slot(2, 3), new BooleanOption(BWHostUtils.optionDisplay("upgrade-" + upgrade.getName() + "-enabled", Material.REDSTONE_TORCH_ON), true));

            int i = 4;
            for (Upgrade.Tier tier : upgrade.getTiers()) {
                this.addOption(slot(i++, 3), new BetterIntegerOption(BWHostUtils.optionDisplay("upgrade-" + upgrade.getName() + "-price-tier-" + tier.getTier(), tier.getIcon()), tier.getPrice().getAmount().get().getDefaultValue(), 0, 1000, 10, null));
            }
        }
    }
}

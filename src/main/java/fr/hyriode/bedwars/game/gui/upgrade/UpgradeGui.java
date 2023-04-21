package fr.hyriode.bedwars.game.gui.upgrade;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.gui.BWGui;
import fr.hyriode.bedwars.game.gui.manager.GuiManager;
import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.bedwars.game.shop.ItemPrice;
import fr.hyriode.bedwars.game.team.upgrade.UpgradeTeam;
import fr.hyriode.bedwars.game.trap.Trap;
import fr.hyriode.bedwars.game.team.trap.TrapTeam;
import fr.hyriode.bedwars.game.type.BWGameType;
import fr.hyriode.bedwars.game.upgrade.Upgrade;
import fr.hyriode.bedwars.host.BWTrapValues;
import fr.hyriode.bedwars.host.BWUpgradeValues;
import fr.hyriode.bedwars.utils.InventoryUtils;
import fr.hyriode.bedwars.utils.SoundUtils;
import fr.hyriode.bedwars.utils.StringUtils;
import fr.hyriode.hyrame.HyrameLoader;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.api.language.HyriLanguageMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UpgradeGui extends BWGui {

    public UpgradeGui(Player owner, HyriBedWars plugin, BWGui backGui) {
        super(owner, plugin, HyriLanguageMessage.get("inventory.upgrade.title"), TypeSize.LINE_6, backGui);
    }

    @Override
    protected void initGui() {
        BWGamePlayer player = this.getPlayer();
        BWGameType gameType = this.plugin.getGame().getType();

        this.setHorizontalLine(0, 8, this.getItemDeco());
        this.setHorizontalLine(45, 53, this.getItemDeco());

        List<Upgrade> upgrades = HyriBedWars.getUpgradeManager().getUpgrades().stream().filter(Upgrade::isEnabled).collect(Collectors.toList());

        int i = 0;
        for(int y = 0; y < 2; ++y) {
            for (int x = 0; x < 3; ++x) {
                if (upgrades.size() > i) {
                    Upgrade upgrade = upgrades.get(i++);
                    UpgradeTeam upgradeTeam = player.getBWTeam().getUpgradeTeam();
                    int currentTier = upgradeTeam.getTier(upgrade.getName());
                    Upgrade.Tier nextTier = !upgradeTeam.hasUpgrade(upgrade.getName())
                            ? upgrade.getTier(currentTier)
                            : upgrade.getTier(Math.min(currentTier + 1, upgrade.getMaxTier()));
                    boolean unlocked = upgradeTeam.hasUpgrade(upgrade.getName()) && currentTier >= upgrade.getMaxTier();
                    this.setItem(2 + x, 3 + y, upgrade.getIconForUpgrade(player.getPlayer(), upgradeTeam), event -> {
                        if(!BWUpgradeValues.UPGRADE_ENABLED.get()) {
                            this.owner.sendMessage(ChatColor.RED + HyriLanguageMessage.get("upgrade.disabled").getValue(this.owner));
                            SoundUtils.playCantBuy(player.getPlayer());
                            return;
                        }
                        if(!unlocked) {
                            ItemPrice price = nextTier.getPrice();

                            if (price.hasPrice(this.owner, nextTier.getPriceAmount())) {
                                upgrade.upgrade(player, nextTier);
                                InventoryUtils.removeMoney(this.owner, price, nextTier.getPriceAmount());
                                this.refresh();
                                return;
                            }

                            SoundUtils.playCantBuy(player.getPlayer());
                            this.owner.sendMessage(ChatColor.RED + HyriLanguageMessage.get("shop.missing").getValue(this.owner)
                                    .replace("%name%", price.getName(this.owner)).replace("%amount%", InventoryUtils.getHasPrice(this.owner, price, nextTier.getPriceAmount()) + ""));
                        }
                    });
                }
            }
        }
        List<Trap> traps = HyriBedWars.getTrapManager().getTraps().stream().filter(trap -> trap.getEnable().get()).collect(Collectors.toList());
        TrapTeam trapTeam = player.getBWTeam().getTrapTeam();

        int j = 0;
        for(int y = 0; y < 2; ++y) {
            for (int x = 0; x < 3; ++x) {
                if (traps.size() > j) {
                    Trap trap = traps.get(j++);
                    this.setItem(6 + x, 3 + y, trap.getIconForUpgrade(player.getPlayer(), trapTeam), event -> {
                        if(!BWTrapValues.TRAP_ENABLED.get()) {
                            this.owner.sendMessage(ChatColor.RED + HyriLanguageMessage.get("trap.disabled").getValue(this.owner));
                            SoundUtils.playCantBuy(player.getPlayer());
                            return;
                        }

                        ItemPrice price = trapTeam.getPrice();
                        if(!trapTeam.isFull() && price.hasPrice(this.owner, price.getAmount().get())) {
                            InventoryUtils.removeMoney(this.owner, price, price.getAmount().get());
                            trapTeam.addTrap(trap.getName());
                            this.refresh();
                        }
                    });
                }
            }
        }

        List<Trap> trapsTeam = trapTeam.getTraps().stream()
                .map(name -> HyriBedWars.getTrapManager().getTrapByName(name)).collect(Collectors.toList());
        int k = 0;
        for (int x = 0; x < 3; ++x) {
            if (trapsTeam.size() > k) {
                Trap trap = trapsTeam.get(k++);
                this.setItem(4 + x, 6, this.getItemTrap(gameType, x, trap));
                continue;
            }
            this.setItem(4 + x, 6, this.getItemTrap(gameType, x, null));
        }

    }

    private ItemStack getItemTrap(BWGameType gameType, int trapNumber, Trap trap){
        ItemBuilder itemBuilder;
        List<String> lore = new ArrayList<>(StringUtils.loreToList(HyriLanguageMessage.get("no-trap.lore").getValue(this.owner).replace("%word%", StringUtils.getWordNumber(trapNumber + 1).getValue(this.owner))));
        if(trap == null){
            itemBuilder = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 7);
            lore.add(" ");
            lore.addAll(StringUtils.loreToList(HyriLanguageMessage.get("no-trap.sub-lore").getValue(this.owner)));
        } else {
            itemBuilder = new ItemBuilder(trap.getIcon());
        }
        lore.add(" ");
        if(this.getTrapTeam().isFull())
            lore.add(ChatColor.RED + HyriLanguageMessage.get("upgrade.gui.trap.full").getValue(this.owner));
        else
            lore.add(ChatColor.GRAY + HyriLanguageMessage.get("trap.next-trap").getValue(this.owner).replace("%trap%", StringUtils.getDisplayPrice(this.owner, this.getTrapTeam().getPrice())));

        return itemBuilder.withName(ChatColor.RED +
                        HyriLanguageMessage.get("no-trap.title").getValue(this.owner).replace("%number%", String.valueOf(trapNumber + 1))
                        + " " +
                        (trap == null ? HyriLanguageMessage.get("no-trap.word").getValue(this.owner) : trap.getDisplayName().getValue(this.owner)))
                .withLore(lore)
                .build();
    }

    @Override
    protected void refresh() {
        GuiManager.openUpgradeGui(this.plugin, this.owner);
    }

    private TrapTeam getTrapTeam(){
        return this.getPlayer().getBWTeam().getTrapTeam();
    }
}

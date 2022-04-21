package fr.hyriode.bedwars.game.npc.inventory.upgrade;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.BWGamePlayer;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import fr.hyriode.bedwars.game.team.upgrade.BWUpgradeTier;
import fr.hyriode.bedwars.game.team.upgrade.BWTeamUpgrades;
import fr.hyriode.bedwars.game.team.upgrade.EBWUpgrades;
import fr.hyriode.bedwars.game.team.upgrade.traps.BWTeamTraps;
import fr.hyriode.bedwars.game.team.upgrade.traps.BWTrap;
import fr.hyriode.bedwars.game.team.upgrade.traps.BWTraps;
import fr.hyriode.bedwars.utils.InventoryBWUtils;
import fr.hyriode.bedwars.utils.StringBWUtils;
import fr.hyriode.hyrame.inventory.HyriInventory;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.language.IHyriLanguageManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BWUpgradeGui extends HyriInventory {

    private final HyriBedWars plugin;
    private final BWGameTeam team;
    private final IHyriLanguageManager lm = HyriBedWars.getLanguageManager();

    public BWUpgradeGui(HyriBedWars plugin, Player owner) {
        super(owner, "Upgrade", 54);
        this.plugin = plugin;
        this.team = plugin.getGame().getPlayer(owner.getUniqueId()).getHyriTeam();
        this.initGui();
    }

    private void initGui(){
        this.initUpgrades();
        this.initTraps();
        this.initLine();
    }

    private void initUpgrades(){
        int i = 10;
        for (EBWUpgrades upgrade : EBWUpgrades.values()) {
            if(i == 13) i = 19;
            this.setItem(i, upgrade.getItemUpgrade(this.owner, this.team),
                    event -> {
                        final BWTeamUpgrades upgradeTeam = this.team.getUpgrades();
                        final BWUpgradeTier tier = !upgradeTeam.containsUpgrade(upgrade) ? upgrade.getUpgradeTier(0) : upgrade.getNextUpgradeTier(upgradeTeam.getCurrentUpgradeTier(upgrade) != null ? upgradeTeam.getCurrentUpgradeTier(upgrade).getTier() : 0);
                        if(!upgradeTeam.containsUpgrade(upgrade) || upgradeTeam.canUpgrade(upgrade)) {
                            if (InventoryBWUtils.hasPrice(this.owner, tier.getPrice())) {
                                this.team.sendMessage(player -> ChatColor.GREEN + this.owner.getName() + " " + this.getMessage("upgrade.purchased") + " " + ChatColor.GOLD + tier.getName().getForPlayer(player));
                                this.owner.playSound(this.owner.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
                                if (upgradeTeam.upgrade(upgrade)) {
                                    InventoryBWUtils.removeItems(this.owner, tier.getPrice());
                                    this.team.getUpgrades().updateForPlayers();
                                    this.team.getPlayersPlaying().forEach(player -> {
                                        ((BWGamePlayer)player).activeUpgradesTeam(upgrade, tier.getTier());
                                    });
                                    upgrade.active(this.plugin, this.getPlayer(), tier.getTier());
                                    this.refreshGui();
                                    return;
                                }
                            } else {
                                this.owner.sendMessage(ChatColor.RED + this.getMessage("upgrade.missing-items"));
                                return;
                            }
                        }
                        this.owner.sendMessage(ChatColor.RED + this.getMessage("upgrade.already"));
                    });
            ++i;
        }
    }

    private void initTraps(){
        int i = 14;
        for (BWTraps trap : BWTraps.values()) {
            if(i == 17) i = 23;
            this.setItem(i, trap.getItemShop(this.getPlayer()), event -> {
                if (InventoryBWUtils.hasPrice(this.owner, Collections.singletonList(trap.getPrice(this.team)))) {
                    if (((BWGameTeam) this.getPlayer().getTeam()).getTraps().addTrap(trap)) {
                        InventoryBWUtils.removeItems(this.owner, trap.getPrice(this.team));
                        this.team.sendMessage(player -> ChatColor.GREEN + this.owner.getName() + " " + this.getMessage("upgrade.purchased") + " " + ChatColor.GOLD + trap.getName().getForPlayer(player));
                        this.owner.playSound(this.owner.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
                        this.refreshGui();
                        return;
                    }
                    this.owner.sendMessage("You have any space in traps (todo change)"); //TODO check le msg sur hypixel
                    return;
                }
                this.owner.sendMessage(this.getMessage("upgrade.missing-items"));
            });
            ++i;
        }

        BWGameTeam team = this.getPlayer().getHyriTeam();
        BWTeamTraps traps = team.getTraps();

        for(int j = 0; j < 3;++j){
            int trap = j + 1;
            final int slot = j + 39;
            final List<String> lore = new ArrayList<>();
            ItemStack empty = new ItemBuilder(Material.STAINED_GLASS, trap, 8).build();
            BWTrap bwTrap = traps.size() > j && traps.getTraps().get(j) != null ? traps.getTraps().get(j) : null;
            ItemStack item = bwTrap != null ? new ItemStack(traps.getTraps().get(j).getMaterial(), trap) : empty;
            boolean buyed = !item.equals(empty);

            if(buyed){
                lore.addAll(StringBWUtils.loreToList(this.getMessage("trap." + bwTrap.getKeyName() + ".lore")));
                lore.add(" ");
            }

            lore.addAll(StringBWUtils.loreToList(this.getMessage("notrap." + trap + ".lore")));

            if(!buyed) {
                lore.add(" ");
                lore.addAll(StringBWUtils.loreToList(this.getMessage("notrap.lore")));
                lore.add(" ");
                lore.add(ChatColor.GRAY + this.getMessage("nexttrap.price.name") + " " + StringBWUtils.getCountPriceAsString(this.owner, traps.getPrice()));
            }

            this.setItem(slot, new ItemBuilder(item)
                    .withName((!buyed ? ChatColor.RED : ChatColor.GREEN) + this.getMessage("notrap." + trap + ".name") + " " + (bwTrap != null ? bwTrap.getName().getForPlayer(this.owner) : this.getMessage("notrap.name")))
                    .withLore(lore)
                    .withAllItemFlags()
                    .build());

            ++trap;
        }
    }

    private void initLine(){
        for(int j = 27 ; j < 36 ; ++j){
            byte data = 7;
            this.setItem(j, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, data)
                    .withName(ChatColor.DARK_GRAY + "⬆" + ChatColor.GRAY + " " +
                            this.getMessage("inv.upgrade.separator.title"))
                    .withLore(ChatColor.DARK_GRAY + "⬇" + ChatColor.GRAY + " " +
                            this.getMessage("inv.upgrade.separator.lore"))
                    .build());
        }
    }

    private void refreshGui(){
        new BWUpgradeGui(this.plugin, this.owner).open();
    }

    private BWGamePlayer getPlayer(){
        return this.plugin.getGame().getPlayer(this.owner.getUniqueId());
    }

    private String getMessage(String value){
        return lm.getValue(this.owner, value);
    }

}

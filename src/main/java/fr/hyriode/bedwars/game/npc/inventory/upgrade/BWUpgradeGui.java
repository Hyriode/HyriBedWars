package fr.hyriode.bedwars.game.npc.inventory.upgrade;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.BWGamePlayer;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import fr.hyriode.bedwars.game.team.upgrade.BWUpgradeTier;
import fr.hyriode.bedwars.game.team.upgrade.BWTeamUpgrades;
import fr.hyriode.bedwars.game.team.upgrade.EBWUpgrades;
import fr.hyriode.bedwars.utils.InventoryBWUtils;
import fr.hyriode.hyrame.inventory.HyriInventory;
import fr.hyriode.hyrame.item.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class BWUpgradeGui extends HyriInventory {

    private final HyriBedWars plugin;
    private final BWGameTeam team;

    public BWUpgradeGui(HyriBedWars plugin, Player owner) {
        super(owner, "Upgrade", 54);
        this.plugin = plugin;
        this.team = plugin.getGame().getPlayer(owner.getUniqueId()).getHyriTeam();
        this.initGui();
    }

    private void initGui(){
        this.initUpgrades();
        this.initLine();
    }

    private void initUpgrades(){
        int i = 10;
        for (EBWUpgrades upgrade : EBWUpgrades.values()) {
            if(i == 13) i = 19;
            try {
                this.setItem(i, upgrade.getItemUpgrade(this.owner, this.team),
                        event -> {
                            final BWTeamUpgrades upgradeTeam = this.team.getUpgrades();
                            final BWUpgradeTier tier = !upgradeTeam.containsUpgrade(upgrade) ? upgrade.getUpgradeTier(0) : upgrade.getNextUpgradeTier(upgradeTeam.getCurrentUpgradeTier(upgrade) != null ? upgradeTeam.getCurrentUpgradeTier(upgrade).getTier() : 0);
                            if(!upgradeTeam.containsUpgrade(upgrade) || upgradeTeam.canUpgrade(upgrade)) {
                                if (InventoryBWUtils.hasItems(this.owner, tier.getPrice())) {
                                    this.team.sendMessage(player -> ChatColor.GREEN + this.owner.getName() + " purchased " + ChatColor.GOLD + tier.getName().getForPlayer(player));
                                    if (upgradeTeam.upgrade(upgrade)) {
                                        InventoryBWUtils.removeItems(this.owner, tier.getPrice());
                                        this.team.getPlayersPlaying().forEach(player -> {
                                            ((BWGamePlayer)player).setUpgradesTeam(upgrade, tier.getTier());
                                            if(upgrade == EBWUpgrades.HEAL_POOL){
                                                ((BWGamePlayer)player).getHyriTeam().startHealPoolUpgrade();
                                            }
                                            if(upgrade == EBWUpgrades.FORGE){
                                                ((BWGamePlayer)player).getHyriTeam().updateGenerator();
                                            }
                                        });
                                    } else {
                                        this.owner.sendMessage("T'as déjà l'upgrade !!");
                                    }
                                    this.refreshGui();
                                } else {
                                    this.owner.sendMessage("Tu n'as pas assez de diams");
                                }
                            }else{
                                this.owner.sendMessage("T'as déjà l'upgrade !");
                            }
                        });
            }catch (Exception e){
                e.printStackTrace();
            }
            ++i;
        }
    }

    private void initLine(){
        for(int j = 27 ; j < 36 ; ++j){
            byte data = 7;
            this.setItem(j, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, data)
                    .withName(ChatColor.DARK_GRAY + "⬆" + ChatColor.GRAY + " " +
                            HyriBedWars.getLanguageManager().getValue(this.owner, "inv.upgrade.separator.title"))
                    .withLore(ChatColor.DARK_GRAY + "⬇" + ChatColor.GRAY + " " +
                            HyriBedWars.getLanguageManager().getValue(this.owner, "inv.upgrade.separator.lore"))
                    .build());
        }
    }

    private void refreshGui(){
        new BWUpgradeGui(this.plugin, this.owner).open();
    }

    private BWGamePlayer getPlayer(){
        return this.plugin.getGame().getPlayer(this.owner.getUniqueId());
    }

}

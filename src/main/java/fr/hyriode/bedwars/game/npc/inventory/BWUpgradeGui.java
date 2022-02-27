package fr.hyriode.bedwars.game.npc.inventory;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import fr.hyriode.bedwars.game.team.upgrade.EBWUpgrades;
import fr.hyriode.hyrame.game.team.HyriGameTeam;
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
        this.team = (BWGameTeam) plugin.getGame().getPlayer(owner.getUniqueId()).getTeam();
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
                this.setItem(i, upgrade.getUpgrade().getItemUpgrade(this.owner, this.team, 0),
                        event -> {
                            this.team.getUpgrades().upgrade(upgrade.getUpgrade().getKeyName());
                            this.refreshGui();
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
        this.open();
    }

}

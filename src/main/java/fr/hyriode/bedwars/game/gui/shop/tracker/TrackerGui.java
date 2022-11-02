package fr.hyriode.bedwars.game.gui.shop.tracker;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.BWGame;
import fr.hyriode.bedwars.game.gui.BWGui;
import fr.hyriode.api.language.HyriLanguageMessage;
import fr.hyriode.bedwars.game.gui.manager.GuiManager;
import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.bedwars.game.player.traker.TeamTraker;
import fr.hyriode.bedwars.game.shop.ItemPrice;
import fr.hyriode.bedwars.game.shop.ShopCategory;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import fr.hyriode.bedwars.utils.InventoryUtils;
import fr.hyriode.bedwars.utils.SoundUtils;
import fr.hyriode.bedwars.utils.StringUtils;
import fr.hyriode.bedwars.utils.TrackerUtil;
import fr.hyriode.hyrame.item.ItemBuilder;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class TrackerGui extends BWGui {

    public TrackerGui(Player owner, HyriBedWars plugin, BWGui backGui) {
        super(owner, plugin, HyriLanguageMessage.get("inv.tracker.title").getValue(owner), TypeSize.LINE_4, backGui);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void initGui() {
        BWGame game = this.plugin.getGame();
        List<BWGameTeam> teams = game.getBWTeams(team -> !team.isEliminated()
                && !team.equals(this.getPlayer().getBWTeam()));
        ItemPrice price = TrackerUtil.getPrice();
        int beds = game.getBWTeams(team -> team.hasBed() && !team.isEliminated()).size();
        boolean hasBed = beds > 0;

        this.setItem(5, 4, this.getItemBack(),
                event -> {
                    if(this.backGui != null) {
                        GuiManager.openShopGui(this.plugin, this.owner, ShopCategory.QUICK_BUY);
                        return;
                    }
                    this.owner.closeInventory();
                });

        int i = 0;
        if(teams.size() == 0){
            this.setItem(5, 2, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 7).withName(ChatColor.RED + HyriLanguageMessage.get("tracker.item.no-team").getValue(this.owner)).build(),
                    event -> {
                        this.owner.closeInventory();
                    });
            return;
        }
        y: for(int y = 0; y < 2; ++y) {
            for (int x = 0; x < 7; ++x) {
                if(i > teams.size() - 1) {
                    break y;
                }
                BWGameTeam team = teams.get(i);
                BWGamePlayer player = this.getPlayer();
                TeamTraker tracker = player.getTracker();
                try{
                    this.setItem(2 + x, 2 + y, new ItemBuilder(Material.WOOL, 1, team.getColor().getDyeColor().getWoolData())
                            .withName(ChatColor.GREEN + HyriLanguageMessage.get("tracker.item.name").getValue(this.owner) + " " + team.getDisplayName().getValue(this.owner))
                            .withLore(StringUtils.loreToList(HyriLanguageMessage.get("shop.tracker.lore").getValue(this.owner)))
                            .build(), event -> {
                        if(tracker.hasTeam()){
                            this.owner.sendMessage(ChatColor.RED + HyriLanguageMessage.get("tracker.already.have.team").getValue(this.owner));
                        } else if(hasBed){
                            this.owner.sendMessage(ChatColor.RED + HyriLanguageMessage.get("tracker.team.have.bed").getValue(this.owner));
                        } else if(InventoryUtils.hasPrice(this.owner, price)) {
                            InventoryUtils.removeMoney(this.owner, price);
                            tracker.setTrackedTeam(team);
                            tracker.start();
                            SoundUtils.playBuy(this.owner);

                            this.refresh();
                            return;
                        }else {
                            this.owner.sendMessage(ChatColor.RED + HyriLanguageMessage.get("shop.missing").getValue(this.owner)
                                    .replace("%name%", price.getName(this.owner))
                                    .replace("%amount%", InventoryUtils.getHasPrice(this.owner, price) + ""));
                        }
                        SoundUtils.playCantBuy(this.owner);
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
                ++i;
            }
        }

    }

    @Override
    protected void refresh() {
        new TrackerGui(this.owner, this.plugin, this.backGui).open();
    }
}

package fr.hyriode.bedwars.game;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.language.HyriLanguage;
import fr.hyriode.api.language.HyriLanguageMessage;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.api.player.BWPlayerStatistics;
import fr.hyriode.bedwars.config.BWConfiguration;
import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.bedwars.game.type.BWGameType;
import fr.hyriode.hyrame.game.waitingroom.HyriWaitingRoom;
import fr.hyriode.hyrame.npc.NPC;
import fr.hyriode.hyrame.utils.DurationFormatter;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class BWWaitingRoom extends HyriWaitingRoom {
    public BWWaitingRoom(BWGame game, BWConfiguration configuration) {
        super(game,
                Material.BED,
                configuration::getWaitingRoom);
        int i = 0;
        for (BWGameType gameType : BWGameType.values()) {
            this.addStatistics(21 + i++, gameType);
        }
        this.addAllStatistics(19);
    }

    private void addStatistics(int slot, BWGameType gameType) {
        final NPCCategory normal = new NPCCategory(new HyriLanguageMessage("").addValue(HyriLanguage.EN, gameType.getDisplayName()));

        normal.addData(new NPCData(this.getDisplayStatistics("kills"), account -> String.valueOf(this.getStatistics(account, gameType).getKills())));
        normal.addData(new NPCData(this.getDisplayStatistics("deaths"), account -> String.valueOf(this.getStatistics(account, gameType).getDeaths())));
        normal.addData(new NPCData(this.getDisplayStatistics("finalKills"), account -> String.valueOf(this.getStatistics(account, gameType).getFinalKills())));
        normal.addData(new NPCData(this.getDisplayStatistics("bedsBroken"), account -> String.valueOf(this.getStatistics(account, gameType).getBedsBroken())));
        normal.addData(NPCData.voidData());
        normal.addData(new NPCData(this.getDisplayStatistics("bestWinStreak"), account -> String.valueOf(this.getStatistics(account, gameType).getBestWinStreak())));
        normal.addData(new NPCData(this.getDisplayStatistics("currentWinStreak"), account -> String.valueOf(this.getStatistics(account, gameType).getCurrentWinStreak())));
        normal.addData(new NPCData(this.getDisplayStatistics("totalWins"), account -> String.valueOf(this.getStatistics(account, gameType).getTotalWins())));
        normal.addData(new NPCData(this.getDisplayStatistics("totalDefeats"), account -> String.valueOf(this.getStatistics(account, gameType).getDefeats())));
        normal.addData(NPCData.voidData());
        normal.addData(new NPCData(this.getDisplayStatistics("games-played"), account -> String.valueOf(this.getStatistics(account, gameType).getPlayedGames())));
        normal.addData(new NPCData(this.getDisplayStatistics("played-time"), account -> this.formatPlayedTime(account, account.getStatistics().getPlayTime(HyriAPI.get().getServer().getType()))));

        this.addNPCCategory(slot, normal);
    }

    private void addAllStatistics(int slot) {
        final NPCCategory normal = new NPCCategory(new HyriLanguageMessage("").addValue(HyriLanguage.EN, "All"));

        normal.addData(new NPCData(this.getDisplayStatistics("kills"), account -> String.valueOf(this.getAllStatistics(account).getKills())));
        normal.addData(new NPCData(this.getDisplayStatistics("deaths"), account -> String.valueOf(this.getAllStatistics(account).getDeaths())));
        normal.addData(new NPCData(this.getDisplayStatistics("finalKills"), account -> String.valueOf(this.getAllStatistics(account).getFinalKills())));
        normal.addData(new NPCData(this.getDisplayStatistics("bedsBroken"), account -> String.valueOf(this.getAllStatistics(account).getBedsBroken())));
        normal.addData(NPCData.voidData());
        normal.addData(new NPCData(this.getDisplayStatistics("bestWinStreak"), account -> String.valueOf(this.getAllStatistics(account).getBestWinStreak())));
        normal.addData(new NPCData(this.getDisplayStatistics("currentWinStreak"), account -> String.valueOf(this.getAllStatistics(account).getCurrentWinStreak())));
        normal.addData(new NPCData(this.getDisplayStatistics("totalWins"), account -> String.valueOf(this.getAllStatistics(account).getTotalWins())));
        normal.addData(new NPCData(this.getDisplayStatistics("totalDefeats"), account -> String.valueOf(this.getAllStatistics(account).getDefeats())));
        normal.addData(NPCData.voidData());
        normal.addData(new NPCData(this.getDisplayStatistics("games-played"), account -> String.valueOf(this.getAllStatistics(account).getPlayedGames())));
        normal.addData(new NPCData(this.getDisplayStatistics("played-time"), account -> this.formatPlayedTime(account, account.getStatistics().getPlayTime(HyriAPI.get().getServer().getType()))));

        this.addNPCCategory(slot, normal);
    }

    private HyriLanguageMessage getDisplayStatistics(String name){
        return HyriLanguageMessage.get("statistics." + name + ".name");
    }

    private String formatPlayedTime(IHyriPlayer account, long playedTime) {
        if(playedTime == 0)
            return "Jamais";
        return new DurationFormatter().format(account.getSettings().getLanguage(), playedTime);
    }

    private BWPlayerStatistics.Data getStatistics(IHyriPlayer account, BWGameType gameType) {
        return ((BWGamePlayer) this.game.getPlayer(account.getUniqueId())).getStatistic(gameType);
    }

    private BWPlayerStatistics.Data getAllStatistics(IHyriPlayer account) {
        return ((BWGamePlayer) this.game.getPlayer(account.getUniqueId())).getAllStatistics();
    }



}

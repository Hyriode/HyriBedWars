package fr.hyriode.bedwars.api.player;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.player.HyriPlayerData;
import fr.hyriode.api.player.IHyriPlayer;

import java.util.UUID;

public class HyriBWStatistics extends HyriPlayerData {

    private long kills;
    private long deaths;
    private long finalKills;
    private long killStreak;
    private long bestWinStreak;
    private long currentWinStreak;
    private long playTime;
    private long brokenBeds;
    private long playedGames;

    public HyriBWStatistics(){
    }

    public long getKills() {
        return kills;
    }

    public long getDeaths() {
        return deaths;
    }

    public long getFinalKills() {
        return finalKills;
    }

    public long getKillStreak() {
        return killStreak;
    }

    public long getBestWinStreak() {
        return bestWinStreak;
    }

    public long getCurrentWinStreak() {
        return currentWinStreak;
    }

    public long getPlayTime() {
        return playTime;
    }

    public long getBrokenBeds() {
        return brokenBeds;
    }

    public void setKills(long kills) {
        this.kills = kills;
    }

    public void setDeaths(long deaths) {
        this.deaths = deaths;
    }

    public void setFinalKills(long finalKills) {
        this.finalKills = finalKills;
    }

    public void setKillStreak(long killStreak) {
        this.killStreak = killStreak;
    }

    public void setBestWinStreak(long bestWinStreak) {
        this.bestWinStreak = bestWinStreak;
    }

    public void setCurrentWinStreak(long currentWinStreak) {
        this.currentWinStreak = currentWinStreak;
    }

    public void setPlayTime(long playTime) {
        this.playTime = playTime;
    }

    public void setBrokenBeds(long brokenBeds) {
        this.brokenBeds = brokenBeds;
    }

    public void setPlayedGames(long playedGames) {
        this.playedGames = playedGames;
    }

    public void addKills() {
        this.kills += 1;
    }

    public void addDeaths() {
        this.deaths += 1;
    }

    public void addFinalKills() {
        this.finalKills += 1;
    }

    public void addKillStreak() {
        this.killStreak += 1;
    }

    public void addBestWinStreak() {
        this.bestWinStreak += 1;
    }

    public void addCurrentWinStreak() {
        this.currentWinStreak += 1;
    }

    public void addPlayTime() {
        this.playTime += 1;
    }

    public void addBrokenBeds() {
        this.brokenBeds += 1;
    }

    public void addPlayedGames() {
        this.playedGames += 1;
    }

    public void update(UUID uuid){
        IHyriPlayer player = HyriAPI.get().getPlayerManager().getPlayer(uuid);
        player.addStatistics("bedwars", this);
        player.update();
    }
}

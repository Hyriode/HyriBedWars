package fr.hyriode.bedwars.api.player;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.player.HyriPlayerData;
import fr.hyriode.api.player.IHyriPlayer;

import java.util.UUID;

public class HyriBWStatistics extends HyriPlayerData {

    private long kills;
    private long deaths;
    private long finalKills;
    private long finalDeaths;
    private long bestWinStreak;
    private long currentWinStreak;
    private long totalWins;
    private long totalDefeats;
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

    public long getFinalKillDeathRatio(){
        return this.finalKills / this.deaths;
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

    public long getFinalDeaths() {
        return finalDeaths;
    }

    public long getPlayedGames() {
        return playedGames;
    }

    public long getTotalDefeats() {
        return totalDefeats;
    }

    public long getTotalWins() {
        return totalWins;
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

    public void setFinalDeaths(long finalDeaths) {
        this.finalDeaths = finalDeaths;
    }

    public void setTotalDefeats(long totalDefeats) {
        this.totalDefeats = totalDefeats;
    }

    public void setTotalWins(long totalWins) {
        this.totalWins = totalWins;
    }

    public void addKills(long i) {
        this.kills += i;
    }

    public void addDeaths(long i) {
        this.deaths += i;
    }

    public void addFinalKills(long i) {
        this.finalKills += i;
    }

    public void addBestWinStreak(long i) {
        this.bestWinStreak += i;
    }

    public void addCurrentWinStreak(long i) {
        this.currentWinStreak += i;
    }

    public void addPlayTime(long i) {
        this.playTime += i;
    }

    public void addBrokenBeds(long i) {
        this.brokenBeds += i;
    }

    public void addPlayedGames(long i) {
        this.playedGames += i;
    }

    public void addFinalDeaths(long i){
        this.finalDeaths += i;
    }

    public void addDefeats(int i){
        this.totalDefeats += i;
    }

    public void addWins(int i){
        this.totalWins += i;
    }

    public void update(UUID uuid){
        IHyriPlayer player = HyriAPI.get().getPlayerManager().getPlayer(uuid);
        player.addStatistics("bedwars", this);
        HyriAPI.get().getPlayerManager().sendPlayer(player);
    }
}

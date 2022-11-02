package fr.hyriode.bedwars.api.player;

import fr.hyriode.api.player.HyriPlayerData;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.bedwars.game.type.BWGameType;

import java.util.HashMap;
import java.util.Map;

public class BWPlayerStatistics extends HyriPlayerData {

    private final Map<BWGameType, Data> data;

    public BWPlayerStatistics() {
        this.data = new HashMap<>();
    }

    public Data getData(BWGameType gameType) {
        Data data = this.data.get(gameType);

        if (data == null) {
            data = new Data();
            this.data.put(gameType, data);
        }

        return data;
    }

    public Map<BWGameType, Data> getData() {
        return data;
    }

    public void update(IHyriPlayer player){
        player.addStatistics("bedwars", this);
        player.update();
    }

    public Data getAllData() {
        Data data = new Data();
        this.data.forEach((__, d) -> {
            data.addBedsBroken(d.getBedsBroken());
            data.addBestWinStreak(d.getBestWinStreak());
            data.addCurrentWinStreak(d.getCurrentWinStreak());
            data.addDeaths(d.getDeaths());
            data.addKills(d.getKills());
            data.addFinalKills(d.getFinalKills());
            data.addWins(d.getTotalWins());
            data.addPlayedGames(d.getPlayedGames());
            data.addPlayTime(d.getPlayTime());
        });
        return data;
    }

    public static class Data {
        private long kills;
        private long deaths;
        private long finalKills;
        private long bestWinStreak;
        private long currentWinStreak;
        private long totalWins;
        private long bedsBroken;
        private long playedGames;
        private long playTime;

        public Data(){
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

        public long getBedsBroken() {
            return bedsBroken;
        }

        public long getPlayedGames() {
            return playedGames;
        }

        public long getDefeats() {
            return this.playedGames - this.totalWins;
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

        public void setBedsBroken(long bedsBroken) {
            this.bedsBroken = bedsBroken;
        }

        public void setPlayedGames(long playedGames) {
            this.playedGames = playedGames;
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

        public void addBedsBroken(long i) {
            this.bedsBroken += i;
        }

        public void addPlayedGames(long i) {
            this.playedGames += i;
        }

        public void addWins(long i){
            this.totalWins += i;
        }
    }
}

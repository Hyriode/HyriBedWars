package fr.hyriode.bedwars.api.player;

import fr.hyriode.api.mongodb.MongoDocument;
import fr.hyriode.api.mongodb.MongoSerializable;
import fr.hyriode.api.mongodb.MongoSerializer;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.model.IHyriPlayerData;
import fr.hyriode.api.player.model.IHyriStatistics;
import fr.hyriode.bedwars.game.type.BWGameType;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

public class BWPlayerStatistics implements IHyriStatistics {

    private final Map<BWGameType, Data> data = new HashMap<>();;

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
        player.getStatistics().add("bedwars", this);
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
        });
        return data;
    }

    @Override
    public void save(MongoDocument document) {
        for (Map.Entry<BWGameType, Data> entry : this.data.entrySet()) {
            document.append(entry.getKey().name(), MongoSerializer.serialize(entry.getValue()));
        }
    }

    @Override
    public void load(MongoDocument document) {
        for (Map.Entry<String, Object> entry : document.entrySet()) {
            final MongoDocument dataDocument = MongoDocument.of((Document) entry.getValue());
            final Data data = new Data();

            data.load(dataDocument);

            this.data.put(BWGameType.valueOf(entry.getKey()), data);
        }
    }

    public static class Data implements MongoSerializable {
        private long kills;
        private long deaths;
        private long finalKills;
        private long bestWinStreak;
        private long currentWinStreak;
        private long totalWins;
        private long bedsBroken;
        private long playedGames;

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

        public double getFinalKillDeathRatio(){
            return this.deaths == 0 ? 0 : (double) this.finalKills / this.deaths;//round
        }

        public long getBestWinStreak() {
            return bestWinStreak;
        }

        public long getCurrentWinStreak() {
            return currentWinStreak;
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

        public void addBedsBroken(long i) {
            this.bedsBroken += i;
        }

        public void addPlayedGames(long i) {
            this.playedGames += i;
        }

        public void addWins(long i){
            this.totalWins += i;
        }

        @Override
        public void save(MongoDocument document) {
            document.append("kills", this.kills);
            document.append("deaths", this.deaths);
            document.append("finalKills", this.finalKills);
            document.append("bestWinStreak", this.bestWinStreak);
            document.append("currentWinStreak", this.currentWinStreak);
            document.append("totalWins", this.totalWins);
            document.append("bedsBroken", this.bedsBroken);
            document.append("playedGames", this.playedGames);
        }

        @Override
        public void load(MongoDocument document) {
            this.kills = document.getLong("kills");
            this.deaths = document.getLong("deaths");
            this.finalKills = document.getLong("finalKills");
            this.bestWinStreak = document.getLong("bestWinStreak");
            this.currentWinStreak = document.getLong("currentWinStreak");
            this.totalWins = document.getLong("totalWins");
            this.bedsBroken = document.getLong("bedsBroken");
            this.playedGames = document.getLong("playedGames");
        }

        @Override
        public String toString() {
            return "Data{" +
                    "kills=" + kills +
                    ", deaths=" + deaths +
                    ", finalKills=" + finalKills +
                    ", bestWinStreak=" + bestWinStreak +
                    ", currentWinStreak=" + currentWinStreak +
                    ", totalWins=" + totalWins +
                    ", bedsBroken=" + bedsBroken +
                    ", playedGames=" + playedGames +
                    '}';
        }
    }
}

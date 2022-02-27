package fr.hyriode.bedwars.api.player;

public class HyriBWStatistics {

    private long kills;
    private long deaths;
    private long finalKills;
    private long killStreak;
    private long bestWinStreak;
    private long currentWinStreak;
    private long playTime;
    private long brokenBeds;
    private long playedGames;
    private int stars;
    private long level;

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
}

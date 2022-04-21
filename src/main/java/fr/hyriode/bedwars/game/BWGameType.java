package fr.hyriode.bedwars.game;

import fr.hyriode.hyrame.game.HyriGameType;

public enum BWGameType implements HyriGameType {

    SOLO("Solo", 1, 8, 4, 8),
    DOUBLES("Doubles", 2, 8, 6, 16),
    TRIO("Trio", 3, 4, 6, 12),
    SQUAD("Squad", 4, 4, 8, 16),
//1v1
    VS1("1v1", 1, 2, 2, 2)
    ;

    private final String name;
    private final int teamsSize;
    private final int maxTeams;
    private final int minPlayers;
    private final int maxPlayers;

    BWGameType(String name, int teamsSize, int maxTeams, int minPlayers, int maxPlayers) {
        this.name = name;
        this.teamsSize = teamsSize;
        this.maxTeams = maxTeams;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public int getTeamsSize() {
        return this.teamsSize;
    }

    public int getMaxTeams() {
        return this.maxTeams;
    }

    public int getMinPlayers() {
        return this.minPlayers;
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }
}

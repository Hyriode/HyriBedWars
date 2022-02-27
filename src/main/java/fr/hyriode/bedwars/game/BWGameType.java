package fr.hyriode.bedwars.game;

import fr.hyriode.hyrame.game.HyriGameType;

public enum BWGameType implements HyriGameType {

    SOLO("Solo", 1),
    DOUBLES("Doubles", 2),
    TRIO("Trio", 3),
    SQUAD("Squad", 4),
    ;

    private final String name;
    private final int teamSize;

    BWGameType(String name, int teamSize) {
        this.name = name;
        this.teamSize = teamSize;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getTeamSize() {
        return teamSize;
    }
}

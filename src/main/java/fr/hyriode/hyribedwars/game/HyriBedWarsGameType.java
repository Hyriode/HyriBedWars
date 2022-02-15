package fr.hyriode.hyribedwars.game;

import fr.hyriode.hyrame.game.HyriGameType;

public enum HyriBedWarsGameType implements HyriGameType {

    SOLO("Solo", 1),
    DOUBLES("Doubles", 2),
    TRIO("Trio", 3),
    SQUAD("Squad", 4),
    ;

    private final String name;
    private final int teamSize;

    HyriBedWarsGameType(String name, int teamSize) {
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

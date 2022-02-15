package fr.hyriode.hyribedwars.game.team;

import fr.hyriode.hyrame.game.team.HyriGameTeamColor;
import fr.hyriode.hyrame.language.HyriLanguageMessage;
import fr.hyriode.hyribedwars.HyriBedWars;

import java.util.function.Supplier;

public enum EHyriBedWarsGameTeam {

    BLUE("blue", HyriGameTeamColor.BLUE),
    RED("red", HyriGameTeamColor.RED),
    GREEN("green", HyriGameTeamColor.GREEN),
    YELLOW("yellow", HyriGameTeamColor.YELLOW),
    AQUA("aqua", HyriGameTeamColor.CYAN),
    PINK("pink", HyriGameTeamColor.PINK),
    WHITE("white", HyriGameTeamColor.WHITE),
    GRAY("gray", HyriGameTeamColor.GRAY),

    ;

    private final String teamName;
    private final HyriGameTeamColor teamColor;
    private final Supplier<HyriLanguageMessage> displayName;

    EHyriBedWarsGameTeam(String teamName, HyriGameTeamColor teamColor) {
        this.teamName = teamName;
        this.teamColor = teamColor;
        this.displayName = () -> HyriBedWars.getHyriLanguageManager().getMessage("team." + this.teamName + ".display");
    }

    public String getName() {
        return this.teamName;
    }

    public HyriGameTeamColor getColor() {
        return this.teamColor;
    }

    public HyriLanguageMessage getDisplayName() {
        return this.displayName.get();
    }

}

package fr.hyriode.bedwars.game.team;

import fr.hyriode.hyrame.game.team.HyriGameTeamColor;
import fr.hyriode.api.language.HyriLanguageMessage;

import java.util.function.Supplier;

public enum BWGameTeamColor {

    RED("red", HyriGameTeamColor.RED),
    BLUE("blue", HyriGameTeamColor.BLUE),
    GREEN("green", HyriGameTeamColor.GREEN),
    YELLOW("yellow", HyriGameTeamColor.YELLOW),
    AQUA("aqua", HyriGameTeamColor.CYAN),
    WHITE("white", HyriGameTeamColor.WHITE),
    PINK("pink", HyriGameTeamColor.PINK),
    GRAY("gray", HyriGameTeamColor.GRAY),

    ;

    private final String teamName;
    private final HyriGameTeamColor teamColor;
    private final Supplier<HyriLanguageMessage> displayName;

    BWGameTeamColor(String teamName, HyriGameTeamColor teamColor) {
        this.teamName = teamName;
        this.teamColor = teamColor;
        this.displayName = () -> HyriLanguageMessage.get("team." + this.teamName + ".display");
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

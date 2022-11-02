package fr.hyriode.bedwars.game.test;

import fr.hyriode.api.game.HyriGameType;
import fr.hyriode.api.game.IHyriGameInfo;

import java.util.HashMap;
import java.util.Map;

public class BWGameInfo implements IHyriGameInfo {
    private final String name;
    private String displayName;
    private final Map<String, HyriGameType> types;

    public BWGameInfo(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
        this.types = new HashMap<>();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public HyriGameType getType(String name) {
        return this.types.get(name);
    }

    @Override
    public void addType(String name, HyriGameType type) {
        this.types.put(name, type);
    }

    @Override
    public void removeType(String name) {
        this.types.remove(name);
    }

    @Override
    public Map<String, HyriGameType> getTypes() {
        return this.types;
    }
}

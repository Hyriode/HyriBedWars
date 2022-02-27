package fr.hyriode.bedwars.api.player;

import fr.hyriode.hyriapi.HyriAPI;
import fr.hyriode.hyriapi.cosmetic.HyriCosmetic;
import fr.hyriode.hyriapi.player.IHyriPlayer;

import java.util.List;
import java.util.UUID;

public class HyriBWPlayer {

    private final UUID uniqueId;
    private final IHyriPlayer player;
    private HyriBWStatistics statistics;
    private List<Class<? extends HyriCosmetic>> cosmetics;
    private HyriCosmetic currentCosmetic;

    public HyriBWPlayer(UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.player = HyriAPI.get().getPlayerManager().getPlayer(uniqueId);
        this.statistics = new HyriBWStatistics();
        this.cosmetics = HyriAPI.get().getPlayerManager().getPlayer(this.uniqueId).getCosmetics();
    }

    public UUID getUUID() {
        return this.uniqueId;
    }

    public IHyriPlayer getPlayer() {
        return player;
    }

    public HyriBWStatistics getStatistics() {
        return this.statistics;
    }

    public void setStatistics(HyriBWStatistics statistics) {
        this.statistics = statistics;
    }

    public List<Class<? extends HyriCosmetic>> getCosmetics() {
        return cosmetics;
    }

    public void setCosmetics(List<Class<? extends HyriCosmetic>> cosmetics) {
        this.cosmetics = cosmetics;
    }

    public void addCosmetic(Class<? extends HyriCosmetic> cosmetic){
        this.cosmetics.add(cosmetic);
    }

    public void addCosmetic(String name){
        this.addCosmetic(HyriAPI.get().getCosmeticManager().getCosmetic(name).getClass());
    }

    public void removeCosmetic(Class<? extends HyriCosmetic> cosmetic){
        this.cosmetics.remove(cosmetic);
    }

    public void removeCosmetic(String name){
        this.removeCosmetic(HyriAPI.get().getCosmeticManager().getCosmetic(name).getClass());
    }

    public HyriCosmetic getCurrentCosmetic() {
        return currentCosmetic;
    }

    public void setCurrentCosmetic(HyriCosmetic currentCosmetic) {
        this.currentCosmetic = currentCosmetic;
    }
}

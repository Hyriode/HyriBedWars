package fr.hyriode.bedwars.api.player;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.player.HyriPlayerData;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.bedwars.api.shop.HyriHotbarCategory;
import fr.hyriode.bedwars.game.material.BWMaterial;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HyriBWPlayer extends HyriPlayerData {

    private Map<HyriHotbarCategory, Integer> hotBar;
    private Map<String, Integer> quickBuy;
    private HyriGameStyle gameStyle;

    public HyriBWPlayer() {
        this.resetQuickBuy();
        this.resetHotbar();
        this.gameStyle = HyriGameStyle.HYRIODE;
    }

    public Map<String, Integer> getQuickBuy() {
        return quickBuy;
    }

    public void putMaterialQuickBuy(int slot, String nameMaterial){
        if(this.quickBuy.containsValue(slot)){
            String material = this.getItemSlotQuickBuy(slot);
            if(material != null) this.quickBuy.remove(material);
        }
        this.quickBuy.put(nameMaterial, slot);
    }

    public String getItemSlotQuickBuy(int slotSearch){
        for (String material : this.quickBuy.keySet()) {
            if(this.quickBuy.get(material) == slotSearch)
                return material;
        }
        return null;
    }

    public void removeMaterialQuickBuy(String nameMaterial){
        this.quickBuy.remove(nameMaterial);
    }

    public void removeMaterialQuickBuy(int slot){
        this.quickBuy.values().remove(slot);
    }

    public Map<HyriHotbarCategory, Integer> getHotBar() {
        return hotBar;
    }

    public void putMaterialHotBar(int slot, HyriHotbarCategory category){
        if(this.hotBar.containsValue(slot)){
            HyriHotbarCategory material = this.getCategorySlotHotBar(slot);
            if(material != null) this.hotBar.remove(material);
        }
        this.hotBar.put(category, slot);
    }

    public HyriHotbarCategory getCategorySlotHotBar(int slotSearch){
        for (HyriHotbarCategory category : this.hotBar.keySet()) {
            if(this.hotBar.get(category) == slotSearch)
                return category;
        }
        return null;
    }

    public int getSlotByHotbar(HyriHotbarCategory category){
        if(this.hotBar.get(category) != null)
            return this.hotBar.get(category);
        return -1;
    }

    public void removeMaterialHotBar(HyriHotbarCategory category){
        this.hotBar.remove(category);
    }

    public void removeMaterialHotBar(int slot){
        this.hotBar.values().remove(slot);
    }

    public void resetHotbar(){
        this.hotBar = new HashMap<>();

        this.putMaterialHotBar(0, HyriHotbarCategory.MELEE);
        this.putMaterialHotBar(8, HyriHotbarCategory.COMPASS);
    }

    public HyriGameStyle changeGamePlayStyle(){
        switch (this.gameStyle){
            case HYPIXEL:
                return this.gameStyle = HyriGameStyle.HYRIODE;
            case HYRIODE:
                return this.gameStyle = HyriGameStyle.HYPIXEL;
        }
        return this.gameStyle;
    }

    public HyriGameStyle getGameStyle() {
        return gameStyle;
    }

    public void update(UUID uuid){
        IHyriPlayer player = HyriAPI.get().getPlayerManager().getPlayer(uuid);
        player.addData("bedwars", this);
        player.update();
    }

    public void resetQuickBuy() {
        this.quickBuy = new HashMap<>();

        this.putMaterialQuickBuy(19, BWMaterial.WOOL.name());
        this.putMaterialQuickBuy(20, BWMaterial.STONE_SWORD.name());
        this.putMaterialQuickBuy(21, BWMaterial.CHAINMAIL_ARMOR.name());
        this.putMaterialQuickBuy(22, BWMaterial.PICKAXE.name());
        this.putMaterialQuickBuy(23, BWMaterial.BOW.name());
        this.putMaterialQuickBuy(24, BWMaterial.POTION_SPEED.name());
        this.putMaterialQuickBuy(25, BWMaterial.TNT.name());
        this.putMaterialQuickBuy(28, BWMaterial.PLANKS.name());
        this.putMaterialQuickBuy(29, BWMaterial.IRON_SWORD.name());
        this.putMaterialQuickBuy(30, BWMaterial.IRON_ARMOR.name());
        this.putMaterialQuickBuy(31, BWMaterial.SHEARS.name());
        this.putMaterialQuickBuy(32, BWMaterial.ARROW.name());
        this.putMaterialQuickBuy(33, BWMaterial.POTION_JUMP.name());
        this.putMaterialQuickBuy(34, BWMaterial.WATER.name());
        this.putMaterialQuickBuy(37, BWMaterial.END_STONE.name());
        this.putMaterialQuickBuy(38, BWMaterial.DIAMOND_SWORD.name());
        this.putMaterialQuickBuy(39, BWMaterial.DIAMOND_ARMOR.name());
        this.putMaterialQuickBuy(40, BWMaterial.AXE.name());
        this.putMaterialQuickBuy(41, BWMaterial.BOW_PUNCH.name());
        this.putMaterialQuickBuy(42, BWMaterial.POTION_INVISIBILITY.name());
        this.putMaterialQuickBuy(43, BWMaterial.GOLDEN_APPLE.name());
    }
}

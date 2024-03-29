package fr.hyriode.bedwars.api.player;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.mongodb.MongoDocument;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.model.IHyriPlayerData;
import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.api.player.style.HyriGameStyle;
import fr.hyriode.bedwars.game.player.hotbar.HotbarCategory;
import fr.hyriode.bedwars.game.shop.material.MaterialShop;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class BWPlayerData implements IHyriPlayerData {

    private Map<Integer, HotbarCategory> hotBar = new HashMap<>();
    private Map<Integer, String> quickBuy = new HashMap<>();
    private HyriGameStyle gameStyle = HyriGameStyle.HYRIODE;
    private transient HyriBedWars plugin;

    public BWPlayerData(HyriBedWars plugin) {
        this.plugin = plugin;
        this.resetQuickBuy();
        this.resetHotbar();
    }

    public Map<Integer, String> getQuickBuy() {
        return this.quickBuy;
    }

    public Map<Integer, MaterialShop> getQuickBuyShop() {
        Map<Integer, MaterialShop> finalQuickBuy = new HashMap<>();
        this.quickBuy.forEach((slot, name) -> {
            finalQuickBuy.put(slot, this.plugin.getShopManager().getMaterialShopByName(true, name));
        });
        return finalQuickBuy;
    }

    public int getSlotQuickBuyByMaterialName(String nameMaterial){
        return this.quickBuy.keySet().stream().filter(slot -> this.quickBuy.get(slot).equals(nameMaterial)).findFirst().orElse(-1);
    }

    public void putMaterialQuickBuy(int slot, String nameMaterial){
        this.quickBuy.remove(this.getSlotQuickBuyByMaterialName(nameMaterial));
        this.quickBuy.remove(slot);
        this.quickBuy.put(slot, nameMaterial);
    }

    public void removeSlotQuickBuy(int slot){
        this.quickBuy.remove(slot);
    }

    public Map<Integer, HotbarCategory> getHotBar() {
        return hotBar;
    }

    public void putMaterialHotBar(int slot, HotbarCategory category){
        if(!category.isCanDuplicate()){
            this.getSlotByHotbar(category).forEach(this::removeMaterialHotBar);
        }
        this.hotBar.remove(slot);
        this.hotBar.put(slot, category);
    }

    public List<Integer> getSlotByHotbar(HotbarCategory category){
        return this.hotBar.keySet().stream().filter(slot -> !this.hotBar.isEmpty() && this.hotBar.get(slot) == category)
                .collect(Collectors.toList());
    }

    public int getSlotByHotbar(Player player, ItemStack item, HotbarCategory category){
        return this.getSlotByHotbar(category).stream().filter(slot -> {
            ItemStack itemStack = player.getInventory().getItem(slot);
            return itemStack == null || !item.isSimilar(itemStack) || item.isSimilar(itemStack) && itemStack.getAmount() < 64;
        }).findFirst().orElse(-1);
    }

    public void removeMaterialHotBar(int slot){
        this.hotBar.remove(slot);
    }

    public void resetHotbar(){
        this.hotBar = new HashMap<>();

        this.putMaterialHotBar(0, HotbarCategory.MELEE);
        this.putMaterialHotBar(8, HotbarCategory.COMPASS);
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
        player.getData().add("bedwars", this);
        player.update();
    }

    public void resetQuickBuy() {
        this.quickBuy = new HashMap<>();
//TODO
        int i = 0;
        this.putMaterialQuickBuy(i, "wool");
        this.putMaterialQuickBuy(++i, "stone-sword");
        this.putMaterialQuickBuy(++i, "chainmail-armor");
        this.putMaterialQuickBuy(++i, "pickaxe");
        this.putMaterialQuickBuy(++i, "bow");
        this.putMaterialQuickBuy(++i, "potion-speed");
        this.putMaterialQuickBuy(++i, "tnt");
        this.putMaterialQuickBuy(++i, "wood");
        this.putMaterialQuickBuy(++i, "iron-sword");
        this.putMaterialQuickBuy(++i, "iron-armor");
        this.putMaterialQuickBuy(++i, "shears");
        this.putMaterialQuickBuy(++i, "arrow");
        this.putMaterialQuickBuy(++i, "potion-jump");
        this.putMaterialQuickBuy(++i, "water-bucket");
        this.putMaterialQuickBuy(++i, "end-stone");
        this.putMaterialQuickBuy(++i, "diamond-sword");
        this.putMaterialQuickBuy(++i, "diamond-armor");
        this.putMaterialQuickBuy(++i, "axe");
        this.putMaterialQuickBuy(++i, "bow-punch");
        this.putMaterialQuickBuy(++i, "potion-invisibility");
        this.putMaterialQuickBuy(++i, "golden-apple");
    }

    @Override
    public void save(MongoDocument document) {
        final Document hotBarDoc = new Document();
        final Document quickBuyDoc = new Document();

        this.hotBar.forEach((slot, category) -> {
            hotBarDoc.append(slot.toString(), category.name());
        });
        document.append("hotbar", hotBarDoc);

        this.quickBuy.forEach((slot, name) -> {
            quickBuyDoc.append(slot.toString(), name);
        });
        document.append("quickbuy", quickBuyDoc);

        document.append("gameplay", this.gameStyle.name());
    }

    @Override
    public void load(MongoDocument document) {
        document.get("hotbar", Document.class).forEach((key, value) -> {
            this.hotBar.put(Integer.parseInt(key), HotbarCategory.valueOf(value.toString()));
        });
        document.get("quickbuy", Document.class).forEach((key, value) -> {
            this.quickBuy.put(Integer.parseInt(key), value.toString());
        });
        this.gameStyle = HyriGameStyle.valueOf(document.getString("gameplay"));
    }
}

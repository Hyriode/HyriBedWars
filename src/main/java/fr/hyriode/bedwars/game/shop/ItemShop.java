package fr.hyriode.bedwars.game.shop;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.bedwars.game.shop.material.MaterialShop;
import fr.hyriode.bedwars.game.type.BWGameType;
import fr.hyriode.bedwars.host.BWShopValues;
import fr.hyriode.bedwars.utils.InventoryUtils;
import fr.hyriode.bedwars.utils.MetadataReferences;
import fr.hyriode.bedwars.utils.StringUtils;
import fr.hyriode.hyrame.game.team.HyriGameTeam;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.api.language.HyriLanguageMessage;
import fr.hyriode.hyrame.utils.Pair;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ItemShop {

    private String name;
    private String materialName;
    private ItemBuilder item;
    private ShopCategory category;
    private final Supplier<ItemPrice> price;

    public ItemShop(ItemBuilder item, ItemPrice price){
        this(item, () -> price);
    }

    public ItemShop(ItemBuilder item, Supplier<ItemPrice> price){
        this.item = item;
        this.price = price;
    }

    public String getName() {
        return this.name;
    }

    public ShopCategory getCategory() {
        return category;
    }

    public ItemShop setName(String name) {
        this.name = name;
        return this;
    }

    public ItemShop setMaterialName(String materialName) {
        this.materialName = materialName;
        return this;
    }

    public ItemShop setCategory(ShopCategory category) {
        this.category = category;
        return this;
    }

    public HyriLanguageMessage getDisplayName(){
        return HyriLanguageMessage.get("item." + this.getName() + ".name");
    }

    public Pair<String, HyriLanguageMessage> getDescription(){
        String key = "item." + this.getName() + ".lore";
        return new Pair<>(key, HyriLanguageMessage.get(key));
    }

    public String getMaterialName() {
        return materialName;
    }

    public ItemStack getItem(ShopManager shopManager) {
        if(this.item.build().getType().isBlock()){
            return this.item.build();
        }
        ItemMeta itemMeta = this.item.build().getItemMeta();

        if(shopManager.getMaterialByItemShop(false, this).isPermanent()) {
            this.item = item.nbt().setBoolean(MetadataReferences.ISPERMANENT, true).toBuilder();
        }
        ItemStack output = this.item.unbreakable().withItemFlags(ItemFlag.HIDE_UNBREAKABLE).nbt().setString(MetadataReferences.MATERIAL, this.materialName).build();
        output.setItemMeta(itemMeta);
        return output;
    }

    @SuppressWarnings("deprecation")
    public ItemStack getItemColored(ShopManager shopManager, HyriGameTeam team){
        ItemStack itemStack = this.getItem(shopManager).clone();
        itemStack.setDurability(team.getColor().getDyeColor().getWoolData());
        return itemStack;
    }

    public ItemStack getItemStack(BWGamePlayer player){
        if(this.isColorable() && player != null) {
            return this.getItemColored(player.getPlugin().getShopManager(), player.getTeam());
        }
        return this.getItem(player.getPlugin().getShopManager()).clone();
    }

    public ItemPrice getPrice() {
        return this.price.get();
    }

    public int getPriceAmount() {
        int percentage = BWShopValues.PRICE_GLOBAL.get();
        int amount = this.getPrice().getAmount().get().get() * percentage / 100;
        if (percentage == 50 && amount == 0) {
            amount = 1;
        }
        return amount;
    }

    public ItemStack getForShop(BWGamePlayer bwPlayer, ShopCategory category){
        if(this.item == null || this.item.build().getType() == Material.AIR)
            return null;
        Player player = bwPlayer.getPlayer();
        ShopManager shopManager = bwPlayer.getPlugin().getShopManager();
        MaterialShop material = shopManager.getMaterialByItemShop(false, this);
        final long missingPrice = InventoryUtils.getHasPrice(player, this.getPrice(), this.getPriceAmount());
        final boolean canBuy = missingPrice <= 0;
        final Pair<String, HyriLanguageMessage> description = this.getDescription();
        final boolean unlocked = bwPlayer.containsItemPermanent(material);
        final boolean maxed = bwPlayer.containsMaterialUpgrade(material) && bwPlayer.getMaterialUpgrade(material).isMaxed();
        final boolean upgradable = material.isUpgradable();
        final boolean quickbuy = category == ShopCategory.QUICK_BUY;
        final List<String> lore = new ArrayList<>();

        if(upgradable) {
            int tier = 0;
            if(bwPlayer.containsMaterialUpgrade(material)){
                tier = bwPlayer.getMaterialUpgrade(material).getNextTier();
            }
            lore.add(ChatColor.GRAY + "Tier: " + ChatColor.AQUA + StringUtils.getLevelLang(tier+ 1));
        }
        lore.add(this.getDisplayPrice(player));
        lore.add(" ");

        String desc = description == null || description.getValue().getValue(player).equals(description.getKey()) ? null : description.getValue().getValue(player);
        if(desc != null) {
            lore.addAll(StringUtils.loreToList(desc));
            lore.add(" ");
        }

        if(unlocked){
            lore.add(ChatColor.GREEN + HyriLanguageMessage.get("shop.inventory.item.unlocked").getValue(player));
        } else if(maxed) {
            lore.add(ChatColor.GREEN + HyriLanguageMessage.get("shop.inventory.item.maxed").getValue(player));
        }else {
            lore.add(canBuy
                    ? ChatColor.YELLOW + HyriLanguageMessage.get("inv.click-purchase").getValue(player)
                    : ChatColor.RED + String.format(HyriLanguageMessage.get("shop.inventory.item.missing")
                    .getValue(player), missingPrice, this.getPrice().getName(player)));
        }

        if(quickbuy) {
            lore.add(ChatColor.AQUA + "Shift right click to remove from quick buy");//TODO unhardcoder
        } else {
            lore.add(ChatColor.AQUA + "Shift right click to add in quick buy");
        }

        return new ItemBuilder(this.getItemStack(bwPlayer))
                .withName(StringUtils.getTitleBuy(maxed || unlocked, canBuy) + this.getDisplayName().getValue(player))
                .withLore(lore).withAllItemFlags()
                .build();
    }

    public boolean hasPrice(Player player) {
        if(this.getPrice() == null){
            return false;
        }
        return InventoryUtils.hasPrice(player, this.getPrice(), this.getPriceAmount());
    }

    protected String getDisplayPrice(Player player){
        return StringUtils.getDisplayCostPrice(player, this);
    }

    public boolean isColorable() {
        ItemStack itemStack = this.item.build().clone();
        switch (itemStack.getType()){
            case WOOL:
            case STAINED_CLAY:
                return true;
        }
        return false;
    }
}

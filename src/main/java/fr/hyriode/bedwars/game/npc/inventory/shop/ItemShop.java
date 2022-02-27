package fr.hyriode.bedwars.game.npc.inventory.shop;

import fr.hyriode.bedwars.utils.StringBWUtils;
import fr.hyriode.hyrame.item.HyriItem;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.language.HyriLanguageMessage;
import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.npc.inventory.EHyriBWShopNavBar;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class ItemShop {

    private final String keyName;
    private final String keyDescription;
    private final ItemStack itemStack;
    private Class<? extends HyriItem<?>> hyriItem;
    private List<ItemShopStack> price;
    private EHyriBWShopNavBar category;
    private boolean permanent;
    private ChatColor color;
    private BWMaterial hyriMaterial;

    private boolean upgradable;

    public ItemShop(ItemStack itemStack, String keyName, ChatColor color){
        this(itemStack, keyName, null, false, color);
    }

    public ItemShop(Material material, String keyName, ChatColor color){
        this(new ItemStack(material), keyName, null, false, color);
    }

    public ItemShop(ItemStack material, String keyName, String keyDescription, EHyriBWShopNavBar category, boolean permanent, ChatColor color, ItemShopStack... price){
        this.itemStack = material.clone();
        this.keyName = keyName;
        this.keyDescription = keyDescription;
        this.category = category;
        this.permanent = permanent;
        this.color = color;
        this.price = Arrays.asList(price);
    }

    public ItemShop(ItemStack material, String keyName, EHyriBWShopNavBar category, boolean permanent, ChatColor color, ItemShopStack... price){
        this(material, keyName, keyName, category, permanent, color, price);
    }

    public ItemShop(Material material, String keyName, String keyDescription, EHyriBWShopNavBar category, boolean permanent, ChatColor color, ItemShopStack... price) {
        this(new ItemStack(material), keyName, keyDescription, category, permanent, color, price);
    }

    public ItemShop(Material material, String keyName, EHyriBWShopNavBar category, boolean permanent, ChatColor color, ItemShopStack... price) {
        this(new ItemStack(material), keyName, keyName, category, permanent, color, price);
    }

    public ItemShop(ItemStack itemStack, Class<? extends HyriItem<?>> hyriItem, String keyName, EHyriBWShopNavBar category, boolean permanent, ChatColor color, ItemShopStack... price) {
        this.itemStack = itemStack;
        this.hyriItem = hyriItem;
        this.keyName = keyName;
        this.keyDescription = keyName;
        this.category = category;
        this.permanent = permanent;
        this.color = color;
        this.price = Arrays.asList(price);
    }

    public String getKeyName() {
        return keyName;
    }

    public ItemStack getMaterial() {
        return itemStack;
    }

    public void setPrice(ItemShopStack... price) {
        this.price = Arrays.asList(price);
    }

    public void setPrice(List<ItemShopStack> price) {
        this.price = price;
    }

    public List<ItemShopStack> getPrice() {
        return price;
    }

    public String getCountPriceAsString(Player player){
        return StringBWUtils.getCountPriceAsString(player, this.price);
    }

    public String getPriceAsString(Player player){
        return StringBWUtils.getPriceAsString(player, this.price);
    }

    public void setCategory(EHyriBWShopNavBar category) {
        this.category = category;
    }

    public EHyriBWShopNavBar getCategory() {
        return category;
    }

    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
    }

    public boolean isPermanent() {
        return permanent;
    }

    public HyriLanguageMessage getName(){
        return HyriBedWars.getLanguageManager().getMessage("shop.item." + this.keyName + ".name");
    }

    public HyriLanguageMessage getDescription(){
        return HyriBedWars.getLanguageManager().getMessage("shop.item." + this.keyName + ".lore");
    }

    public ItemStack getItemStack(){
        return new ItemBuilder(itemStack).nbt().setBoolean("IsPermanent", this.permanent).build();
    }

    public ChatColor getColor() {
        return color;
    }

    public void setColor(ChatColor color) {
        this.color = color;
    }

    public Class<? extends HyriItem<?>> getHyriItem() {
        return hyriItem;
    }

    public ItemShop setUpgradable(boolean upgradable) {
        this.upgradable = upgradable;
        return this;
    }

    public boolean isUpgradable() {
        return upgradable;
    }

    public ItemShop setHyriMaterial(BWMaterial material){
        this.hyriMaterial = material;
        return this;
    }

    public BWMaterial getHyriMaterial() {
        return hyriMaterial;
    }
}

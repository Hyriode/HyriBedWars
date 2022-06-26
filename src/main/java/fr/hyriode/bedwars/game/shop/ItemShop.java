package fr.hyriode.bedwars.game.shop;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.bedwars.utils.InventoryUtils;
import fr.hyriode.bedwars.utils.MetadataReferences;
import fr.hyriode.bedwars.utils.StringUtils;
import fr.hyriode.hyrame.game.team.HyriGameTeam;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.language.HyriLanguageMessage;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemShop {

    private String name;
    private ItemBuilder item;
    private ShopCategory category;
    private final ItemPrice price;

    public ItemShop(ItemBuilder item, ItemPrice price){
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

    public ItemShop setCategory(ShopCategory category) {
        this.category = category;
        return this;
    }

    public HyriLanguageMessage getDisplayName(){
        return HyriLanguageMessage.get("item." + this.getName() + ".name");
    }

    public HyriLanguageMessage getDescription(){
        return HyriLanguageMessage.get("item." + this.getName() + ".description");
    }

    public ItemBuilder getItem() {
        if(this.getMaterialShop().isPermanent()) {
            this.item = item.nbt().setBoolean(MetadataReferences.ISPERMANENT, true).toBuilder();
        }
        return this.item.unbreakable().withItemFlags(ItemFlag.HIDE_UNBREAKABLE);
    }

    @SuppressWarnings("deprecation")
    public ItemStack getItemColored(HyriGameTeam team){
        ItemStack itemStack = this.getItem().build().clone();
        return new ItemBuilder(itemStack.getType(), itemStack.getAmount(), team.getColor().getDyeColor().getWoolData()).build();
    }

    public ItemStack getItemStack(BWGamePlayer player){
        if(this.isColorable() && player != null) {
            return this.getItemColored(player.getTeam());
        }
        return this.getItem().build().clone();
    }

    public ItemPrice getPrice() {
        return this.price;
    }

    public ItemStack getForShop(BWGamePlayer bwPlayer){
        if(this.item == null || this.item.build().getType() == Material.AIR)
            return null;
        Player player = bwPlayer.getPlayer();
        MaterialShop material = this.getMaterialShop();
        final long missingPrice = InventoryUtils.getHasPrice(player, this.getPrice());
        final boolean canBuy = missingPrice <= 0;
        final HyriLanguageMessage description = this.getDescription();
        final boolean unlocked = bwPlayer.containsItemPermanent(material);
        final boolean maxed = bwPlayer.containsMaterialUpgrade(material) && bwPlayer.getMaterialUpgrade(material).isMaxed();
        final boolean upgradable = material.isUpgradable();
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

        if(description != null) {
            lore.addAll(StringUtils.loreToList(description.getForPlayer(player)));
            lore.add(" ");
        }

        if(unlocked){
            lore.add(ChatColor.GREEN + HyriLanguageMessage.get("shop.inventory.item.unlocked").getForPlayer(player));
        } else if(maxed) {
            lore.add(ChatColor.GREEN + HyriLanguageMessage.get("shop.inventory.item.maxed").getForPlayer(player));
        }else {
            lore.add(canBuy
                    ? ChatColor.YELLOW + HyriLanguageMessage.get("inv.click-purchase").getForPlayer(player)
                    : ChatColor.RED + String.format(HyriLanguageMessage.get("shop.inventory.item.missing")
                    .getForPlayer(player), missingPrice, this.getPrice().getName(player)));
        }

        return new ItemBuilder(this.getItemStack(null))
                .withName(StringUtils.getTitleBuy(maxed || unlocked, canBuy) + " " + this.getDisplayName().getForPlayer(player))
                .withLore(lore).withAllItemFlags()
                .build();
    }

    private MaterialShop getMaterialShop() {
        return HyriBedWars.getShopManager().getMaterialByItemShop(this);
    }

    public boolean hasPrice(Player player) {
        if(this.getPrice() == null){
            return false;
        }
        return InventoryUtils.hasPrice(player, this.price);
    }


    protected String getDisplayPrice(Player player){
        return StringUtils.getDisplayCostPrice(player, this.price);
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

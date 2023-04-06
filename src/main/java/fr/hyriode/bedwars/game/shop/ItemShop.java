package fr.hyriode.bedwars.game.shop;

import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.bedwars.game.shop.material.MaterialShop;
import fr.hyriode.bedwars.utils.InventoryUtils;
import fr.hyriode.bedwars.utils.MetadataReferences;
import fr.hyriode.bedwars.utils.StringUtils;
import fr.hyriode.hyrame.game.team.HyriGameTeam;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.api.language.HyriLanguageMessage;
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

    public HyriLanguageMessage getDescription(){
        return HyriLanguageMessage.get("item." + this.getName() + ".lore");
    }

    public String getMaterialName() {
        return materialName;
    }

    public ItemStack getItem() {
        if(this.item.build().getType().isBlock()){
            return this.item.build();
        }
        ItemMeta itemMeta = this.item.build().getItemMeta();

        if(this.getMaterialShop().isPermanent()) {
            this.item = item.nbt().setBoolean(MetadataReferences.ISPERMANENT, true).toBuilder();
        }
        ItemStack output = this.item.unbreakable().withItemFlags(ItemFlag.HIDE_UNBREAKABLE).nbt().setString(MetadataReferences.MATERIAL, this.materialName).build();
        output.setItemMeta(itemMeta);
        return output;
    }

    @SuppressWarnings("deprecation")
    public ItemStack getItemColored(HyriGameTeam team){
        ItemStack itemStack = this.getItem().clone();
        itemStack.setDurability(team.getColor().getDyeColor().getWoolData());
        return itemStack;
    }

    public ItemStack getItemStack(BWGamePlayer player){
        if(this.isColorable() && player != null) {
            return this.getItemColored(player.getTeam());
        }
        return this.getItem().clone();
    }

    public ItemPrice getPrice() {
        return this.price.get();
    }

    public ItemStack getForShop(BWGamePlayer bwPlayer, ShopCategory category){
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

        if(description != null) {
            lore.addAll(StringUtils.loreToList(description.getValue(player)));
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
            lore.add(ChatColor.AQUA + "Shift right click to remove from quick buy");
        } else {
            lore.add(ChatColor.AQUA + "Shift right click to add in quick buy");
        }

        return new ItemBuilder(this.getItemStack(null))
                .withName(StringUtils.getTitleBuy(maxed || unlocked, canBuy) + this.getDisplayName().getValue(player))
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
        return InventoryUtils.hasPrice(player, this.getPrice());
    }


    protected String getDisplayPrice(Player player){
        return StringUtils.getDisplayCostPrice(player, this.getPrice());
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
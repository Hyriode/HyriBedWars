package fr.hyriode.bedwars.game.material;

import fr.hyriode.bedwars.api.player.HyriBWPlayer;
import fr.hyriode.bedwars.game.BWGamePlayer;
import fr.hyriode.bedwars.game.npc.inventory.shop.BWShopCategory;
import fr.hyriode.bedwars.game.material.upgradable.ArmorBW;
import fr.hyriode.bedwars.game.npc.inventory.shop.BWShopInventory;
import fr.hyriode.bedwars.game.npc.inventory.shop.pages.BWChoiceSlotGUI;
import fr.hyriode.bedwars.game.npc.inventory.shop.pages.BWShopQuickBuy;
import fr.hyriode.bedwars.game.team.upgrade.EBWUpgrades;
import fr.hyriode.bedwars.utils.InventoryBWUtils;
import fr.hyriode.bedwars.utils.MetadataReferences;
import fr.hyriode.bedwars.utils.StringBWUtils;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.language.HyriLanguageMessage;
import fr.hyriode.bedwars.HyriBedWars;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class ItemShop {

    private final String keyName;
    private final ItemStack item;
    private OreStack price;
    private final BWShopCategory category;
    private final boolean permanent;
    private ChatColor color;
    private BWMaterial hyriMaterial;

    private boolean upgradable;

    public ItemShop(String keyName, ItemStack item, BWShopCategory category, boolean permanent, OreStack price) {
        this.keyName = keyName;
        this.item = item;
        this.category = category;
        this.permanent = permanent;
        this.price = price;
    }

    public ItemShop(String keyName, Material material, int amount, BWShopCategory category, boolean permanent, OreStack price){
        this(keyName, new ItemStack(material, amount), category, permanent, price);
    }

    public ItemShop(String keyName, Material material, BWShopCategory category, boolean permanent, OreStack price){
        this(keyName, material, 1, category, permanent, price);
    }

    public ItemShop(String keyName, Material material){
        this(keyName, material, null, false, null);
    }

    public ItemShop(String keyName, Material material, OreStack price){
        this(keyName, material, null, false, price);
    }

    public String getKeyName() {
        return keyName;
    }

    public OreStack getPrice() {
        return price;
    }

    public String getCountPriceAsString(Player player){
        return StringBWUtils.getCountPriceAsString(player, this.price);
    }

    public String getPriceAsString(Player player){
        return StringBWUtils.getPriceAsString(player, this.price);
    }

    public BWShopCategory getCategory() {
        return category;
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

    public ItemStack getItemStack(Player player){
        if(this.permanent)
            return new ItemBuilder(item.clone()).unbreakable().withItemFlags(ItemFlag.HIDE_UNBREAKABLE).nbt().setBoolean(MetadataReferences.ISPERMANENT, true).build();
        else
            return new ItemBuilder(item.clone()).withName(ChatColor.WHITE + this.getName().getForPlayer(player)).build();
    }

    public ItemStack getItemStack(){
        if(this.permanent)
            return new ItemBuilder(item.clone()).unbreakable().withItemFlags(ItemFlag.HIDE_UNBREAKABLE).nbt().setBoolean(MetadataReferences.ISPERMANENT, true).build();
        else
            return new ItemBuilder(item.clone()).build();
    }

    public ChatColor getColor() {
        return color;
    }

    public ItemShop setColor(ChatColor color) {
        this.color = color;
        return this;
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

    public ItemStack getItemForShop(BWShopInventory inventory, BWGamePlayer hyriPlayer){
        List<String> lore = new ArrayList<>();
        Player player = hyriPlayer.getPlayer();
        boolean isQuickBuy = inventory instanceof BWShopQuickBuy;

        ItemShop itemShop = this.getItemToBuy(hyriPlayer);

        boolean isMaxed = itemShop.isUpgradable() && hyriPlayer.hasUpgradeMaterial(itemShop.getHyriMaterial()) && hyriPlayer.getItemUpgradable(itemShop.getHyriMaterial()).isMaxed();

        if(!isMaxed) {
            lore.add(String.format(ChatColor.GRAY + this.getValue(player, "cost"), itemShop.getCountPriceAsString(player)));
        }

        if(this.isUpgradable()) {
            int tier = !hyriPlayer.hasUpgradeMaterial(this.getHyriMaterial()) ? 0 : hyriPlayer.getItemUpgradable(this.getHyriMaterial()).getNextTier();
            lore.add(String.format(ChatColor.GRAY + this.getValue(player, "tier"), ChatColor.YELLOW + InventoryBWUtils.getTierString(player, (isMaxed ? hyriPlayer.getItemUpgradable(this.getHyriMaterial()).getMaxTier() + 2 : tier + 1))));
        }
        lore.add(" ");

        if(this.getDescription() != null) {
            lore.addAll(StringBWUtils.loreToList(this.getDescription().getForPlayer(player)));
            lore.add(" ");
        }

        if(itemShop instanceof ArmorBW)
            if(hyriPlayer.getPermanentArmor() != null && ((ArmorBW) hyriPlayer.getPermanentArmor().getItemShop()).getLevel() >= ((ArmorBW)itemShop).getLevel()){
                lore.add(ChatColor.GREEN + this.getValue(player, "unlocked"));
                lore.add(" ");
            }

        if(isQuickBuy)
            lore.add(ChatColor.AQUA + this.getValue(player, "sneak.remove"));
        else if(!hyriPlayer.getAccount().getQuickBuy().containsKey(this.getHyriMaterial().name()))
            lore.add(ChatColor.AQUA + this.getValue(player, "sneak.add"));


        boolean hasItems = InventoryBWUtils.hasPrice(player, itemShop.getPrice());

        if(isMaxed)
            lore.add(ChatColor.GREEN + this.getValue(player, "maxed"));
        else {
            lore.add((hasItems ? ChatColor.YELLOW + HyriBedWars.getLanguageManager().getValue(player, "inv.shop.click.purchase") : ChatColor.RED + HyriBedWars.getLanguageManager().getValue(player, "inv.shop.enough.item") + " " + this.getPriceAsString(player)));
        }

        return new ItemBuilder(itemShop.getItemStack(player))
                .withName((hasItems ? ChatColor.GREEN : ChatColor.RED) + itemShop.getName().getForPlayer(player))
                .withAllItemFlags().withLore(lore).build();
    }

    private ItemShop getItemToBuy(BWGamePlayer player){
        if(this.isUpgradable()){
            if(player.hasUpgradeMaterial(this.getHyriMaterial())){
                if(player.getItemUpgradable(this.getHyriMaterial()).isMaxed()){
                    return this.getHyriMaterial().getItemUpgradable().getTierItem(this.getHyriMaterial().getItemUpgradable().getMaxTier());
                }else{
                    return this.getHyriMaterial().getItemUpgradable().getNextTierItem();
                }
            }else{
                return this.getHyriMaterial().getItemUpgradable().getTierItem(0);
            }
        }else{
            return this;
        }
    }

    @SuppressWarnings("deprecation")
    public Consumer<InventoryClickEvent> getClick(HyriBedWars plugin, BWShopInventory inventory) {
        return event -> {
            final BWMaterial material = this.getHyriMaterial();
            boolean isQuickBuy = inventory instanceof BWShopQuickBuy;
            BWGamePlayer player = inventory.getPlayer();

            // For Quick buy
            if(event.isShiftClick()){
                if(!isQuickBuy) {
                    new BWChoiceSlotGUI(plugin, player.getPlayer(), material, inventory.isHyriode()).open();
                }else{
                    HyriBWPlayer account = player.getAccount();
                    account.removeMaterialQuickBuy(event.getSlot());
                    account.update(player.getUUID());
                    inventory.refreshGui();
                }
                return;
            }

            final Player owner = player.getPlayer();
            HyriBWPlayer account = inventory.getPlayer().getAccount();
            final ItemShop itemShop = material.isItemUpgradable() ?
                    player.getItemUpgradable(material) != null ?
                            material.getItemUpgradable().getNextTierItem() :
                            material.getItemUpgradable().getTierItem(0) :
                    material.getItemShop();
            final boolean isMaxed = itemShop.isUpgradable() &&
                    player.hasUpgradeMaterial(itemShop.getHyriMaterial()) &&
                    player.getItemUpgradable(itemShop.getHyriMaterial()).isMaxed();

            if(InventoryBWUtils.isFull(owner, itemShop.getItemStack(owner))){
                owner.sendMessage(ChatColor.RED + this.getValue(owner, "player-inventory.full"));
                return;
            }
            int slotAccount = account.getSlotByHotbar(this.getCategory().getHotbar());
            int slot = event.getHotbarButton() != -1 ? event.getHotbarButton() : account.getSlotByHotbar(this.getCategory().getHotbar()) != -1 ? slotAccount : -1;

            if (InventoryBWUtils.hasPrice(owner, itemShop.getPrice()) && !isMaxed) {
                if (material.isArmor()) {
                    if (player.getPermanentArmor() != null && player.getPermanentArmor().getArmor().getLevel() >= ((ArmorBW) itemShop).getLevel()) {
                        owner.sendMessage(ChatColor.RED + this.getValue(owner, "armor.lower"));
                        owner.playSound(owner.getLocation(), Sound.ENDERMAN_TELEPORT, 0.8F, 0.1F);
                        return;
                    } else {
                        player.giveArmor(material);
                        player.activeUpgradesTeam(EBWUpgrades.PROTECTION_ARMOR);
                    }
                } else {
                    if (material.isItemUpgradable()) {
                        player.nextUpgradeItem(material);
                    } else {
                        if (material.getItemShop().isPermanent()) {
                            if (player.hasPermanentItem(material)) {
                                owner.sendMessage(ChatColor.RED + this.getValue(owner, "item.already"));
                                return;
                            }
                            player.addPermanentItem(material);
                        }
                        if (material.isHyriItem()) {
                            plugin.getHyrame().getItemManager().giveItem(owner, material.getHyriItem());
                        } else {
                            if (material == BWMaterial.WOOL) {
                                InventoryBWUtils.addItem(owner, slot,
                                        new ItemBuilder(itemShop.getItemStack(owner).getType(),
                                                itemShop.getItemStack(owner).getAmount(),
                                                player.getTeam().getColor().getDyeColor().getWoolData()).build());
                            } else if (Arrays.asList(BWMaterial.getSwords()).contains(material)) {
                                if(InventoryBWUtils.hasItem(owner, new ItemStack(Material.WOOD_SWORD)))
                                    InventoryBWUtils.setItemsSlot(owner, s -> material.getItemShop().getItemStack(owner),
                                            new ItemStack(Material.WOOD_SWORD));
                                else
                                    InventoryBWUtils.addItem(owner, slot, itemShop.getItemStack(owner));
                                plugin.getGame().getPlayer(owner).activeUpgradesTeam(EBWUpgrades.SHARPNESS);
                            } else {
                                InventoryBWUtils.addItem(owner, slot, itemShop.getItemStack(owner));
                            }
                        }
                    }
                }
                InventoryBWUtils.removeItems(owner, itemShop.getPrice());
                owner.playSound(owner.getLocation(), Sound.NOTE_PLING, 0.8F, 2.0F);
                owner.sendMessage(ChatColor.GREEN + this.getValue(owner, "purchased") + " " + ChatColor.GOLD + itemShop.getName().getForPlayer(owner));
            } else if (isMaxed) {
                owner.sendMessage(ChatColor.RED + this.getValue(owner, "maxed.warn"));
                owner.playSound(owner.getLocation(), Sound.ENDERMAN_TELEPORT, 0.8F, 0.1F);
            } else {
                owner.sendMessage(String.format(ChatColor.RED + this.getValue(owner, "purchased.missing"), StringBWUtils.getPriceAsString(owner, itemShop.getPrice()), StringBWUtils.getCountPriceMissing(owner, itemShop.getPrice())));
                owner.playSound(owner.getLocation(), Sound.ENDERMAN_TELEPORT, 0.8F, 0.1F);
            }
            inventory.refreshGui();
        };
    }

    public ItemStack getItemToPlace(BWGamePlayer hyriPlayer){
        Player player = hyriPlayer.getPlayer();
        List<String> lore = new ArrayList<>();

        ItemShop itemShop = this.getItemToBuy(hyriPlayer);

        boolean isMaxed = itemShop.isUpgradable() && hyriPlayer.hasUpgradeMaterial(itemShop.getHyriMaterial()) && hyriPlayer.getItemUpgradable(itemShop.getHyriMaterial()).isMaxed();

        if(!isMaxed) {
            lore.add(ChatColor.GRAY + String.format(this.getValue(player, "cost"), itemShop.getCountPriceAsString(player)));
        }

        lore.add(" ");

        if(this.getDescription() != null) {
            lore.addAll(StringBWUtils.loreToList(this.getDescription().getForPlayer(player)));
            lore.add(" ");
        }

        boolean hasItems = InventoryBWUtils.hasPrice(player, itemShop.getPrice());

        lore.add(ChatColor.YELLOW + HyriBedWars.getLanguageManager().getValue(player, "inv.choice_slot.add"));

        return new ItemBuilder(itemShop.getItemStack(player))
                .withName((hasItems ? ChatColor.GREEN : ChatColor.RED) + itemShop.getName().getForPlayer(player))
                .withAllItemFlags().withLore(lore).build();
    }

    public ItemStack getItemToReplace(BWGamePlayer hyriPlayer){
        Player player = hyriPlayer.getPlayer();
        ItemShop itemShop = this.getItemToBuy(hyriPlayer);

        boolean hasItems = InventoryBWUtils.hasPrice(player, itemShop.getPrice());

        return new ItemBuilder(itemShop.getItemStack(player))
                .withName((hasItems ? ChatColor.GREEN : ChatColor.RED) + itemShop.getName().getForPlayer(player))
                .withAllItemFlags()
                .withLore(ChatColor.YELLOW + HyriBedWars.getLanguageManager().getValue(player, "inv.choice_slot.replace")).build();
    }

    public void setPrice(OreStack price){
        this.price = price;
    }

    public void changePrice(int price){
        this.price.setAmount(price);
    }

    private String getValue(Player player, String key){
        return HyriBedWars.getLanguageManager().getValue(player, "shop." + key);
    }
}

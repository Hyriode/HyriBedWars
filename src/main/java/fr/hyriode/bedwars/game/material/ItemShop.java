package fr.hyriode.bedwars.game.material;

import fr.hyriode.bedwars.game.BWGamePlayer;
import fr.hyriode.bedwars.game.npc.inventory.shop.BWShopCategory;
import fr.hyriode.bedwars.game.npc.inventory.shop.BWShopInventory;
import fr.hyriode.bedwars.game.material.upgradable.ArmorBW;
import fr.hyriode.bedwars.game.npc.inventory.shop.pages.BWChoiceSlotGUI;
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
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class ItemShop {

    private final String keyName;
    private final ItemStack item;
    private final List<OreStack> price;
    private final BWShopCategory category;
    private final boolean permanent;
    private ChatColor color;
    private BWMaterial hyriMaterial;

    private boolean upgradable;

    public ItemShop(String keyName, ItemStack item, BWShopCategory category, boolean permanent, OreStack... price) {
        this.keyName = keyName;
        this.item = item;
        this.category = category;
        this.permanent = permanent;
        this.price = Arrays.asList(price);
    }

    public ItemShop(String keyName, Material material, int amount, BWShopCategory category, boolean permanent, OreStack... price){
        this(keyName, new ItemStack(material, amount), category, permanent, price);
    }

    public ItemShop(String keyName, Material material, BWShopCategory category, boolean permanent, OreStack... price){
        this(keyName, material, 1, category, permanent, price);
    }

    public ItemShop(String keyName, Material material, boolean permanent, OreStack... price){
        this(keyName, material, null, permanent, price);
    }

    public ItemShop(String keyName, Material material, OreStack... price){
        this(keyName, material, false, price);
    }

    public String getKeyName() {
        return keyName;
    }

    public List<OreStack> getPrice() {
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

    public ItemStack getItemStack(){
        return new ItemBuilder(item).nbt().setBoolean(MetadataReferences.ISPERMANENT, this.permanent).build();
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

    public ItemStack getItemForShop(BWGamePlayer hyriPlayer){
        Player player = hyriPlayer.getPlayer();
        List<String> lore = new ArrayList<>();

        ItemShop itemShop = this.getItemToBuy(hyriPlayer);

        boolean isMaxed = itemShop.isUpgradable() && hyriPlayer.hasUpgradeMaterial(itemShop.getHyriMaterial()) && hyriPlayer.getItemUpgradable(itemShop.getHyriMaterial()).isMaxed();

        if(!isMaxed && !itemShop.getPrice().isEmpty()) {
            lore.add(ChatColor.GRAY + "Cost: " + itemShop.getCountPriceAsString(player));
        }

        if(this.isUpgradable()) {
            int tier = !hyriPlayer.hasUpgradeMaterial(this.getHyriMaterial()) ? 0 : hyriPlayer.getItemUpgradable(this.getHyriMaterial()).getNextTier();
            lore.add(ChatColor.GRAY + "Tier: " + ChatColor.YELLOW + InventoryBWUtils.getTierString(player, (isMaxed ? hyriPlayer.getItemUpgradable(this.getHyriMaterial()).getMaxTier() + 2 : tier + 1)));
        }
        if(!itemShop.getPrice().isEmpty())
            lore.add(ChatColor.GRAY + " ");

        if(this.getDescription() != null) {
            String[] description = this.getDescription().getForPlayer(player).split("\n");
            for(String desc : description){
                lore.add(ChatColor.GRAY + desc);
            }
            lore.add(ChatColor.GRAY + " ");
        }

        if(itemShop instanceof ArmorBW)
            if(hyriPlayer.getPermanentArmor() != null && ((ArmorBW) hyriPlayer.getPermanentArmor().getItemShop()).getLevel() >= ((ArmorBW)itemShop).getLevel()){
                lore.add(ChatColor.GREEN + "UNLOCKED !");
                lore.add(" ");
            }

        boolean hasItems = InventoryBWUtils.hasItems(player, itemShop.getPrice());

        if(isMaxed)
            lore.add(ChatColor.GREEN + "MAXED !");
        else {
            lore.add((hasItems ? ChatColor.YELLOW + HyriBedWars.getLanguageManager().getValue(player, "inv.shop.click.purchase") : ChatColor.RED + HyriBedWars.getLanguageManager().getValue(player, "inv.shop.enough.item") + " " + this.getPriceAsString(player)));
        }

        return new ItemBuilder(itemShop.getItemStack())
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

    public Consumer<InventoryClickEvent> getClick(HyriBedWars plugin, BWShopInventory inventory) {
        return event -> {
            final BWMaterial material = this.getHyriMaterial();

            // For Quick buy
            if(event.isShiftClick()){
                new BWChoiceSlotGUI(plugin, inventory.getPlayer().getPlayer(), material).open();
                return;
            }

            final Player owner = inventory.getPlayer().getPlayer();
            final ItemShop itemShop = material.isItemUpgradable() ?
                    inventory.getPlayer().getItemUpgradable(material) != null ?
                            material.getItemUpgradable().getNextTierItem() :
                            material.getItemUpgradable().getTierItem(0) :
                    material.getItemShop();
            final boolean isMaxed = itemShop.isUpgradable() &&
                    inventory.getPlayer().hasUpgradeMaterial(itemShop.getHyriMaterial()) &&
                    inventory.getPlayer().getItemUpgradable(itemShop.getHyriMaterial()).isMaxed();
            if(InventoryBWUtils.isFull(owner, itemShop.getItemStack())){
                owner.sendMessage("Your inventory is full !");
                return;
            }
            if (InventoryBWUtils.hasItems(owner, itemShop.getPrice()) && !isMaxed) {
                if (material.isArmor()) {
                    if (inventory.getPlayer().getPermanentArmor() != null && inventory.getPlayer().getPermanentArmor().getArmor().getLevel() >= ((ArmorBW) itemShop).getLevel()) {
                        owner.sendMessage("Vous ne pouvé plu prandre d'armor < a la votr !");
                        owner.playSound(owner.getLocation(), Sound.ENDERMAN_STARE, 0.8F, 0.1F);
                        return;
                    } else {
                        inventory.getPlayer().giveArmor(material);
                        inventory.getPlayer().setUpgradesTeam(EBWUpgrades.PROTECTION_ARMOR);
                        InventoryBWUtils.removeItems(owner, itemShop.getPrice());
                    }
                } else {
                    if (material.isItemUpgradable()) {
                        inventory.getPlayer().nextUpgradeItem(material);
                    } else {
                        if (material.getItemShop().isPermanent()) {
                            if (inventory.getPlayer().hasPermanentItem(material)) {
                                owner.sendMessage("Vous avez déjà cette item !");
                                return;
                            }
                            inventory.getPlayer().addPermanentItem(material);
                        }
                        if (material.isHyriItem()) {
                            plugin.getHyrame().getItemManager().giveItem(owner, material.getHyriItem());
                        } else {
                            if (material == BWMaterial.WOOL) {
                                InventoryBWUtils.addItem(owner, event.getHotbarButton(),
                                        new ItemBuilder(itemShop.getItemStack().getType(),
                                                itemShop.getItemStack().getAmount(),
                                                inventory.getPlayer().getTeam().getColor().getDyeColor().getWoolData()).build());
                            } else if ((material == BWMaterial.DIAMOND_SWORD || material == BWMaterial.IRON_SWORD || material == BWMaterial.STONE_SWORD) && InventoryBWUtils.hasItems(owner, new ItemStack(Material.WOOD_SWORD))) {
                                if (InventoryBWUtils.hasItems(owner, new ItemStack(Material.WOOD_SWORD))) {
                                    InventoryBWUtils.setItemsSlot(owner, slot -> material.getItemShop().getItemStack(),
                                            new ItemStack(Material.WOOD_SWORD));
                                }
                            } else {
                                InventoryBWUtils.addItem(owner, event.getHotbarButton(), new ItemStack(itemShop.getItemStack()));
                            }
                        }
                    }
                }
                owner.playSound(owner.getLocation(), Sound.NOTE_PLING, 0.8F, 2.0F);
                InventoryBWUtils.removeItems(owner, itemShop.getPrice());
            } else if (isMaxed) {
                owner.sendMessage("Vous avez tout upgrade !");
                owner.playSound(owner.getLocation(), Sound.ENDERMAN_TELEPORT, 0.8F, 0.1F);
            } else {
                owner.sendMessage("Vous n'avez pas ce qui faut !");
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

        if(!isMaxed && !itemShop.getPrice().isEmpty()) {
            lore.add(ChatColor.GRAY + "Cost: " + itemShop.getCountPriceAsString(player));
        }

        lore.add(ChatColor.GRAY + " ");

        if(this.getDescription() != null) {
            String[] description = this.getDescription().getForPlayer(player).split("\n");
            for(String desc : description){
                lore.add(ChatColor.GRAY + desc);
            }
            lore.add(ChatColor.GRAY + " ");
        }

        boolean hasItems = InventoryBWUtils.hasItems(player, itemShop.getPrice());

        lore.add(ChatColor.YELLOW + HyriBedWars.getLanguageManager().getValue(player, "inv.choice_slot.add"));

        return new ItemBuilder(itemShop.getItemStack())
                .withName((hasItems ? ChatColor.GREEN : ChatColor.RED) + itemShop.getName().getForPlayer(player))
                .withAllItemFlags().withLore(lore).build();
    }

    public ItemStack getItemToReplace(BWGamePlayer hyriPlayer){
        Player player = hyriPlayer.getPlayer();
        ItemShop itemShop = this.getItemToBuy(hyriPlayer);

        boolean hasItems = InventoryBWUtils.hasItems(player, itemShop.getPrice());

        return new ItemBuilder(itemShop.getItemStack())
                .withName((hasItems ? ChatColor.GREEN : ChatColor.RED) + itemShop.getName().getForPlayer(player))
                .withAllItemFlags()
                .withLore(ChatColor.YELLOW + HyriBedWars.getLanguageManager().getValue(player, "inv.choice_slot.replace")).build();
    }
}

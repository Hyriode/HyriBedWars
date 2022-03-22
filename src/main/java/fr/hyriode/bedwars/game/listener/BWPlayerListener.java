package fr.hyriode.bedwars.game.listener;

import fr.hyriode.bedwars.game.BWGameOre;
import fr.hyriode.bedwars.game.material.utility.PopupTowerSchematic;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import fr.hyriode.bedwars.utils.InventoryBWUtils;
import fr.hyriode.bedwars.utils.MetadataReferences;
import fr.hyriode.hyrame.game.HyriGamePlayer;
import fr.hyriode.hyrame.game.team.HyriGameTeam;
import fr.hyriode.hyrame.item.ItemNBT;
import fr.hyriode.hyrame.listener.HyriListener;
import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.BWGamePlayer;
import fr.hyriode.bedwars.game.material.BWMaterial;
import fr.hyriode.hyrame.utils.BroadcastUtil;
import net.minecraft.server.v1_8_R3.ItemArmor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BWPlayerListener extends HyriListener<HyriBedWars> {
    public BWPlayerListener(HyriBedWars plugin) {
        super(plugin);
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event){
        Item item = event.getItemDrop();

        if(item.getItemStack().getType() == Material.WOOD_SWORD){
            event.setCancelled(true);
            return;
        }

        for (BWMaterial material : BWMaterial.values()) {
            if(material.isItemUpgradable()){
                for(int i = 0 ; i < material.getItemUpgradable().getMaxTier(); ++i){
                    ItemStack itemUpgradable = material.getItemUpgradable().getTierItem(i).getItemStack();
                    if(item.getItemStack().getType() == itemUpgradable.getType()) {
                        event.setCancelled(true);
                        return;
                    }
                }
            }

            if(item.getItemStack().getType() == material.getItemShop().getItemStack().getType()){
                if(material.getItemShop().isPermanent()){
                    event.setCancelled(true);
                    return;
                }
                if(Arrays.asList(BWMaterial.getSwords()).contains(material)){
                    if(!InventoryBWUtils.hasItems(event.getPlayer(), Arrays.stream(BWMaterial.getSwords()).map(mat -> mat.getItemShop().getItemStack()).collect(Collectors.toList()), new ItemStack(Material.WOOD_SWORD))){
                        InventoryBWUtils.addItem(event.getPlayer(), 0, new ItemStack(Material.WOOD_SWORD));
                        return;
                    }
                }
            }
        }

        ItemNBT nbt = new ItemNBT(item.getItemStack());

        if(event.getItemDrop() instanceof ItemArmor){
            event.setCancelled(true);
        }

        if(nbt.hasTag(MetadataReferences.ISPERMANENT) && nbt.getBoolean(MetadataReferences.ISPERMANENT)){
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event){
        if(InventoryBWUtils.isItem(event.getItem().getItemStack(), Arrays.stream(BWMaterial.getSwords()).map(mat -> mat.getItemShop().getItemStack()).collect(Collectors.toList()).toArray(new ItemStack[0]))){
            if(InventoryBWUtils.hasItems(event.getPlayer(), new ItemStack(Material.WOOD_SWORD))){
//               InventoryBWUtils.addItem(event.getPlayer(), 0, event.getItem().getItemStack());
                InventoryBWUtils.setItemsSlot(event.getPlayer(), integer -> null, new ItemStack(Material.WOOD_SWORD));
                //TODO
            }
        }
    }

    @EventHandler
    public void onBreakBed(BlockBreakEvent event){
        if(event.getBlock().getType() == Material.BED_BLOCK) {
            for (HyriGameTeam hTeam : this.plugin.getGame().getTeams()) {
                BWGameTeam team = ((BWGameTeam) hTeam);
                if (team.getBaseArea().isInArea(event.getBlock().getLocation())) {
                    BWGamePlayer breaker = this.plugin.getGame().getPlayer(event.getPlayer().getUniqueId());
                    event.setCancelled(true);
                    if (breaker.getTeam() == team) {
                        event.getPlayer().sendMessage(HyriBedWars.getLanguageManager().getValue(event.getPlayer(), "team.cant-break.bed"));
                        return;
                    }
                    if(team.hasBed()) {
                        event.getBlock().setType(Material.AIR);
                        team.destroyBed(breaker);
                        this.plugin.getGame().getPlayers().forEach(player -> {
                            player.getScoreboard().update();
                            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENDERDRAGON_GROWL, 1.0F, 1.0F);
                        });
                    }
                    break;
                }
            }
            return;
        }
        if(!event.getBlock().hasMetadata(MetadataReferences.PLACEBYPLAYER)) {
            event.getPlayer().sendMessage(HyriBedWars.getLanguageManager().getValue(event.getPlayer(), "block.cant-break.this"));
            event.setCancelled(true);
        }
        for (HyriGameTeam team : this.plugin.getGame().getTeams()) {
            if (((BWGameTeam) team).getProtectArea().isInArea(event.getBlock().getLocation())) {
                event.setCancelled(true);
                break;
            }
        }
    }

    @EventHandler
    public void onInteract(InventoryClickEvent event){
        if(event.getSlotType() == InventoryType.SlotType.ARMOR){
            event.setCancelled(true);
        }

        if(InventoryBWUtils.hasItems((Player) event.getWhoClicked(), Arrays.stream(BWMaterial.getSwords())
                .map(material -> material.getItemShop().getItemStack()).collect(Collectors.toList()))){
            InventoryBWUtils.removeItem((Player) event.getWhoClicked(), new ItemStack(Material.WOOD_SWORD));
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event){
        if(!(event.getEntity() instanceof Player)) return;
        if(event.getCause() == EntityDamageEvent.DamageCause.VOID){
            event.setCancelled(true);

            BWGamePlayer victim = this.getPlayer((Player) event.getEntity());

            victim.kill();
            BroadcastUtil.broadcast(player -> victim.getTeam().getColor().getChatColor() + victim.getPlayer().getName() + ChatColor.GRAY + " fell into the void");

        }else if(((Player) event.getEntity()).getGameMode() == GameMode.ADVENTURE){
            event.setCancelled(true);
        }else if (((Player) event.getEntity()).getHealth() - event.getFinalDamage() <= 0D){
            event.setCancelled(true);
            BWGamePlayer victim = this.getPlayer((Player) event.getEntity());
            victim.kill();
        }
        if(event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION){
            event.getEntity().setVelocity(new Vector().setY(1.5));
        }

    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event){
        if(!(event.getEntity() instanceof Player)) return;
        if (((Player) event.getEntity()).getHealth() - event.getFinalDamage() <= 0D){
            event.setCancelled(true);
            final BWGamePlayer victim = this.getPlayer((Player) event.getEntity());

            if(event.getDamager() instanceof Player) {
                final HyriGamePlayer damager = this.getPlayer((Player) event.getDamager());
                final List<ItemStack> itemStacks = InventoryBWUtils.getItemsInventory(victim.getPlayer(), BWGameOre.GOLD.getItemStack(), BWGameOre.IRON.getItemStack(), BWGameOre.DIAMOND.getItemStack(), BWGameOre.EMERALD.getItemStack());

                for(ItemStack item : itemStacks){
                    damager.getPlayer().getInventory().addItem(item);
                }

                damager.getPlayer().updateInventory();
                if(event.getCause() == EntityDamageEvent.DamageCause.VOID) {
                    BroadcastUtil.broadcast(player -> victim.getTeam().getColor().getChatColor() + victim.getPlayer().getName() + ChatColor.GRAY + " was push into the void by " + damager.getTeam().getColor().getChatColor() + damager.getPlayer().getName());
                }else {
                    BroadcastUtil.broadcast(player -> victim.getTeam().getColor().getChatColor() + victim.getPlayer().getName() + ChatColor.GRAY + " was killed by " + damager.getTeam().getColor().getChatColor() + damager.getPlayer().getName());
                }
            }
            victim.kill();
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        event.getEntity().spigot().respawn();
        this.getPlayer(event.getEntity()).kill();
        event.setDeathMessage("");
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event){
        final Location loc = event.getEntity().getLocation();
        loc.getWorld().playEffect(loc, Effect.EXPLOSION_LARGE, 5);
        event.blockList().forEach(block -> {
            if(block.hasMetadata(MetadataReferences.PLACEBYPLAYER)){
               block.setType(Material.AIR);
            }
        });
    }

    @SuppressWarnings("ALL")
    @EventHandler
    public void onPlacedBlock(BlockPlaceEvent event){
        for (HyriGameTeam team : this.plugin.getGame().getTeams()) {
            if(((BWGameTeam) team).getProtectArea().isInArea(event.getBlock().getLocation())) {
                event.setCancelled(true);
                return;
            }
        }
        Player p = event.getPlayer();
        Block b = event.getBlock();

        if(b.getType() == Material.BED_BLOCK)
            event.setCancelled(true);

        if(b.getType() == Material.TNT){
            b.setType(Material.AIR);
            b.getWorld().spawn(event.getBlock().getLocation(), TNTPrimed.class);
            return;
        }
        if(b.getType() == Material.CHEST){
            b.breakNaturally(new ItemStack(Material.AIR));
            new PopupTowerSchematic(this.plugin, this.getPlayer(p), b.getLocation(), event.getItemInHand()).placeTower();
            return;
        }

        b.setMetadata(MetadataReferences.PLACEBYPLAYER, new FixedMetadataValue(this.plugin, true));
    }

    @EventHandler
    public void onProjectileHit(PlayerEggThrowEvent event){
        event.setHatching(false);
    }

    private BWGamePlayer getPlayer(Player player){
        return this.plugin.getGame().getPlayer(player.getUniqueId());
    }
}

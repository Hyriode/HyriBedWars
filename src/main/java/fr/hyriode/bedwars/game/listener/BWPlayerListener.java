package fr.hyriode.bedwars.game.listener;

import fr.hyriode.bedwars.game.material.utility.entity.BedBugEntity;
import fr.hyriode.bedwars.game.material.utility.entity.DreamDefenderEntity;
import fr.hyriode.bedwars.game.material.utility.popuptower.TowerEast;
import fr.hyriode.bedwars.game.material.utility.popuptower.TowerNorth;
import fr.hyriode.bedwars.game.material.utility.popuptower.TowerSouth;
import fr.hyriode.bedwars.game.material.utility.popuptower.TowerWest;
import fr.hyriode.bedwars.game.material.utility.sponge.SpongeAnimationTask;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import fr.hyriode.bedwars.game.team.upgrade.traps.BWTeamTraps;
import fr.hyriode.bedwars.utils.InventoryBWUtils;
import fr.hyriode.bedwars.utils.MetadataReferences;
import fr.hyriode.bedwars.utils.Utils;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.game.HyriGameState;
import fr.hyriode.hyrame.game.team.HyriGameTeam;
import fr.hyriode.hyrame.item.ItemNBT;
import fr.hyriode.hyrame.listener.HyriListener;
import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.BWGamePlayer;
import fr.hyriode.bedwars.game.material.BWMaterial;
import fr.hyriode.hyrame.utils.Area;
import net.minecraft.server.v1_8_R3.ItemArmor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftIronGolem;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftSilverfish;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
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
        Player player = event.getPlayer();

        if(event.getPlayer().getLocation().subtract(0, 1, 0).getBlock().getType() == Material.AIR)
            event.setCancelled(true);

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
        if(event.getItem().getItemStack().getType() == Material.BED)
            event.setCancelled(true);
        if(InventoryBWUtils.isItem(event.getItem().getItemStack(), Arrays.stream(BWMaterial.getSwords()).map(mat -> mat.getItemShop().getItemStack()).collect(Collectors.toList()).toArray(new ItemStack[0]))){
            if(InventoryBWUtils.hasItem(event.getPlayer(), new ItemStack(Material.WOOD_SWORD))){
                InventoryBWUtils.setItemsSlot(event.getPlayer(), slot -> null, new ItemStack(Material.WOOD_SWORD));
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
            event.getPlayer().sendMessage(ChatColor.RED + HyriBedWars.getLanguageManager().getValue(event.getPlayer(), "block.cant-break.this"));
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
    public void onCrafting(CraftItemEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onInteractInventory(InventoryClickEvent event){
        if(event.getSlotType() == InventoryType.SlotType.CRAFTING) {
            event.setCancelled(true);
        }

        if(event.getSlotType() == InventoryType.SlotType.ARMOR){
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageEvent event){
        if(!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();

        if(event.getCause() == EntityDamageEvent.DamageCause.VOID){
            event.setCancelled(true);
            player.teleport(this.plugin.getConfiguration().getKillLoc());
            return;
        }

        if(player.getGameMode() == GameMode.ADVENTURE){
            event.setCancelled(true);
        }

        if(event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION || event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
            event.setCancelled(true);
            event.setDamage(0);
            BWGamePlayer bwPlayer = this.getPlayer(player);
            if(!bwPlayer.isSpectator() && !bwPlayer.isDead())
                player.damage(2.0F);
        }
//        else if (((Player) event.getEntity()).getHealth() - event.getFinalDamage() <= 0D){
//            event.setCancelled(true);
//            BWGamePlayer victim = this.getPlayer((Player) event.getEntity());
//            victim.kill();
//        }

    }

//    @EventHandler
//    public void onDamageByEntity(EntityDamageByEntityEvent event){
//        if(!(event.getEntity() instanceof Player)) return;
//        if (((Player) event.getEntity()).getHealth() - event.getFinalDamage() <= 0D){
//            event.setCancelled(true);
//            final BWGamePlayer victim = this.getPlayer((Player) event.getEntity());
//
//            if(event.getDamager() instanceof Player) {
//                final HyriGamePlayer damager = this.getPlayer((Player) event.getDamager());
//                final List<ItemStack> itemStacks = InventoryBWUtils.getItemsInventory(victim.getPlayer(), BWGameOre.GOLD.getItemStack(), BWGameOre.IRON.getItemStack(), BWGameOre.DIAMOND.getItemStack(), BWGameOre.EMERALD.getItemStack());
//
//                for(ItemStack item : itemStacks){
//                    damager.getPlayer().getInventory().addItem(item);
//                }
//
//                damager.getPlayer().updateInventory();
//                if(event.getCause() == EntityDamageEvent.DamageCause.VOID) {
//                    BroadcastUtil.broadcast(player -> victim.getTeam().getColor().getChatColor() + victim.getPlayer().getName() + ChatColor.GRAY + " was push into the void by " + damager.getTeam().getColor().getChatColor() + damager.getPlayer().getName());
//                }else {
//                    BroadcastUtil.broadcast(player -> victim.getTeam().getColor().getChatColor() + victim.getPlayer().getName() + ChatColor.GRAY + " was killed by " + damager.getTeam().getColor().getChatColor() + damager.getPlayer().getName());
//                }
//            }
//            victim.kill();
//        }
//    }

    @EventHandler
    public void onRightClickBed(PlayerInteractEvent event){
        if(event.getItem() != null && event.getItem().getType() == Material.FIREBALL)
            event.setCancelled(true);

        if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.CHEST){
            BWGameTeam team = this.getPlayer(event.getPlayer()).getHyriTeam();
            this.plugin.getGame().getTeams().forEach(t -> {
                BWGameTeam hTeam = (BWGameTeam) t;
                if(hTeam.getName().equals(team.getName()))return;
                if(!hTeam.hasBed()) return;

                if(hTeam.getBaseArea().isInArea(event.getPlayer().getLocation())){
                    event.setCancelled(true);
                }
            });
        }

        if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.BED_BLOCK
                && !event.getPlayer().isSneaking() && event.getAction() == Action.RIGHT_CLICK_BLOCK)
            event.setCancelled(true);
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event){
        event.setCancelled(true);
        final Location loc = event.getEntity().getLocation();
        loc.getWorld().playEffect(loc, Effect.EXPLOSION_LARGE, 5);
        event.blockList().forEach(block -> {
            if(block.hasMetadata(MetadataReferences.PLACEBYPLAYER)){
                block.setType(Material.AIR);
            }
        });
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlacedBlock(BlockPlaceEvent event){
        if(!this.plugin.getConfiguration().getGameArea().isInArea(event.getBlockPlaced().getLocation())){
            event.setCancelled(true);
            return;
        }

        for (HyriGameTeam team : this.plugin.getGame().getTeams()) {
            if(((BWGameTeam) team).getProtectArea().isInArea(event.getBlock().getLocation())) {
                event.setCancelled(true);
                return;
            }
        }

        for (Location diamondLocation : this.plugin.getConfiguration().getDiamondLocations()) {
            if(new Area(diamondLocation, diamondLocation).isInRange(event.getBlockPlaced().getLocation(), 2)){
                event.setCancelled(true);
                return;
            }
        }

        for (Location emeraldLocation : this.plugin.getConfiguration().getEmeraldLocations()) {
            if(new Area(emeraldLocation, emeraldLocation).isInRange(event.getBlockPlaced().getLocation(), 2)){
                event.setCancelled(true);
                return;
            }
        }

        final Player player = event.getPlayer();
        final Block block = event.getBlock();

        if(block.getType() == Material.BED_BLOCK) {
            event.setCancelled(true);
            return;
        }

        if(block.getType() == Material.TNT){
            block.setType(Material.AIR);
            block.getWorld().spawn(event.getBlock().getLocation().add(0.5, 0, 0.5), TNTPrimed.class);
            return;
        }
        if(block.getType() == Material.CHEST){
            event.setCancelled(true);
            event.getItemInHand().setAmount(0);
            Location loc = event.getBlockPlaced().getLocation();
            Block chest = event.getBlockPlaced();
            int col = this.getPlayer(player).getTeam().getColor().getDyeColor().getWoolData();
            double rotation = ((player.getLocation().getYaw() - 90.0F) % 360.0F);
            if (rotation < 0.0D)
                rotation += 360.0D;
            if (45.0D <= rotation && rotation < 135.0D) {
                new TowerSouth(loc, chest, col, player, this.plugin);
            } else if (225.0D <= rotation && rotation < 315.0D) {
                new TowerNorth(loc, chest, col, player, this.plugin);
            } else if (135.0D <= rotation && rotation < 225.0D) {
                new TowerWest(loc, chest, col, player, this.plugin);
            } else if (0.0D <= rotation && rotation < 45.0D) {
                new TowerEast(loc, chest, col, player, this.plugin);
            } else if (315.0D <= rotation && rotation < 360.0D) {
                new TowerEast(loc, chest, col, player, this.plugin);
            }
            return;
        }

        if(block.getType() == Material.SPONGE){
            new SpongeAnimationTask(block).runTaskTimer(this.plugin, 0L, 8L);
            return;
        }

        block.setMetadata(MetadataReferences.PLACEBYPLAYER, new FixedMetadataValue(this.plugin, true));
    }

    @EventHandler
    public void onProjectileHit(PlayerEggThrowEvent event){
        event.setHatching(false);
    }

    @EventHandler
    public void onDeathEntity(EntityDeathEvent event){
        if(event.getEntity() instanceof CraftSilverfish){
            BedBugEntity bedBug = this.plugin.getGame().getBedBug(event.getEntity().getUniqueId());
            if(bedBug != null){
                this.plugin.getGame().removeBedBug(event.getEntity().getUniqueId());
                return;
            }
        }

        if(event.getEntity() instanceof CraftIronGolem){
            DreamDefenderEntity dreamDefender = this.plugin.getGame().getDreamDefender(event.getEntity().getUniqueId());
            if(dreamDefender != null){
                this.plugin.getGame().removeDreamDefender(event.getEntity().getUniqueId());
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event){
        BWGamePlayer damager = event.getDamager() instanceof Projectile ? this.getPlayer((Player) ((Projectile) event.getDamager()).getShooter()) : event.getDamager() instanceof Player ? this.getPlayer((Player) event.getDamager()) : null;

        if(damager == null) return;

        if(event.getEntity() instanceof CraftSilverfish){
            BedBugEntity bedBug = this.plugin.getGame().getBedBug(event.getEntity().getUniqueId());
            if(bedBug != null && bedBug.getTeam().getName().equals(damager.getHyriTeam().getName())){
                event.setCancelled(true);
                return;
            }
        }

        if(event.getEntity() instanceof CraftIronGolem){
            DreamDefenderEntity dreamDefender = this.plugin.getGame().getDreamDefender(event.getEntity().getUniqueId());
            if(dreamDefender != null && dreamDefender.getTeam().getName().equals(damager.getHyriTeam().getName())){
                event.setCancelled(true);
                return;
            }
        }

        if(!(event.getEntity() instanceof Player)) return;

        BWGamePlayer victim = this.getPlayer((Player) event.getEntity());
        if(victim.isCooldownRespawn()){
            event.setCancelled(true);
        }

        damager.setCooldownRespawn(false);

    }

    @EventHandler
    public void onConsumePotion(PlayerItemConsumeEvent event){
        final ItemStack item = event.getItem();
        final Player player = event.getPlayer();
        if(item == null) return;

        switch (item.getType()){
            case POTION:
                event.setCancelled(true);
                final PotionMeta meta = (PotionMeta) item.getItemMeta();

                player.getInventory().setItem(player.getInventory().getHeldItemSlot(), null);
                meta.getCustomEffects().forEach(potionEffect -> {
                    player.addPotionEffect(potionEffect);
                    if(potionEffect.getType().equals(PotionEffectType.INVISIBILITY)){
                        player.getPlayer().setMetadata(MetadataReferences.ISINVISIBLE, new FixedMetadataValue(this.plugin, true));
                        int duration = potionEffect.getDuration();
                        new BukkitRunnable(){
                            int i = 0;
                            @Override
                            public void run() {
                                if(!MetadataReferences.isInvisible(player)){
                                    this.cancel();
                                    return;
                                }

                                if(i > duration) {
                                    player.getPlayer().removeMetadata(MetadataReferences.ISINVISIBLE, plugin);
                                    this.cancel();
                                    return;
                                }
                                ++i;
                            }
                        }.runTaskTimer(this.plugin, 0, 1);
                    }
                });
                break;
            case MILK_BUCKET:
                event.setCancelled(true);
                BWGamePlayer hPlayer = this.getPlayer(player);
                if(!hPlayer.isCooldownTrap()){
                    hPlayer.setCooldownTrap(true);
                    Bukkit.getScheduler().runTaskLater(this.plugin, () -> hPlayer.setCooldownTrap(false), 20*30);
                }
                break;
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        if(this.plugin.getGame().getState() != HyriGameState.PLAYING) return;

        Player p = event.getPlayer();
        BWGamePlayer bwPlayer = this.getPlayer(p);

        if(MetadataReferences.isInvisible(p)){
            if(!bwPlayer.isCooldownInvisibilityParticle() && p.getLocation().subtract(0, 0.1, 0).getBlock().getType() != Material.AIR) {
                bwPlayer.setCooldownInvisibilityParticle(true);
                IHyrame.WORLD.get().playEffect(p.getLocation().add(0, 0.05, 0), Effect.FOOTSTEP, 2);
            }
        }
        if(event.getPlayer().getLocation().getY() <= this.plugin.getConfiguration().getCancelOpenInventoryY())
            p.closeInventory();

        for (HyriGameTeam team : this.plugin.getGame().getTeams()) {
            final BWGameTeam hTeam = (BWGameTeam) team;

            if(hTeam.getName().equals(this.getPlayer(event.getPlayer()).getHyriTeam().getName())) continue;

            if(!bwPlayer.isSpectator() && !bwPlayer.isDead() && hTeam.getBaseArea().isInArea(event.getPlayer().getLocation())){
                BWGamePlayer player = this.plugin.getGame().getPlayer(event.getPlayer());
                BWTeamTraps traps = hTeam.getTraps();
                if(traps.canTrap() && !player.isCooldownTrap()) {
                    traps.getFirstTrap().active(player, hTeam);
                    traps.removeTrap();
                    traps.setCanTrap(false);
                    Bukkit.getScheduler().runTaskLater(this.plugin, () -> traps.setCanTrap(true), 15*20);
                }
            }
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event){
        int slot = Utils.getFirstElementOfInt(event.getInventorySlots());
        if(event.getInventory().getType() == InventoryType.CRAFTING && slot >= 1 && slot <= 4){
            event.setCancelled(true);
        }
    }

    private BWGamePlayer getPlayer(Player player){
        return this.plugin.getGame().getPlayer(player.getUniqueId());
    }
}

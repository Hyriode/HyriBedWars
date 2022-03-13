package fr.hyriode.bedwars.game.listener;

import fr.hyriode.bedwars.game.BWGameOre;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import fr.hyriode.bedwars.utils.InventoryBWUtils;
import fr.hyriode.bedwars.utils.MetadataReferences;
import fr.hyriode.hyrame.game.HyriGamePlayer;
import fr.hyriode.hyrame.game.team.HyriGameTeam;
import fr.hyriode.hyrame.game.util.HyriDeadScreen;
import fr.hyriode.hyrame.item.ItemNBT;
import fr.hyriode.hyrame.listener.HyriListener;
import fr.hyriode.bedwars.HyriBedWars;
import fr.hyriode.bedwars.game.BWGamePlayer;
import fr.hyriode.bedwars.game.npc.inventory.shop.material.BWMaterial;
import fr.hyriode.hyrame.utils.BroadcastUtil;
import net.minecraft.server.v1_8_R3.ItemArmor;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

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

            if(item.getItemStack().getType() == material.getItemShop().getItemStack().getType() && material.getItemShop().isPermanent()){
                event.setCancelled(true);
                return;
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
    public void onBreakBed(BlockBreakEvent event){
        if(event.getBlock().getType() == Material.BED_BLOCK) {
            for (HyriGameTeam team : this.plugin.getGame().getTeams()) {
                if (((BWGameTeam) team).getBaseArea().isInArea(event.getBlock().getLocation())) {
                    BWGamePlayer breaker = this.plugin.getGame().getPlayer(event.getPlayer().getUniqueId());
                    event.setCancelled(true);
                    if (breaker.getTeam() == team) {
                        event.getPlayer().sendMessage("You cant break your bed !");
                        return;
                    }
                    if(((BWGameTeam) team).hasBed()) {
                        event.getBlock().setType(Material.AIR);
                        ((BWGameTeam) team).setHasBed(false);
                        BroadcastUtil.broadcast(player -> "  ");
                        BroadcastUtil.broadcast(player -> ChatColor.BOLD + "BED DESTRUCTION > " + ChatColor.RESET + team.getColor().getChatColor() + team.getDisplayName().getForPlayer(player) + " Bed" + ChatColor.GRAY + " was destroyed by " + breaker.getTeam().getColor().getChatColor() + breaker.getPlayer().getName());
                        BroadcastUtil.broadcast(player -> "  ");
                        team.sendTitle(player -> ChatColor.RED + "BED DESTROYED!", player -> "You will no longer respawn!", 10, 3 * 20, 10);
                        this.plugin.getGame().getPlayers().forEach(player -> player.getScoreboard().update());
                    }
                    break;
                }
            }
            return;
        }
        if(!event.getBlock().hasMetadata(MetadataReferences.PLACEBYPLAYER)) {
            event.getPlayer().sendMessage("You can't break this block");
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
    public void onPlaceBlockInProtectBase(BlockPlaceEvent event){
        for (HyriGameTeam team : this.plugin.getGame().getTeams()) {
            if(((BWGameTeam) team).getProtectArea().isInArea(event.getBlock().getLocation())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInteract(InventoryClickEvent event){
        if(event.getSlotType() == InventoryType.SlotType.ARMOR){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event){
        if(!(event.getEntity() instanceof Player)) return;

        if(event.getCause() == EntityDamageEvent.DamageCause.VOID){
            event.setCancelled(true);
            BWGamePlayer victim = this.getPlayer((Player) event.getEntity());
            this.kill(victim);
            BroadcastUtil.broadcast(player -> victim.getTeam().getColor().getChatColor() + victim.getPlayer().getName() + ChatColor.GRAY + " fell into the void");
        }else if(((Player) event.getEntity()).getGameMode() == GameMode.ADVENTURE){
            event.setCancelled(true);
        }else if (((Player) event.getEntity()).getHealth() - event.getFinalDamage() < 0D){
            event.setCancelled(true);
            BWGamePlayer victim = this.getPlayer((Player) event.getEntity());
            this.kill(victim);
        }

    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event){
        if(!(event.getEntity() instanceof Player)) return;
        if (((Player) event.getEntity()).getHealth() - event.getFinalDamage() <= 0D){
            event.setCancelled(true);
            BWGamePlayer victim = this.getPlayer((Player) event.getEntity());

            if(event.getDamager() instanceof Player) {
                HyriGamePlayer damager = this.getPlayer((Player) event.getDamager());
                List<ItemStack> itemStacks = InventoryBWUtils.getItemsInventory(victim.getPlayer(), BWGameOre.GOLD.getItemStack(), BWGameOre.IRON.getItemStack(), BWGameOre.DIAMOND.getItemStack(), BWGameOre.EMERALD.getItemStack());
                for(ItemStack item : itemStacks){
                    damager.getPlayer().getInventory().addItem(item);
                }
                damager.getPlayer().updateInventory();
                if(event.getCause() == EntityDamageEvent.DamageCause.VOID)
                    BroadcastUtil.broadcast(player -> victim.getTeam().getColor().getChatColor() + victim.getPlayer().getName() + ChatColor.GRAY + " was push into the void by " + damager.getTeam().getColor().getChatColor() + damager.getPlayer().getName());
                else
                    BroadcastUtil.broadcast(player -> victim.getTeam().getColor().getChatColor() + victim.getPlayer().getName() + ChatColor.GRAY + " was killed by " + damager.getTeam().getColor().getChatColor() + damager.getPlayer().getName());
            }
            this.kill(victim);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        event.setDeathMessage("");
    }

    @EventHandler
    public void onExplode(BlockExplodeEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onExplode(ProjectileHitEvent event){
//        System.out.println("EXPLODE");
//        if(event.getEntityType() == EntityType.FIREBALL){
//            Fireball fb = (Fireball) event.getEntity();
//
//            event.getEntity().getLocation().getWorld().createExplosion(event.getEntity().getLocation(), 2, false);
//
//        }
//        if(!Arrays.stream(BWMaterial.values()).map(bwMaterial -> bwMaterial.getItemShop().getItem().getMaterial().getType())
//                .collect(Collectors.toList()).contains(event.getBlock().getType())){
//            event.setCancelled(true);
//        }
    }

    @EventHandler
    public void onPlacedBlock(BlockPlaceEvent event){
        Block b = event.getBlock();
        if(b.getType() == Material.TNT){
            b.setType(Material.AIR);
            b.getWorld().spawn(event.getBlock().getLocation(), TNTPrimed.class);
            return;
        }

        b.setMetadata(MetadataReferences.PLACEBYPLAYER, new FixedMetadataValue(this.plugin, true));
    }

    private void kill(BWGamePlayer player){
        Player victim = player.getPlayer();
        player.setDead(true);
        player.setSpectator(true);
        player.downItemsUpgradable();
        victim.teleport(this.plugin.getConfiguration().getKillLoc());
        victim.setAllowFlight(true);
        victim.setFlying(true);
        victim.setHealth(20.0F);
        victim.getInventory().clear();
        victim.getInventory().setArmorContents(null);
        victim.setGameMode(GameMode.ADVENTURE);
        victim.getActivePotionEffects().forEach(potionEffect -> victim.removePotionEffect(potionEffect.getType()));
        victim.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20*9999, 0, true, false));

        if(this.getPlayer(victim).getHyriTeam().hasBed()) {
            HyriDeadScreen.create(this.plugin, victim, 5, () -> {
                player.setDead(false);
                player.setSpectator(false);
                victim.setFallDistance(0);
                victim.setFlying(false);
                victim.setAllowFlight(false);
                victim.setGameMode(GameMode.SURVIVAL);
                victim.getActivePotionEffects().forEach(potionEffect -> victim.removePotionEffect(potionEffect.getType()));
                victim.setHealth(20.0F);
                victim.teleport(this.plugin.getConfiguration().getTeam(this.getPlayer(victim).getTeam().getName()).getRespawnLocation());
                this.getPlayer(victim).respawn();
            });
        }else{
            this.getPlayer(victim).setEliminated(true);
            if(((BWGameTeam)this.getPlayer(victim).getTeam()).isEliminated()){
                this.plugin.getGame().win();
            }
            this.plugin.getGame().getPlayers().forEach(player1 -> player1.getScoreboard().update());
        }

    }

    private BWGamePlayer getPlayer(Player player){
        return this.plugin.getGame().getPlayer(player.getUniqueId());
    }
}

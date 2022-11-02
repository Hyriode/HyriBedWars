package fr.hyriode.bedwars.game.upgrade;

import fr.hyriode.bedwars.game.player.BWGamePlayer;
import fr.hyriode.bedwars.game.shop.ItemPrice;
import fr.hyriode.bedwars.game.team.BWGameTeam;
import fr.hyriode.bedwars.game.team.upgrade.UpgradeTeam;
import fr.hyriode.bedwars.utils.SoundUtils;
import fr.hyriode.bedwars.utils.StringUtils;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.api.language.HyriLanguageMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class Upgrade {

    private final String name;
    private final boolean player;
    private final BiConsumer<BWGamePlayer, Integer> action;
    private final List<Tier> tiers;

    public Upgrade(String name, boolean player, BiConsumer<BWGamePlayer, Integer> action, List<Tier> tiers) {
        this.name = name;
        this.player = player;
        this.action = action;
        this.tiers = tiers;
        this.tiers.forEach(tier -> {
            int tierNumber = tier.getTier() + 1;
            tier.setName("upgrade." + this.name + ".tier-" + tierNumber + ".name");
        });
    }

    public String getName() {
        return name;
    }

    public String getDisplayDescription(Player player) {
        return HyriLanguageMessage.get("upgrade." + this.name + ".lore").getValue(player);
    }

    public String getDisplayName(Player player) {
       return HyriLanguageMessage.get("upgrade." + this.name + ".name").getValue(player);
    }

    public List<Tier> getTiers() {
        return tiers;
    }

    public boolean isForPlayer() {
        return player;
    }

    public Tier getTier(int tier){
        if(tier > this.getMaxTier())
            tier = this.getMaxTier();
        return this.tiers.get(tier);
    }

    public void addTier(Tier tier){
        this.tiers.add(tier);
    }

    public void upgrade(BWGamePlayer player, Tier tier){
        this.upgrade(player, tier, true);
    }

    public void upgrade(BWGamePlayer player, Tier tier, boolean nextUpgrade) {
        BWGameTeam team = player.getBWTeam();
        UpgradeTeam upgradeTeam = team.getUpgradeTeam();

        if(this.player && nextUpgrade) {
            for (BWGamePlayer bwPlayer : team.getBWPlayers()) {
                action.accept(bwPlayer, tier.getTier());
            }
        }else {
            action.accept(player, tier.getTier());
        }

        if(nextUpgrade) {
            team.getPlayers().forEach(p -> {
                Player pl = p.getPlayer();
                SoundUtils.playBuy(pl);
                p.sendMessage(ChatColor.GREEN + HyriLanguageMessage.get("shop.purchased.team").getValue(pl)
                        .replace("%item%", ChatColor.GOLD + this.getDisplayName(pl) + (tier.getDisplayName() != null ? " (" + tier.getDisplayName().getValue(pl) + ")" : ""))
                        .replace("%player%", pl.getName()));
            });

            if (upgradeTeam.hasUpgrade(this.name)) {
                UpgradeTeam.Upgrade upgrade = upgradeTeam.getUpgradeByName(this.name);
                upgrade.addTier();
                return;
            }
            upgradeTeam.addUpgrade(this.name);
        }
    }

    public int getMaxTier() {
        return this.tiers.size() - 1;
    }

    public ItemStack getIconForUpgrade(Player player, UpgradeTeam upgradeTeam){
        Tier nextTier = this.getTier(upgradeTeam.getNextTier(this.name));
        Tier currentTier = this.getTier(upgradeTeam.getTier(this.name));
        ItemPrice itemPrice = nextTier.getPrice();
        boolean hasPrice = itemPrice.hasPrice(player);
        boolean unlocked = upgradeTeam.hasUpgrade(this.name) && upgradeTeam.getTier(this.name) >= this.getMaxTier();

        ItemBuilder itemBuilder = new ItemBuilder(nextTier.getIcon());
        List<String> lore = new ArrayList<>(StringUtils.loreToList(this.getDisplayDescription(player)));

        lore.add(" ");

        if(this.getMaxTier() == 0) {
            lore.add(itemPrice.getDisplayCostPrice(player));
        } else {
            this.tiers.forEach(tier -> {
                boolean hasTier = upgradeTeam.hasUpgrade(this.name) && currentTier.getTier() >= tier.getTier();
                lore.add((hasTier ? ChatColor.GREEN : ChatColor.GRAY) + "Tier " + (tier.getTier() + 1) + ": " +
                        tier.getDisplayName().getValue(player) + ", " + tier.getPrice().getDisplayPrice(player));
            });
        }
        if(unlocked) {
            lore.add(" ");
            lore.add(ChatColor.GREEN + "UNLOCKED");
            itemBuilder.withGlow();
        }

        return itemBuilder
                .withName(StringUtils.getTitleBuy(unlocked, hasPrice) + this.getDisplayName(player))
                .withLore(lore)
                .withAllItemFlags()
                .build();
    }

    public static class Tier {
        private String name;
        private final ItemStack icon;
        private final ItemPrice price;
        private final int tier;

        public Tier(int tier, ItemStack itemStack, ItemPrice price) {
            this.icon = itemStack;
            this.price = price;
            this.tier = tier;
        }

        public ItemStack getIcon() {
            return icon;
        }

        public Tier setName(String name) {
            this.name = name;
            return this;
        }

        public String getName() {
            return name;
        }

        public HyriLanguageMessage getDisplayName(){
            return HyriLanguageMessage.get(this.name);
        }

        public ItemPrice getPrice() {
            return price;
        }

        public int getTier() {
            return tier;
        }
    }

}

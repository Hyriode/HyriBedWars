package fr.hyriode.bedwars.utils;

import fr.hyriode.hyrame.item.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

import java.util.Collections;

public class ItemPotionBuilder extends ItemBuilder {

    private final PotionType type;
    private final int duration;
    private final int amplifier;

    private boolean isSplash = false;

    public ItemPotionBuilder(PotionType type, int duration, int amplifier) {
        super(Material.POTION);
        this.type = type;
        this.duration = duration;
        this.amplifier = amplifier;
    }

    public ItemPotionBuilder withSplash(){
        this.isSplash = true;
        return this;
    }

    @SuppressWarnings("deprecation")
    @Override
    public ItemStack build() {
        ItemStack itemStack = super.build();
        PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
        potionMeta.addCustomEffect(new PotionEffect(this.type.getEffectType(), duration, amplifier, true), true);
        if(amplifier > 2) {
            potionMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            if(this.type == PotionType.JUMP)
                potionMeta.setLore(Collections.singletonList(ChatColor.GRAY + "Jump Boost" + " " + StringUtils.getLevelLang(amplifier + 1) + " (" + StringUtils.formatTime(duration/20) + ")"));
        }
        itemStack.setItemMeta(potionMeta);
        System.out.println(((PotionMeta) itemStack.getItemMeta()).getCustomEffects());
        if(this.isSplash){
            itemStack.setDurability((short) ((short) this.type.getDamageValue() | 16384));
        }else itemStack.setDurability((short) this.type.getDamageValue());

        return itemStack;
    }
}

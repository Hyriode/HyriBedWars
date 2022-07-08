package fr.hyriode.bedwars.utils;

import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class NBTGetter {

    private final NBTTagCompound nbtTagCompound;
    private final net.minecraft.server.v1_8_R3.ItemStack nmsItemStack;

    public NBTGetter(ItemStack itemStack) {
        this.nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        this.nbtTagCompound = this.nmsItemStack.hasTag() ? this.nmsItemStack.getTag() : new NBTTagCompound();
    }

    public List<String> getTags(){
        return new ArrayList<>(this.nbtTagCompound.c());
    }

    public Map<String, NBTBase> getNBTMap(){
        Map<String, NBTBase> map = new HashMap<>();
        for (String tag : this.getTags()) {
            map.put(tag, this.nbtTagCompound.get(tag));
        }
        return map;
    }

}

package fr.hyriode.bedwars.game.generator.item;

import fr.hyriode.hyrame.generator.IHyriGeneratorTier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Function;

public class BWItemGenerator implements IHyriGeneratorTier {

    private final ItemStack itemStack;
    private final int spawnLimit;
    private final long timeBetweenSpawns;

    public BWItemGenerator(ItemStack itemStack, int spawnLimit, long timeBetweenSpawns){
        this.itemStack = itemStack;
        this.spawnLimit = spawnLimit;
        this.timeBetweenSpawns = timeBetweenSpawns;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public Function<Player, String> getName() {
        return null;
    }

    @Override
    public int getSpawnLimit() {
        return spawnLimit;
    }

    @Override
    public long getTimeBetweenSpawns() {
        return timeBetweenSpawns;
    }

    @Override
    public boolean isSplitting() {
        return false;
    }
}

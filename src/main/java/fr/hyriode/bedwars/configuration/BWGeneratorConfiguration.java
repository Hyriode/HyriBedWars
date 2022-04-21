package fr.hyriode.bedwars.configuration;

import fr.hyriode.bedwars.HyriBedWars;
import static fr.hyriode.hyrame.configuration.HyriConfigurationEntry.*;
import fr.hyriode.hyrame.configuration.IHyriConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class BWGeneratorConfiguration implements IHyriConfiguration {

    private int ironGeneratorSpawnLimitTier1;
    private IntegerEntry ironGeneratorSpawnLimitTier1Entry;
    private int ironGeneratorTimeBetweenSpawnsTier1;
    private IntegerEntry ironGeneratorTimeBetweenSpawnsTier1Entry;

    private int ironGeneratorSpawnLimitTier2;
    private IntegerEntry ironGeneratorSpawnLimitTier2Entry;
    private int ironGeneratorTimeBetweenSpawnsTier2;
    private IntegerEntry ironGeneratorTimeBetweenSpawnsTier2Entry;

    private int ironGeneratorSpawnLimitTier3;
    private IntegerEntry ironGeneratorSpawnLimitTier3Entry;
    private int ironGeneratorTimeBetweenSpawnsTier3;
    private IntegerEntry ironGeneratorTimeBetweenSpawnsTier3Entry;

    private int ironGeneratorSpawnLimitTier4;
    private IntegerEntry ironGeneratorSpawnLimitTier4Entry;
    private int ironGeneratorTimeBetweenSpawnsTier4;
    private IntegerEntry ironGeneratorTimeBetweenSpawnsTier4Entry;

    private int goldGeneratorSpawnLimitTier1;
    private IntegerEntry goldGeneratorSpawnLimitTier1Entry;
    private int goldGeneratorTimeBetweenSpawnsTier1;
    private IntegerEntry goldGeneratorTimeBetweenSpawnsTier1Entry;

    private int goldGeneratorSpawnLimitTier2;
    private IntegerEntry goldGeneratorSpawnLimitTier2Entry;
    private int goldGeneratorTimeBetweenSpawnsTier2;
    private IntegerEntry goldGeneratorTimeBetweenSpawnsTier2Entry;

    private int goldGeneratorSpawnLimitTier3;
    private IntegerEntry goldGeneratorSpawnLimitTier3Entry;
    private int goldGeneratorTimeBetweenSpawnsTier3;
    private IntegerEntry goldGeneratorTimeBetweenSpawnsTier3Entry;

    private int goldGeneratorSpawnLimitTier4;
    private IntegerEntry goldGeneratorSpawnLimitTier4Entry;
    private int goldGeneratorTimeBetweenSpawnsTier4;
    private IntegerEntry goldGeneratorTimeBetweenSpawnsTier4Entry;

    private final HyriBedWars plugin;
    private FileConfiguration config;
    private final File dir;

    public BWGeneratorConfiguration(HyriBedWars plugin){
        this.plugin = plugin;
        this.dir = new File(plugin.getDataFolder().getPath(), "generators.yml");

        if(!dir.exists()){
            try {
                if(!dir.createNewFile()){
                    plugin.getLogger().log(Level.SEVERE, "Could not create generators file");
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.config = YamlConfiguration.loadConfiguration(dir);
        this.config.options().copyDefaults(true);

        String base = "base.";

        String iron = base + "iron.";

        String ironTier1 = iron + "tier1.";
        this.ironGeneratorSpawnLimitTier1 = 48;
        this.ironGeneratorSpawnLimitTier1Entry = new IntegerEntry(ironTier1 + "spawnLimit", this.config);
        this.ironGeneratorTimeBetweenSpawnsTier1 = 30;
        this.ironGeneratorTimeBetweenSpawnsTier1Entry = new IntegerEntry(ironTier1 + "timeBetweenSpawns", this.config);

        String ironTier2 = iron + "tier2.";
        this.ironGeneratorSpawnLimitTier2 = 48;
        this.ironGeneratorSpawnLimitTier2Entry = new IntegerEntry(ironTier2 + "spawnLimit", this.config);
        this.ironGeneratorTimeBetweenSpawnsTier2 = 25;
        this.ironGeneratorTimeBetweenSpawnsTier2Entry = new IntegerEntry(ironTier2 + "timeBetweenSpawns", this.config);

        String ironTier3 = iron + "tier3.";
        this.ironGeneratorSpawnLimitTier3 = 48;
        this.ironGeneratorSpawnLimitTier3Entry = new IntegerEntry(ironTier3 + "spawnLimit", this.config);
        this.ironGeneratorTimeBetweenSpawnsTier3 = 20;
        this.ironGeneratorTimeBetweenSpawnsTier3Entry = new IntegerEntry(ironTier3 + "timeBetweenSpawns", this.config);

        String ironTier4 = iron + "tier4.";
        this.ironGeneratorSpawnLimitTier4 = 48;
        this.ironGeneratorSpawnLimitTier4Entry = new IntegerEntry(ironTier4 + "spawnLimit", this.config);
        this.ironGeneratorTimeBetweenSpawnsTier4 = 15;
        this.ironGeneratorTimeBetweenSpawnsTier4Entry = new IntegerEntry(ironTier4 + "timeBetweenSpawns", this.config);

        String gold = base + "gold.";

        String goldTier1 = gold + "tier1.";
        this.goldGeneratorSpawnLimitTier1 = 48;
        this.goldGeneratorSpawnLimitTier1Entry = new IntegerEntry(goldTier1 + "spawnLimit", this.config);
        this.goldGeneratorTimeBetweenSpawnsTier1 = 80;
        this.goldGeneratorTimeBetweenSpawnsTier1Entry = new IntegerEntry(goldTier1 + "timeBetweenSpawns", this.config);

        String goldTier2 = gold + "tier2.";
        this.goldGeneratorSpawnLimitTier2 = 48;
        this.goldGeneratorSpawnLimitTier2Entry = new IntegerEntry(goldTier2 + "spawnLimit", this.config);
        this.goldGeneratorTimeBetweenSpawnsTier2 = 80;
        this.goldGeneratorTimeBetweenSpawnsTier2Entry = new IntegerEntry(goldTier2 + "timeBetweenSpawns", this.config);

        String goldTier3 = gold + "tier3.";
        this.goldGeneratorSpawnLimitTier3 = 48;
        this.goldGeneratorSpawnLimitTier3Entry = new IntegerEntry(goldTier3 + "spawnLimit", this.config);
        this.goldGeneratorTimeBetweenSpawnsTier3 = 80;
        this.goldGeneratorTimeBetweenSpawnsTier3Entry = new IntegerEntry(goldTier3 + "timeBetweenSpawns", this.config);

        String goldTier4 = gold + "tier4.";
        this.goldGeneratorSpawnLimitTier4 = 48;
        this.goldGeneratorSpawnLimitTier4Entry = new IntegerEntry(goldTier4 + "spawnLimit", this.config);
        this.goldGeneratorTimeBetweenSpawnsTier4 = 80;
        this.goldGeneratorTimeBetweenSpawnsTier4Entry = new IntegerEntry(goldTier4 + "timeBetweenSpawns", this.config);
    }

    @Override
    public void create() {
        this.ironGeneratorSpawnLimitTier1Entry.setDefault(this.ironGeneratorSpawnLimitTier1);
        this.ironGeneratorTimeBetweenSpawnsTier1Entry.setDefault(this.ironGeneratorTimeBetweenSpawnsTier1);

        this.ironGeneratorSpawnLimitTier2Entry.setDefault(this.ironGeneratorSpawnLimitTier2);
        this.ironGeneratorTimeBetweenSpawnsTier2Entry.setDefault(this.ironGeneratorTimeBetweenSpawnsTier2);

        this.ironGeneratorSpawnLimitTier3Entry.setDefault(this.ironGeneratorSpawnLimitTier3);
        this.ironGeneratorTimeBetweenSpawnsTier3Entry.setDefault(this.ironGeneratorTimeBetweenSpawnsTier3);

        this.ironGeneratorSpawnLimitTier4Entry.setDefault(this.ironGeneratorSpawnLimitTier4);
        this.ironGeneratorTimeBetweenSpawnsTier4Entry.setDefault(this.ironGeneratorTimeBetweenSpawnsTier4);

        this.goldGeneratorSpawnLimitTier1Entry.setDefault(this.goldGeneratorSpawnLimitTier1);
        this.goldGeneratorTimeBetweenSpawnsTier1Entry.setDefault(this.goldGeneratorTimeBetweenSpawnsTier1);

        this.goldGeneratorSpawnLimitTier2Entry.setDefault(this.goldGeneratorSpawnLimitTier2);
        this.goldGeneratorTimeBetweenSpawnsTier2Entry.setDefault(this.goldGeneratorTimeBetweenSpawnsTier2);

        this.goldGeneratorSpawnLimitTier3Entry.setDefault(this.goldGeneratorSpawnLimitTier3);
        this.goldGeneratorTimeBetweenSpawnsTier3Entry.setDefault(this.goldGeneratorTimeBetweenSpawnsTier3);

        this.goldGeneratorSpawnLimitTier4Entry.setDefault(this.goldGeneratorSpawnLimitTier4);
        this.goldGeneratorTimeBetweenSpawnsTier4Entry.setDefault(this.goldGeneratorTimeBetweenSpawnsTier4);

        this.saveConfig();
    }

    @Override
    public void load() {
        this.ironGeneratorSpawnLimitTier1 = this.ironGeneratorSpawnLimitTier1Entry.get();
        this.ironGeneratorTimeBetweenSpawnsTier1 = this.ironGeneratorTimeBetweenSpawnsTier1Entry.get();

        this.ironGeneratorSpawnLimitTier2 = this.ironGeneratorSpawnLimitTier2Entry.get();
        this.ironGeneratorTimeBetweenSpawnsTier2 = this.ironGeneratorTimeBetweenSpawnsTier2Entry.get();

        this.ironGeneratorSpawnLimitTier3 = this.ironGeneratorSpawnLimitTier3Entry.get();
        this.ironGeneratorTimeBetweenSpawnsTier3 = this.ironGeneratorTimeBetweenSpawnsTier3Entry.get();

        this.ironGeneratorSpawnLimitTier4 = this.ironGeneratorSpawnLimitTier4Entry.get();
        this.ironGeneratorTimeBetweenSpawnsTier4 = this.ironGeneratorTimeBetweenSpawnsTier4Entry.get();

        this.goldGeneratorSpawnLimitTier1 = this.goldGeneratorSpawnLimitTier1Entry.get();
        this.goldGeneratorTimeBetweenSpawnsTier1 = this.goldGeneratorTimeBetweenSpawnsTier1Entry.get();

        this.goldGeneratorSpawnLimitTier2 = this.goldGeneratorSpawnLimitTier2Entry.get();
        this.goldGeneratorTimeBetweenSpawnsTier2 = this.goldGeneratorTimeBetweenSpawnsTier2Entry.get();

        this.goldGeneratorSpawnLimitTier3 = this.goldGeneratorSpawnLimitTier3Entry.get();
        this.goldGeneratorTimeBetweenSpawnsTier3 = this.goldGeneratorTimeBetweenSpawnsTier3Entry.get();

        this.goldGeneratorSpawnLimitTier4 = this.goldGeneratorSpawnLimitTier4Entry.get();
        this.goldGeneratorTimeBetweenSpawnsTier4 = this.goldGeneratorTimeBetweenSpawnsTier4Entry.get();
    }

    @Override
    public void save() {
        this.ironGeneratorSpawnLimitTier1Entry.set(this.ironGeneratorSpawnLimitTier1);
        this.ironGeneratorTimeBetweenSpawnsTier1Entry.set(this.ironGeneratorTimeBetweenSpawnsTier1);

        this.ironGeneratorSpawnLimitTier2Entry.set(this.ironGeneratorSpawnLimitTier2);
        this.ironGeneratorTimeBetweenSpawnsTier2Entry.set(this.ironGeneratorTimeBetweenSpawnsTier2);

        this.ironGeneratorSpawnLimitTier3Entry.set(this.ironGeneratorSpawnLimitTier3);
        this.ironGeneratorTimeBetweenSpawnsTier3Entry.set(this.ironGeneratorTimeBetweenSpawnsTier3);

        this.ironGeneratorSpawnLimitTier4Entry.set(this.ironGeneratorSpawnLimitTier4);
        this.ironGeneratorTimeBetweenSpawnsTier4Entry.set(this.ironGeneratorTimeBetweenSpawnsTier4);

        this.goldGeneratorSpawnLimitTier1Entry.set(this.goldGeneratorSpawnLimitTier1);
        this.goldGeneratorTimeBetweenSpawnsTier1Entry.set(this.goldGeneratorTimeBetweenSpawnsTier1);

        this.goldGeneratorSpawnLimitTier2Entry.set(this.goldGeneratorSpawnLimitTier2);
        this.goldGeneratorTimeBetweenSpawnsTier2Entry.set(this.goldGeneratorTimeBetweenSpawnsTier2);

        this.goldGeneratorSpawnLimitTier3Entry.set(this.goldGeneratorSpawnLimitTier3);
        this.goldGeneratorTimeBetweenSpawnsTier3Entry.set(this.goldGeneratorTimeBetweenSpawnsTier3);

        this.goldGeneratorSpawnLimitTier4Entry.set(this.goldGeneratorSpawnLimitTier4);
        this.goldGeneratorTimeBetweenSpawnsTier4Entry.set(this.goldGeneratorTimeBetweenSpawnsTier4);

        this.saveConfig();
    }

    @Override
    public FileConfiguration getConfig() {
        return this.config;
    }

    public int getIronGeneratorSpawnLimitTier1() {
        return ironGeneratorSpawnLimitTier1;
    }

    public int getIronGeneratorTimeBetweenSpawnsTier1() {
        return ironGeneratorTimeBetweenSpawnsTier1;
    }

    public int getIronGeneratorSpawnLimitTier2() {
        return ironGeneratorSpawnLimitTier2;
    }

    public int getIronGeneratorTimeBetweenSpawnsTier2() {
        return ironGeneratorTimeBetweenSpawnsTier2;
    }

    public int getIronGeneratorSpawnLimitTier3() {
        return ironGeneratorSpawnLimitTier3;
    }

    public int getIronGeneratorTimeBetweenSpawnsTier3() {
        return ironGeneratorTimeBetweenSpawnsTier3;
    }

    public int getIronGeneratorSpawnLimitTier4() {
        return ironGeneratorSpawnLimitTier4;
    }

    public int getIronGeneratorTimeBetweenSpawnsTier4() {
        return ironGeneratorTimeBetweenSpawnsTier4;
    }

    public int getGoldGeneratorSpawnLimitTier1() {
        return goldGeneratorSpawnLimitTier1;
    }

    public int getGoldGeneratorTimeBetweenSpawnsTier1() {
        return goldGeneratorTimeBetweenSpawnsTier1;
    }

    public int getGoldGeneratorSpawnLimitTier2() {
        return goldGeneratorSpawnLimitTier2;
    }

    public int getGoldGeneratorTimeBetweenSpawnsTier2() {
        return goldGeneratorTimeBetweenSpawnsTier2;
    }

    public int getGoldGeneratorSpawnLimitTier3() {
        return goldGeneratorSpawnLimitTier3;
    }

    public int getGoldGeneratorTimeBetweenSpawnsTier3() {
        return goldGeneratorTimeBetweenSpawnsTier3;
    }

    public int getGoldGeneratorSpawnLimitTier4() {
        return goldGeneratorSpawnLimitTier4;
    }

    public int getGoldGeneratorTimeBetweenSpawnsTier4() {
        return goldGeneratorTimeBetweenSpawnsTier4;
    }

    private void saveConfig(){
        try {
            this.config.save(dir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

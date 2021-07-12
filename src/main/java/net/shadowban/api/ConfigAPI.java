package net.shadowban.api;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.shadowban.ShadowBan;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ConfigAPI {

    public static File file = new File(ShadowBan.getInstance().getDataFolder().getPath(), "config.yml");
    public static Configuration configuration;

    public static void loadConfig() {
        try {
            if (!ShadowBan.getInstance().getDataFolder().exists()) {
                ShadowBan.getInstance().getDataFolder().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    public static void createCounter() {

        ArrayList<String> red = new ArrayList<>();

        red.add("509279793926111242");

        if (configuration.get("Counter.count") != null) return;
        configuration.set("Counter.count", 0);
        configuration.set("Counter.rqid", 0);
        configuration.set("Counter.mods", red);
        configuration.set("Counter.key", red);
        saveConfig();
    }

    public static void setCounter(int i) {
        configuration.set("Counter.count", i);
        saveConfig();
    }

    public static int getCounter() {
        loadConfig();
        return configuration.getInt("Counter.count");
    }

    public static void setRQID(int i) {
        configuration.set("Counter.rqid", i);
        saveConfig();
    }

    public static int getRQID() {
        loadConfig();
        return configuration.getInt("Counter.rqid");
    }

    public static ArrayList<String> getModerator() {
        loadConfig();
        return (ArrayList<String>) configuration.getStringList("Counter.mods");
    }

    public static ArrayList<String> getAuthKeys() {
        loadConfig();
        return (ArrayList<String>) configuration.getStringList("Counter.key");
    }

    public static void setAuthKeys(ArrayList<String> list) {
        configuration.set("Counter.key", list);
        saveConfig();
    }

    public static void addAuthKey(String key) {
        ArrayList<String> list = getAuthKeys();
        list.add(key);
        setAuthKeys(list);
    }

    public static void addCounter(int i) {
        setCounter(getCounter() + i);
    }

    public static void removeCounter(int i) {
        setCounter(getCounter() - i);
        saveConfig();
    }

    public static void addRQID(int i) {
        setRQID(getRQID() + i);
    }

    public static void removeRQID(int i) {
        setRQID(getRQID() - i);
        saveConfig();
    }

    public static void saveConfig() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, file);
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

}

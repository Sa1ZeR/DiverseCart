package me.Sa1ZeR_.DiverseCart.Utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class FileUtils {

    File file;
    YamlConfiguration yml;

    public FileUtils(String name, Plugin plugin) {
        file = new File(plugin.getDataFolder(), name + ".yml");
        if(!file.exists()) {
            file.getParentFile().mkdir();
            try {
                file.createNewFile();
                plugin.saveResource(name + ".yml", true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        yml = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration get() {
        return yml;
    }
}

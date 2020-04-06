package me.Sa1ZeR_.DiverseCart.Utils;

import com.mysql.jdbc.Buffer;
import me.Sa1ZeR_.DiverseCart.DiverseCart;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Debug {

    private File file;
    private SimpleDateFormat dateFormat;

    public Debug(String name, Plugin plugin) {
        dateFormat = new SimpleDateFormat("HH:mm:ss dd:MM");
        file = new File(plugin.getDataFolder(), name + ".log");
        if(!file.exists()) {
            file.getParentFile().mkdir();
            try {
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Can't create new log file: \n" + e);
            }
        }
    }

    private void write(String line, String prefix) {
        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter(file, true));
            bw.append("[").append(dateFormat.format(new Date(System.currentTimeMillis()))).append("] ");
            bw.append(prefix).append(" ");
            bw.append(line);
            bw.newLine();
            bw.flush();
            bw.close();
        } catch (IOException e) {
            Bukkit.getLogger().severe("Can't write to file: \n" + e);
        }
    }

    public void info(String message) {
        DiverseCart.instance.getLogger().info(message);
        write(message, "[INFO] ");
    }

    public void error(String message) {
        write(message, "[ERROR] ");
        DiverseCart.instance.getLogger().severe(message);
    }
}

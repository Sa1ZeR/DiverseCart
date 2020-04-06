package me.Sa1ZeR_.DiverseCart.manager;

import me.Sa1ZeR_.DiverseCart.DiverseCart;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class MessageManager {

    FileConfiguration lang;
    private String prefix;

    public MessageManager() {
        lang = DiverseCart.instance.getLang();
        prefix = DiverseCart.instance.getLang().getString("prefix");
    }

    public void sendPlayer(String path, Player player, String... args) {
        String msg = getMsg(path);
        msg = replaceArgs(msg, args);
        msg = translateColorCodes(msg);
        player.sendMessage(msg);
    }

    public void sendPrefixMessage(String path, Player player, String... args) {
        String msg = getMsg(path);
        msg = appendPrefix(prefix, msg);
        msg = replaceArgs(msg, args);
        msg = translateColorCodes(msg);
        player.sendMessage(msg);
    }

    public void sendMsg(String path, CommandSender player, String... args) {
        String msg = getMsg(path);
        msg = appendPrefix(prefix, msg);
        msg = replaceArgs(msg, args);
        msg = translateColorCodes(msg);
        player.sendMessage(msg);
    }

    private String appendPrefix(String prefix, String msg) {
        StringBuilder sb = new StringBuilder(msg);
        sb.insert(0, prefix);
        return sb.toString();
    }
    private String getMsg(String path) {
        String s = lang.getString(path);
        if(s == null) {
            Bukkit.getLogger().severe("Can't find path: " + path + " in lang file");
        }
        return s;
    }

    private String replaceArgs(String msg, String[] args) {
        String line = msg;
        for(String s: args) {
            line = line.replaceFirst("%s%", s);
        }
        return line;
    }

    private String translateColorCodes(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}

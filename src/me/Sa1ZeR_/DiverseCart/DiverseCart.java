package me.Sa1ZeR_.DiverseCart;

import me.Sa1ZeR_.DiverseCart.Utils.Debug;
import me.Sa1ZeR_.DiverseCart.Utils.FileUtils;
import me.Sa1ZeR_.DiverseCart.Utils.MySQL;
import me.Sa1ZeR_.DiverseCart.commands.CommandManager;
import me.Sa1ZeR_.DiverseCart.manager.CartManager;
import me.Sa1ZeR_.DiverseCart.manager.MessageManager;
import me.Sa1ZeR_.DiverseCart.manager.StorageManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class DiverseCart extends JavaPlugin {

    public static DiverseCart instance;

    private Debug debug;
    private FileUtils config;
    private FileUtils lang;
    private MessageManager messageManager;
    private MySQL mySQL;
    private StorageManager storageManager;
    private CommandManager commandManager;
    private CartManager cartManager;

    public void onEnable() {
        instance = this;
        debug = new Debug("DiverseCart", this);
        config = new FileUtils("config", this);
        lang = new FileUtils("lang", this);
        messageManager = new MessageManager();
        mySQL = new MySQL();
        storageManager = new StorageManager();
        cartManager = new CartManager();
        commandManager = new CommandManager();
        for(String cmd : getDescription().getCommands().keySet()) {
            getCommand(cmd).setExecutor(commandManager);
        }
    }

    public FileConfiguration getCfg() {
        return config.get();
    }

    public FileConfiguration getLang() {
        return lang.get();
    }

    public MySQL getMySQL() {
        return mySQL;
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }

    public Debug getDebug() {
        return debug;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public CartManager getCartManager() {
        return cartManager;
    }
}

package me.Sa1ZeR_.DiverseCart.manager;

import me.Sa1ZeR_.DiverseCart.CartType;
import me.Sa1ZeR_.DiverseCart.DiverseCart;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.*;
import java.util.HashMap;
import java.util.List;

public class StorageManager {

    private Connection connection;
    private FileConfiguration config;

    private String tableName;
    private String username;
    private String title;
    private String iid;
    private String extra;
    private String type;
    private String amount;
    private String serverId;

    public StorageManager() {
        try {
            config = DiverseCart.instance.getCfg();
            connection = DiverseCart.instance.getMySQL().getConection();
            prepareDB();

            tableName = config.getString("cart-table.table-name");
            username = config.getString("cart-table.username");
            title = config.getString("cart-table.title");
            iid = config.getString("cart-table.iid");
            extra = config.getString("cart-table.extra");
            type = config.getString("cart-table.type");
            amount = config.getString("cart-table.amount");
            serverId = config.getString("cart-table.server-id");
        } catch (SQLException e) {
            DiverseCart.instance.getServer().shutdown();
            DiverseCart.instance.getDebug().error("Can't connect to MySQL: \n" + e);
        }
    }

    private void prepareDB() {
        try {
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS " + tableName + " (`id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT, `"+ username + "` VARCHAR(24) NOT NULL, "
                    + title + " VARCHAR(128) NOT NULL, " + iid + " VARCHAR(128) NOT NULL, " + extra + " TEXT NOT NULL, " +  type + "VARCHAR(24) DEFAULT 'item', " + amount + " SMALLINT(3) NOT NULL, " + serverId + " int(11) NOT NULL);" +
                    "ALTER TABLE " + tableName + " ADD PRIMARY KEY (`id`);" + "ALTER TABLE + "+ tableName + " MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;");
        } catch (SQLException e) {
            //DiverseCart.instance.getDebug().error("Can't prepare DB: \n" + e);
        }
    }

    public void putItem(ItemStack itemStack, String player) {
        DiverseCart.instance.getMySQL().getExecutor().submit(new Runnable() {
            @Override
            public void run() {
                PreparedStatement ps = null;
                try {
                    String extrajson = DiverseCart.instance.getCartManager().getExtra(itemStack);
                    ps = connection.prepareStatement("INSERT INTO " + tableName +" (" + username +", " + title + ", " + iid + ", " + extra + ", " + type + ", " + amount + ", " + serverId +") VALUES (?, ?, ?, ?, ?, ?, ?)");
                    ps.setString(1, player);
                    ps.setString(2, itemStack.getType().name());
                    ps.setString(3, String.valueOf(itemStack.getType() + ":" +itemStack.getDurability()));
                    if(extrajson != null) {
                        ps.setString(4, extrajson);
                    } else {
                        ps.setString(4, null);
                    }
                    ps.setString(5, CartType.ITEM.getName());
                    ps.setInt(6, itemStack.getAmount());
                    ps.setInt(7, DiverseCart.instance.getCfg().getInt("server-id"));
                    ps.execute();
                    DiverseCart.instance.getDebug().info(player + " put itemstack: " + itemStack.toString());
                } catch (SQLException e) {
                    DiverseCart.instance.getDebug().error("SQL error while putting item: \n" + e);
                    for(StackTraceElement element : e.getStackTrace()) {
                        DiverseCart.instance.getDebug().error(element.toString());
                    }
                } finally {
                    closeStatament(ps);
                }
            }
        });
    }

    public HashMap cartList(String player) {
        HashMap<Integer, String> map = new HashMap<>();
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("SELECT * FROM " + tableName + " WHERE " + username +"=? AND " +serverId + "=?");
            ps.setString(1, player);
            ps.setString(2, DiverseCart.instance.getCfg().getString("server-id"));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String value = rs.getString(type) + ";" + rs.getString(iid) + ";" + rs.getInt(amount);
                map.put(id, value);
            }
        } catch (SQLException e) {
            DiverseCart.instance.getDebug().error("SQL error while player getting info about his cart: \n" + e);
            for(StackTraceElement element : e.getStackTrace()) {
                DiverseCart.instance.getDebug().error(element.toString());
            }
        } finally {
            closeStatament(ps);
        }
        return map;
    }

    public void getItem(int id, Player player) {
        DiverseCart.instance.getMySQL().getExecutor().submit(new Runnable() {
            @Override
            public void run() {
                PreparedStatement ps = null;
                try {
                    ps = connection.prepareStatement("SELECT * FROM " + tableName + " WHERE id=? AND " + serverId + "=? AND " + username + "=?");
                    ps.setInt(1, id);
                    ps.setInt(2, DiverseCart.instance.getCfg().getInt("server-id"));
                    ps.setString(3, player.getName());
                    ResultSet rs = ps.executeQuery();
                    boolean given = false;
                    while (rs.next()) {
                        given = true;
                        CartType cartType = CartType.getType(rs.getString(type));
                        String itemId = rs.getString(iid);
                        int itemAmount = rs.getInt(amount);
                        String jsonextra = null;
                        if(rs.getString(extra) != null) {
                            jsonextra = rs.getString(extra);
                        }
                        DiverseCart.instance.getCartManager().performCartItem(cartType, itemId, itemAmount, jsonextra, player);
                        clearItems(id);
                    }
                    if(!given) {
                        DiverseCart.instance.getMessageManager().sendPlayer("commands.get.cart-empty", player, new String[0]);
                    }
                } catch (Exception e) {
                    DiverseCart.instance.getDebug().error("SQL error while getting item: \n" + e);
                    for(StackTraceElement element : e.getStackTrace()) {
                        DiverseCart.instance.getDebug().error(element.toString());
                    }
                } finally {
                    closeStatament(ps);
                }
            }
        });
    }

    public void getAll(Player player) {
        DiverseCart.instance.getMySQL().getExecutor().submit(new Runnable() {
            @Override
            public void run() {
                PreparedStatement ps = null;
                try {
                    ps = connection.prepareStatement("SELECT * FROM " + tableName + " WHERE " + serverId + "=? AND " + username + "=?");
                    ps.setInt(1,DiverseCart.instance.getCfg().getInt("server-id"));
                    ps.setString(2, player.getName());
                    ResultSet rs = ps.executeQuery();
                    boolean given = false;
                    while (rs.next()) {
                        given = true;
                        CartType cartType = CartType.getType(rs.getString(type));
                        String itemId = rs.getString(iid);
                        int itemAmount = rs.getInt(amount);
                        String jsonExtra = null;
                        if(rs.getString(extra) != null) {
                            jsonExtra = rs.getString(extra);
                        }
                        DiverseCart.instance.getCartManager().performCartItem(cartType, itemId, itemAmount, jsonExtra, player);
                    }
                    clearItems(player);
                    if(!given) {
                        DiverseCart.instance.getMessageManager().sendPlayer("commands.all.cart-empty", player);
                    }
                } catch (SQLException e) {
                    DiverseCart.instance.getDebug().error("SQL error while getting all items: \n" + e);
                    for(StackTraceElement element : e.getStackTrace()) {
                        DiverseCart.instance.getDebug().error(element.toString());
                    }
                } finally {
                    closeStatament(ps);
                }
            }
        });
    }

    private void clearItems(Player player) {
        DiverseCart.instance.getMySQL().getExecutor().submit(new Runnable() {
            @Override
            public void run() {
                PreparedStatement ps = null;
                try {
                    ps = connection.prepareStatement("DELETE FROM " + tableName + " WHERE " + username + "=? AND " + serverId + "=?");
                    ps.setString(1, player.getName());
                    ps.setInt(2, DiverseCart.instance.getCfg().getInt("server-id"));
                    ps.execute();
                } catch (SQLException e) {
                    DiverseCart.instance.getDebug().error("SQL error while remove items" + e);
                    for(StackTraceElement element : e.getStackTrace()) {
                        DiverseCart.instance.getDebug().error(element.toString());
                    }
                    e.printStackTrace();
                } finally {
                    closeStatament(ps);
                }
            }
        });
    }

    private void clearItems(int id) {
        DiverseCart.instance.getMySQL().getExecutor().submit(new Runnable() {
            @Override
            public void run() {
                PreparedStatement ps = null;
                try {
                    ps = connection.prepareStatement("DELETE FROM " + tableName + " WHERE id=? AND " + serverId + "=?");
                    ps.setInt(1, id);
                    ps.setString(2, DiverseCart.instance.getCfg().getString("server-id"));
                    ps.execute();
                } catch (SQLException e) {
                    DiverseCart.instance.getDebug().error("SQL error while remove items" + e);
                    for(StackTraceElement element : e.getStackTrace()) {
                        DiverseCart.instance.getDebug().error(element.toString());
                    }
                    e.printStackTrace();
                } finally {
                    closeStatament(ps);
                }
            }
        });
    }

    private void closeStatament(Statement statement) {
        DiverseCart.instance.getMySQL().getExecutor().submit(new Runnable() {
            @Override
            public void run() {
                if(statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        DiverseCart.instance.getDebug().error("Can't close statement: \n" + e);
                        for(StackTraceElement element : e.getStackTrace()) {
                            DiverseCart.instance.getDebug().error(element.toString());
                        }
                    }
                }
            }
        });
    }
}

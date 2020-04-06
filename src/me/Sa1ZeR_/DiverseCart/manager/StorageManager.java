package me.Sa1ZeR_.DiverseCart.manager;

import me.Sa1ZeR_.DiverseCart.CartType;
import me.Sa1ZeR_.DiverseCart.DiverseCart;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.*;
import java.util.HashMap;
import java.util.List;

public class StorageManager {

    private Connection connection;

    public StorageManager() {
        try {
            connection = DiverseCart.instance.getMySQL().getConection();
            prepareDB();
        } catch (SQLException e) {
            DiverseCart.instance.getServer().shutdown();
            DiverseCart.instance.getDebug().error("Can't connect to MySQL: \n" + e);
        }
    }

    private void prepareDB() {
        try {
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS diverse_cart (`id` INT UNSIGNED NOT NULL AUTO_INCREMENT, `server` VARCHAR(32) NOT NULL, `type` VARCHAR(24) NOT NULL, `username` VARCHAR(24) NOT NULL, `iid` TEXT NOT NULL, `amount` TINYINT UNSIGNED NOT NULL, `enchantments` TEXT, `extra` TEXT, PRIMARY KEY (`id`))");
        } catch (SQLException e) {
            DiverseCart.instance.getDebug().error("Can't prepare DB: \n" + e);
        }
    }

    public void putItem(ItemStack itemStack, String player) {
        DiverseCart.instance.getMySQL().getExecutor().submit(new Runnable() {
            @Override
            public void run() {
                PreparedStatement ps = null;
                try {
                    String ench = DiverseCart.instance.getCartManager().getEnchantments(itemStack);
                    String extra = DiverseCart.instance.getCartManager().getExtra(itemStack);
                    ps = connection.prepareStatement("INSERT INTO diverse_cart (server, type, username, iid, amount, enchantments, extra) VALUES (?, ?, ?, ?, ?, ?, ?)");
                    ps.setString(1, DiverseCart.instance.getCfg().getString("server-id"));
                    ps.setString(2,"item");
                    ps.setString(3, player);
                    ps.setString(4, String.valueOf(itemStack.getType() + ":" +itemStack.getDurability()));
                    ps.setInt(5, itemStack.getAmount());
                    if(ench != null) {
                        ps.setString(6, ench);
                    } else {
                        ps.setString(6, null);
                    }
                    if(extra != null) {
                        ps.setString(7, extra);
                    } else {
                        ps.setString(7, null);
                    }
                    ps.execute();
                    DiverseCart.instance.getDebug().info(player + " put itemstack: " + itemStack.toString());
                } catch (SQLException e) {
                    DiverseCart.instance.getDebug().error("SQL error while putting item: \n" + e);
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
            ps = connection.prepareStatement("SELECT * FROM diverse_cart WHERE username=? AND server=?");
            ps.setString(1, player);
            ps.setString(2, DiverseCart.instance.getCfg().getString("server-id"));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String value = rs.getString("type") + ";" + rs.getString("iid") + ";" + rs.getInt("amount");
                map.put(id, value);
            }
        } catch (SQLException e) {
            DiverseCart.instance.getDebug().error("SQL error while player getting info about his cart: \n" + e);
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
                    ps = connection.prepareStatement("SELECT * FROM diverse_cart WHERE id=? AND server=? AND username=?");
                    ps.setInt(1, id);
                    ps.setString(2, DiverseCart.instance.getCfg().getString("server-id"));
                    ps.setString(3, player.getName());
                    ResultSet rs = ps.executeQuery();
                    if(rs != null) {
                        while (rs.next()) {
                            CartType type = CartType.getType(rs.getString("type"));
                            String iid = rs.getString("iid");
                            int amount = rs.getInt("amount");
                            String ench = null;
                            if(rs.getString("enchantments") != null) {
                                ench = rs.getString("enchantments");
                            }
                            String extra = null;
                            if(rs.getString("extra") != null) {
                                extra = rs.getString("extra");
                            }
                            DiverseCart.instance.getCartManager().performCartItem(type, iid, amount, ench, extra, player);
                            clearItems(id);
                        }
                    } else {
                        DiverseCart.instance.getMessageManager().sendPlayer("commands.get.cart-empty", player, new String[0]);
                    }
                } catch (SQLException e) {
                    DiverseCart.instance.getDebug().error("SQL error while getting item: \n" + e);
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
                    ps = connection.prepareStatement("SELECT * FROM diverse_cart WHERE server=? AND username=?");
                    ps.setString(1,DiverseCart.instance.getCfg().getString("server-id"));
                    ps.setString(2, player.getName());
                    ResultSet rs = ps.executeQuery();
                    if(rs != null) {
                        while (rs.next()) {
                            CartType type = CartType.getType(rs.getString("type"));
                            String iid = rs.getString("iid");
                            int amount = rs.getInt("amount");
                            String ench = null;
                            if(rs.getString("enchantments") != null) {
                                ench = rs.getString("enchantments");
                            }
                            String extra = null;
                            if(rs.getString("extra") != null) {
                                extra = rs.getString("extra");
                            }
                            DiverseCart.instance.getCartManager().performCartItem(type, iid, amount, ench, extra, player);
                        }
                        clearItems(player);
                    } else {
                        DiverseCart.instance.getMessageManager().sendPlayer("commands.get.cart-empty", player, new String[0]);
                    }
                } catch (SQLException e) {
                    DiverseCart.instance.getDebug().error("SQL error while getting all items: \n" + e);
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
                    ps = connection.prepareStatement("DELETE FROM diverse_cart WHERE username=? AND server=?");
                    ps.setString(1, player.getName());
                    ps.setString(2, DiverseCart.instance.getCfg().getString("server-id"));
                    ps.execute();
                } catch (SQLException e) {
                    DiverseCart.instance.getDebug().error("SQL error while remove items" + e);
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
                    ps = connection.prepareStatement("DELETE FROM diverse_cart WHERE id=? AND server=?");
                    ps.setInt(1, id);
                    ps.setString(2, DiverseCart.instance.getCfg().getString("server-id"));
                    ps.execute();
                } catch (SQLException e) {
                    DiverseCart.instance.getDebug().error("SQL error while remove items" + e);
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
                    }
                }
            }
        });
    }
}

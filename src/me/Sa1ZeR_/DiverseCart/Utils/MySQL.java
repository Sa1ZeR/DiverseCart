package me.Sa1ZeR_.DiverseCart.Utils;

import me.Sa1ZeR_.DiverseCart.DiverseCart;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MySQL {

    private String host;
    private String pass;
    private String db;
    private String user;
    private String URL;
    private ExecutorService executor;

    public MySQL() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            host = DiverseCart.instance.getCfg().getString("mysql.host");
            pass = DiverseCart.instance.getCfg().getString("mysql.password");
            db = DiverseCart.instance.getCfg().getString("mysql.database");
            user = DiverseCart.instance.getCfg().getString("mysql.user");
            URL = "jdbc:mysql://" + host + "/" + db;
        } catch (InstantiationException|IllegalAccessException|ClassNotFoundException e) {
            e.printStackTrace();
        }
        executor = Executors.newSingleThreadExecutor();
    }

    public Connection getConection() throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", user);
        properties.setProperty("password", pass);
        properties.setProperty("useUnicode", "true");
        properties.setProperty("connectTimeout", "1000");
        properties.setProperty("socketTimeout", "1000");
        properties.setProperty("autoReconnect", "true");
        properties.setProperty("failOverReadOnly", "false");
        properties.setProperty("maxReconnects", "10");
        properties.setProperty("characterEncoding", "utf-8");
        return DriverManager.getConnection(URL, properties);
    }

    public ExecutorService getExecutor() {
        return executor;
    }
}

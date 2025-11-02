package utils;

import config.ConfigManager;
import dbmanager.DbConnector;
import dbmanager.DbSshConfig;
import javax.sql.DataSource;
import java.sql.Connection;

public class DBUtils {
    // jdbc:mysql://host:port/dbname?serverTimezone=UTC&useSSL=false
    // jdbc:mysql://127.0.0.1:3306/testsigma_opensource?serverTimezone=UTC&useSSL=false
    public static void main(String[] args) {
        DbSshConfig cfg = new DbSshConfig();

        cfg.dbJdbcPrefix = ConfigManager.getProperty("db.jdbcPrefix");
        cfg.dbHost = ConfigManager.getProperty("db.host");
        cfg.dbPort = ConfigManager.getIntProperty("db.port", 3306);
        cfg.dbName = ConfigManager.getProperty("db.name");
        cfg.dbUser = ConfigManager.getProperty("db.user");
        cfg.dbPassword = ConfigManager.getProperty("db.password");

        // SSH config
        cfg.sshHost = ConfigManager.getProperty("ssh.host");
        if (cfg.sshHost != null && !cfg.sshHost.isEmpty()) {
            cfg.sshPort = ConfigManager.getIntProperty("ssh.port", 22);
            cfg.sshUser = ConfigManager.getProperty("ssh.user");
            cfg.sshPrivateKey = ConfigManager.getProperty("ssh.privateKey");
            cfg.localForwardPort = ConfigManager.getIntProperty("ssh.localForwardPort", 0);
        }

        cfg.maximumPoolSize = ConfigManager.getIntProperty("db.maximumPoolSize", 10);

        DbConnector connector = new DbConnector(cfg);
        runWithDb(cfg,connector);
        stopWithDb(connector);
    }
    public static void runWithDb(DbSshConfig cfg,DbConnector connector) {
        try {
            DataSource ds = connector.start();
            try (Connection con = ds.getConnection()) {
                System.out.println("Connected. Catalog: " + con.getCatalog());
                // TODO: business logic, query, update, ...
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void stopWithDb(DbConnector connector) {
        connector.stop();
    }
}

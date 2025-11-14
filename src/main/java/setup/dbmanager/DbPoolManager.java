package setup.dbmanager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;

import javax.sql.DataSource;

public class DbPoolManager {
    private final DbSshConfig cfg;
    @Getter
    private HikariDataSource dataSource;

    public DbPoolManager(DbSshConfig cfg) {
        this.cfg = cfg;
    }

    /**
     * Create and return DataSource. dbConnectHost/dbConnectPort are the host/port that DB pool should use.
     */
    public DataSource startPool(String dbConnectHost, int dbConnectPort) {
        HikariConfig hc = new HikariConfig();

        String jdbcUrl = String.format("%s%s:%d/%s", cfg.dbJdbcPrefix, dbConnectHost, dbConnectPort, cfg.dbName);
        hc.setJdbcUrl(jdbcUrl);
        hc.setUsername(cfg.dbUser);
        hc.setPassword(cfg.dbPassword);

        hc.setMaximumPoolSize(cfg.maximumPoolSize);
        hc.setMinimumIdle(cfg.minimumIdle);
        hc.setPoolName("app-db-pool");
        // thêm các config hữu ích
        hc.addDataSourceProperty("cachePrepStmts", "true");
        hc.addDataSourceProperty("prepStmtCacheSize", "250");
        hc.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        dataSource = new HikariDataSource(hc);
        return dataSource;
    }

    public void stopPool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

}

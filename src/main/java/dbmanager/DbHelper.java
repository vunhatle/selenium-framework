package dbmanager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;

public class DbHelper {

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    public DbHelper(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void testConnection() {
        try (Connection con = dataSource.getConnection()) {
            System.out.println("✅ Connected to DB: " + con.getMetaData().getURL());
        } catch (Exception e) {
            throw new RuntimeException("❌ DB connection failed", e);
        }
    }

    public String queryForSingleValue(String sql) {
        return jdbcTemplate.queryForObject(sql, String.class);
    }

    public static void main(String[] args) {
        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/testsigma_opensource?serverTimezone=UTC&useSSL=false");
        cfg.setUsername("root");
        cfg.setPassword("root123");

        DataSource ds = new HikariDataSource(cfg);
        DbHelper helper = new DbHelper(ds);

        helper.testConnection();
        String version = helper.queryForSingleValue("SELECT VERSION()");
        System.out.println("MySQL Version = " + version);
    }
}

package setup.dbmanager;
import com.jcraft.jsch.JSchException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
public class DbConnector {
    private final DbSshConfig cfg;
    private final SshTunnelManager sshManager;
    private final DbPoolManager poolManager;

    public DbConnector(DbSshConfig cfg) {
        this.cfg = cfg;
        this.sshManager = new SshTunnelManager(cfg);
        this.poolManager = new DbPoolManager(cfg);
    }

    /**
     * Start connection: if SSH configured -> start tunnel then start pool to localhost:forwardedPort
     * returns DataSource ready to use.
     */
    public DataSource start() throws Exception {
        int connectPort = cfg.dbPort;
        String connectHost = cfg.dbHost;

        if (sshManager.isSshConfigured()) {
            try {
                int localPort = sshManager.start();
                connectHost = "127.0.0.1";
                connectPort = localPort;
            } catch (JSchException e) {
                // nếu muốn fallback: thử kết nối trực tiếp nếu local allowed
                throw new RuntimeException("Failed to establish SSH tunnel: " + e.getMessage(), e);
            }
        }

        DataSource dataSource = poolManager.startPool(connectHost, connectPort);

        // optional: quick validation
        validateConnection(dataSource, Duration.ofSeconds(5));
        return dataSource;
    }

    private void validateConnection(DataSource ds, Duration timeout) throws SQLException {
        try (Connection c = ds.getConnection()) {
            // optional: run a simple validation query if needed
            // For Postgres/MySQL, just checking getCatalog or isValid
            if (!c.isValid((int) timeout.getSeconds())) {
                throw new SQLException("Connection test failed");
            }
        }
    }

    public void stop() {
        try {
            poolManager.stopPool();
        } finally {
            sshManager.stop();
        }
    }
}

package dbmanager;

public class DbSshConfig {
    // DB
    public String dbHost;
    public int dbPort;
    public String dbName;
    public String dbUser;
    public String dbPassword;
    public String dbJdbcPrefix; // e.g. "jdbc:postgresql://" or "jdbc:mysql://"

    // SSH (nullable: nếu null/empty -> connect local)
    public String sshHost;
    public int sshPort = 22;
    public String sshUser;
    public String sshPassword;    // optional if using key
    public String sshPrivateKey;  // path to private key (optional)
    public String sshPassphrase;  // optional

    // local forwarding port (0 để lấy port tự do)
    public int localForwardPort = 0;

    // pool settings
    public int maximumPoolSize = 10;
    public int minimumIdle = 2;
}

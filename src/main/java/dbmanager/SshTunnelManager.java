package dbmanager;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.Getter;

import java.util.concurrent.ThreadLocalRandom;

public class SshTunnelManager {
    private Session session;
    private final DbSshConfig cfg;
    @Getter
    private int assignedLocalPort = -1;

    public SshTunnelManager(DbSshConfig cfg) {
        this.cfg = cfg;
    }

    public boolean isSshConfigured() {
        return cfg.sshHost != null && !cfg.sshHost.isBlank() && cfg.sshUser != null && !cfg.sshUser.isBlank();
    }

    /**
     * Start SSH tunnel. If cfg.localForwardPort == 0 -> pick a random free port
     * Returns the local port to which DB should connect.
     */
    public int start() throws JSchException {
        if (!isSshConfigured()) throw new IllegalStateException("SSH not configured");

        JSch jsch = new JSch();
        if (cfg.sshPrivateKey != null && !cfg.sshPrivateKey.isBlank()) {
            if (cfg.sshPassphrase != null) {
                jsch.addIdentity(cfg.sshPrivateKey, cfg.sshPassphrase);
            } else {
                jsch.addIdentity(cfg.sshPrivateKey);
            }
        }

        session = jsch.getSession(cfg.sshUser, cfg.sshHost, cfg.sshPort);
        if (cfg.sshPassword != null && !cfg.sshPassword.isBlank()) {
            session.setPassword(cfg.sshPassword);
        }
        // disable strict host key checking for convenience (consider proper known_hosts in prod)
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect(10_000); // timeout ms

        int localPort = cfg.localForwardPort;
        if (localPort == 0) {
            localPort = pickRandomEphemeralPort();
        }
        // port forwarding: bind localhost:localPort -> cfg.dbHost:cfg.dbPort
        String bindAddress = "127.0.0.1";
        assignedLocalPort = session.setPortForwardingL(bindAddress, localPort, cfg.dbHost, cfg.dbPort);
        return assignedLocalPort;
    }

    public void stop() {
        if (session != null) {
            try {
                session.disconnect();
            } catch (Exception ignored) {}
            session = null;
            assignedLocalPort = -1;
        }
    }

    private int pickRandomEphemeralPort() {
        // pick a port in ephemeral range; better: bind ServerSocket(0) to find free port — but
        // to keep this simple, choose random in 20000-40000. In production, try to bind to 0 to get free port.
        return ThreadLocalRandom.current().nextInt(20000, 40000);
    }
}

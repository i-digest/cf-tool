package org.cftool.cftool.client;

import com.jcraft.jsch.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.cftool.cftool.model.SshCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class SshClient {
    private final JSch jsch;
    @Getter private final String address;
    private final int port;

    @Value("${cftool.path.config.ssh.known_hosts}")
    private String knownHostsFilePath;

    @Value("${cftool.path.config.ssh.private_key}")
    private String privateKeyFilePath;

    @Value("${cftool.path.config.ssh.remote_user}")
    private String sshRemoteUser;

    public SshClient(@NonNull final String address, @NonNull final int port) throws JSchException {
        this.address = address;
        this.port = port;

        this.jsch = new JSch();
        this.jsch.setKnownHosts(this.knownHostsFilePath);
        this.jsch.addIdentity(this.privateKeyFilePath);
    }

    /**
     * Connect to a remote host via SSH and execute a command.
     *
     * @param command SSH command to execute
     * @return The result of the command execution
     */
    public boolean executeCommand(final SshCommand command) {
        Session session = null;
        Channel channel = null;
        try {
            session = getSession();
            session.connect();

            channel = getExecChannel(session, command);
            readConsole(channel);

            return true;
        } catch (final JSchException e) {
            log.error("Error occurs during execution ssh command {}. Error: {}", command.getValue(), e.getMessage());
            return false;
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
    }

    private void readConsole(final Channel channel) throws JSchException {
        try (final InputStream in = channel.getInputStream()) {
            channel.connect();
            final byte[] tmp = new byte[1024];
            do {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, tmp.length);
                    if (i < 0) {
                        return;
                    }
                    log.debug(new String(tmp));
                }
            } while (!channel.isClosed());
        } catch (final IOException e) {
            log.error("Error occurs while reading stream from channel: {}", e.getMessage());
        }
    }

    private ChannelExec getExecChannel(final Session session, final SshCommand command) throws JSchException {
        final ChannelExec channel = (ChannelExec) session.openChannel("exec");
        channel.setInputStream(null);
        channel.setErrStream(System.err);
        channel.setCommand(command.getValue());

        return channel;
    }

    private Session getSession() throws JSchException {
        final Session session = jsch.getSession(this.sshRemoteUser, this.address, this.port);
        session.setConfig("StrictHostKeyChecking", "no");
        return session;
    }
}

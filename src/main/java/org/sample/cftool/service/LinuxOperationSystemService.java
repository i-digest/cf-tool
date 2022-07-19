package org.sample.cftool.service;

import com.jcraft.jsch.JSchException;
import lombok.AccessLevel;
import lombok.Getter;
import org.sample.cftool.ssh.SshClient;

abstract class LinuxOperationSystemService implements OperationSystemService {
    @Getter(value = AccessLevel.PACKAGE)
    private final SshClient sshClient;

    protected LinuxOperationSystemService(final String address, final int port) throws JSchException {
        this.sshClient = new SshClient(address, port);
    }
}

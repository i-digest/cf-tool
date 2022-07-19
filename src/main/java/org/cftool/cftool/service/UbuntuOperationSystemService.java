package org.cftool.cftool.service;

import com.jcraft.jsch.JSchException;
import lombok.extern.slf4j.Slf4j;
import org.cftool.cftool.model.LogMessages;
import org.cftool.cftool.model.SshCommand;
import org.cftool.cftool.model.exception.RuntimeInterruptedException;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.TimeUnit;


@Slf4j
public class UbuntuOperationSystemService extends LinuxOperationSystemService {

    public UbuntuOperationSystemService(@NonNull final String address, final int port) throws JSchException {
        super(address, port);
    }

    @Override
    public boolean rebootSystem() {
        final boolean commandIssued = getSshClient().executeCommand(SshCommand.REBOOT);
        if (commandIssued) {
            safeSleep(30);

            return waitUntilSystemReboot(16);
        } else {
            return false;
        }
    }

    @Override
    public boolean upgradeSystemPackages() {
        return getSshClient().executeCommand(SshCommand.SYSTEM_FORCE_UPGRADE);
    }

    private void safeSleep(final int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            throw new RuntimeInterruptedException(e);
        }
    }

    private boolean waitUntilSystemReboot(int tries) {
        try {
            final InetAddress address = InetAddress.getByName(getSshClient().getAddress());
            return address.isReachable(1000);
        } catch (final IOException e) {
            if (0 < tries) {
                safeSleep(1 + tries);
                tries++;

                return waitUntilSystemReboot(tries);
            } else {
                log.error(LogMessages.SYSTEM_REBOOT_TOOK_TO_LONG_MESSAGE);
                return false;
            }
        }
    }
}

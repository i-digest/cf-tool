package org.cftool.cftool.service;

import com.jcraft.jsch.JSchException;
import org.cftool.cftool.model.OperationSystem;
import org.cftool.cftool.model.OperationSystemCredentials;

public interface OperationSystemService {

    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    static OperationSystemService getService(final OperationSystem operationSystem, final OperationSystemCredentials credentials) throws JSchException {
        switch (operationSystem) {
            case UBUNTU:
                return new UbuntuOperationSystemService(credentials.getAddress(), credentials.getPort());
            default:
                throw new UnsupportedOperationException("Operation system " + operationSystem.name() + " not implemented");
        }
    }

    boolean rebootSystem();

    boolean upgradeSystemPackages();

}

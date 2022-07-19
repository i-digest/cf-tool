package org.sample.cftool.service;

import com.jcraft.jsch.JSchException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.sample.cftool.configuration.NodeConfigurationLoader;
import org.sample.cftool.model.LogMessages;
import org.sample.cftool.model.Node;
import org.sample.cftool.model.cloudflare.Host;
import org.sample.cftool.model.exception.RuntimeInterruptedException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class NodeManageService {
    private static final int DEFAULT_SSH_PORT = 22;
    private final NodeConfigurationLoader nodeConfigurationLoader;
    private final CloudstackService cloudstackService;

    public NodeManageService(final NodeConfigurationLoader nodeConfigurationLoader, final CloudstackService cloudstackService) {
        this.nodeConfigurationLoader = nodeConfigurationLoader;
        this.cloudstackService = cloudstackService;
    }

    @SneakyThrows
    public void rebootNode(@NonNull final String nodeName)  {
        final Node node = nodeConfigurationLoader.getNodeAddress(nodeName);
        if (node != null) {
            try {
                final OperationSystemService operationSystemService = new UbuntuOperationSystemService(node.getNodeAddress(), DEFAULT_SSH_PORT);
                final boolean rebooted = operationSystemService.rebootSystem();
                if (rebooted) {
                    log.info("Node {} successfully rebooted", nodeName);
                } else {
                    log.info("Node {} failed to reboot", nodeName);
                }
            } catch (final JSchException e) {
                log.error(LogMessages.ERROR_DURING_EXECUTION_SSH_COMMAND_MESSAGE, nodeName);
            }
        } else {
            log.error(LogMessages.HOST_NOT_FOUND_IN_CONFIGURATION_FILE_MESSAGE, nodeName);
        }
    }

    @SneakyThrows
    public void upgradeSystemPackages(@NonNull final String nodeName) {
        final Node node = nodeConfigurationLoader.getNodeAddress(nodeName);
        if (node != null) {
            try {
                final OperationSystemService operationSystemService = new UbuntuOperationSystemService(node.getNodeAddress(), DEFAULT_SSH_PORT);
                log.info("Starting package upgrading on node {}", nodeName);
                final boolean upgraded = operationSystemService.upgradeSystemPackages();
                if (upgraded) {
                    log.info("System packages successfully upgraded on node {}", nodeName);
                } else {
                    log.error("Error occurs during system package upgrading on node {}", nodeName);
                }
            } catch (final JSchException e) {
                log.error(LogMessages.ERROR_DURING_EXECUTION_SSH_COMMAND_MESSAGE, nodeName);
            }
        } else {
            log.error(LogMessages.HOST_NOT_FOUND_IN_CONFIGURATION_FILE_MESSAGE, nodeName);
        }
    }

    @SneakyThrows
    public void enableMaintenanceMode(@NonNull final String nodeName) {
        final Node node = nodeConfigurationLoader.getNodeAddress(nodeName);
        if (node != null) {
            final Host host = cloudstackService.getHost(nodeName);
            if (host != null) {
                cloudstackService.enableMaintenanceMode(host.getId(), nodeName);
            } else {
                log.error("Host with name {} not found in cloudstack service", nodeName);
            }
        } else {
            log.error(LogMessages.HOST_NOT_FOUND_IN_CONFIGURATION_FILE_MESSAGE, nodeName);
        }
    }

    @SneakyThrows
    public void disableMaintenanceMode(@NonNull final String nodeName) {
        final Node node = nodeConfigurationLoader.getNodeAddress(nodeName);
        if (node != null) {
            final Host host = cloudstackService.getHost(nodeName);
            if (host != null) {
                cloudstackService.disableMaintenanceMode(host.getId(), nodeName);
            } else {
                log.error("Host with name {} not found in cloudstack service", nodeName);
            }
        } else {
            log.error(LogMessages.HOST_NOT_FOUND_IN_CONFIGURATION_FILE_MESSAGE, nodeName);
        }
    }

    @SneakyThrows
    public void upgradeNodes(@NonNull final List<String> nodeNameList) {
        final List<Node> nodeList = nodeConfigurationLoader.getNodesAddresses(nodeNameList);
        if (!nodeList.isEmpty()) {
            nodeList.forEach(node -> {
                final String nodeName = node.getNodeName();
                final Host host = cloudstackService.getHost(nodeName);
                if (host != null) {
                    final String hostId = host.getId();
                    try {
                        log.info("Switching node {} into maintenance mode", nodeName);
                        final boolean enabled = cloudstackService.enableMaintenanceMode(hostId, nodeName);
                        if (enabled) {
                            proceedNodeUpgrade(node, nodeName, hostId);
                        } else {
                            log.error("Cannot put node {} into maintenance mode", nodeName);
                        }
                    } catch (final InterruptedException e) {
                        throw new RuntimeInterruptedException(e);
                    }
                } else {
                    log.error(LogMessages.HOST_NOT_FOUND_IN_CONFIGURATION_FILE_MESSAGE, nodeName);
                }
            });
        } else {
            log.error(LogMessages.HOSTS_NOT_FOUND_IN_CONFIGURATION_FILE_MESSAGE);
        }
    }

    @SneakyThrows
    private void proceedNodeUpgrade(final Node node, final String nodeName, final String hostId) {
        try {
            upgradeSystemPackagesAndReboot(node, nodeName, hostId);
        } catch (final JSchException e) {
            log.error(LogMessages.ERROR_DURING_EXECUTION_SSH_COMMAND_MESSAGE, nodeName);
        }
    }

    @SneakyThrows
    private void upgradeSystemPackagesAndReboot(final Node node, final String nodeName, final String hostId) throws JSchException {
        final OperationSystemService operationSystemService = new UbuntuOperationSystemService(node.getNodeAddress(), DEFAULT_SSH_PORT);
        log.info("Starting package upgrading on node {}", nodeName);
        final boolean upgraded = operationSystemService.upgradeSystemPackages();
        if (upgraded) {
            log.info("System packages successfully upgraded on node {}", nodeName);
            log.info("Rebooting node {}", nodeName);
            final boolean rebooted = operationSystemService.rebootSystem();
            if (rebooted) {
                log.info("Node {} successfully rebooted", nodeName);
                cloudstackService.disableMaintenanceMode(hostId, nodeName);
            } else {
                log.info("Node {} failed to reboot", nodeName);
            }
        } else {
            log.error("Error occurs during system package upgrading on node {}", nodeName);
        }
    }
}

package org.cftool.cftool.model;

public class LogMessages {
    public final static String ERROR_DURING_EXECUTION_SSH_COMMAND_MESSAGE = "Error occurs during creating ssh client for node {}";
    public final static String SYSTEM_REBOOT_TOOK_TO_LONG_MESSAGE = "Waiting for system reboot took too long";
    public final static String HOST_NOT_FOUND_IN_CONFIGURATION_FILE_MESSAGE = "Host with name {} not found in hosts configuration file";
    public final static String HOSTS_NOT_FOUND_IN_CONFIGURATION_FILE_MESSAGE = "Hosts not found in hosts configuration file";
    public final static String HOSTS_SUCCESSFULLY_REBOOTED_MESSAGE = "Node {} successfully rebooted";
    public final static String HOSTS_FAILED_TO_REBOOT_MESSAGE = "Node {} failed to reboot";
    public final static String SYSTEM_PACKAGES_SUCCESSFULLY_UPGRADED_MESSAGE = "System packages successfully upgraded on node {}";
    public final static String SYSTEM_PACKAGES_FAILED_TO_UPGRADED_MESSAGE = "Error occurs during system package upgrading on node {}";
}

package org.sample.cftool;


import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.sample.cftool.model.AppCommand;
import org.sample.cftool.model.NodeCommand;
import org.sample.cftool.model.exception.EmptyValueException;
import org.sample.cftool.model.exception.UnknownCommandException;
import org.sample.cftool.service.NodeManageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication(scanBasePackages = "org.sample.cftool")
public class Main implements CommandLineRunner {
    private final NodeManageService nodeManageService;
    private static List<String> nodeList;
    private static NodeCommand nodeCommand;

    public Main(final NodeManageService nodeManageService) {
        this.nodeManageService = nodeManageService;
    }

    public static void main(final String[] args) {
        proceedInputArgs(args);

        new SpringApplicationBuilder(Main.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }

    @Override
    public void run(final String... args) {
        if (nodeCommand != null) {
            proceedCommandExecution(nodeList);
        } else {
            nodeManageService.upgradeNodes(nodeList);
        }
    }

    private static void proceedInputArgs(final String... args) {
        if (ArrayUtils.isNotEmpty(args)) {
            try {
                for (final String arg : args) {
                    if (!arg.contains("=") || !arg.startsWith("--")) {
                        System.out.println("Wrong command: '" + arg + "'");
                        writeHelpAndCloseApp();
                    } else {
                        final String[] split = arg.split("=");

                        final AppCommand appCommand = AppCommand.fromString(split[0]);
                        final String value = split[1];
                        if (StringUtils.isNotBlank(value)) {
                            switch (appCommand) {
                                case NODES:
                                    nodeList = Arrays.asList(value.split(","));
                                    break;
                                case COMMAND:
                                    nodeCommand = NodeCommand.fromString(value);
                                    break;
                            }
                        } else {
                            throw new EmptyValueException("Value for command: " + appCommand.getValue() + " should not be empty or blank.");
                        }

                    }
                }
                if (nodeList == null || nodeList.isEmpty()) {
                    throw new EmptyValueException("No nodes were provided, exiting");
                }

            } catch (final UnknownCommandException | EmptyValueException e) {
                System.out.println(e.getMessage());
                writeHelpAndCloseApp();
            }
        } else {
            writeHelpAndCloseApp();
        }
    }

    private void proceedCommandExecution(final List<String> nodeList) {
        switch (nodeCommand) {
            case REBOOT:
                nodeList.forEach(nodeManageService::rebootNode);
                break;
            case ENABLE_MAINTENANCE:
                nodeList.forEach(nodeManageService::enableMaintenanceMode);
                break;
            case DISABLE_MAINTENANCE:
                nodeList.forEach(nodeManageService::disableMaintenanceMode);
                break;
            case UPGRADE_SYSTEM_PACKAGES:
                nodeList.forEach(nodeManageService::upgradeSystemPackages);
                break;
        }
    }

    private static void writeHelpAndCloseApp() {
        System.out.println("usage: --nodes=node1,node2,node3 [optional] --command=enable_maintenance");
        System.out.println("commands: <enable_maintenance,disable_maintenance,reboot,upgrade_system_packages>");
        System.exit(0);
    }
}

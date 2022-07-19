package org.cftool.cftool.model;

import org.cftool.cftool.model.exception.UnknownCommandException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Arrays;

public enum NodeCommand {
    ENABLE_MAINTENANCE("enable_maintenance"),
    DISABLE_MAINTENANCE("disable_maintenance"),
    REBOOT("reboot"),
    UPGRADE_SYSTEM_PACKAGES("upgrade_system_packages");

    private final String value;

    NodeCommand(final String value) {
        this.value = value;
    }

    @Nullable
    public static NodeCommand fromString(@NonNull final String command) throws UnknownCommandException {
        return Arrays.stream(values())
                .filter(appCommand -> appCommand.value.equals(command))
                .findFirst()
                .orElseThrow(() -> new UnknownCommandException("Unknown command value: " + command));
    }
}

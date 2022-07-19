package org.cftool.cftool.model;

import lombok.Getter;
import org.cftool.cftool.model.exception.UnknownCommandException;
import org.springframework.lang.NonNull;

import java.util.Arrays;

public enum AppCommand {
    NODES("--nodes"),
    COMMAND("--command");

    @Getter private final String value;

    AppCommand(final String value) {
        this.value = value;
    }

    @NonNull
    public static AppCommand fromString(@NonNull final String command) throws UnknownCommandException {
        return Arrays.stream(values())
                .filter(appCommand -> appCommand.value.equals(command))
                .findFirst()
                .orElseThrow(() -> new UnknownCommandException("Unknown command: " + command));
    }
}

package org.sample.cftool.model;

import lombok.Getter;

public enum SshCommand {
    REBOOT("reboot"),
    SYSTEM_FORCE_UPGRADE("apt-get --assume-yes update && apt-get --assume-yes upgrade");

    @Getter private final String value;

    SshCommand(final String value) {
        this.value = value;
    }
}

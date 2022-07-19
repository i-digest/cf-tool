package org.cftool.cftool.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OperationSystemCredentials {
    private String address;
    private int port;
}

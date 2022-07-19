package org.sample.cftool.model.cloudflare;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Details {

    @SerializedName("Host.OS.Kernel.Version")
    private String hostOSKernelVersion;

    @SerializedName("com.cloud.network.Networks.RouterPrivateIpStrategy")
    private String comCloudNetworkNetworksRouterPrivateIpStrategy;

    @SerializedName("Host.OS.Version")
    private String hostOSVersion;

    @SerializedName("secured")
    private String secured;

    @SerializedName("Host.OS")
    private String hostOS;

}

package org.sample.cftool.model.cloudflare;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class Host{

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("state")
    private String state;

    @SerializedName("type")
    private String type;

    @SerializedName("ipaddress")
    private String ipAddress;

    @SerializedName("zoneid")
    private String zoneId;

    @SerializedName("zonename")
    private String zoneName;

    @SerializedName("podid")
    private String podId;

    @SerializedName("podname")
    private String podName;

    @SerializedName("version")
    private String version;

    @SerializedName("hypervisor")
    private String hypervisor;

    @SerializedName("cpusockets")
    private int cpuSockets;

    @SerializedName("cpunumber")
    private int cpuNumber;

    @SerializedName("cpuspeed")
    private int cpuSpeed;

    @SerializedName("cpuallocated")
    private String cpuAllocated;

    @SerializedName("cpuallocatedvalue")
    private int cpuAllocatedValue;

    @SerializedName("cpuallocatedpercentage")
    private String cpuAllocatedPercentage;

    @SerializedName("cpuallocatedwithoverprovisioning")
    private String cpuAllocatedWithOverProvisioning;

    @SerializedName("cpuused")
    private String cpuUsed;

    @SerializedName("cpuwithoverprovisioning")
    private String cpuWithOverProvisioning;

    @SerializedName("cpuloadaverage")
    private double cpuLoadAverage;

    @SerializedName("networkkbsread")
    private int networkKbsRead;

    @SerializedName("networkkbswrite")
    private int networkKbsWrite;

    @SerializedName("memorytotal")
    private Object memoryTotal;

    @SerializedName("memorywithoverprovisioning")
    private String memoryWithOverProvisioning;

    @SerializedName("memoryallocated")
    private long memoryAllocated;

    @SerializedName("memoryallocatedpercentage")
    private String memoryAllocatedPercentage;

    @SerializedName("memoryallocatedbytes")
    private long memoryAllocatedBytes;

    @SerializedName("memoryused")
    private Object memoryUsed;

    @SerializedName("capabilities")
    private String capabilities;

    @SerializedName("lastpinged")
    private Date lastPinged;

    @SerializedName("managementserverid")
    private String managementServerId;

    @SerializedName("clusterid")
    private String clusterId;

    @SerializedName("clustername")
    private String clusterName;

    @SerializedName("clustertype")
    private String clusterType;

    @SerializedName("islocalstorageactive")
    private boolean isLocalStorageActive;

    @SerializedName("created")
    private Date created;

    @SerializedName("events")
    private String events;

    @SerializedName("hostha")
    private HostHA hostHA;

    @SerializedName("outofbandmanagement")
    private OutOfBandManagement outOfBandManagement;

    @SerializedName("resourcestate")
    private String resourceState;

    @SerializedName("hahost")
    private boolean haHost;

    @SerializedName("details")
    private Details details;

    @SerializedName("ueficapability")
    private boolean uefiCapability;

    @SerializedName("hasannotations")
    private boolean hasAnnotations;

}

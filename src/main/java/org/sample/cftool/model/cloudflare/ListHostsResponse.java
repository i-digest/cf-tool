package org.sample.cftool.model.cloudflare;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ListHostsResponse {

    @SerializedName("count")
    private int count;

    @SerializedName("host")
    private List<Host> hostList;

}

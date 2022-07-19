package org.sample.cftool.model.cloudflare;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PrepareHostForMaintenanceResponse {

    @SerializedName("preparehostformaintenanceresponse")
    private JobIdResponse jobIdResponse;

}

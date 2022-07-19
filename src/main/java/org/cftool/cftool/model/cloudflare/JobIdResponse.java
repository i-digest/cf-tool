package org.cftool.cftool.model.cloudflare;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class JobIdResponse {

    @SerializedName("jobid")
    private String jobId;

}

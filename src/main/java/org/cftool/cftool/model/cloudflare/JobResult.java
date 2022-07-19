package org.cftool.cftool.model.cloudflare;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter @Setter
public class JobResult {

    @SerializedName("host")
    private Host host;

    @SerializedName("errorcode")
    private int errorCode;

    @SerializedName("errortext")
    private String errorText;

}

package org.sample.cftool.model.cloudflare;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class QueryAsyncJobResult {

    @SerializedName("accountid")
    private String accountId;

    @SerializedName("cmd")
    private String cmd;

    @SerializedName("completed")
    private Date completed;

    @SerializedName("created")
    private Date created;

    @SerializedName("jobid")
    private String jobId;

    @SerializedName("jobinstanceid")
    private String jobInstanceId;

    @SerializedName("jobinstancetype")
    private String jobInstanceType;

    @SerializedName("jobprocstatus")
    private int jobProcStatus;

    @SerializedName("jobresult")
    private JobResult jobResult;

    @SerializedName("jobresultcode")
    private int jobResultCode;

    @SerializedName("jobresulttype")
    private String jobResultType;

    @SerializedName("jobstatus")
    private int jobStatus;

    @SerializedName("userid")
    private String userid;

}

package org.sample.cftool.service;

import br.com.autonomiccs.apacheCloudStack.client.ApacheCloudStackClient;
import br.com.autonomiccs.apacheCloudStack.client.ApacheCloudStackRequest;
import br.com.autonomiccs.apacheCloudStack.client.beans.ApacheCloudStackUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.sample.cftool.model.cloudflare.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class CloudstackService {
    private ApacheCloudStackClient apacheCloudStackClient;

    @Value("${sample.path.config.cmk}")
    private File cmkConfigFile;

    @PostConstruct
    public void init() throws IOException {
        try (final InputStream input = Files.newInputStream(cmkConfigFile.toPath())) {
            final Properties properties = new Properties();
            properties.load(input);

            final String url = properties.getProperty("url");
            final String username = properties.getProperty("username");
            final String password = properties.getProperty("password");
            final String domain = properties.getProperty("domain");
            final String apikey = properties.getProperty("apikey");
            final String secretKey = properties.getProperty("secretkey");

            final ApacheCloudStackUser apacheCloudStackUser;
            if (StringUtils.isNotBlank(apikey) && StringUtils.isNotBlank(secretKey)) {
                apacheCloudStackUser = new ApacheCloudStackUser(apikey, secretKey);
            } else if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password) && StringUtils.isNotBlank(domain)) {
                apacheCloudStackUser = new ApacheCloudStackUser(username, password, domain);
            } else {
                throw new IOException("Authentication credentials not provided for accessing CloudStack API");
            }

            this.apacheCloudStackClient = new ApacheCloudStackClient(url, apacheCloudStackUser);
        }
    }

    @Nullable
    public Host getHost(@NonNull final String hostName) {
        final ApacheCloudStackRequest request = new ApacheCloudStackRequest("listHosts");
        request.addParameter("hypervisor", "KVM");
        final RootWithHostsList rootWithHostsList = apacheCloudStackClient.executeRequest(request, RootWithHostsList.class);

        return rootWithHostsList.getListHostsResponse().getHostList().stream()
                .filter(host -> host.getName().equals(hostName))
                .findFirst()
                .orElse(null);
    }

    public boolean enableMaintenanceMode(@NonNull final String hostId, @NonNull final String nodeName) throws InterruptedException {
        final ApacheCloudStackRequest request = new ApacheCloudStackRequest("prepareHostForMaintenance");
        request.addParameter("id", hostId);
        final PrepareHostForMaintenanceResponse response = apacheCloudStackClient.executeRequest(request, PrepareHostForMaintenanceResponse.class);
        final QueryAsyncJobResult queryAsyncJobResult = waitJobResult(response.getJobIdResponse().getJobId());

        switch (queryAsyncJobResult.getJobStatus()) {
            case 0:
                log.error("Timeout during waiting while node {} enter in maintenance mode", nodeName);
                return false;
            case 1:
                log.info("Node {} successfully switched into maintenance mode", nodeName);
                return true;
            case 2:
                log.error("Node {} failed to enter in maintenance mode. Error: {}", nodeName, queryAsyncJobResult.getJobResult().getErrorText());
                return false;
            default:
                return false;
        }
    }

    public void disableMaintenanceMode(@NonNull final String hostId, @NonNull final String nodeName) throws InterruptedException {
        final ApacheCloudStackRequest request = new ApacheCloudStackRequest("cancelHostMaintenance");
        request.addParameter("id", hostId);
        final CancelHostMaintenanceResponse response = apacheCloudStackClient.executeRequest(request, CancelHostMaintenanceResponse.class);
        final QueryAsyncJobResult queryAsyncJobResult = waitJobResult(response.getJobIdResponse().getJobId());

        switch (queryAsyncJobResult.getJobStatus()) {
            case 0:
                log.error("Timeout during waiting while node leave {} maintenance mode", nodeName);
                break;
            case 1:
                log.info("Node {} successfully leave maintenance mode", nodeName);
                break;
            case 2:
                log.error("Node {} failed to leave from maintenance mode. Error: {}", nodeName, queryAsyncJobResult.getJobResult().getErrorText());
                break;
        }
    }

    private QueryAsyncJobResult waitJobResult(final String jobId) throws InterruptedException {
        final ApacheCloudStackRequest request = new ApacheCloudStackRequest("queryAsyncJobResult");
        request.addParameter("jobid", jobId);

        int tries = 0;
        QueryAsyncJobResultResponse queryAsyncJobResultResponse = apacheCloudStackClient.executeRequest(request, QueryAsyncJobResultResponse.class);
        final QueryAsyncJobResult queryAsyncJobResult = queryAsyncJobResultResponse.getQueryAsyncJobResult();
        while (queryAsyncJobResult.getJobStatus() == 0 && tries < 16) {
            TimeUnit.SECONDS.sleep(1L + tries);
            queryAsyncJobResultResponse = apacheCloudStackClient.executeRequest(request, QueryAsyncJobResultResponse.class);
            tries += 1;
        }

        return queryAsyncJobResultResponse.getQueryAsyncJobResult();
    }
}

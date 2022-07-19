package org.cftool.cftool.configuration;

import org.apache.commons.io.FileUtils;
import org.cftool.cftool.model.Node;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class NodeConfigurationLoader {

    @Value("${cftool.path.config.hosts}")
    private File hosts;

    @Nullable
    public Node getNodeAddress(@NonNull final String nodeName) throws IOException {
        final List<String> readLines = FileUtils.readLines(hosts, Charset.defaultCharset());

        return readLines.stream()
                .filter(line -> line.contains(nodeName))
                .findFirst()
                .map(this::getNodeFromLine)
                .orElse(null);
    }

    @NonNull
    public List<Node> getNodesAddresses(@NonNull final List<String> nodeNameList) throws IOException {
        final List<String> readLines = FileUtils.readLines(hosts, Charset.defaultCharset());

        return readLines.stream()
                .filter(readLine -> nodeNameList.stream()
                        .anyMatch(readLine::contains))
                .map(this::getNodeFromLine)
                .collect(Collectors.toCollection(() -> new ArrayList<>(nodeNameList.size())));
    }

    private Node getNodeFromLine(final String line) {
        final String[] s = line.split(" ");
        final Node node = new Node();
        node.setNodeAddress(s[0].trim());
        node.setNodeName(s[1].trim());

        return node;
    }
}

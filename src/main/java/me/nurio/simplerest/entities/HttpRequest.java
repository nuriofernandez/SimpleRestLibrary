package me.nurio.simplerest.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.net.InetAddress;
import java.util.Map;
import java.util.StringTokenizer;

@Getter
@RequiredArgsConstructor
public class HttpRequest {

    private final String requestId;
    private final InetAddress remoteAddress;

    private final String method;
    private final String path;
    private final String protocol;

    private final Map<String, String> headers;

    public HttpRequest(String requestId, InetAddress remoteAddress, String startLine, Map<String, String> headers) {
        this.remoteAddress = remoteAddress;
        this.requestId = requestId;
        this.headers = headers;

        // Parse the header
        StringTokenizer parse = new StringTokenizer(startLine);
        method = parse.nextToken().toUpperCase();
        path = parse.nextToken().toLowerCase();
        protocol = parse.nextToken().toUpperCase();
    }

}
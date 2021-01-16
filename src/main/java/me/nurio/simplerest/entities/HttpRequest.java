package me.nurio.simplerest.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.net.InetAddress;
import java.util.Map;
import java.util.StringTokenizer;

@Getter
@RequiredArgsConstructor
public class HttpRequest extends HttpHeadersManagement {

    private final String requestId;
    private InetAddress remoteAddress;

    private String method;
    private String path;
    private String protocol;

    public HttpRequest(String requestId, InetAddress remoteAddress, String startLine, Map<String, String> headers) {
        this.remoteAddress = remoteAddress;
        this.requestId = requestId;

        // Parse raw http contents.
        parseHeaders(headers);
        parseStartLine(startLine);
    }

    /**
     * Parses raw headers to use HttpHeadersManagement.
     *
     * @param headers Map of raw key-value headers.
     */
    private void parseHeaders(Map<String, String> headers) {
        for (Map.Entry<String, String> header : headers.entrySet()) {
            String key = header.getKey();
            for (String value : header.getValue().split(",")) {
                addHeader(key, value);
            }
        }
    }

    /**
     * Parses raw http start line.
     *
     * @param startLine Http raw start line.
     */
    private void parseStartLine(String startLine) {
        StringTokenizer parse = new StringTokenizer(startLine);
        method = parse.nextToken().toUpperCase();
        path = parse.nextToken().toLowerCase();
        protocol = parse.nextToken().toUpperCase();
    }

}
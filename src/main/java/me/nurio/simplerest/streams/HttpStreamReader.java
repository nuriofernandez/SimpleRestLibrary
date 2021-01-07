package me.nurio.simplerest.streams;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpStreamReader extends BufferedReader {

    private String startLine;
    private Map<String, String> headers;
    private String body;

    public HttpStreamReader(@NotNull InputStream inputStream) {
        super(new InputStreamReader(inputStream));
    }

    public Map<String, String> getHeaders() throws IOException {
        if (headers == null) readData();
        return headers;
    }

    public String getStartLine() throws IOException {
        if (startLine == null) readData();
        return startLine;
    }

    public String getBody() {
        throw new RuntimeException("Not yet implemented");
    }

    private void readData() throws IOException {
        startLine = readLine();
        if (startLine == null || !startLine.contains("HTTP/1.1")) {
            throw new RuntimeException("Protocol not supported");
        }

        headers = readAllHeaders()
            .stream()
            .filter(line -> line.contains(":"))
            .map(header -> header.split(": "))
            .filter(headers -> headers.length == 2)
            .collect(Collectors.toMap(header -> header[0], header -> header[1]));

        // TODO Read body
    }

    @NotNull
    private List<@NotNull String> readAllHeaders() throws IOException {
        List<String> headers = new ArrayList<>();

        // Allow up to 100 headers to avoid infinite loops
        for (int intent = 0; intent < 100; intent++) {
            String line = readLine();
            if (line == null) {
                throw new RuntimeException("Invalid headers");
            }
            if (line.isEmpty()) break;
            headers.add(line);
        }

        return headers;
    }

}
package me.nurio.simplerest;

import lombok.RequiredArgsConstructor;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@RequiredArgsConstructor
public class HttpRequest {

    private final HttpConnection httpConnection;

    private final PrintWriter out;
    private final BufferedOutputStream dataOut;

    private final String path;
    private final String method;

    public void process() throws IOException {
        httpConnection.log("Requested method: " + method);
        httpConnection.log("Requested path: " + path);

        String body = "Works!";
        String content = "text/html";
        byte[] fileData = body.getBytes(StandardCharsets.UTF_8);
        int fileLength = fileData.length;

        // send HTTP Headers
        out.println("HTTP/1.1 200 OK");
        out.println("Server: SimpleRestLibrary : 1.0");
        out.println("Date: " + new Date());
        out.println("Content-type: " + content);
        out.println("Content-length: " + fileLength);
        out.println(); // blank line between headers and content, very important !
        out.flush(); // flush character output stream buffer

        dataOut.write(fileData, 0, fileLength);
        dataOut.flush();
    }

}
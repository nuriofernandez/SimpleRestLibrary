package me.nurio.simplerest.streams;

import me.nurio.simplerest.entities.HttpResponse;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;

public class HttpStreamWriter implements AutoCloseable {

    private PrintWriter out;
    private BufferedOutputStream dataOut;

    public HttpStreamWriter(OutputStream stream) {
        out = new PrintWriter(stream);
        dataOut = new BufferedOutputStream(stream);
    }

    public void send(HttpResponse response) throws IOException {
        // Send HTTP Headers
        out.println("HTTP/1.1 " + response.getStatus());
        out.println("Server: SimpleRestLibrary : 1.0");
        out.println("Date: " + new Date());
        out.println("Content-type: " + response.getContentType());
        out.println("Content-length: " + response.getLength());
        out.println(); // blank line between headers and content, very important !
        out.flush(); // flush character output stream buffer

        // Send body
        dataOut.write(response.getBytes(), 0, response.getLength());
        dataOut.flush();
    }

    public void close() throws Exception {
        out.close();
        dataOut.close();
    }

}
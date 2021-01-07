package me.nurio.simplerest;

import lombok.Data;
import me.nurio.events.EventManager;
import me.nurio.simplerest.entities.HttpRequest;
import me.nurio.simplerest.entities.HttpResponse;
import me.nurio.simplerest.events.HttpResponseSendEvent;
import me.nurio.simplerest.streams.HttpStreamReader;

import java.io.*;
import java.net.Socket;
import java.util.Date;

@Data
public class HttpConnectionProcessor extends Thread {

    private static final EventManager eventManager = SimpleRestLibrary.getEventManager();

    private Socket connection;
    private String ipAddress;
    private String requestId;

    public HttpConnectionProcessor(Socket connection) throws IOException {
        this.connection = connection;
        ipAddress = connection.getInetAddress().getHostAddress();
        requestId = Integer.toHexString(hashCode()).substring(0, 5);
        log("Connection started." + "(" + new Date() + ")");
    }

    @Override
    public void run() {
        log("Accepted HTTP request");

        try (
            HttpStreamReader httpStream = new HttpStreamReader(connection.getInputStream());
            PrintWriter out = new PrintWriter(connection.getOutputStream());
            BufferedOutputStream dataOut = new BufferedOutputStream(connection.getOutputStream());
            Socket connection = this.connection
        ) {

            // Define request params
            HttpRequest request = new HttpRequest(
                requestId, connection.getInetAddress(),
                httpStream.getStartLine(), httpStream.getHeaders()
            );

            // Call event handler
            HttpResponseSendEvent sendEvent = new HttpResponseSendEvent(request);
            eventManager.callEvent(sendEvent);

            HttpResponse response = sendEvent.getResponse();

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

        } catch (Exception ioe) {
            log("Server error : " + ioe.getMessage());
            ioe.printStackTrace();
        }

        log("Connection closed");
    }

    public void log(String message) {
        System.out.println("[" + ipAddress + " @ " + requestId + "] >> " + message);
    }

}
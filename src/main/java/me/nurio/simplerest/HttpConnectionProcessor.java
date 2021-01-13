package me.nurio.simplerest;

import me.nurio.events.EventManager;
import me.nurio.simplerest.entities.HttpRequest;
import me.nurio.simplerest.entities.HttpResponse;
import me.nurio.simplerest.events.HttpResponseSendEvent;
import me.nurio.simplerest.streams.HttpStreamReader;
import me.nurio.simplerest.streams.HttpStreamWriter;

import java.io.*;
import java.net.Socket;
import java.util.Date;

public class HttpConnectionProcessor extends Thread {

    private final EventManager eventManager;

    private Socket connection;
    private String ipAddress;
    private String requestId;

    public HttpConnectionProcessor(EventManager eventManager, Socket connection) throws IOException {
        this.eventManager = eventManager;
        this.connection = connection;

        ipAddress = connection.getInetAddress().getHostAddress();
        requestId = Integer.toHexString(hashCode()).substring(0, 5);
        log("Connection started." + "(" + new Date() + ")");
    }

    @Override
    public void run() {
        log("Accepted HTTP request");

        try (
            HttpStreamReader httpReader = new HttpStreamReader(connection.getInputStream());
            HttpStreamWriter httpWriter = new HttpStreamWriter(connection.getOutputStream());
            Socket connection = this.connection
        ) {

            // Define request params
            HttpRequest request = new HttpRequest(
                requestId, connection.getInetAddress(),
                httpReader.getStartLine(), httpReader.getHeaders()
            );

            // Call event handler
            HttpResponseSendEvent sendEvent = new HttpResponseSendEvent(request);
            eventManager.callEvent(sendEvent);

            // Send HTTP response
            HttpResponse response = sendEvent.getResponse();
            httpWriter.send(response);

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
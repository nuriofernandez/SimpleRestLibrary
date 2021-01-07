package me.nurio.simplerest;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.nurio.events.EventManager;

import java.io.IOException;
import java.net.ServerSocket;

@Getter
@RequiredArgsConstructor
public class SimpleRestServer extends Thread {

    private EventManager eventManager = new EventManager();
    private final int port;

    private boolean online;

    public SimpleRestServer(EventManager eventManager, int port) {
        this.eventManager = eventManager;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverConnect = new ServerSocket(port);
            System.out.println("Server started.");
            System.out.println("Listening for connections on port : " + port + " ...");
            online = true;

            // we listen until user halts server execution
            while (true) {
                HttpConnectionProcessor myServer = new HttpConnectionProcessor(eventManager, serverConnect.accept());
                myServer.start();
            }
        } catch (IOException e) {
            System.err.println("Server Connection error : " + e.getMessage());
        } finally {
            online = false;
        }
    }

    @SneakyThrows
    public void waitUntilOnline() {
        while (!online) Thread.sleep(50);
    }

}
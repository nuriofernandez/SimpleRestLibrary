package me.nurio.simplerest;

import java.io.IOException;
import java.net.ServerSocket;

public class SimpleRestLibrary {

    static final int PORT = 8080;

    public static void main(String[] args) {
        try {
            ServerSocket serverConnect = new ServerSocket(PORT);
            System.out.println("Server started.");
            System.out.println("Listening for connections on port : " + PORT + " ...");

            // we listen until user halts server execution
            while (true) {
                HttpConnection myServer = new HttpConnection(serverConnect.accept());
                myServer.start();
            }
        } catch (IOException e) {
            System.err.println("Server Connection error : " + e.getMessage());
        }
    }

}
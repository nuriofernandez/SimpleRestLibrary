package me.nurio.simplerest;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SimpleRestLibrary {

    public static SimpleRestServer startInstance(int port) {
        SimpleRestServer instance = new SimpleRestServer(port);
        instance.start();
        return instance;
    }

    public static void main(String[] args) {
        startInstance(8080);
    }

}
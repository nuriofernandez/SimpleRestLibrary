package me.nurio.simplerest;

import lombok.Getter;
import org.junit.After;
import org.junit.Before;

import java.util.Random;

public class TestingSimpleRestServer {

    @Getter private SimpleRestServer testingServer;

    @Before
    public void setupTestingSimpleRestServer() {
        // Start the http server on a random port
        int randomPort = new Random().nextInt(2000) + 40000;
        testingServer = SimpleRestLibrary.startInstance(randomPort);

        // Wait to server to start
        testingServer.waitUntilOnline();
    }

    @After
    public void shutDownTestingSimpleRestServer() {
        // Shutdown http server instance
        testingServer.interrupt();
    }

}
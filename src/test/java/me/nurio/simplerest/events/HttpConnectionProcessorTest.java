package me.nurio.simplerest.events;

import me.nurio.events.handler.EventHandler;
import me.nurio.events.handler.EventListener;
import me.nurio.simplerest.HttpTestingUtil;
import me.nurio.simplerest.TestingSimpleRestServer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class HttpConnectionProcessorTest extends TestingSimpleRestServer {

    @Before
    public void setup() {
        // Register event manager and register this test listener
        getTestingServer().getEventManager().registerEvents(new HttpListener());
    }

    @Test
    public void httpCall_shouldRespondWithTheHttpEventListenerResponse_whenProvidedPathMatchesWhatTheListenerExpects() throws IOException {
        String response = HttpTestingUtil.get("http://localhost:" + getTestingServer().getPort() + "/listener-expected-path");
        assertEquals("test-expected-response", response);
    }

    public static class HttpListener implements EventListener {

        @EventHandler
        public void onPotato(HttpResponseSendEvent event) {
            String path = event.getRequest().getPath();
            if (!path.toLowerCase().contains("listener-expected-path")) return;
            event.getResponse().setBody("test-expected-response");
        }

    }

}
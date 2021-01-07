package me.nurio.simplerest.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.nurio.events.handler.Event;
import me.nurio.simplerest.entities.HttpRequest;
import me.nurio.simplerest.entities.HttpResponse;

@Getter
@AllArgsConstructor
public class HttpResponseSendEvent extends Event {

    private final HttpRequest request;
    private final HttpResponse response;

    public HttpResponseSendEvent(HttpRequest request) {
        this.request = request;
        this.response = new HttpResponse();
    }

}
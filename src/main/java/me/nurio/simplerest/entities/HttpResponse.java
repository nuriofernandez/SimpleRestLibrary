package me.nurio.simplerest.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;

@Data
@NoArgsConstructor
public class HttpResponse {

    private String contentType = "text/html";

    private String body = "";
    private byte[] bytes = body.getBytes(StandardCharsets.UTF_8);

    private HttpStatus status = HttpStatus.OK;

    public HttpResponse(String contentType, String body) {
        setContentType(contentType);
        setBody(body);
    }

    public void setBody(String body) {
        this.body = body;
        this.bytes = body.getBytes(StandardCharsets.UTF_8);
    }

    public int getLength() {
        return bytes.length;
    }

}
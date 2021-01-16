package me.nurio.simplerest.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class HttpHeadersManagement {

    private Map<String, List<String>> headers = new HashMap<>();

    public List<String> getHeaders() {
        return List.copyOf(headers.keySet());
    }

    public List<String> getHeader(String key) {
        if (!headers.containsKey(key)) headers.put(key, new ArrayList<>());
        return headers.get(key);
    }

    public boolean containsHeader(String key) {
        return headers.containsKey(key);
    }

    public boolean containsHeader(String key, String value) {
        if (!containsHeader(key)) return false;
        return getHeader(key).contains(value);
    }

    public void removeHeader(String key) {
        if (!containsHeader(key)) return;
        headers.remove(key);
    }

    public void removeHeader(String key, String value) {
        if (!containsHeader(key, value)) return;
        getHeader(key).remove(value);
    }

    public void addHeader(String key, String value) {
        if (!containsHeader(key)) return;
        if (containsHeader(key, value)) return;
        getHeader(key).add(value);
    }

}
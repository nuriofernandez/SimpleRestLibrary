package me.nurio.simplerest.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HttpStatus {

    OK(200, "OK");

    private int code;
    private String description;

    public String toString() {
        return code + " " + description;
    }

}
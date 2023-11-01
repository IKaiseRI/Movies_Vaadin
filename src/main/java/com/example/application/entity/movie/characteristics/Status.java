package com.example.application.entity.movie.characteristics;

import java.util.HashMap;
import java.util.Map;

public enum Status {
    RELEASED("Released"),
    POST_PRODUCTION("Post Production"),
    IN_PRODUCTION("In Production"),
    PLANNED("Planned");
    private static final Map<String, Status> stringToEnum = new HashMap<>();

    private final String value;

    Status(String value) {
        this.value = value;
    }

    static {
        for (Status status : values()) {
            stringToEnum.put(status.value, status);
        }
    }

    public static Status fromString(String value) {
        return stringToEnum.get(value);
    }

    public static String getStringValue(Status status) {
        return status.value;
    }
}

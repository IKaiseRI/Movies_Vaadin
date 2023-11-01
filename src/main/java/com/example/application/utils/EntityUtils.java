package com.example.application.utils;

import com.example.application.entity.movie.characteristics.Status;

import java.util.Arrays;
import java.util.List;

public class EntityUtils {

    public static Status convertToStatus(String input) {
        return Status.fromString(input);
    }

    public static Integer convertLongToInteger(Long id) {
        return (id != null) ? id.intValue() : 0;
    }

    public static Long convertIntegerToLong(Integer id) {
        return (id != null) ? id.longValue() : 0L;
    }

    public static List<String> parseGenresFromString(String stringOfGenres) {
        return Arrays.stream(stringOfGenres.split(", ")).toList();
    }

}

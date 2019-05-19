package com.joe.preview.data.local.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.joe.preview.data.local.entity.Movie;

import java.lang.reflect.Type;
import java.util.List;

public class MovieConverter {

    @TypeConverter
    public List<Movie> fromString(String value) {
        Type listType = new TypeToken<List<Movie>>() {
        }.getType();

        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public String fromList(List<Movie> movies) {
        return new Gson().toJson(movies);
    }

}

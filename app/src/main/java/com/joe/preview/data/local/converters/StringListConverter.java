package com.joe.preview.data.local.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class StringListConverter {

    @TypeConverter
    public List<String> fromString(String value) {
        Type listType = new TypeToken<List<String>>() {
        }.getType();

        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public  String fromList(List<String> videos) {
        return new Gson().toJson(videos);
    }

}

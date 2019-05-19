package com.joe.preview.data.local.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.joe.preview.data.local.entity.Series;

import java.lang.reflect.Type;
import java.util.List;

public class SeriesConverter {

    @TypeConverter
    public List<Series> fromString(String value) {
        Type listType = new TypeToken<List<Series>>() {
        }.getType();

        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public String fromList(List<Series> series) {
        return new Gson().toJson(series);
    }

}

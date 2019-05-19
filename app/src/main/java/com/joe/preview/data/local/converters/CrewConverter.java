package com.joe.preview.data.local.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.joe.preview.data.remote.model.Crew;

import java.lang.reflect.Type;
import java.util.List;

public class CrewConverter {

    @TypeConverter
    public List<Crew> fromString(String value) {
        Type listType = new TypeToken<List<Crew>>() {
        }.getType();

        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public String fromList(List<Crew> crews) {
        return new Gson().toJson(crews);
    }

}

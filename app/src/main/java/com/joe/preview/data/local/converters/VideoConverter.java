package com.joe.preview.data.local.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.joe.preview.data.remote.Video;

import java.lang.reflect.Type;
import java.util.List;

public class VideoConverter {

    @TypeConverter
    public List<Video> fromString(String value) {
        Type listType = new TypeToken<List<Video>>() {
        }.getType();

        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public String fromList(List<Video> videos) {
        return new Gson().toJson(videos);
    }

}

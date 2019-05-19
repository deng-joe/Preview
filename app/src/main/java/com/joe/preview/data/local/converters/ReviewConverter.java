package com.joe.preview.data.local.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.joe.preview.data.remote.Review;

import java.lang.reflect.Type;
import java.util.List;

public class ReviewConverter {

    @TypeConverter
    public List<Review> fromString(String value) {
        Type listType = new TypeToken<List<Review>>() {
        }.getType();

        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public String fromList(List<Review> reviews) {
        return new Gson().toJson(reviews);
    }

}

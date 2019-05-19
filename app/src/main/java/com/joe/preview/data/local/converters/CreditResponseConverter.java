package com.joe.preview.data.local.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.joe.preview.data.remote.model.CreditResponse;

import java.lang.reflect.Type;

public class CreditResponseConverter {

    @TypeConverter
    public CreditResponse fromString(String value) {
        Type listType = new TypeToken<CreditResponse>() {
        }.getType();

        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public String fromList(CreditResponse creditResponse) {
        return new Gson().toJson(creditResponse);
    }

}

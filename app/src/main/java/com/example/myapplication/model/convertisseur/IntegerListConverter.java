package com.example.myapplication.model.convertisseur;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class IntegerListConverter {
    @TypeConverter
    public static String fromIntegerList(List<Integer> integers) {
        if (integers == null) {
            return null;
        }
        Gson gson = new Gson();
        return gson.toJson(integers);
    }

    @TypeConverter
    public static List<Integer> toIntegerList(String integerListString) {
        if (integerListString == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Integer>>() {}.getType();
        return gson.fromJson(integerListString, type);
    }
}

package com.example.myapplication.bddsqlite;

import android.content.Context;

import androidx.room.Room;

import com.example.myapplication.bddsqlite.database.AppDatabase;

public class ConnectBddSqlite {
    public static AppDatabase connectBdd(Context context){
        return Room.databaseBuilder(context, AppDatabase.class, "dataoffline")
                .fallbackToDestructiveMigration()
                .build();
    }

}

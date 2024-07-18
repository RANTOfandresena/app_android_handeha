package com.example.myapplication.bddsqlite;

import android.content.Context;

import androidx.room.Room;

import com.example.myapplication.bddsqlite.database.AppDatabase;

public class ConnectBddSqlite {
    public static AppDatabase connectBdd(Context context){
        return Room.databaseBuilder(context,
                        AppDatabase.class, "dataoffline")
                .allowMainThreadQueries() // Utiliser cela seulement pour les tests, préférer les exécuteurs de fond pour la production
                .build();
    }

}

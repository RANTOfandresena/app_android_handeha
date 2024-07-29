package com.example.myapplication.bddsqlite.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.myapplication.bddsqlite.dao.PaiementDao;
import com.example.myapplication.bddsqlite.dao.ReservationDao;
import com.example.myapplication.bddsqlite.dao.TrajetDao;
import com.example.myapplication.model.ReservationModel;
import com.example.myapplication.model.PaiementModel;
import com.example.myapplication.model.TrajetModel;
import com.example.myapplication.model.convertisseur.IntegerListConverter;
import com.example.myapplication.model.convertisseur.PaiementModelConverter;
import com.example.myapplication.model.convertisseur.TrajetModelConverter;
import com.example.myapplication.model.convertisseur.UtilisateurModelConverter;

@Database(entities = {TrajetModel.class, ReservationModel.class, PaiementModel.class}, version = 1,exportSchema = false  )
@TypeConverters({UtilisateurModelConverter.class, IntegerListConverter.class, TrajetModelConverter.class, PaiementModelConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract TrajetDao trajetDao();
    public abstract ReservationDao reservationDao();
    public abstract PaiementDao paiementDao();
}

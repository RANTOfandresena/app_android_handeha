package com.example.myapplication.bddsqlite.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.model.TrajetModel;

import java.util.List;

@Dao
public interface TrajetDao {
    @Insert
    void insertTrajets(List<TrajetModel> trajets);
    @Query("DELETE FROM trajet")
    void viderTable();
    @Query("SELECT * FROM trajet WHERE idTrajet=:id")
    TrajetModel getTrajetId(String id);
    @Update
    void updateTrajet(TrajetModel trajet);

    @Query("SELECT * FROM trajet")
    List<TrajetModel> getAllTrajets();
}


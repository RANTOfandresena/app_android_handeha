package com.example.myapplication.bddsqlite.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.myapplication.model.PaiementModel;

import java.util.List;
@Dao
public interface PaiementDao {
    @Insert
    void insertPaiement(PaiementModel paiement);
    @Query("DELETE FROM paiementModel")
    void supprPaiements();
    @Query("SELECT * FROM paiementModel")
    List<PaiementModel> getAllPaiements();
}

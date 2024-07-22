package com.example.myapplication.bddsqlite.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.myapplication.model.ReservationModel;

import java.util.List;
@Dao
public interface ReservationDao {
    @Insert
    void insertReservations(List<ReservationModel> reservations);
    @Insert
    void insertReservation(ReservationModel reservations);
    @Query("DELETE FROM reservation WHERE idTrajet=:id")
    void viderTable(String id);

    @Query("DELETE FROM reservation")
    void viderTable();
    @Query("SELECT * FROM reservation WHERE idTrajet=:id")
    List<ReservationModel> getAllReservations(String id);

    @Query("SELECT * FROM reservation")
    List<ReservationModel> getAllReservations();
}

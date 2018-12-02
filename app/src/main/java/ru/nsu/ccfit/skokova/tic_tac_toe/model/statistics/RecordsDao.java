package ru.nsu.ccfit.skokova.tic_tac_toe.model.statistics;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface RecordsDao {
    @Query("SELECT * from record")
    Flowable<List<Record>> getRecords();

    @Insert
    void insert(Record record);
}

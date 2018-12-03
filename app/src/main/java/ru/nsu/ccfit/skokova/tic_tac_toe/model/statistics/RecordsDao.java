package ru.nsu.ccfit.skokova.tic_tac_toe.model.statistics;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Maybe;

@Dao
public interface RecordsDao {
    @Query("SELECT * from record")
    Maybe<List<Record>> getRecords();

    @Query("SELECT * from record WHERE record_author = :recordAuthorId")
    Maybe<Record> getRecordByAuthor(int recordAuthorId);

    @Update
    void updateRecord(Record record);

    @Insert
    void insertRecord(Record record);
}

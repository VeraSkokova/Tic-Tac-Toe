package ru.nsu.ccfit.skokova.tic_tac_toe.model.statistics;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Record.class}, version = 1)
public abstract class RecordsDatabase extends RoomDatabase {
    public abstract RecordsDao recordsDao();
}

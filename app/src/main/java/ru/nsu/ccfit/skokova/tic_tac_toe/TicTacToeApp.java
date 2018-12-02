package ru.nsu.ccfit.skokova.tic_tac_toe;

import android.app.Application;
import android.arch.persistence.room.Room;

import ru.nsu.ccfit.skokova.tic_tac_toe.model.statistics.RecordsDatabase;

public class TicTacToeApp extends Application {
    public static final String DATABASE = "database";
    public static TicTacToeApp instance;

    private RecordsDatabase recordsDatabase;

    public static TicTacToeApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        recordsDatabase = Room.databaseBuilder(this, RecordsDatabase.class, DATABASE)
                .build();
    }

    public RecordsDatabase getRecordsDatabase() {
        return recordsDatabase;
    }
}

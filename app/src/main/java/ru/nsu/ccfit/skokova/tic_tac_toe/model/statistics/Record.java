package ru.nsu.ccfit.skokova.tic_tac_toe.model.statistics;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Record {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "wins_count")
    private int winsCount;

    public Record(String name, int winsCount) {
        this.name = name;
        this.winsCount = winsCount;
    }

    public String getName() {
        return name;
    }

    public int getWinsCount() {
        return winsCount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWinsCount(int winsCount) {
        this.winsCount = winsCount;
    }

    public void addWin() {
        ++winsCount;
    }
}

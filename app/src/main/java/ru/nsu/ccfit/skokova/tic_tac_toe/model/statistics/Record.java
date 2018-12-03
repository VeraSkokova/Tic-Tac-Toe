package ru.nsu.ccfit.skokova.tic_tac_toe.model.statistics;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

@Entity
public class Record {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "record_author")
    @TypeConverters(RecordAuthorConverter.class)
    private RecordAuthor recordAuthor;

    @ColumnInfo(name = "wins_count")
    private int winsCount;

    public Record(RecordAuthor recordAuthor, int winsCount) {
        this.recordAuthor = recordAuthor;
        this.winsCount = winsCount;
    }

    public RecordAuthor getRecordAuthor() {
        return recordAuthor;
    }

    public int getWinsCount() {
        return winsCount;
    }

    public void setRecordAuthor(RecordAuthor recordAuthor) {
        this.recordAuthor = recordAuthor;
    }

    public void setWinsCount(int winsCount) {
        this.winsCount = winsCount;
    }

    public void addWin() {
        ++winsCount;
    }
}

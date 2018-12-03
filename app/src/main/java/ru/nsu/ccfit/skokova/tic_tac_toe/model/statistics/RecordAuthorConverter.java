package ru.nsu.ccfit.skokova.tic_tac_toe.model.statistics;

import android.arch.persistence.room.TypeConverter;

public class RecordAuthorConverter {
    @TypeConverter
    public RecordAuthor fromInteger(int value) {
        if (value == RecordAuthor.USER.ordinal()) {
            return RecordAuthor.USER;
        } else if (value == RecordAuthor.COMPUTER.ordinal()) {
            return RecordAuthor.COMPUTER;
        }
        return null;
    }

    @TypeConverter
    public int fromRecrodAuthor(RecordAuthor recordAuthor) {
        return recordAuthor.ordinal();
    }
}

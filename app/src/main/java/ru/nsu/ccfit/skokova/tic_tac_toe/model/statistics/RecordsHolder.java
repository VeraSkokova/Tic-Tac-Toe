package ru.nsu.ccfit.skokova.tic_tac_toe.model.statistics;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import ru.nsu.ccfit.skokova.tic_tac_toe.TicTacToeApp;

public class RecordsHolder {
    private List<Record> recordList;

    public void readRecords() {
        RecordsDatabase recordsDatabase = TicTacToeApp.getInstance().getRecordsDatabase();
        recordsDatabase.recordsDao().getRecords()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(records -> recordList = records);

    }

    public List<Record> getRecordList() {
        return recordList;
    }
}

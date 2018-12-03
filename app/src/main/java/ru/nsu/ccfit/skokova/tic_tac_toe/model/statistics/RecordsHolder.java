package ru.nsu.ccfit.skokova.tic_tac_toe.model.statistics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;
import ru.nsu.ccfit.skokova.tic_tac_toe.TicTacToeApp;
import ru.nsu.ccfit.skokova.tic_tac_toe.presenter.StatisticsPresenter;

public class RecordsHolder {
    private Map<RecordAuthor, Integer> recordsMap;

    private StatisticsPresenter statisticsPresenter;

    public RecordsHolder() {
        recordsMap = new HashMap<>();
        recordsMap.put(RecordAuthor.USER, 0);
        recordsMap.put(RecordAuthor.COMPUTER, 0);
    }

    public void setStatisticsPresenter(StatisticsPresenter statisticsPresenter) {
        this.statisticsPresenter = statisticsPresenter;
    }

    public void readRecords() {
        RecordsDatabase database = TicTacToeApp.getInstance().getRecordsDatabase();
        RecordsDao recordsDao = database.recordsDao();

        recordsDao.getRecords().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableMaybeObserver<List<Record>>() {
                    @Override
                    public void onSuccess(List<Record> records) {
                        processRecords(records);
                    }

                    @Override
                    public void onError(Throwable e) {
                        statisticsPresenter.onUserRecordReady(0);
                        statisticsPresenter.onUserRecordReady(0);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void processRecords(List<Record> records) {
        for (Record record : records) {
            int winsCount = record.getWinsCount();
            recordsMap.put(record.getRecordAuthor(), winsCount);
        }
        statisticsPresenter.onUserRecordReady(recordsMap.get(RecordAuthor.USER));
        statisticsPresenter.onComputerRecordReady(recordsMap.get(RecordAuthor.COMPUTER));
    }
}

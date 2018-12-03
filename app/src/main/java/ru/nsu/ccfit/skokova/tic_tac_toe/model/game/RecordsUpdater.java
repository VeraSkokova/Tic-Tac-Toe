package ru.nsu.ccfit.skokova.tic_tac_toe.model.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;
import ru.nsu.ccfit.skokova.tic_tac_toe.TicTacToeApp;
import ru.nsu.ccfit.skokova.tic_tac_toe.model.Updater;
import ru.nsu.ccfit.skokova.tic_tac_toe.model.statistics.Record;
import ru.nsu.ccfit.skokova.tic_tac_toe.model.statistics.RecordAuthor;
import ru.nsu.ccfit.skokova.tic_tac_toe.model.statistics.RecordsDao;
import ru.nsu.ccfit.skokova.tic_tac_toe.model.statistics.RecordsDatabase;

public class RecordsUpdater implements Updater {
    private static final Logger logger = LoggerFactory.getLogger(RecordsUpdater.class);

    @Override
    public void updateRecord(RecordAuthor recordAuthor) {
        RecordsDatabase database = TicTacToeApp.getInstance().getRecordsDatabase();
        RecordsDao recordsDao = database.recordsDao();

        recordsDao.getRecordByAuthor(recordAuthor.ordinal())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableMaybeObserver<Record>() {
                    @Override
                    public void onSuccess(Record record) {
                        logger.debug("success");
                        record.addWin();
                        onRecordExists(record, recordsDao);
                    }

                    @Override
                    public void onError(Throwable e) {
                        logger.debug("error");
                    }

                    @Override
                    public void onComplete() {
                        logger.debug("Complete");
                        onNewRecord(recordsDao, recordAuthor);
                    }
                });
    }

    private void onNewRecord(RecordsDao recordsDao, RecordAuthor recordAuthor) {
        Completable.fromAction(() -> recordsDao.insertRecord(new Record(recordAuthor, 1)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    private void onRecordExists(Record record, RecordsDao recordsDao) {
        Completable.fromAction(() -> recordsDao.updateRecord(record))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe();
    }
}

package ru.nsu.ccfit.skokova.tic_tac_toe.presenter;

import ru.nsu.ccfit.skokova.tic_tac_toe.model.statistics.RecordsHolder;
import ru.nsu.ccfit.skokova.tic_tac_toe.view.StatisticsView;

public class StatisticsPresenter {
    private StatisticsView statisticsView;

    private RecordsHolder recordsHolder;

    public StatisticsPresenter() {
        recordsHolder = new RecordsHolder();
        recordsHolder.setStatisticsPresenter(this);
    }

    public void onStatsShow() {
        recordsHolder.readRecords();
    }

    public void attachView(StatisticsView statisticsView) {
        this.statisticsView = statisticsView;
    }

    public void onUserRecordReady(int winsCount) {
        statisticsView.showUserWins(winsCount);
    }

    public void onComputerRecordReady(int winsCount) {
        statisticsView.showComputerWins(winsCount);
    }
}

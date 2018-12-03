package ru.nsu.ccfit.skokova.tic_tac_toe.presenter;

public class PresenterHolder {
    private final GamePresenter gamePresenter;
    private final StatisticsPresenter statisticsPresenter;

    public PresenterHolder() {
        this.gamePresenter = new GamePresenter();
        this.statisticsPresenter = new StatisticsPresenter();
    }

    public GamePresenter getGamePresenter() {
        return gamePresenter;
    }

    public StatisticsPresenter getStatisticsPresenter() {
        return statisticsPresenter;
    }
}

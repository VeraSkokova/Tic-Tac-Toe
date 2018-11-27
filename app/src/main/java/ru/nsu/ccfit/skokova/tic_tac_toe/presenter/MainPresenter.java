package ru.nsu.ccfit.skokova.tic_tac_toe.presenter;

import ru.nsu.ccfit.skokova.tic_tac_toe.model.Game;
import ru.nsu.ccfit.skokova.tic_tac_toe.view.MainView;

public class MainPresenter {
    private MainView view;
    private Game game;

    public MainPresenter() {
        this.game = new Game(3);
        this.game.setPresenter(this);
    }

    public void attachView(MainView mainView) {
        this.view = mainView;
    }

    public void detachView() {
        this.view = null;
    }

    public void onSizeChanged(int size) {
        view.changeSize(size);
    }

    public void changeFieldSize(int newSize) {
        game.changeSize(newSize);
    }
}

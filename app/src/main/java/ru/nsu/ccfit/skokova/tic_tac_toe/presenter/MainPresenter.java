package ru.nsu.ccfit.skokova.tic_tac_toe.presenter;

import ru.nsu.ccfit.skokova.tic_tac_toe.model.Game;
import ru.nsu.ccfit.skokova.tic_tac_toe.model.field.Cell;
import ru.nsu.ccfit.skokova.tic_tac_toe.view.MainView;

public class MainPresenter {
    private MainView view;
    private Game game;

    public MainPresenter() {
        game = new Game();
        game.setPresenter(this);
    }

    public void attachView(MainView mainView) {
        this.view = mainView;
    }

    public void detachView() {
        this.view = null;
    }

    public void onSizeChanged(int size) {
        view.drawNewField(size);
    }

    public void changeFieldSize(int newSize) {
        game.changeField(newSize);
    }

    public void performStep(int x, int y) {
        game.performUserStep(x, y);
    }

    public void onUserStep(int cellX, int cellY) {
        view.showUserStep(cellX, cellY);
    }

    public void viewIsReady() {
        onSizeChanged(3); //TODO : find best way to pass default size
    }

    public void onComputerStep(Cell cell) {
        view.showComputerStep(cell.getCellX(), cell.getCellY());
    }
}

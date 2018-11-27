package ru.nsu.ccfit.skokova.tic_tac_toe.model;

import ru.nsu.ccfit.skokova.tic_tac_toe.presenter.MainPresenter;

public class Game {
    private MainPresenter presenter;

    private Field field;

    public Game(int fieldSize) {
        field = new Field(fieldSize);
        field.init();
    }

    public void changeSize(int newSize) {
        field = new Field(newSize);
        field.init();
        presenter.onSizeChanged(newSize);
    }

    public void setPresenter(MainPresenter presenter) {
        this.presenter = presenter;
    }

    public void performUserStep(int cellX, int cellY) {
        field.userStep(cellX, cellY);
        presenter.onUserStep(cellX, cellY);
    }
}

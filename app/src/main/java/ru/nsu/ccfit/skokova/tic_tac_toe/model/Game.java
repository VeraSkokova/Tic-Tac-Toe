package ru.nsu.ccfit.skokova.tic_tac_toe.model;

import ru.nsu.ccfit.skokova.tic_tac_toe.presenter.MainPresenter;

public class Game {
    private MainPresenter presenter;

    private Field field;

    public Game(int fieldSize) {
        this.field = new Field(fieldSize);
    }

    public void changeSize(int newSize) {
        field = new Field(newSize);
        presenter.onSizeChanged(newSize);
    }

    public void setPresenter(MainPresenter presenter) {
        this.presenter = presenter;
    }
}

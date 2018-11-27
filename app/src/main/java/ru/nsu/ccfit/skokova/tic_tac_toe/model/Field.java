package ru.nsu.ccfit.skokova.tic_tac_toe.model;

import ru.nsu.ccfit.skokova.tic_tac_toe.presenter.MainPresenter;

public class Field {
    private MainPresenter mainPresenter;

    private int size;

    public Field(int size) {
        this.size = size;
    }

    public void setSize(int fieldSize) {
        this.size = fieldSize;
        mainPresenter.onSizeChanged(size);
    }
}

package ru.nsu.ccfit.skokova.tic_tac_toe.model;

import java.util.ArrayList;
import java.util.List;

import ru.nsu.ccfit.skokova.tic_tac_toe.presenter.MainPresenter;

public class Field {
    private MainPresenter mainPresenter;

    private int size;
    private List<Cell> cells;

    public Field(int size) {
        this.size = size;
        this.cells = new ArrayList<>();
    }

    public void init() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells.add(new Cell(i, j));
            }
        }
    }

    public void setSize(int fieldSize) {
        this.size = fieldSize;
        mainPresenter.onSizeChanged(size);
    }

    public void userStep(int cellX, int cellY) {
        Cell cell = cells.get(cellX * size + cellY);

        if (cell.getCellState() != CellState.UNDEFINED) {
            return;
        }

        cell.setCellState(CellState.CROSS);
    }
}

package ru.nsu.ccfit.skokova.tic_tac_toe.model;

public class Cell {
    private final int cellX;
    private final int cellY;
    private CellState cellState;

    public Cell(int cellX, int cellY) {
        this.cellX = cellX;
        this.cellY = cellY;
        this.cellState = CellState.UNDEFINED;
    }

    public int getCellX() {
        return cellX;
    }

    public int getCellY() {
        return cellY;
    }

    public CellState getCellState() {
        return cellState;
    }

    public void setCellState(CellState cellState) {
        this.cellState = cellState;
    }
}

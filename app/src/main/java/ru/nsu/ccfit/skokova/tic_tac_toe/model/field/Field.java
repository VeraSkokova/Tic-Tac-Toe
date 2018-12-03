package ru.nsu.ccfit.skokova.tic_tac_toe.model.field;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Field {
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

    public int getSize() {
        return size;
    }

    public Cell getCell(int x, int y) {
        return cells.get(x * size + y);
    }

    public List<Cell> getRow(int rowNumber) {
        List<Cell> rowCells = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            rowCells.add(getCell(i, rowNumber));
        }
        return rowCells;
    }

    public List<Cell> getColumn(int columnNumber) {
        List<Cell> columnCells = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            columnCells.add(getCell(columnNumber, i));
        }
        return columnCells;
    }

    public List<Cell> getMainDiagonal() {
        List<Cell> diagonalCells = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            diagonalCells.add(getCell(i, i));
        }
        return diagonalCells;
    }

    public List<Cell> getIndirectDiagonal() {
        List<Cell> indirectDiagonalCells = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            indirectDiagonalCells.add(getCell(i, size - i - 1));
        }
        return indirectDiagonalCells;
    }

    public boolean isCellInField(int cellX, int cellY) {
        return cellX >= 0 && cellX < size && cellY >= 0 && cellY < size;
    }

    public List<Cell> getCellsList() {
        return Collections.unmodifiableList(cells);
    }
}

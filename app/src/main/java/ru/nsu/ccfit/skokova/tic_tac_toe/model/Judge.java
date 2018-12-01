package ru.nsu.ccfit.skokova.tic_tac_toe.model;

import java.util.List;

import ru.nsu.ccfit.skokova.tic_tac_toe.model.field.Cell;
import ru.nsu.ccfit.skokova.tic_tac_toe.model.field.CellState;
import ru.nsu.ccfit.skokova.tic_tac_toe.model.field.Field;

public class Judge {
    private int stepsCount;

    public Judge(int fieldSize) {
        this.stepsCount = fieldSize * fieldSize;
    }

    public boolean isWin(Cell lastStep, Field field) {
        int rowNumber = lastStep.getCellY();
        int columnNumber = lastStep.getCellX();
        CellState lastStepState = lastStep.getCellState();

        List<Cell> row = field.getRow(rowNumber);
        if (checkLine(row, lastStepState)) {
            return true;
        }

        List<Cell> column = field.getColumn(columnNumber);
        if (checkLine(column, lastStepState)) {
            return true;
        }

        if (rowNumber == columnNumber) {
            List<Cell> mainDiagonal = field.getMainDiagonal();
            if (checkLine(mainDiagonal, lastStepState)) {
                return true;
            }
        }

        if (field.getSize() - rowNumber == columnNumber + 1) {
            List<Cell> indirectDiagonal = field.getIndirectDiagonal();
            return checkLine(indirectDiagonal, lastStepState);
        }

        return false;
    }

    public boolean isDraw() {
        return stepsCount == 0;
    }

    public void stepDone() {
        --stepsCount;
    }

    private boolean checkLine(List<Cell> line, CellState necessaryState) {
        for (Cell cell : line) {
            if (cell.getCellState() != necessaryState) {
                return false;
            }
        }
        return true;
    }
}

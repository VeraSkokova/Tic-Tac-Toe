package ru.nsu.ccfit.skokova.tic_tac_toe.model.player;

import ru.nsu.ccfit.skokova.tic_tac_toe.model.field.Cell;
import ru.nsu.ccfit.skokova.tic_tac_toe.model.field.CellState;
import ru.nsu.ccfit.skokova.tic_tac_toe.model.field.Field;

public class UserPlayer implements Player {
    @Override
    public Cell makeStep(Field field, Cell lastUserStepCell) {
        if (lastUserStepCell.getCellState() != CellState.UNDEFINED) {
            return null;
        }

        lastUserStepCell.setCellState(CellState.CROSS);
        return lastUserStepCell;
    }
}

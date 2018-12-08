package ru.nsu.ccfit.skokova.tic_tac_toe.model.player;

import ru.nsu.ccfit.skokova.tic_tac_toe.model.field.Cell;
import ru.nsu.ccfit.skokova.tic_tac_toe.model.field.CellState;
import ru.nsu.ccfit.skokova.tic_tac_toe.model.field.Field;
import ru.nsu.ccfit.skokova.tic_tac_toe.model.game.StepMode;

public class UserPlayer implements Player {
    @Override
    public void makeStep(Field field, Cell lastUserStepCell, StepCallback stepCallback) {
        if (lastUserStepCell.getCellState() != CellState.UNDEFINED) {
            return;
        }

        lastUserStepCell.setCellState(CellState.CROSS);
        stepCallback.onStepMade(lastUserStepCell, StepMode.CROSS);
    }
}

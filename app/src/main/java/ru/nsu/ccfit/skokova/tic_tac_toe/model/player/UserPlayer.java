package ru.nsu.ccfit.skokova.tic_tac_toe.model.player;

import ru.nsu.ccfit.skokova.tic_tac_toe.model.field.Cell;
import ru.nsu.ccfit.skokova.tic_tac_toe.model.field.CellState;
import ru.nsu.ccfit.skokova.tic_tac_toe.model.field.Field;

public class UserPlayer implements Player {
    private CellState stepMode = CellState.CROSS;

    public void setStepMode(CellState stepMode) {
        this.stepMode = stepMode;
    }

    @Override
    public void makeStep(Field field, Cell lastUserStepCell, StepCallback stepCallback) {
        if (lastUserStepCell.getCellState() != CellState.UNDEFINED) {
            return;
        }

        lastUserStepCell.setCellState(stepMode);
        stepCallback.onStepMade(lastUserStepCell, stepMode);
    }
}

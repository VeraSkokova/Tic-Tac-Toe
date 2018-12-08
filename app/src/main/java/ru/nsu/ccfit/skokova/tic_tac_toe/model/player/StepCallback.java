package ru.nsu.ccfit.skokova.tic_tac_toe.model.player;

import ru.nsu.ccfit.skokova.tic_tac_toe.model.field.Cell;
import ru.nsu.ccfit.skokova.tic_tac_toe.model.game.StepMode;

public interface StepCallback {
    void onStepMade(Cell stepCell, StepMode stepMode);
}

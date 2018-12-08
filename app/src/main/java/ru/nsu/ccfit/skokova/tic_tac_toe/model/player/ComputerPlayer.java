package ru.nsu.ccfit.skokova.tic_tac_toe.model.player;

import ru.nsu.ccfit.skokova.tic_tac_toe.model.field.Cell;
import ru.nsu.ccfit.skokova.tic_tac_toe.model.field.Field;
import ru.nsu.ccfit.skokova.tic_tac_toe.model.game.StepMode;

public class ComputerPlayer implements Player {
    private PlayerStrategy playerStrategy;

    public ComputerPlayer() {
        this.playerStrategy = new ComputerStrategy();
    }

    @Override
    public void makeStep(Field field, Cell lastUserStepCell, StepCallback stepCallback) {
        Cell stepCell = playerStrategy.nextStep(field, lastUserStepCell);
        stepCallback.onStepMade(stepCell, StepMode.NOUGHT);
    }

    public void setPlayerStrategy(PlayerStrategy playerStrategy) {
        this.playerStrategy = playerStrategy;
    }
}

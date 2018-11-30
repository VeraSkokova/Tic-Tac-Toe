package ru.nsu.ccfit.skokova.tic_tac_toe.model.player;

import ru.nsu.ccfit.skokova.tic_tac_toe.model.field.Cell;
import ru.nsu.ccfit.skokova.tic_tac_toe.model.field.Field;

public class ComputerPlayer implements Player {
    private PlayerStrategy playerStrategy;

    @Override
    public Cell makeStep(Field field, Cell lastUserStepCell) {
        return playerStrategy.nextStep(field, lastUserStepCell);
    }

    public void setPlayerStrategy(PlayerStrategy playerStrategy) {
        this.playerStrategy = playerStrategy;
    }
}

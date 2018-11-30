package ru.nsu.ccfit.skokova.tic_tac_toe.model;

import ru.nsu.ccfit.skokova.tic_tac_toe.model.field.Cell;
import ru.nsu.ccfit.skokova.tic_tac_toe.model.field.Field;
import ru.nsu.ccfit.skokova.tic_tac_toe.model.player.ComputerPlayer;
import ru.nsu.ccfit.skokova.tic_tac_toe.model.player.ComputerStrategy;
import ru.nsu.ccfit.skokova.tic_tac_toe.presenter.MainPresenter;

public class Game {
    private static final int DEFAULT_SIZE = 3;

    private MainPresenter presenter;

    private Field field;

    private ComputerPlayer computerPlayer;

    public Game() {
        field = new Field(DEFAULT_SIZE);
        field.init();

        computerPlayer = new ComputerPlayer();
        computerPlayer.setPlayerStrategy(new ComputerStrategy());
    }

    public Game(int fieldSize) {
        field = new Field(fieldSize);
        field.init();

        computerPlayer = new ComputerPlayer();
        computerPlayer.setPlayerStrategy(new ComputerStrategy());
    }

    public void changeField(int newSize) {
        field = new Field(newSize);
        field.init();

        computerPlayer = new ComputerPlayer();
        computerPlayer.setPlayerStrategy(new ComputerStrategy());

        presenter.onSizeChanged(newSize);
    }

    public void setPresenter(MainPresenter presenter) {
        this.presenter = presenter;
    }

    public void performUserStep(int cellX, int cellY) {
        if (field.userStep(cellX, cellY)) {
            presenter.onUserStep(cellX, cellY);
            Cell cell = computerPlayer.makeStep(field, field.getCell(cellX, cellY));
            presenter.onComputerStep(cell);
        }
    }
}

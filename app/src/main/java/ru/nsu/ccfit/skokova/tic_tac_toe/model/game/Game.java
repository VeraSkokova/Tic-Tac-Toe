package ru.nsu.ccfit.skokova.tic_tac_toe.model.game;

import ru.nsu.ccfit.skokova.tic_tac_toe.model.field.Cell;
import ru.nsu.ccfit.skokova.tic_tac_toe.model.field.Field;
import ru.nsu.ccfit.skokova.tic_tac_toe.model.player.ComputerPlayer;
import ru.nsu.ccfit.skokova.tic_tac_toe.model.player.ComputerStrategy;
import ru.nsu.ccfit.skokova.tic_tac_toe.model.player.UserPlayer;
import ru.nsu.ccfit.skokova.tic_tac_toe.presenter.MainPresenter;

public class Game {
    private static final int DEFAULT_SIZE = 3;

    private MainPresenter presenter;

    private Field field;

    private UserPlayer userPlayer;
    private ComputerPlayer computerPlayer;

    private Judge judge;

    private boolean isRunning;

    public Game() {
        field = new Field(DEFAULT_SIZE);
        field.init();

        userPlayer = new UserPlayer();
        computerPlayer = new ComputerPlayer();
        computerPlayer.setPlayerStrategy(new ComputerStrategy());

        judge = new Judge(field.getSize());

        isRunning = true;
    }

    public Game(int fieldSize) {
        field = new Field(fieldSize);
        field.init();

        userPlayer = new UserPlayer();
        computerPlayer = new ComputerPlayer();
        computerPlayer.setPlayerStrategy(new ComputerStrategy());

        judge = new Judge(fieldSize);

        isRunning = true;
    }

    public void changeField(int newSize) {
        field = new Field(newSize);
        field.init();

        computerPlayer = new ComputerPlayer();
        computerPlayer.setPlayerStrategy(new ComputerStrategy());

        judge = new Judge(newSize);

        isRunning = true;

        presenter.onSizeChanged(newSize);
    }

    public void setPresenter(MainPresenter presenter) {
        this.presenter = presenter;
    }

    public void performUserStep(int cellX, int cellY) {
        if (!isRunning) {
            return;
        }

        Cell userStepCell = field.getCell(cellX, cellY);
        if (userPlayer.makeStep(field, userStepCell) != null) {
            judge.stepDone();
            presenter.onUserStep(cellX, cellY);
            if (judge.isWin(userStepCell, field)) {
                userWins();
                return;
            }

            if (judge.isDraw()) {
                draw();
                return;
            }

            Cell computerStepCell = computerPlayer.makeStep(field, field.getCell(cellX, cellY));
            judge.stepDone();
            presenter.onComputerStep(computerStepCell);
            if (judge.isWin(computerStepCell, field)) {
                computerWins();
                return;
            }

            if (judge.isDraw()) {
                draw();
            }
        }
    }

    private void computerWins() {
        isRunning = false;
        presenter.onComputerWin();
    }

    private void draw() {
        isRunning = false;
        presenter.onDraw();
    }

    private void userWins() {
        isRunning = false;
        presenter.onUserWin();
    }
}

package ru.nsu.ccfit.skokova.tic_tac_toe.model.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import ru.nsu.ccfit.skokova.tic_tac_toe.model.Updater;
import ru.nsu.ccfit.skokova.tic_tac_toe.model.field.Cell;
import ru.nsu.ccfit.skokova.tic_tac_toe.model.field.Field;
import ru.nsu.ccfit.skokova.tic_tac_toe.model.player.ComputerPlayer;
import ru.nsu.ccfit.skokova.tic_tac_toe.model.player.NoMultiPlayerException;
import ru.nsu.ccfit.skokova.tic_tac_toe.model.player.OpponentPlayer;
import ru.nsu.ccfit.skokova.tic_tac_toe.model.player.Player;
import ru.nsu.ccfit.skokova.tic_tac_toe.model.player.UserPlayer;
import ru.nsu.ccfit.skokova.tic_tac_toe.model.statistics.RecordAuthor;
import ru.nsu.ccfit.skokova.tic_tac_toe.presenter.GamePresenter;

public class Game {
    private static final Logger logger = LoggerFactory.getLogger(Game.class);

    private static final int DEFAULT_SIZE = 3;

    private GamePresenter presenter;

    private Field field;

    private UserPlayer userPlayer;
    private Player secondPlayer;

    private Judge judge;

    private boolean isRunning;

    private Updater updater;

    public Game() {
        field = new Field(DEFAULT_SIZE);
        field.init();

        userPlayer = new UserPlayer();
        secondPlayer = new ComputerPlayer();

        judge = new Judge(field.getSize());

        updater = new RecordsUpdater();

        isRunning = true;
    }

    public Game(int fieldSize) {
        field = new Field(fieldSize);
        field.init();

        userPlayer = new UserPlayer();
        secondPlayer = new ComputerPlayer();

        judge = new Judge(fieldSize);

        updater = new RecordsUpdater();

        isRunning = true;
    }

    public void startOrContinue() {
        if (isRunning) {
            changeField(DEFAULT_SIZE);
        } else {
            changeField(DEFAULT_SIZE);
        }
    }

    public void changeField(int newSize) {
        field = new Field(newSize);
        field.init();

        judge = new Judge(newSize);

        isRunning = true;

        presenter.onSizeChanged(newSize);
    }

    public void singlePlayerGame() {
        secondPlayer = new ComputerPlayer();
        changeField(DEFAULT_SIZE);
    }

    public void multiPlayerGame() {
        try {
            secondPlayer = new OpponentPlayer();
            boolean isSetup = ((OpponentPlayer) secondPlayer).setup();
            if (!isSetup) {
                presenter.enableConnection();
                return;
            }
            changeField(DEFAULT_SIZE);
        } catch (NoMultiPlayerException e) {
            logger.error("Bluetooth is not enabled");
            presenter.noMultiPlayer();
        }
    }

    public void setPresenter(GamePresenter presenter) {
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

            Cell computerStepCell = secondPlayer.makeStep(field, field.getCell(cellX, cellY));
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

    public List<Cell> getCells() {
        return field.getCellsList();
    }

    private void computerWins() {
        isRunning = false;
        updater.updateRecord(RecordAuthor.COMPUTER);
        presenter.onComputerWin();
    }

    private void draw() {
        isRunning = false;
        presenter.onDraw();
    }

    private void userWins() {
        isRunning = false;
        updater.updateRecord(RecordAuthor.USER);
        presenter.onUserWin();
    }

    public void connectToOpponent() {

    }
}

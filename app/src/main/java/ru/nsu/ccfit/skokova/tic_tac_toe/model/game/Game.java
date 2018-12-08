package ru.nsu.ccfit.skokova.tic_tac_toe.model.game;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

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

    private StepMode stepMode;
    private BluetoothManager bluetoothManager;

    public Game() {
        field = new Field(DEFAULT_SIZE);
        field.init();

        userPlayer = new UserPlayer();
        secondPlayer = new ComputerPlayer();
        stepMode = StepMode.CROSS;

        judge = new Judge(field.getSize());

        updater = new RecordsUpdater();

        isRunning = true;
    }

    public Game(int fieldSize) {
        field = new Field(fieldSize);
        field.init();

        userPlayer = new UserPlayer();
        secondPlayer = new ComputerPlayer();
        stepMode = StepMode.CROSS;

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
        stepMode = StepMode.CROSS;
        bluetoothManager = null;
        changeField(DEFAULT_SIZE);
    }

    public void multiPlayerGame() {
        try {
            secondPlayer = new OpponentPlayer();
            bluetoothManager = new BluetoothManager(this::setOpponentSocket, this::setOpponentSocket);
            boolean isSetup = ((OpponentPlayer) secondPlayer).setup();
            if (!isSetup) {
                presenter.enableConnection();
                return;
            }
            presenter.askForServerOrClientMode();
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
        StepMode isMyTurn = StepMode.CROSS;
        userPlayer.makeStep(field, userStepCell, this::stepDone);

        if (!isRunning) {
            return;
        }

        isMyTurn = StepMode.NOUGHT;
        secondPlayer.makeStep(field, field.getCell(cellX, cellY), this::stepDone);
    }


    public void connectToOpponent(BluetoothDevice device) {
        stepMode = StepMode.NOUGHT;
        ((OpponentPlayer) secondPlayer).setStepMode(StepMode.CROSS);
        startConnectorThread(device);
    }

    public void serverMode() {
        stepMode = StepMode.CROSS;
        ((OpponentPlayer) secondPlayer).setStepMode(StepMode.NOUGHT);
        startAcceptorThread();
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

    private void stepDone(Cell cell, StepMode isUserTurn) {
        judge.stepDone();
        if (stepMode == isUserTurn) {
            presenter.onUserStep(cell.getCellX(), cell.getCellY());
        } else {
            presenter.onComputerStep(cell);
        }

        if (judge.isWin(cell, field)) {
            selectWinner(isUserTurn);
            return;
        }

        if (judge.isDraw()) {
            draw();
        }
    }

    private void selectWinner(StepMode isUserTurn) {
        if (stepMode == isUserTurn) {
            userWins();
        } else {
            computerWins();
        }
    }

    private void startAcceptorThread() {
        bluetoothManager.startAcceptorThread();
    }

    private void setOpponentSocket(BluetoothSocket socket) {
        ((OpponentPlayer) secondPlayer).setSocket(socket);
        ((OpponentPlayer) secondPlayer).setupStreams();
    }

    private void startConnectorThread(BluetoothDevice device) {
        bluetoothManager.startConnectorThread(device);
    }
}

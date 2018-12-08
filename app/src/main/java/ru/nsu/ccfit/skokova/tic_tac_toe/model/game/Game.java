package ru.nsu.ccfit.skokova.tic_tac_toe.model.game;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import ru.nsu.ccfit.skokova.tic_tac_toe.model.Updater;
import ru.nsu.ccfit.skokova.tic_tac_toe.model.field.Cell;
import ru.nsu.ccfit.skokova.tic_tac_toe.model.field.CellState;
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
    private boolean canPerformStep;
    private boolean isMultiPlayer;
    private boolean isConnected;

    private Updater updater;

    private CellState cellState;
    private BluetoothManager bluetoothManager;

    public Game() {
        field = new Field(DEFAULT_SIZE);
        field.init();

        userPlayer = new UserPlayer();
        secondPlayer = new ComputerPlayer();
        cellState = CellState.CROSS;

        judge = new Judge(field.getSize());

        updater = new RecordsUpdater();

        isRunning = true;
        canPerformStep = true;
        isMultiPlayer = false;
        isConnected = true;
    }

    public Game(int fieldSize) {
        field = new Field(fieldSize);
        field.init();

        userPlayer = new UserPlayer();
        secondPlayer = new ComputerPlayer();
        cellState = CellState.CROSS;

        judge = new Judge(fieldSize);

        updater = new RecordsUpdater();

        isRunning = true;
        canPerformStep = true;
        isMultiPlayer = false;
        isConnected = true;
    }

    public void startOrContinue() {
        if (isRunning) {
            changeField(DEFAULT_SIZE);
        } else {
            changeField(DEFAULT_SIZE);
        }
    }

    public void changeField(int newSize) {
        if (isMultiPlayer) {
            presenter.sizeChangingDenied();
            return;
        }

        field = new Field(newSize);
        field.init();

        judge = new Judge(newSize);

        isRunning = true;
        canPerformStep = true;

        presenter.onSizeChanged(newSize);
    }

    public void reset() {
        int fieldSize = field.getSize();
        changeField(fieldSize);
    }

    public void singlePlayerGame() {
        secondPlayer = new ComputerPlayer();
        isMultiPlayer = false;
        isConnected = true;

        cellState = CellState.CROSS;
        userPlayer.setStepMode(CellState.CROSS);
        bluetoothManager = null;
        changeField(DEFAULT_SIZE);
        presenter.modeChangedToSingle();
    }

    public void multiPlayerGame() {
        try {
            isConnected = false;
            secondPlayer = new OpponentPlayer();
            isMultiPlayer = true;
            presenter.modeChangedToMulti();

            bluetoothManager = new BluetoothManager(this::setServerOpponentSocket,
                    this::setClientOpponentSocket);
            boolean isSetup = ((OpponentPlayer) secondPlayer).setup();
            if (!isSetup) {
                presenter.enableConnection();
                return;
            }
            presenter.askForServerOrClientMode();

            changeFieldIgnoresMultiPlayer(DEFAULT_SIZE);
            ((OpponentPlayer) secondPlayer).setField(field);
        } catch (NoMultiPlayerException e) {
            logger.error("Bluetooth is not enabled");
            presenter.noMultiPlayer();
        }
    }

    public void setPresenter(GamePresenter presenter) {
        this.presenter = presenter;
    }

    public void performUserStep(int cellX, int cellY) {
        if (!isRunning || !canPerformStep || !isConnected) {
            return;
        }

        Cell userStepCell = field.getCell(cellX, cellY);
        CellState isMyTurn = CellState.CROSS;
        userPlayer.makeStep(field, userStepCell, this::stepDone);
        canPerformStep = false;

        if (!isRunning) { //finish game
            return;
        }

        isMyTurn = CellState.NOUGHT;
        secondPlayer.makeStep(field, field.getCell(cellX, cellY), this::stepDone);
    }


    public void connectToOpponent(BluetoothDevice device) {
        canPerformStep = false;
        cellState = CellState.NOUGHT;
        userPlayer.setStepMode(CellState.NOUGHT);
        ((OpponentPlayer) secondPlayer).setStepMode(CellState.CROSS);
        startConnectorThread(device);
    }

    public void serverMode() {
        canPerformStep = true;
        cellState = CellState.CROSS;
        userPlayer.setStepMode(CellState.CROSS);
        ((OpponentPlayer) secondPlayer).setStepMode(CellState.NOUGHT);
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

    private void stepDone(Cell cell, CellState isUserTurn) {
        judge.stepDone();
        if (isUserTurn == CellState.CROSS) {
            presenter.onCrossStep(cell.getCellX(), cell.getCellY());
        } else {
            presenter.onNoughtStep(cell.getCellX(), cell.getCellY());
        }

        if (judge.isWin(cell, field)) {
            selectWinner(isUserTurn);
            sendGameFinishedIfNecessary(cell);
            return;
        }

        if (judge.isDraw()) {
            draw();
            sendGameFinishedIfNecessary(cell);
            return;
        }
        canPerformStep = true;
    }

    private void sendGameFinishedIfNecessary(Cell cell) {
        if (isMultiPlayer) {
            ((OpponentPlayer) secondPlayer).createWriterThread(cell, null);
        }
    }

    private void selectWinner(CellState isUserTurn) {
        if (cellState == isUserTurn) {
            userWins();
        } else {
            computerWins();
        }
    }

    private void startAcceptorThread() {
        bluetoothManager.startAcceptorThread();
    }

    private void setClientOpponentSocket(BluetoothSocket socket) {
        ((OpponentPlayer) secondPlayer).setSocket(socket);
        ((OpponentPlayer) secondPlayer).setupStreams();
        ((OpponentPlayer) secondPlayer).createReaderThread(this::stepDone);
        isConnected = true;
    }

    private void setServerOpponentSocket(BluetoothSocket socket) {
        ((OpponentPlayer) secondPlayer).setSocket(socket);
        ((OpponentPlayer) secondPlayer).setupStreams();
        isConnected = true;
    }

    private void startConnectorThread(BluetoothDevice device) {
        bluetoothManager.startConnectorThread(device);
    }

    //To change field size when switching to multiplayer mode
    private void changeFieldIgnoresMultiPlayer(int newSize) {
        field = new Field(newSize);
        field.init();

        judge = new Judge(newSize);

        isRunning = true;
        canPerformStep = true;

        presenter.onSizeChanged(newSize);
    }
}

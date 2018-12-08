package ru.nsu.ccfit.skokova.tic_tac_toe.model.player;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import ru.nsu.ccfit.skokova.tic_tac_toe.model.field.Cell;
import ru.nsu.ccfit.skokova.tic_tac_toe.model.field.CellState;
import ru.nsu.ccfit.skokova.tic_tac_toe.model.field.Field;

public class OpponentPlayer implements Player {
    private static final Logger logger = LoggerFactory.getLogger(OpponentPlayer.class);

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket socket;

    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    private CellState stepMode;
    private Field field;

    @Override
    public void makeStep(Field field, Cell lastUserStepCell, StepCallback stepCallback) {
        createWriterThread(lastUserStepCell, stepCallback);
    }

    public void createWriterThread(Cell lastUserStepCell, StepCallback stepCallback) {
        new Thread(new CellWriter(lastUserStepCell, stepCallback)).start();
    }

    public void createReaderThread(StepCallback stepCallback) {
        new Thread(new CellReader(stepCallback)).start();
    }

    public boolean setup() throws NoMultiPlayerException {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            throw new NoMultiPlayerException("No MultiPlayer Enabled");
        }
        return bluetoothAdapter.isEnabled();
    }

    public boolean setupStreams() {
        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            logger.error("Error occurred when creating output stream: {}", e.getMessage());
            return false;
        }

        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            logger.error("Error occurred when creating input stream: {}", e.getMessage());
            return false;
        }

        return true;
    }

    public void setStepMode(CellState stepMode) {
        this.stepMode = stepMode;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public void setSocket(BluetoothSocket socket) {
        this.socket = socket;
    }

    private void sendCell(Cell cell) throws IOException {
        objectOutputStream.writeObject(cell);
        objectOutputStream.flush();
    }

    private Cell receiveCell() throws IOException, ClassNotFoundException {
        return (Cell) objectInputStream.readObject();
    }

    private class CellWriter implements Runnable {

        private Cell lastUserStepCell;
        private StepCallback stepCallback;

        CellWriter(Cell lastUserStepCell, StepCallback stepCallback) {
            this.lastUserStepCell = lastUserStepCell;
            this.stepCallback = stepCallback;
        }

        @Override
        public void run() {
            try {
                sendCell(lastUserStepCell);
                createReaderThreadIfNecessary();
            } catch (IOException e) {
                logger.error("Error occurred when reading cell: {}", e.getMessage());
            }
        }

        private void createReaderThreadIfNecessary() {
            if (stepCallback != null) {
                createReaderThread(stepCallback);
            }
        }
    }

    private class CellReader implements Runnable {
        private StepCallback stepCallback;

        CellReader(StepCallback stepCallback) {
            this.stepCallback = stepCallback;
        }

        @Override
        public void run() {
            try {
                Cell receivedCell = receiveCell();
                CellState receivedState = receivedCell.getCellState();
                field.getCell(receivedCell.getCellX(), receivedCell.getCellY()).setCellState(receivedState);
                stepCallback.onStepMade(receivedCell, stepMode);
            } catch (IOException | ClassNotFoundException e) {
                logger.error("Error occurred when reading cell: {}", e.getMessage());
            }
        }
    }
}

package ru.nsu.ccfit.skokova.tic_tac_toe.model.player;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import ru.nsu.ccfit.skokova.tic_tac_toe.model.field.Cell;
import ru.nsu.ccfit.skokova.tic_tac_toe.model.field.Field;
import ru.nsu.ccfit.skokova.tic_tac_toe.model.game.StepMode;

public class OpponentPlayer implements Player {
    private static final Logger logger = LoggerFactory.getLogger(OpponentPlayer.class);

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket socket;

    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    private StepMode stepMode;

    @Override
    public void makeStep(Field field, Cell lastUserStepCell, StepCallback stepCallback) {
        new Thread(new CellReaderWriter(lastUserStepCell, stepCallback)).start();
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

    public void setStepMode(StepMode stepMode) {
        this.stepMode = stepMode;
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

    private class CellReaderWriter implements Runnable {

        private Cell lastUserStepCell;
        private StepCallback stepCallback;

        CellReaderWriter(Cell lastUserStepCell, StepCallback stepCallback) {
            this.lastUserStepCell = lastUserStepCell;
            this.stepCallback = stepCallback;
        }

        @Override
        public void run() {
            try {
                if (stepMode == StepMode.CROSS) {
                    sendCell(lastUserStepCell);
                }
                stepCallback.onStepMade(receiveCell(), stepMode);
                if (stepMode == StepMode.NOUGHT) {
                    sendCell(lastUserStepCell);
                }
            } catch (IOException | ClassNotFoundException e) {
                logger.error("Error occurred when reading cell: {}", e.getMessage());
            }
        }
    }
}

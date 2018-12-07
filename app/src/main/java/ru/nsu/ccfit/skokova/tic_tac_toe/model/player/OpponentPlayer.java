package ru.nsu.ccfit.skokova.tic_tac_toe.model.player;

import android.bluetooth.BluetoothAdapter;

import ru.nsu.ccfit.skokova.tic_tac_toe.model.field.Cell;
import ru.nsu.ccfit.skokova.tic_tac_toe.model.field.Field;

public class OpponentPlayer implements Player {
    private BluetoothAdapter adapter;

    @Override
    public Cell makeStep(Field field, Cell lastUserStepCell) {
        return null;
    }

    public boolean setup() throws NoMultiPlayerException {
        adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            throw new NoMultiPlayerException("No MultiPlayer Enabled");
        }
        return adapter.isEnabled();
    }
}

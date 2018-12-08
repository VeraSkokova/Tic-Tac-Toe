package ru.nsu.ccfit.skokova.tic_tac_toe.model.game;

import android.bluetooth.BluetoothSocket;

public interface ConnectionCallback {
    void onConnectionSet(BluetoothSocket socket);
}

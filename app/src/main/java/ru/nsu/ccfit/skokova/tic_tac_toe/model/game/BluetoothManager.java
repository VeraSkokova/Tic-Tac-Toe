package ru.nsu.ccfit.skokova.tic_tac_toe.model.game;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

public class BluetoothManager {
    private static final Logger logger = LoggerFactory.getLogger(BluetoothManager.class);

    private static final String NAME = "TicTacToe";
    private static final UUID MY_UUID = UUID.fromString("fa87c1d0-afac-11de-8a39-0800209c9a66");

    private BluetoothAdapter bluetoothAdapter;

    private ConnectionCallback serverConnectionCallback;
    private ConnectionCallback clientConnectionCallback;


    public BluetoothManager(ConnectionCallback serverConnectionCallback,
                            ConnectionCallback clientConnectionCallback) {
        this.serverConnectionCallback = serverConnectionCallback;
        this.clientConnectionCallback = clientConnectionCallback;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void startAcceptorThread() {
        Thread acceptorThread = new Thread(new AcceptRunnable());
        acceptorThread.start();
    }

    public void startConnectorThread(BluetoothDevice device) {
        Thread connectorThread = new Thread(new ConnectRunnable(device));
        connectorThread.start();
    }

    private class AcceptRunnable implements Runnable {
        private final BluetoothServerSocket serverSocket;

        public AcceptRunnable() {
            BluetoothServerSocket temp = null;
            try {
                temp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
            } catch (IOException e) {
                logger.error("Socket listen method failed");
            }
            serverSocket = temp;
        }

        @Override
        public void run() {
            BluetoothSocket socket;
            while (true) {
                try {
                    socket = serverSocket.accept();
                    logger.debug("Accept on server");
                } catch (IOException e) {
                    logger.error("Socket's accept() method failed: {}", e);
                    break;
                }

                if (socket != null) {
                    serverConnectionCallback.onConnectionSet(socket);
                    cancel();
                    break;
                }
            }
        }

        public void cancel() {
            try {
                serverSocket.close();
            } catch (IOException e) {
                logger.error("Couldn't close server socket: {}", e);
            }
        }
    }

    private class ConnectRunnable implements Runnable {
        private final BluetoothSocket socket;
        private final BluetoothDevice device;

        public ConnectRunnable(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            this.device = device;

            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                logger.error("Socket's create() method failed: {}", e);
            }
            socket = tmp;
        }

        @Override
        public void run() {
            bluetoothAdapter.cancelDiscovery();
            try {
                socket.connect();
                clientConnectionCallback.onConnectionSet(socket);
                logger.debug("Socket connect");
            } catch (IOException connectException) {
                cancel();
            }
        }

        public void cancel() {
            try {
                socket.close();
            } catch (IOException e) {
                logger.error("Couldn't close client socket: {}", e);
            }
        }
    }
}

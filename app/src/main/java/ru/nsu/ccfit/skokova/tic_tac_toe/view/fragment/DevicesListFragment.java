package ru.nsu.ccfit.skokova.tic_tac_toe.view.fragment;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ru.nsu.ccfit.skokova.tic_tac_toe.R;
import ru.nsu.ccfit.skokova.tic_tac_toe.presenter.GamePresenter;
import ru.nsu.ccfit.skokova.tic_tac_toe.view.adapter.DevicesAdapter;

public class DevicesListFragment extends DialogFragment {
    private static final Logger logger = LoggerFactory.getLogger(DevicesListFragment.class);

    @BindView(R.id.devices_list)
    RecyclerView devicesList;

    private DevicesAdapter devicesAdapter;

    private Unbinder unbinder;

    private BluetoothAdapter bluetoothAdapter;

    private BroadcastReceiver broadcastReceiver;

    private GamePresenter presenter;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_devices_list, container, false);
        unbinder = ButterKnife.bind(this, view);

        devicesAdapter = new DevicesAdapter(device -> {
            presenter.onConnectionAsked(device);
            dismiss();
        });
        devicesList.setAdapter(devicesAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        devicesList.setLayoutManager(layoutManager);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                devicesAdapter.getDisplayedDevices().add(device);
                devicesAdapter.notifyDataSetChanged();
            }
        }

        createBroadcastReceiver();

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        getActivity().registerReceiver(broadcastReceiver, filter);
        bluetoothAdapter.startDiscovery();
        logger.info("Start searching for devices");

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        getActivity().unregisterReceiver(broadcastReceiver);
        bluetoothAdapter.cancelDiscovery();
        logger.info("Discovery cancelled");
    }

    public void setPresenter(GamePresenter presenter) {
        this.presenter = presenter;
    }

    private void createBroadcastReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                logger.debug("Receive intent");
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    logger.info("New device");
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    devicesAdapter.getDisplayedDevices().add(device);
                    devicesAdapter.notifyDataSetChanged();
                }
                if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                    logger.info("Finished");
                }
            }
        };
    }
}

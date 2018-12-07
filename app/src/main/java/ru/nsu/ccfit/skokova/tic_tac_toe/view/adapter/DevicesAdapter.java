package ru.nsu.ccfit.skokova.tic_tac_toe.view.adapter;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.nsu.ccfit.skokova.tic_tac_toe.R;

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.ViewHolder> {
    private final List<BluetoothDevice> displayedDevices;
    private final OnItemClickListener onItemClickListener;

    public DevicesAdapter(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        displayedDevices = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.device_string, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.bind(displayedDevices.get(position), onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return displayedDevices.size();
    }

    public List<BluetoothDevice> getDisplayedDevices() {
        return displayedDevices;
    }

    public interface OnItemClickListener {
        void onItemClick(BluetoothDevice device);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textView;

        ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.device_info);
        }

        void bind(BluetoothDevice device, OnItemClickListener onItemClickListener) {
            String displayedName = device.getName() != null ? device.getName() : device.getAddress();
            textView.setText(displayedName);
            itemView.setOnClickListener(v -> onItemClickListener.onItemClick(device));
        }
    }
}

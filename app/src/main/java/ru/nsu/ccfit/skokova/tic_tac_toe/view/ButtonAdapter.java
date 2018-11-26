package ru.nsu.ccfit.skokova.tic_tac_toe.view;

import android.content.Context;
import android.support.v7.widget.AppCompatImageButton;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public class ButtonAdapter extends BaseAdapter {
    private Context context;

    private List<AppCompatImageButton> buttons;

    public ButtonAdapter(Context context, List<AppCompatImageButton> buttons) {
        this.context = context;
        this.buttons = buttons;
    }

    @Override
    public int getCount() {
        return buttons.size();
    }

    @Override
    public Object getItem(int position) {
        return buttons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AppCompatImageButton button;
        if (convertView == null) {
            button = buttons.get(position);
        } else {
            button = (AppCompatImageButton) convertView;
        }
        return button;
    }
}

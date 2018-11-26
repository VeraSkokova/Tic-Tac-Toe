package ru.nsu.ccfit.skokova.tic_tac_toe.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import ru.nsu.ccfit.skokova.tic_tac_toe.R;

public class FieldSizeDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.field_message)
                .setPositiveButton(R.string.ok, (dialog, id) -> {
                    //TODO : save new size
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> {
                    //TODO : exit
                });
        return builder.create();
    }
}

package ru.nsu.ccfit.skokova.tic_tac_toe.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import ru.nsu.ccfit.skokova.tic_tac_toe.R;

public class GameFinishDialogFragment extends DialogFragment {
    private String finishMessage;

    @Override
    public void setArguments(@Nullable Bundle args) {
        if (args != null) {
            finishMessage = args.getString(MainActivity.MSG_KEY);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(finishMessage)
                .setNeutralButton(R.string.ok, (dialog, which) -> dismiss());
        return builder.create();
    }
}

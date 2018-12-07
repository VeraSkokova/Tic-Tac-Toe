package ru.nsu.ccfit.skokova.tic_tac_toe.view.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

import ru.nsu.ccfit.skokova.tic_tac_toe.R;
import ru.nsu.ccfit.skokova.tic_tac_toe.presenter.GamePresenter;

public class GameModeDialogFragment extends AppCompatDialogFragment {
    private GamePresenter presenter;

    public void setPresenter(GamePresenter presenter) {
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.choose_mode)
                .setItems(R.array.modes, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            presenter.onSinglePlayerChosen();
                            break;
                        case 1:
                            presenter.onMultiPlayerChosen();
                            break;
                        default:
                            break;
                    }
                });
        return builder.create();
    }
}

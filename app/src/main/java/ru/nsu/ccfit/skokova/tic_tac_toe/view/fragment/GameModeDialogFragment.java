package ru.nsu.ccfit.skokova.tic_tac_toe.view.fragment;

import android.support.v7.app.AppCompatDialogFragment;

import ru.nsu.ccfit.skokova.tic_tac_toe.presenter.GamePresenter;

public class GameModeDialogFragment extends AppCompatDialogFragment {
    private GamePresenter presenter;

    public void setPresenter(GamePresenter presenter) {
        this.presenter = presenter;
    }
}

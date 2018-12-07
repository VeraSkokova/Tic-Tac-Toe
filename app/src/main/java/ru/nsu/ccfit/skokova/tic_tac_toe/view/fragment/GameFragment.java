package ru.nsu.ccfit.skokova.tic_tac_toe.view.fragment;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ru.nsu.ccfit.skokova.tic_tac_toe.R;
import ru.nsu.ccfit.skokova.tic_tac_toe.presenter.GamePresenter;
import ru.nsu.ccfit.skokova.tic_tac_toe.view.GameView;
import ru.nsu.ccfit.skokova.tic_tac_toe.view.activity.MainActivity;

public class GameFragment extends Fragment implements GameView {
    private static final int REQUEST_ENABLE_BLUETOOTH = 10;

    @BindView(R.id.game_panel)
    TableLayout gamePanel;

    private Unbinder unbinder;

    private GamePresenter presenter;

    private AppCompatImageButton[][] buttons;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        unbinder = ButterKnife.bind(this, view);
        presenter.viewIsReady();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void drawNewField(int size) {
        gamePanel.removeAllViews();
        drawField(size, false);
    }

    @Override
    public void drawGameField(int size) {
        gamePanel.removeAllViews();
        drawField(size, true);
    }

    @Override
    public void showUserStep(int cellX, int cellY) {
        buttons[cellX][cellY].setImageResource(R.drawable.ic_cross_black_24dp);
    }

    @Override
    public void showComputerStep(int cellX, int cellY) {
        buttons[cellX][cellY].setImageResource(R.drawable.ic_circle_black_24dp);
    }

    @Override
    public void showUserWin() {
        showGameFinishDialog("You won!!!");
    }

    @Override
    public void showComputerWin() {
        showGameFinishDialog("You loose:(");
    }

    @Override
    public void showDraw() {
        showGameFinishDialog("Draw");
    }

    @Override
    public void askForConnectionEnable() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == Activity.RESULT_OK) {
                presenter.onMultiPlayerChosen();
            }
        }
    }

    private void drawField(int size, boolean checkState) {
        buttons = new AppCompatImageButton[size][size];

        for (int i = 0; i < size; i++) {
            TableRow tableRow = new TableRow(getActivity());

            for (int j = 0; j < size; j++) {
                LayoutInflater layoutInflater = getLayoutInflater();
                final View view = layoutInflater.inflate(R.layout.cell_button, null);
                AppCompatImageButton cellButton = view.findViewById(R.id.cell);
                int x = i;
                int y = j;
                cellButton.setOnClickListener(v -> onCellClicked(x, y));
                buttons[x][y] = cellButton;
                tableRow.addView(cellButton, j);
            }

            gamePanel.addView(tableRow, i);
        }
    }

    public void setPresenter(GamePresenter presenter) {
        this.presenter = presenter;
    }

    private void onCellClicked(int x, int y) {
        presenter.performStep(x, y);
    }

    private void showGameFinishDialog(String message) {
        GameFinishDialogFragment gameFinishDialogFragment = new GameFinishDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putString(MainActivity.MSG_KEY, message);
        gameFinishDialogFragment.setArguments(bundle);

        gameFinishDialogFragment.show(getFragmentManager(), "GAME_FINISH_DIALOG");
    }
}

package ru.nsu.ccfit.skokova.tic_tac_toe.view.fragment;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ru.nsu.ccfit.skokova.tic_tac_toe.R;
import ru.nsu.ccfit.skokova.tic_tac_toe.presenter.GamePresenter;
import ru.nsu.ccfit.skokova.tic_tac_toe.view.GameView;
import ru.nsu.ccfit.skokova.tic_tac_toe.view.activity.MainActivity;

public class GameFragment extends Fragment implements GameView {
    public static final int DISCOVERABLE_DURATION = 300;
    private static final int REQUEST_ENABLE_BLUETOOTH = 10;

    @BindView(R.id.game_panel)
    TableLayout gamePanel;

    @BindView(R.id.fab_new_game)
    FloatingActionButton newGameButton;

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
    public void showCrossStep(int cellX, int cellY) {
        getActivity().runOnUiThread(() -> buttons[cellX][cellY]
                .setImageResource(R.drawable.ic_cross_black_24dp));
    }

    @Override
    public void showNoughtStep(int cellX, int cellY) {
        getActivity().runOnUiThread(() -> buttons[cellX][cellY]
                .setImageResource(R.drawable.ic_circle_black_24dp));
    }

    @Override
    public void showUserWin() {
        showGameFinishDialog(getString(R.string.win));
    }

    @Override
    public void showComputerWin() {
        showGameFinishDialog(getString(R.string.loose));
    }

    @Override
    public void showDraw() {
        showGameFinishDialog(getString(R.string.draw));
    }

    @Override
    public void askForConnectionEnable() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH);
    }

    @Override
    public void showNoMultiPlayer() {
        showToast(getString(R.string.no_multi_player));
    }

    @Override
    public void showWaitOrConnect() {
        showGamePossibilitiesDialog();
    }

    @Override
    public void showSizeChangingDenied() {
        showToast(getString(R.string.field_change_denied_multiplayer));
    }

    @OnClick(R.id.fab_new_game)
    void onNewGameClicked() {
        presenter.resetGame();
    }

    @Override
    public void showSinglePlayerView() {
        newGameButton.show();
    }

    @Override
    public void showMultiPlayerView() {
        newGameButton.hide();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == Activity.RESULT_OK) {
                showGamePossibilitiesDialog();
            } else {
                presenter.onSinglePlayerChosen();
            }
        }
    }

    private void showDevicesList() {
        DevicesListFragment devicesListFragment = new DevicesListFragment();
        devicesListFragment.setPresenter(presenter);
        devicesListFragment.show(getFragmentManager(), "DEVICES_LIST");
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

    private void showGamePossibilitiesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.possibilities)
                .setPositiveButton(getString(R.string.wait), (dialog, which) -> {
                    ensureDiscoverable();
                    presenter.onWaitForGame();
                })
                .setNegativeButton(getString(R.string.find_friend), (dialog, which) -> showDevicesList())
                .create()
                .show();
    }

    private void ensureDiscoverable() {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);

    }

    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}

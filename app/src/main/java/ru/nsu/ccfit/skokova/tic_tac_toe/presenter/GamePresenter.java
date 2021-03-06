package ru.nsu.ccfit.skokova.tic_tac_toe.presenter;

import android.bluetooth.BluetoothDevice;

import ru.nsu.ccfit.skokova.tic_tac_toe.model.game.Game;
import ru.nsu.ccfit.skokova.tic_tac_toe.view.GameView;

public class GamePresenter {
    private GameView view;
    private Game game;

    public GamePresenter() {
        game = new Game();
        game.setPresenter(this);
    }

    public void attachView(GameView gameView) {
        this.view = gameView;
    }

    public void detachView() {
        this.view = null;
    }

    public void onSizeChanged(int size) {
        view.drawNewField(size);
    }

    public void changeFieldSize(int newSize) {
        game.changeField(newSize);
    }

    public void performStep(int x, int y) {
        game.performUserStep(x, y);
    }

    public void onCrossStep(int cellX, int cellY) {
        view.showCrossStep(cellX, cellY);
    }

    public void viewIsReady() {
        game.startOrContinue();
    }

    public void resumeGame(int size) {
        view.drawGameField(size);
    }

    public void onNoughtStep(int cellX, int cellY) {
        view.showNoughtStep(cellX, cellY);
    }

    public void onUserWin() {
        view.showUserWin();
    }

    public void onComputerWin() {
        view.showComputerWin();
    }

    public void onDraw() {
        view.showDraw();
    }

    public void onSinglePlayerChosen() {
        game.singlePlayerGame();
    }

    public void onMultiPlayerChosen() {
        game.multiPlayerGame();
    }

    public void enableConnection() {
        view.askForConnectionEnable();
    }

    public void noMultiPlayer() {
        view.showNoMultiPlayer();
    }

    public void onConnectionAsked(BluetoothDevice device) {
        game.connectToOpponent(device);
    }

    public void onWaitForGame() {
        game.serverMode();
    }

    public void askForServerOrClientMode() {
        view.showWaitOrConnect();
    }

    public void sizeChangingDenied() {
        view.showSizeChangingDenied();
    }

    public void resetGame() {
        game.reset();
    }

    public void modeChangedToMulti() {
        view.showMultiPlayerView();
    }

    public void modeChangedToSingle() {
        view.showSinglePlayerView();
    }
}

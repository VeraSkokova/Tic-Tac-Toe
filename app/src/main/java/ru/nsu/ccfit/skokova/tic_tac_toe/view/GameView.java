package ru.nsu.ccfit.skokova.tic_tac_toe.view;

public interface GameView {
    void drawNewField(int size);

    void onCrossStep(int cellX, int cellY);

    void showNoughtStep(int cellX, int cellY);

    void showUserWin();

    void showComputerWin();

    void showDraw();

    void drawGameField(int size);

    void askForConnectionEnable();

    void showNoMultiPlayer();

    void showWaitOrConnect();

    void showSizeChangingDenied();
}

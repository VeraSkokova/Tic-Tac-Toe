package ru.nsu.ccfit.skokova.tic_tac_toe.view;

public interface MainView {
    void drawNewField(int size);

    void showUserStep(int cellX, int cellY);

    void showComputerStep(int cellX, int cellY);
}

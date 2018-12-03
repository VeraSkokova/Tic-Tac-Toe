package ru.nsu.ccfit.skokova.tic_tac_toe.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ru.nsu.ccfit.skokova.tic_tac_toe.R;
import ru.nsu.ccfit.skokova.tic_tac_toe.presenter.StatisticsPresenter;
import ru.nsu.ccfit.skokova.tic_tac_toe.view.StatisticsView;

public class StatisticsFragment extends Fragment implements StatisticsView {
    @BindView(R.id.computer_wins_value)
    TextView computerWinsText;

    @BindView(R.id.user_wins_value)
    TextView userWinsText;

    private Unbinder unbinder;

    private StatisticsPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        unbinder = ButterKnife.bind(this, view);
        showWinsCount();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showUserWins(int winsCount) {
        userWinsText.setText(String.valueOf(winsCount));
    }

    @Override
    public void showComputerWins(int winsCount) {
        computerWinsText.setText(String.valueOf(winsCount));
    }

    public void setPresenter(StatisticsPresenter presenter) {
        this.presenter = presenter;
    }

    private void showWinsCount() {
        presenter.onStatsShow();
    }
}

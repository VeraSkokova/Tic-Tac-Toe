package ru.nsu.ccfit.skokova.tic_tac_toe.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.nsu.ccfit.skokova.tic_tac_toe.R;
import ru.nsu.ccfit.skokova.tic_tac_toe.presenter.MainPresenter;

public class MainActivity extends AppCompatActivity implements MainView {
    private static final int DEFAULT_SIZE = 3;

    @BindView(R.id.game_panel)
    TableLayout gamePanel;

    private MainPresenter presenter;

    private AppCompatImageButton[][] buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        attachPresenter();

        drawField(DEFAULT_SIZE);
        Log.d("debug", "onCreate");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.settings_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.field_size:
                FieldSizeDialogFragment fieldSizeDialogFragment
                        = new FieldSizeDialogFragment();
                fieldSizeDialogFragment.setPresenter(presenter);
                fieldSizeDialogFragment.show(getSupportFragmentManager(), "FIELD_SIZE_DIALOG");
                return true;
            case R.id.game_mode:
                //...
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void drawNewField(int size) {
        gamePanel.removeAllViews();
        drawField(size);
    }

    @Override
    public void showUserStep(int cellX, int cellY) {
        buttons[cellX][cellY].setImageResource(R.drawable.ic_cross_black_24dp);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return presenter;
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    private void attachPresenter() {
        presenter = (MainPresenter) getLastNonConfigurationInstance();
        if (presenter == null) {
            presenter = new MainPresenter();
        }
        presenter.attachView(this);
    }

    private void drawField(int size) {
        buttons = new AppCompatImageButton[size][size];

        for (int i = 0; i < size; i++) {
            TableRow tableRow = new TableRow(this);

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

    private void onCellClicked(int x, int y) {
        presenter.performStep(x, y);
    }
}

package ru.nsu.ccfit.skokova.tic_tac_toe.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.nsu.ccfit.skokova.tic_tac_toe.R;
import ru.nsu.ccfit.skokova.tic_tac_toe.presenter.GamePresenter;

public class MainActivity extends AppCompatActivity {
    private static final int DEFAULT_SIZE = 3;

    @BindView(R.id.game_panel)
    TableLayout gamePanel;

    private List<AppCompatImageButton> cellButtons = new ArrayList<>(DEFAULT_SIZE * DEFAULT_SIZE);

    private GamePresenter gamePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        drawField();
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
                //...
                return true;
            case R.id.game_mode:
                //...
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void drawField() {
        for (int i = 0; i < DEFAULT_SIZE; i++) { //TODO : fix size
            TableRow tableRow = new TableRow(this);

            for (int j = 0; j < DEFAULT_SIZE; j++) {
                LayoutInflater layoutInflater = getLayoutInflater();
                final View view = layoutInflater.inflate(R.layout.cell_button, null);
                AppCompatImageButton cellButton = view.findViewById(R.id.cell);
                tableRow.addView(cellButton, j);
            }

            gamePanel.addView(tableRow, i);
        }
    }
}

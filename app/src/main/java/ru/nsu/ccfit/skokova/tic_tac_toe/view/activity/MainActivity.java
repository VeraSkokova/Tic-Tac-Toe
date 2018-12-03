package ru.nsu.ccfit.skokova.tic_tac_toe.view.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.nsu.ccfit.skokova.tic_tac_toe.R;
import ru.nsu.ccfit.skokova.tic_tac_toe.presenter.GamePresenter;
import ru.nsu.ccfit.skokova.tic_tac_toe.presenter.PresenterHolder;
import ru.nsu.ccfit.skokova.tic_tac_toe.presenter.StatisticsPresenter;
import ru.nsu.ccfit.skokova.tic_tac_toe.view.fragment.FieldSizeDialogFragment;
import ru.nsu.ccfit.skokova.tic_tac_toe.view.fragment.GameFragment;
import ru.nsu.ccfit.skokova.tic_tac_toe.view.fragment.StatisticsFragment;

public class MainActivity extends AppCompatActivity {
    public static final String MSG_KEY = "FINISH";

    @BindView(R.id.main_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    private PresenterHolder presenterHolder;

    private GameFragment gameFragment;
    private StatisticsFragment statisticsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        navigationView.setNavigationItemSelectedListener(this::navigate);

        gameFragment = new GameFragment();
        statisticsFragment = new StatisticsFragment();

        attachPresenter();

        gameFragment.setPresenter(presenterHolder.getGamePresenter());
        statisticsFragment.setPresenter(presenterHolder.getStatisticsPresenter());

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, gameFragment)
                .commit();
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
                fieldSizeDialogFragment.setPresenter(presenterHolder.getGamePresenter());
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
    public Object onRetainCustomNonConfigurationInstance() {
        return presenterHolder;
    }

    private void attachPresenter() {
        presenterHolder = (PresenterHolder) getLastNonConfigurationInstance();
        if (presenterHolder == null) {
            presenterHolder = new PresenterHolder();
        }
        GamePresenter gamePresenter = presenterHolder.getGamePresenter();
        StatisticsPresenter statisticsPresenter = presenterHolder.getStatisticsPresenter();

        gamePresenter.attachView(gameFragment);
        statisticsPresenter.attachView(statisticsFragment);
    }

    private boolean navigate(MenuItem menuItem) {
        menuItem.setChecked(true);
        drawerLayout.closeDrawers();
        switch (menuItem.getItemId()) {
            case R.id.menu_game:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, gameFragment)
                        .commit();
                return true;
            case R.id.menu_stats:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, statisticsFragment)
                        .commit();
                return true;
            default:
                return false;
        }
    }
}

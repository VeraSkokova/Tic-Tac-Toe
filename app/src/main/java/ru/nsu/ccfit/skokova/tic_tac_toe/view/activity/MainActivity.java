package ru.nsu.ccfit.skokova.tic_tac_toe.view.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.nsu.ccfit.skokova.tic_tac_toe.R;
import ru.nsu.ccfit.skokova.tic_tac_toe.presenter.GamePresenter;
import ru.nsu.ccfit.skokova.tic_tac_toe.presenter.PresenterHolder;
import ru.nsu.ccfit.skokova.tic_tac_toe.presenter.StatisticsPresenter;
import ru.nsu.ccfit.skokova.tic_tac_toe.view.fragment.FieldSizeDialogFragment;
import ru.nsu.ccfit.skokova.tic_tac_toe.view.fragment.GameFragment;
import ru.nsu.ccfit.skokova.tic_tac_toe.view.fragment.GameModeDialogFragment;
import ru.nsu.ccfit.skokova.tic_tac_toe.view.fragment.StatisticsFragment;

public class MainActivity extends AppCompatActivity {
    public static final String MSG_KEY = "FINISH";
    private static final int REQUEST_CODE_PERMISSION_LOCATION = 0;

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
                onPermission();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return presenterHolder;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showGameModeDialogFragment();
                } else {
                    showToast(getString(R.string.no_multi_player));
                }
            }
        }
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

    private void onPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
                showPermissionExplanation();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_CODE_PERMISSION_LOCATION);
            }
        } else {
            showGameModeDialogFragment();
        }
    }

    private void showGameModeDialogFragment() {
        GameModeDialogFragment gameModeDialogFragment = new GameModeDialogFragment();
        gameModeDialogFragment.setPresenter(presenterHolder.getGamePresenter());
        gameModeDialogFragment.show(getSupportFragmentManager(), "GAME_MODE_FRAGMENT");
    }

    private void showPermissionExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.location_explanation)
                .setNeutralButton(R.string.ok, (dialog, which) -> dialog.dismiss())
                .create();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

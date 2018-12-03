package ru.nsu.ccfit.skokova.tic_tac_toe.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ru.nsu.ccfit.skokova.tic_tac_toe.R;
import ru.nsu.ccfit.skokova.tic_tac_toe.presenter.GamePresenter;

public class FieldSizeDialogFragment extends DialogFragment {
    @BindView(R.id.field_size_picker)
    NumberPicker fieldSizePicker;

    private GamePresenter presenter;

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_field_size, container, false);
        unbinder = ButterKnife.bind(this, view);

        fieldSizePicker.setMinValue(3);
        fieldSizePicker.setMaxValue(100);
        fieldSizePicker.setWrapSelectorWheel(false);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setPresenter(GamePresenter presenter) {
        this.presenter = presenter;
    }

    @OnClick(R.id.ok_field_size_button)
    void onFieldSizeSet() {
        presenter.changeFieldSize(fieldSizePicker.getValue());
        dismiss();
    }

    @OnClick(R.id.cancel_field_size_button)
    void onCancelled() {
        dismiss();
    }
}

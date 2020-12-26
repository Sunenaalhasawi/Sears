package com.hasawi.sears.utils.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.hasawi.sears.R;

public class ProgressBarDialog extends DialogFragment {

    public static ProgressBarDialog newInstance() {
        return new ProgressBarDialog();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Translucent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_loading_indicator, container, false);
    }

    public void hideProgressBar() {
        this.dismissAllowingStateLoss();
    }
}

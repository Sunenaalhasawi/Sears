package com.hasawi.sears.utils;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.hasawi.sears.R;
import com.hasawi.sears.databinding.FragmentDialogLoadingIndicatorBinding;
import com.hasawi.sears.ui.base.BaseActivity;

public class LoadingIndicator extends DialogFragment {

    FragmentDialogLoadingIndicatorBinding loadingIndicatorBinding;
    String message = "";

    public LoadingIndicator() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment   --- 2 layout files.
        loadingIndicatorBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dialog_loading_indicator, null, false);
//        if (message != null && !message.equals("")) {
//            loadingIndicatorBinding.message.setText(message);
//        }
        setCancelable(false);
        return loadingIndicatorBinding.getRoot();
    }

    public void showDialog(BaseActivity activity, String message) {
        try {
            this.message = message;
            if (null != activity) {
                FragmentManager fm = activity.getSupportFragmentManager();
                Fragment oldFragment = fm.findFragmentByTag("progress_dialog");
                if (oldFragment != null) {
                    fm.beginTransaction().remove(oldFragment).commit();
                    fm.beginTransaction().add(this, "progress_dialog");
                }

//                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                try {
                    show(fm, "progress_dialog");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideDialog() {
        try {
            dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}


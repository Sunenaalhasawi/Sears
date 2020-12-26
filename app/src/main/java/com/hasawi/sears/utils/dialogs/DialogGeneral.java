package com.hasawi.sears.utils.dialogs;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import com.hasawi.sears.R;
import com.hasawi.sears.databinding.LayoutDialogGeneralBinding;
import com.hasawi.sears.ui.base.BaseActivity;

public class DialogGeneral extends DialogFragment {
    DialogGeneralInterface dialogGeneralInterface;
    LayoutDialogGeneralBinding dialogGeneralBoxBinding;
    String positiveLeft = null;
    String negativeRight = null;
    String title = null;
    String description = null;
    Drawable res = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
        setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        dialogGeneralBoxBinding = DataBindingUtil.inflate(inflater, R.layout.layout_dialog_general, null, false);
        if (negativeRight == null) {
            dialogGeneralBoxBinding.btnCancel.setVisibility(View.GONE);
        } else {
            dialogGeneralBoxBinding.btnCancel.setText(negativeRight);
            dialogGeneralBoxBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (dialogGeneralInterface != null)
                            dialogGeneralInterface.negativeClick();
                        dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        if (positiveLeft == null)
            dialogGeneralBoxBinding.btnRetry.setVisibility(View.GONE);
        else {
            dialogGeneralBoxBinding.btnRetry.setText(positiveLeft);
            dialogGeneralBoxBinding.btnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (dialogGeneralInterface != null)
                            dialogGeneralInterface.positiveClick();
                        dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

//        if (res == null) {
//            dialogGeneralBoxBinding.imageView20.setVisibility(View.GONE);
//            dialogGeneralBoxBinding.imageView27.setVisibility(View.GONE);
//        } else {
//            dialogGeneralBoxBinding.imageView20.setVisibility(View.VISIBLE);
//            dialogGeneralBoxBinding.imageView20.setImageDrawable(res);
//        }

//        dialogGeneralBoxBinding.tvDesc.setText(description == null ? "" : description);
        if (title == null)
            dialogGeneralBoxBinding.tvTitle.setVisibility(View.GONE);
        else
            dialogGeneralBoxBinding.tvTitle.setText(title);
        dialogGeneralBoxBinding.tvTitle.setVisibility(View.GONE);
        return dialogGeneralBoxBinding.getRoot();


    }


    @Override
    public void onStart() {
        super.onStart();
        final Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
//
//            dialogGeneralBoxBinding.imageView27.setOnClickListener(
//                    new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            dismiss();
//                        }
//                    });
        }
    }

    public void setClickListener(DialogGeneralInterface dialogGeneralInterface) {
        this.dialogGeneralInterface = dialogGeneralInterface;
    }

    public void setTexts(String title, String description, String positiveLeft, String negativeRight, Drawable res) {
        this.title = title;
        this.description = description;
        this.positiveLeft = positiveLeft;
        this.negativeRight = negativeRight;
        this.res = res;
    }

    public void showDialog(BaseActivity activity) {
        try {
            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
            show(ft, "Frag");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isDialogShowingCurrently() {
        return this.getDialog().isShowing();
    }

    public interface DialogGeneralInterface {
        void positiveClick();

        void negativeClick();
    }
}

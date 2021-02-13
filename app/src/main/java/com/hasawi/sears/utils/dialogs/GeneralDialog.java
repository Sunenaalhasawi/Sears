package com.hasawi.sears.utils.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.hasawi.sears.R;
import com.hasawi.sears.databinding.LayoutGeneralDialogBinding;

public class GeneralDialog extends DialogFragment {

    String title = "", content = "";

    public GeneralDialog(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Translucent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LayoutGeneralDialogBinding layoutGeneralDialogBinding = DataBindingUtil.inflate(inflater, R.layout.layout_general_dialog, null, true);
        layoutGeneralDialogBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        layoutGeneralDialogBinding.tvTitle.setText(title);
        layoutGeneralDialogBinding.tvContent.setText(content);
        return layoutGeneralDialogBinding.getRoot();
    }

}

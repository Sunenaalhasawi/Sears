package com.hasawi.sears.utils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.hasawi.sears.R;
import com.hasawi.sears.databinding.LayoutDialogPaymentFailedBinding;
import com.hasawi.sears.ui.main.view.DashboardActivity;

public class FailedPaymentDialog extends DialogFragment {

    DashboardActivity dashboardActivity;

    public FailedPaymentDialog(DashboardActivity dashboardActivity) {
        this.dashboardActivity = dashboardActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Translucent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LayoutDialogPaymentFailedBinding paymentFailedBinding = DataBindingUtil.inflate(inflater, R.layout.layout_dialog_payment_failed, null, true);
        paymentFailedBinding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardActivity.popBackStack();
                dismiss();
            }
        });

        return paymentFailedBinding.getRoot();
    }

//    @Override
//    public Dialog onCreateDialog(final Bundle savedInstanceState) {
//        LayoutInflater inflater = getLayoutInflater();
//        LayoutCartDialogNewDesignBinding cartDialogNewDesignBinding = DataBindingUtil.inflate(inflater, R.layout.layout_cart_dialog_new_design, null, true);
//
//        cartDialogNewDesignBinding.btnContinueShopping.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
//
//        cartDialogNewDesignBinding.btnViewCart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(dashboardActivity, MyCartActivity.class);
//                startActivity(intent);
//                dismiss();
//            }
//        });
//
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setView(cartDialogNewDesignBinding.getRoot());
//        return builder.create();
//    }
}

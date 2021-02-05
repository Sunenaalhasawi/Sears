package com.hasawi.sears.ui.main.view.dashboard.navigation_drawer_menu;

import android.Manifest;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.hasawi.sears.R;
import com.hasawi.sears.databinding.FragmentContactUsBinding;
import com.hasawi.sears.ui.base.BaseFragment;
import com.hasawi.sears.ui.main.view.DashboardActivity;

import static com.hasawi.sears.utils.AppConstants.CALL_PHONE_REQUEST_CODE;

public class ContactUsFragment extends BaseFragment {

    FragmentContactUsBinding fragmentContactUsBinding;
    DashboardActivity dashboardActivity;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_contact_us;
    }

    @Override
    protected void setup() {
        fragmentContactUsBinding = (FragmentContactUsBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        fragmentContactUsBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardActivity.onBackPressed();
            }
        });
        fragmentContactUsBinding.tvSearsCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission(Manifest.permission.CALL_PHONE, CALL_PHONE_REQUEST_CODE);
            }
        });
        fragmentContactUsBinding.tvSearsEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardActivity.sendEmailToCustomerCare();
            }
        });


    }



    // Function to check and request permission
    public void checkPermission(String permission, int requestCode) {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(
                dashboardActivity,
                permission)
                == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                    CALL_PHONE_REQUEST_CODE);
        } else {
            dashboardActivity.callSearsCustomerCare();
        }
    }

    // This function is called when user accept or decline the permission.
// Request Code is used to check which permission called this function.
// This request code is provided when user is prompt for permission.

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALL_PHONE_REQUEST_CODE) {
            // Checking whether user granted the permission or not.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dashboardActivity.callSearsCustomerCare();
            } else {
                Toast.makeText(dashboardActivity, "Call Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}

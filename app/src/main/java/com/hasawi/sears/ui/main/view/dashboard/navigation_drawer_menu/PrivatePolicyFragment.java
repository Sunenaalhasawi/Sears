package com.hasawi.sears.ui.main.view.dashboard.navigation_drawer_menu;

import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.hasawi.sears.R;
import com.hasawi.sears.databinding.FragmentDynamicContentBinding;
import com.hasawi.sears.ui.base.BaseFragment;
import com.hasawi.sears.ui.main.view.DashboardActivity;
import com.hasawi.sears.ui.main.viewmodel.DynamicContentViewModel;
import com.hasawi.sears.utils.AppConstants;

public class PrivatePolicyFragment extends BaseFragment {
    FragmentDynamicContentBinding fragmentDynamicContentBinding;
    DynamicContentViewModel dynamicContentViewModel;
    DashboardActivity dashboardActivity;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_dynamic_content;
    }

    @Override
    protected void setup() {
        fragmentDynamicContentBinding = (FragmentDynamicContentBinding) viewDataBinding;
        dynamicContentViewModel = new ViewModelProvider(this).get(DynamicContentViewModel.class);
        dashboardActivity = (DashboardActivity) getActivity();
        fragmentDynamicContentBinding.tvTitle.setText("PRIVATE POLICY");
        fragmentDynamicContentBinding.imageViewcontent.setImageDrawable(getResources().getDrawable(R.drawable.privacy_policy));
        getContent();

    }

    private void getContent() {
        fragmentDynamicContentBinding.progressBar.setVisibility(View.VISIBLE);
        dynamicContentViewModel.getDynamicWebviewContent(AppConstants.PRIVATE_POLICY).observe(this, dynamicContentResponseResource -> {
            switch (dynamicContentResponseResource.status) {
                case SUCCESS:
                    try {
                        fragmentDynamicContentBinding.tvTitle.setText(dynamicContentResponseResource.data.getData().getName());
                        Glide.with(getContext())
                                .load(getResources().getDrawable(R.drawable.about_us))
                                .into(fragmentDynamicContentBinding.imageViewcontent);
                        String htmlContent = dynamicContentResponseResource.data.getData().getFulfilment();
                        final String mimeType = "text/html";
                        final String encoding = "UTF-8";
                        fragmentDynamicContentBinding.webviewDynamicContent.loadData(htmlContent, mimeType, encoding);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case LOADING:
                    break;
                case ERROR:
                    Toast.makeText(dashboardActivity, dynamicContentResponseResource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
            fragmentDynamicContentBinding.progressBar.setVisibility(View.GONE);
        });
    }
}
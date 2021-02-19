package com.hasawi.sears_outlet.ui.main.view.dashboard.navigation_drawer_menu;

import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.hasawi.sears_outlet.R;
import com.hasawi.sears_outlet.databinding.FragmentDynamicContentBinding;
import com.hasawi.sears_outlet.ui.base.BaseFragment;
import com.hasawi.sears_outlet.ui.main.view.DashboardActivity;
import com.hasawi.sears_outlet.ui.main.viewmodel.DynamicContentViewModel;
import com.hasawi.sears_outlet.utils.AppConstants;

public class AboutUsFragment extends BaseFragment {
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
        dashboardActivity.handleActionMenuBar(false, false, "");
        fragmentDynamicContentBinding.tvTitle.setText("ABOUT US");
        fragmentDynamicContentBinding.imageViewcontent.setImageDrawable(getResources().getDrawable(R.drawable.about_us));
        getAboutUsContent();

    }

    private void getAboutUsContent() {
        fragmentDynamicContentBinding.progressBar.setVisibility(View.VISIBLE);
        dynamicContentViewModel.getDynamicWebviewContent(AppConstants.ABOUT_US).observe(this, dynamicContentResponseResource -> {
            switch (dynamicContentResponseResource.status) {
                case SUCCESS:
                    try {
                        fragmentDynamicContentBinding.tvTitle.setText(dynamicContentResponseResource.data.getData().getName());
                        try {
                            Glide.with(getContext())
                                    .load(dynamicContentResponseResource.data.getData().getIcon())
                                    .into(fragmentDynamicContentBinding.imageViewcontent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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


    @Override
    public void onPause() {
        super.onPause();
        dashboardActivity.handleActionMenuBar(false, true, "");
    }
}

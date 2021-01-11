package com.hasawi.sears.ui.main.view.dashboard.home;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hasawi.sears.R;
import com.hasawi.sears.databinding.FragmentWebviewBinding;
import com.hasawi.sears.ui.base.BaseFragment;
import com.hasawi.sears.ui.main.view.DashboardActivity;

public class WebviewFragment extends BaseFragment {
    FragmentWebviewBinding fragmentWebviewBinding;
    String url = "";
    DashboardActivity dashboardActivity;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_webview;
    }

    @Override
    protected void setup() {
        fragmentWebviewBinding = (FragmentWebviewBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        dashboardActivity.handleActionBarIcons(false);
        try {
            Bundle bundle = getArguments();
            url = bundle.getString("url");
            fragmentWebviewBinding.webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                //Show loader on url load
                @Override
                public void onLoadResource(WebView view, String url) {

                }

                @Override
                public void onPageFinished(WebView view, String url) {

                }
            });

            fragmentWebviewBinding.webView.loadUrl(url);
            fragmentWebviewBinding.webView.getSettings().setJavaScriptEnabled(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.hasawi.sears.ui.main.view.checkout;

import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.pojo.PaymentMode;
import com.hasawi.sears.data.api.model.pojo.PaymentResponse;
import com.hasawi.sears.databinding.FragmentPaymentNewBinding;
import com.hasawi.sears.ui.base.BaseFragment;
import com.hasawi.sears.ui.main.view.DashboardActivity;
import com.hasawi.sears.ui.main.viewmodel.PaymentViewModel;
import com.hasawi.sears.utils.FailedPaymentDialog;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentFragment extends BaseFragment {
    FragmentPaymentNewBinding fragmentPaymentNewBinding;
    PaymentViewModel paymentViewModel;
    PaymentMode selectedPaymentMode;
    DashboardActivity dashboardActivity;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_payment_new;
    }

    @Override
    protected void setup() {
        fragmentPaymentNewBinding = (FragmentPaymentNewBinding) viewDataBinding;
        paymentViewModel = new ViewModelProvider(getActivity()).get(PaymentViewModel.class);
        dashboardActivity = (DashboardActivity) getActivity();
        try {
            Bundle bundle = getArguments();
            String paymentModeString = bundle.getString("payment_mode");
            Gson gson = new Gson();
            selectedPaymentMode = gson.fromJson(paymentModeString, PaymentMode.class);
            paymentViewModel.setSelectedPaymentMode(selectedPaymentMode);
            callPaymentApi(selectedPaymentMode);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void callPaymentApi(PaymentMode selectedPaymentMode) {

        FailedPaymentDialog failedPaymentDialog = new FailedPaymentDialog(dashboardActivity);
        fragmentPaymentNewBinding.webviewPayment.setWebViewClient(new WebViewClient() {
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
                fragmentPaymentNewBinding.progressBar.setVisibility(View.GONE);
                try {

                    if (url.contains(selectedPaymentMode.getFailedUrl())) {
                        failedPaymentDialog.show(getParentFragmentManager(), "PAYMENT_FAILED");
                    } else if (url.contains(selectedPaymentMode.getSuccessUrl())) {
                        fragmentPaymentNewBinding.webviewPayment.clearHistory();
                        fragmentPaymentNewBinding.webviewPayment.loadUrl("javascript:HtmlViewer.showHTML" +
                                "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                        fragmentPaymentNewBinding.progressBar.setVisibility(View.VISIBLE);
                    }

                } catch (Exception exception) {
                    failedPaymentDialog.show(getParentFragmentManager(), "PAYMENT_FAILED");
                    exception.printStackTrace();
                }
            }
        });

        //Load url in webView
        fragmentPaymentNewBinding.webviewPayment.loadUrl(selectedPaymentMode.getPaymentUrl());
        fragmentPaymentNewBinding.webviewPayment.getSettings().setJavaScriptEnabled(true);
        fragmentPaymentNewBinding.webviewPayment.getSettings().setSupportZoom(true);
        fragmentPaymentNewBinding.webviewPayment.getSettings().setBuiltInZoomControls(true);
        fragmentPaymentNewBinding.webviewPayment.addJavascriptInterface(new MyJavaScriptInterface(dashboardActivity), "HtmlViewer");

    }


    public class MyJavaScriptInterface {

        PaymentResponse paymentResponse;
        private DashboardActivity dashboardActivity;

        MyJavaScriptInterface(DashboardActivity dashboardActivity) {
            this.dashboardActivity = dashboardActivity;
        }

        @JavascriptInterface
        public void showHTML(String html) {
            try {
//                System.out.println(html);
                int startingIndex = html.indexOf("{");
                int closingIndex = html.indexOf("statusCode");
                String responseString = html.substring(startingIndex, closingIndex + 16);
                JSONObject jsonObject = new JSONObject(responseString);
                Gson gson = new Gson();
                String paymentObject = gson.toJson(selectedPaymentMode, PaymentMode.class);
//                JSONObject paymentJson = new JSONObject(paymentObject);
//                Map<String, Object> jsonParams = new ArrayMap<>();
//                jsonParams.put("payment", paymentObject);
//                jsonParams.put("getPaymentStatusResponse", jsonObject.get("data"));

                Bundle bundle = new Bundle();
                bundle.putString("payment", paymentObject);
                bundle.putString("getPaymentStatusResponse", jsonObject.get("data").toString());
                dashboardActivity.showBackButton(true);
                dashboardActivity.setTitle("Order Details");
                dashboardActivity.replaceFragment(R.id.fragment_replacer, new OrderFragment(), bundle, true, false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}

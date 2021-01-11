package com.hasawi.sears.ui.main.view.checkout;

import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.pojo.PaymentMode;
import com.hasawi.sears.databinding.FragmentPaymentNewBinding;
import com.hasawi.sears.ui.base.BaseFragment;
import com.hasawi.sears.ui.main.adapters.PaymentSuccessListener;
import com.hasawi.sears.ui.main.view.DashboardActivity;
import com.hasawi.sears.ui.main.viewmodel.PaymentViewModel;
import com.hasawi.sears.utils.DateTimeUtils;
import com.hasawi.sears.utils.FailedPaymentDialog;

import org.json.JSONArray;
import org.json.JSONObject;

public class PaymentFragment extends BaseFragment implements PaymentSuccessListener {
    FragmentPaymentNewBinding fragmentPaymentNewBinding;
    PaymentViewModel paymentViewModel;
    PaymentMode selectedPaymentMode;
    DashboardActivity dashboardActivity;
    PaymentSuccessListener paymentSuccessListener;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_payment_new;
    }

    @Override
    protected void setup() {
        fragmentPaymentNewBinding = (FragmentPaymentNewBinding) viewDataBinding;
        paymentViewModel = new ViewModelProvider(getActivity()).get(PaymentViewModel.class);
        paymentSuccessListener = this;
        dashboardActivity = (DashboardActivity) getActivity();
        dashboardActivity.handleActionBarIcons(false);
        try {
            Bundle bundle = getArguments();
            String paymentModeString = bundle.getString("payment_mode");
            Gson gson = new Gson();
            selectedPaymentMode = gson.fromJson(paymentModeString, PaymentMode.class);
            paymentViewModel.setSelectedPaymentMode(selectedPaymentMode);
            if (selectedPaymentMode.isPostPay()) {
                Bundle paymentbundle = new Bundle();
                String paymentJson = gson.toJson(selectedPaymentMode, PaymentMode.class);
                paymentbundle.putString("payment", paymentJson);
                paymentbundle.putString("payment_url", "");
                paymentbundle.putString("paymentId", null);
                dashboardActivity.handleActionMenuBar(false, true, "Order Confirmation");
                dashboardActivity.replaceFragment(R.id.fragment_replacer, new OrderFragment(), paymentbundle, true, false);
            } else
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
                    Bundle analyticsBundle = new Bundle();
                    analyticsBundle.putString(FirebaseAnalytics.Param.PAYMENT_TYPE, selectedPaymentMode.getName());
                    try {
                        analyticsBundle.putString(FirebaseAnalytics.Param.END_DATE, DateTimeUtils.getCurrentStringDateTime());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (url.contains(selectedPaymentMode.getFailedUrl())) {
                        failedPaymentDialog.show(getParentFragmentManager(), "PAYMENT_FAILED");
                        analyticsBundle.putLong(FirebaseAnalytics.Param.SUCCESS, 0);
                        analyticsBundle.putString("payment_status", "failed");
                        dashboardActivity.getmFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.ADD_PAYMENT_INFO, analyticsBundle);
                    } else if (url.contains(selectedPaymentMode.getSuccessUrl())) {
                        fragmentPaymentNewBinding.webviewPayment.stopLoading();
//                        fragmentPaymentNewBinding.webviewPayment.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");

//                        fragmentPaymentNewBinding.webviewPayment.loadUrl("javascript:HtmlViewer.showHTML" +
//                                "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                        analyticsBundle.putLong(FirebaseAnalytics.Param.SUCCESS, 1);
                        analyticsBundle.putString("payment_status", "success");
                        analyticsBundle.putString("payment_method", selectedPaymentMode.getName());
                        dashboardActivity.getmFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.ADD_PAYMENT_INFO, analyticsBundle);

                        Bundle paymentBundle = new Bundle();
                        Gson gson = new Gson();
                        String paymentJson = gson.toJson(selectedPaymentMode, PaymentMode.class);
                        paymentBundle.putString("payment", paymentJson);
                        paymentBundle.putString("payment_url", url);
                        paymentBundle.putString("paymentId", "");
                        dashboardActivity.replaceFragment(R.id.fragment_replacer, new OrderFragment(), paymentBundle, true, false);

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
//        fragmentPaymentNewBinding.webviewPayment.getSettings().setSupportZoom(true);
//        fragmentPaymentNewBinding.webviewPayment.getSettings().setBuiltInZoomControls(true);
//        fragmentPaymentNewBinding.webviewPayment.addJavascriptInterface(new MyJavaScriptInterface(dashboardActivity), "HtmlViewer");

    }


    @Override
    public void onPaymentSuccess(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray invoiceTransactions = data.getJSONArray("InvoiceTransactions");
            JSONObject invoiceTransactionObj = invoiceTransactions.getJSONObject(0);
            String paymentId = invoiceTransactionObj.getString("PaymentId");
            Gson gson = new Gson();
            String paymentJson = gson.toJson(selectedPaymentMode, PaymentMode.class);
            Bundle paymentBundle = new Bundle();
            paymentBundle.putString("payment", paymentJson);
            paymentBundle.putString("payment_id", paymentId);
            dashboardActivity.replaceFragment(R.id.fragment_replacer, new OrderFragment(), paymentBundle, true, false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class MyJavaScriptInterface {

        private DashboardActivity dashboardActivity;

        MyJavaScriptInterface(DashboardActivity dashboardActivity) {
            this.dashboardActivity = dashboardActivity;
        }

        @JavascriptInterface
        public void showHTML(String html) {
            try {
                int startingIndex = html.indexOf("{");
                int closingIndex = html.indexOf("statusCode");
                String responseString = html.substring(startingIndex, closingIndex + 16);
                paymentSuccessListener.onPaymentSuccess(responseString);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


}

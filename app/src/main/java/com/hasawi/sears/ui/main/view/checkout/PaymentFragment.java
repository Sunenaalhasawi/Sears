package com.hasawi.sears.ui.main.view.checkout;

import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

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
import com.hasawi.sears.utils.PreferenceHandler;

import org.json.JSONObject;

import java.util.Map;

public class PaymentFragment extends BaseFragment {
    FragmentPaymentNewBinding fragmentPaymentNewBinding;
    PaymentViewModel paymentViewModel;
    PaymentMode selectedPaymentMode;
    DashboardActivity dashboardActivity;
    String shippingModeId = "";

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


    private void callReviewOrderApi(Map<String, Object> jsonParams) {
        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        String customerId = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
//        String addressId = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_SELECTED_ADDRESS_ID, "");
        String addressId = "ADRE_00038";
        shippingModeId = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_SELECTED_SHIPPING_MODE_ID, "");
        jsonParams.put("shippingId", shippingModeId);
        String cartId = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_CART_ID, "");
        String sessionToken = preferenceHandler.getData(PreferenceHandler.LOGIN_TOKEN, "");

        paymentViewModel.orderConfirmation(customerId, addressId, cartId, sessionToken, jsonParams).observe(getActivity(), orderResponseResource -> {
            fragmentPaymentNewBinding.progressBar.setVisibility(View.GONE);
            switch (orderResponseResource.status) {
                case SUCCESS:
                    Bundle bundle = new Bundle();
                    Gson gson = new Gson();
                    String orderConfirmationResponse = gson.toJson(orderResponseResource.data);
                    bundle.putString("order_confirmed_response", orderConfirmationResponse);
                    dashboardActivity.showBackButton(true, true);
                    dashboardActivity.replaceFragment(R.id.fragment_replacer, new OrderFragment(), bundle, true, false);
                    break;
                case LOADING:
                    break;
                case ERROR:
                    Toast.makeText(getActivity(), orderResponseResource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
        });

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
//                fragmentPaymentNewBinding.progressBar.setVisibility(View.VISIBLE);
                int startingIndex = html.indexOf("{");
                int closingIndex = html.indexOf("statusCode");
                String responseString = html.substring(startingIndex, closingIndex + 16);
                JSONObject jsonObject = new JSONObject(responseString);
                Gson gson = new Gson();
                String paymentObject = gson
                        .toJson(selectedPaymentMode, PaymentMode.class);
                Map<String, Object> jsonParams = new ArrayMap<>();
                jsonParams.put("payment", paymentObject);
                jsonParams.put("paymentId", selectedPaymentMode.getPaymentId());
                callReviewOrderApi(jsonParams);
            } catch (Exception e) {

                e.printStackTrace();
            }
        }

    }

}

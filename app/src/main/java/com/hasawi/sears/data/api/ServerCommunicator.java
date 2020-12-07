package com.hasawi.sears.data.api;

import android.content.Context;
import android.widget.Toast;

import com.hasawi.sears.ui.base.BaseActivity;

import java.net.SocketTimeoutException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class ServerCommunicator {

    protected static final int SelectedapiURLGetCategory = 100;
    protected static final int SelectedapiURLGetProducts = 101;
    private BaseActivity baseActivity = null;
    private Context context = null;
    private int selectedUrlID;
    private HashMap<String, String> hashMapData;
    private ServerComunicatorListener serverComunicatorListnerListener;
    private int max_time_out = 10000;
    private int min_time_out = 3000;
    private int temp_time = min_time_out;
    private IsNetSlow isNetSlow;

    public ServerCommunicator(int selectedURL, BaseActivity baseActivity, ServerComunicatorListener serverComunicatorListener) {
        selectedUrlID = selectedURL;
        this.baseActivity = baseActivity;
        context = baseActivity;
        setServerComunicatorListnerListener(serverComunicatorListener);
    }

    public ServerCommunicator(int selectedURL, Context context, ServerComunicatorListener serverComunicatorListener) {
        selectedUrlID = selectedURL;
        this.context = context;
        setServerComunicatorListnerListener(serverComunicatorListener);
    }

    private void callApi(boolean shouldShowLoadingIndicator) {

        try {

            ApiInterface apiInterface = RetrofitApiClient.getInstance().getApiInterface();
            Call repos = getCallFromSelectedUrl(apiInterface);
            if (repos != null)
                repos.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        try {
                            ServerCommunicator.this.onResponse(call, response.body());
                            if (response != null)
                                if (serverComunicatorListnerListener != null) {
                                    serverComunicatorListnerListener.onResponse(call, response.body());
                                    if (isNetSlow != null) {
                                        isNetSlow.netIsFast();
                                    }
                                }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                       hideLoadingIndicator();
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        if (t instanceof SocketTimeoutException && max_time_out > temp_time) {
                            try {
//                                recallAPi();
                                if (temp_time == min_time_out && isNetSlow != null) {
                                    isNetSlow.netIsSlow();
                                    if (baseActivity != null) {
                                        Toast.makeText(baseActivity, "Slow internet connection.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {

//                                if (isRetryPopup && baseActivity != null &&
//                                        (t instanceof TimeoutException
//                                                || t instanceof SocketTimeoutException
//                                                || t instanceof ConnectException)) {
//
//                                    new MaterialAlertDialogBuilder(baseActivity)
//                                            .setTitle(baseActivity.getString(R.string.warning))
//                                            .setMessage(baseActivity.getString(R.string.wake_up_your_connection))
//                                            .setNeutralButton(baseActivity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//
//                                                }
//                                            })
//                                            .setPositiveButton(baseActivity.getString(R.string.retry), new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    recallAPi();
//                                                }
//                                            }).show();
//                                } else {
//                                    ServerCommunicator.this.onFailure(call, t);
//                                    if (serverComunicatorListnerListener != null) {
//                                        serverComunicatorListnerListener.onFailure(call, t);
//                                    }
//                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
//                            hideLoadingIndicator();
                        }
                    }
                });
        } catch (Exception e) {
            e.printStackTrace();
//            FirebaseCrashlytics.getInstance().recordException(e);
//            hideLoadingIndicator();
        }

    }

    public abstract void onResponse(Call call, Object response);

    public abstract void onFailure(Call call, Throwable t);


    private Call getCallFromSelectedUrl(ApiInterface apiInterface) {

        Call apiCall = null;
        switch (selectedUrlID) {

            case SelectedapiURLGetCategory:
                apiCall = apiInterface.getProductCategories(hashMapData);
                break;
            case SelectedapiURLGetProducts:
//                apiCall = apiInterface.getProductsList();
                break;

            default:
        }
        return apiCall;

    }

    public void setServerComunicatorListnerListener(ServerComunicatorListener serverComunicatorListnerListener) {
        this.serverComunicatorListnerListener = serverComunicatorListnerListener;
    }

    public interface IsNetSlow {
        void netIsSlow();

        void netIsFast();
    }
}

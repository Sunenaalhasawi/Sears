package com.hasawi.sears.ui.main.view.paging_lib;

import android.util.ArrayMap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.hasawi.sears.data.api.RetrofitApiClient;
import com.hasawi.sears.data.api.model.pojo.Content;
import com.hasawi.sears.data.api.response.ProductResponse;
import com.hasawi.sears.utils.NetworkState;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDataSource extends PageKeyedDataSource<Long, Content> {

    private static final String TAG = ProductDataSource.class.getSimpleName();

    /*
     * Step 1: Initialize the restApiFactory.
     * The networkState and initialLoading variables
     * are for updating the UI when data is being fetched
     * by displaying a progress bar
     */
    JSONArray filterArray;
    private MutableLiveData networkState;
    private MutableLiveData initialLoading;
    private String categoryId = "", page_no = "", sort = "";

    public ProductDataSource(String categoryId, JSONArray filterArray, String page_no, String sort) {
        this.categoryId = categoryId;
        this.filterArray = filterArray;
        this.page_no = page_no;
        this.sort = sort;
        networkState = new MutableLiveData();
        initialLoading = new MutableLiveData();
    }

    public MutableLiveData getNetworkState() {
        return networkState;
    }

    public MutableLiveData getInitialLoading() {
        return initialLoading;
    }


    /*
     * Step 2: This method is responsible to load the data initially
     * when app screen is launched for the first time.
     * We are fetching the first page data from the api
     * and passing it via the callback method to the UI.
     */
    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params,
                            @NonNull LoadInitialCallback<Long, Content> callback) {

        initialLoading.postValue(NetworkState.LOADING);
        networkState.postValue(NetworkState.LOADING);

        RequestBody body = addInputParams(categoryId, filterArray, sort);

        Call<ProductResponse> productsResponseCall = RetrofitApiClient.getInstance().getApiInterface().getProductsList(body, page_no);
        productsResponseCall.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                try {
                    if (response.isSuccessful()) {
                        callback.onResult(response.body().getData().getProducts().getContent(), null, 2l);
                        initialLoading.postValue(NetworkState.LOADED);
                        networkState.postValue(NetworkState.LOADED);

                    } else {
                        initialLoading.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                        networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                String errorMessage = t == null ? "unknown error" : t.getMessage();
                networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
            }
        });

    }


    @Override
    public void loadBefore(@NonNull LoadParams<Long> params,
                           @NonNull LoadCallback<Long, Content> callback) {

    }


    /*
     * Step 3: This method it is responsible for the subsequent call to load the data page wise.
     * This method is executed in the background thread
     * We are fetching the next page data from the api
     * and passing it via the callback method to the UI.
     * The "params.key" variable will have the updated value.
     */
    @Override
    public void loadAfter(@NonNull LoadParams<Long> params,
                          @NonNull LoadCallback<Long, Content> callback) {

        Log.i(TAG, "Loading Rang " + params.key + " Count " + params.requestedLoadSize);
        networkState.postValue(NetworkState.LOADING);
        RequestBody body = addInputParams(categoryId, filterArray, sort);
        Call<ProductResponse> productsResponseCall = RetrofitApiClient.getInstance().getApiInterface().getProductsList(body, params.key + "");
        productsResponseCall.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                /*
                 * If the request is successful, then we will update the callback
                 * with the latest feed items and
                 * "params.key+1" -> set the next key for the next iteration.
                 */
                if (response.isSuccessful()) {
                    long nextKey = response.body().getData().getProducts().getPageable().getPageNumber() + 1;
//                    long nextKey = (params.key == response.body().getData().getProducts().getPageable().getPageNumber()) ? -1 : params.key + 1;
                    callback.onResult(response.body().getData().getProducts().getContent(), nextKey);
                    networkState.postValue(NetworkState.LOADED);

                } else
                    networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                String errorMessage = t == null ? "unknown error" : t.getMessage();
                networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
            }
        });

    }

    private RequestBody addInputParams(String categoryId, JSONArray filterArray, String sort) {
        JSONArray categoryIds = new JSONArray();
        categoryIds.put(categoryId);
        JSONArray attributeIds;
        if (filterArray == null)
            attributeIds = new JSONArray();
        else
            attributeIds = filterArray;
        JSONArray brandIds = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("categoryIds", categoryIds);
            jsonObject.put("attributeIds", attributeIds);
            jsonObject.put("brandIds", brandIds);

        } catch (Exception e) {
            e.printStackTrace();
        }


        String jsonString = jsonObject.toString();

        Map<String, Object> jsonParams = new ArrayMap<>();
//put something inside the map, could be null
        jsonParams.put("categoryIds", categoryIds);
        jsonParams.put("attributeIds", attributeIds);
        jsonParams.put("brandIds", brandIds);
        jsonParams.put("sorting", sort);


        return RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());
    }
}

package com.hasawi.sears.ui.main.view.dashboard.product.paging;

import android.util.ArrayMap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.hasawi.sears.data.api.RetrofitApiClient;
import com.hasawi.sears.data.api.model.pojo.Category;
import com.hasawi.sears.data.api.model.pojo.FilterAttributeValues;
import com.hasawi.sears.data.api.model.pojo.Product;
import com.hasawi.sears.data.api.response.ProductResponse;
import com.hasawi.sears.utils.NetworkState;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDataSource extends PageKeyedDataSource<Long, Product> {

    private static final String TAG = ProductDataSource.class.getSimpleName();

    /*
     * Step 1: Initialize the restApiFactory.
     * The networkState and initialLoading variables
     * are for updating the UI when data is being fetched
     * by displaying a progress bar
     */
    JSONArray filterArray, brandArray, colorArray, sizeArray;
    private MutableLiveData networkState;
    private MutableLiveData initialLoading;
    private MutableLiveData<Map<String, List<FilterAttributeValues>>> filterAttributeMap;
    private MutableLiveData<List<String>> sortStrings;
    private MutableLiveData<List<Category>> categoryList;
    private String categoryId = "", page_no = "", sort = "", userId = "";
    private boolean loadedInitial = false;
    private JSONObject productPayload = null;

    public ProductDataSource(String categoryId, JSONArray filterArray, JSONArray brandArray, JSONArray colorArray, JSONArray sizeArray, String page_no, String sort, String userId, JSONObject productPayload) {
        this.categoryId = categoryId;
        this.filterArray = filterArray;
        this.brandArray = brandArray;
        this.colorArray = colorArray;
        this.sizeArray = sizeArray;
        this.page_no = page_no;
        this.sort = sort;
        this.userId = userId;
        this.productPayload = productPayload;
        networkState = new MutableLiveData();
        initialLoading = new MutableLiveData();
        filterAttributeMap = new MutableLiveData<>();
        sortStrings = new MutableLiveData<>();
        categoryList = new MutableLiveData<>();
    }

    public MutableLiveData getNetworkState() {
        return networkState;
    }

    public MutableLiveData getInitialLoading() {
        return initialLoading;
    }

    public MutableLiveData<Map<String, List<FilterAttributeValues>>> getFilterAttributeMap() {
        return filterAttributeMap;
    }

    public MutableLiveData<List<String>> getSortStrings() {
        return sortStrings;
    }

    public MutableLiveData<List<Category>> getCategoryList() {
        return categoryList;
    }

    /*
     * Step 2: This method is responsible to load the data initially
     * when app screen is launched for the first time.
     * We are fetching the first page data from the api
     * and passing it via the callback method to the UI.
     */
    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params,
                            @NonNull LoadInitialCallback<Long, Product> callback) {

        initialLoading.postValue(NetworkState.LOADING);
        networkState.postValue(NetworkState.LOADING);

        RequestBody body = addInputParams(categoryId, filterArray, brandArray, colorArray, sizeArray, sort, userId, productPayload);

        Call<ProductResponse> productsResponseCall = RetrofitApiClient.getInstance().getApiInterface().getProductsList(body, page_no);
        productsResponseCall.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                try {
                    loadedInitial = true;
                    if (response.isSuccessful()) {
                        if (response.body().getData().getFilterAttributes() != null) {
                            filterAttributeMap.postValue(response.body().getData().getFilterAttributes());
                        } else {
                            filterAttributeMap.postValue(null);
                        }
                        if (response.body().getData().getSortStrings() != null) {
                            sortStrings.postValue(response.body().getData().getSortStrings());
                        } else {
                            sortStrings.postValue(null);
                        }
                        if (response.body().getData().getCategories() != null) {
                            categoryList.postValue(response.body().getData().getCategories());
                        } else {
                            categoryList.postValue(null);
                        }
                        if (response.body().getData().getProductList() == null) {
                            initialLoading.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                            networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                        } else {
                            callback.onResult(response.body().getData().getProductList().getProducts(), null, 1l);
                            initialLoading.postValue(NetworkState.LOADED);
                            networkState.postValue(NetworkState.LOADED);

                        }
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
                           @NonNull LoadCallback<Long, Product> callback) {

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
                          @NonNull LoadCallback<Long, Product> callback) {

        Log.i(TAG, "Loading Rang " + params.key + " Count " + params.requestedLoadSize);
        if (loadedInitial)
            networkState.postValue(NetworkState.LOADING);
        RequestBody body = addInputParams(categoryId, filterArray, brandArray, colorArray, sizeArray, sort, userId, productPayload);
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
                    long nextKey = response.body().getData().getProductList().getPageable().getPageNumber() + 1;
//                    long nextKey = (params.key == response.body().getData().getProducts().getPageable().getPageNumber()) ? -1 : params.key + 1;
                    callback.onResult(response.body().getData().getProductList().getProducts(), nextKey);
                    networkState.postValue(NetworkState.LOADED);
                    if (response.body().getData().getFilterAttributes() != null) {
                        filterAttributeMap.postValue(response.body().getData().getFilterAttributes());
                    } else {
                        filterAttributeMap.postValue(null);
                    }
                    if (response.body().getData().getSortStrings() != null) {
                        sortStrings.postValue(response.body().getData().getSortStrings());
                    } else {
                        sortStrings.postValue(null);
                    }

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

    private RequestBody addInputParams(String categoryId, JSONArray filterArray, JSONArray brandArray, JSONArray colorArray, JSONArray sizeArray, String sort, String userId, JSONObject productPayload) {
        JSONArray categoryIds = new JSONArray();
        categoryIds.put(categoryId);
        JSONArray attributeIds, productIds;
        productIds = new JSONArray();
        if (filterArray == null)
            attributeIds = new JSONArray();
        else
            attributeIds = filterArray;
        JSONArray brandIds, colorIds, sizeIds;
        if (brandArray == null)
            brandIds = new JSONArray();
        else
            brandIds = brandArray;

        if (colorArray == null)
            colorIds = new JSONArray();
        else
            colorIds = colorArray;

        if (sizeArray == null)
            sizeIds = new JSONArray();
        else
            sizeIds = sizeArray;

//        JSONObject jsonObject = new JSONObject();
//        try {
//
//            jsonObject.put("categoryIds", categoryIds);
//            jsonObject.put("attributeIds", attributeIds);
//            jsonObject.put("brandIds", brandIds);
//            jsonObject.put("productIds",productIds);
//            jsonObject.put("sorting",sort);
//            jsonObject.put("customerId",userId);
//            jsonObject.put("sizes",sizeIds);
//            jsonObject.put("colors",colorIds);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        String jsonString = jsonObject.toString();


        Map<String, Object> jsonParams = new ArrayMap<>();
//put something inside the map, could be null
        jsonParams.put("categoryIds", categoryIds);
        jsonParams.put("attributeIds", attributeIds);
        jsonParams.put("brandIds", brandIds);
        jsonParams.put("productIds", productIds);
        jsonParams.put("sorting", sort);
        jsonParams.put("customerId", userId);
        jsonParams.put("sizes", sizeIds);
        jsonParams.put("colors", colorIds);

        if (productPayload == null)
            return RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());
        else
            return RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), productPayload.toString());
    }
}

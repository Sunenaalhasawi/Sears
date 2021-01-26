package com.hasawi.sears.ui.main.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.hasawi.sears.data.api.Resource;
import com.hasawi.sears.data.api.RetrofitApiClient;
import com.hasawi.sears.data.api.model.pojo.FilterAttributeValues;
import com.hasawi.sears.data.api.model.pojo.Product;
import com.hasawi.sears.data.api.response.CartResponse;
import com.hasawi.sears.data.api.response.ProductDetailsResponse;
import com.hasawi.sears.data.api.response.ProductResponse;
import com.hasawi.sears.data.api.response.SearchedProductDetailsResponse;
import com.hasawi.sears.data.api.response.WishlistResponse;
import com.hasawi.sears.data.repository.ProductRepository;
import com.hasawi.sears.ui.main.view.dashboard.product.paging.ProductDataFactory;
import com.hasawi.sears.utils.NetworkState;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SharedHomeViewModel extends ViewModel {

    private final MutableLiveData<Product> selectedProduct = new MutableLiveData<Product>();
    private final MutableLiveData<Map<String, List<FilterAttributeValues>>> filterAttributesLiveData = new MutableLiveData<>();
    MutableLiveData<Resource<ProductResponse>> generalMutsbleLiveData;
    MutableLiveData<List<Product>> wishListedProducts = new MutableLiveData<>();
    MutableLiveData<List<Product>> cartedProducts = new MutableLiveData<>();
    ArrayList<Product> wishlistItems = new ArrayList<>();
    ArrayList<Product> cartedItems = new ArrayList<>();
    private ProductRepository productRepository;
    //Pagination
    private Executor executor;
    private LiveData<NetworkState> networkState;
    private LiveData<PagedList<Product>> productPaginationLiveData;


    public SharedHomeViewModel() {
        productRepository = new ProductRepository();
        generalMutsbleLiveData = new MutableLiveData<>();
    }

    public void select(Product product) {
        selectedProduct.setValue(product);
    }

    public LiveData<Product> getSelected() {
        return selectedProduct;
    }


    public MutableLiveData<Resource<ProductResponse>> getProductsInfo(String selectedCategoryId, String page_no, JSONArray filterArray) {
        return productRepository.getProductsInfo(selectedCategoryId, page_no, filterArray);
    }

    public MutableLiveData<Resource<ProductDetailsResponse>> getProductDetails(String productName) {
        return productRepository.getProductDetails(productName);
    }

    public void fetchProductData(String categoryId, JSONArray filterArray, JSONArray brandArray, JSONArray colorArray, JSONArray sizeArray, String pageNo, String sortString, String userId, JSONObject productPayload) {
        if (executor == null)
            executor = Executors.newFixedThreadPool(5);

        ProductDataFactory productDataFactory = new ProductDataFactory(categoryId, filterArray, brandArray, colorArray, sizeArray, pageNo, sortString, userId, productPayload);
        networkState = Transformations.switchMap(productDataFactory.getMutableLiveData(),
                dataSource -> dataSource.getNetworkState());

        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setInitialLoadSizeHint(10)
                        .setPageSize(30).build();

        productPaginationLiveData = (new LivePagedListBuilder(productDataFactory, pagedListConfig))
                .setFetchExecutor(executor)
                .build();

    }
    public MutableLiveData<Resource<ProductResponse>> callProductDataApi(String categoryId, String page_no) {

        RequestBody body = ProductRepository.addInputParams(categoryId, null);
        MutableLiveData<Resource<ProductResponse>> generalMutsbleLiveData = new MutableLiveData<>();
        Call<ProductResponse> productsResponseCall = RetrofitApiClient.getInstance().getApiInterface().getProductsList(body, page_no);
        productsResponseCall.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.code() == 200) {
                    if (response.body() != null)
                        generalMutsbleLiveData.setValue(Resource.success(response.body()));
                } else {
                    generalMutsbleLiveData.setValue(Resource.error("Network Error !", null));
                }

            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                generalMutsbleLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return generalMutsbleLiveData;
    }

    public LiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    public LiveData<PagedList<Product>> getProductPaginationLiveData() {
        return productPaginationLiveData;
    }

    public void invalidateProductLiveData() {
        productPaginationLiveData = null;
    }


    public MutableLiveData<Map<String, List<FilterAttributeValues>>> getFilterData() {
        return filterAttributesLiveData;
    }

    public void setFilterData(Map<String, List<FilterAttributeValues>> filterData) {
        this.filterAttributesLiveData.postValue(filterData);
    }

    public MutableLiveData<Resource<SearchedProductDetailsResponse>> getSearchedProductDetails(String objectId) {
        return productRepository.getSearchedProductDetails(objectId);
    }

    public MutableLiveData<Resource<WishlistResponse>> addToWishlist(String productID, String userID, String sessionToken) {
        return productRepository.addToWishlist(productID, userID, sessionToken);
    }

    public MutableLiveData<Resource<CartResponse>> addToCart(String userID, String jsonParams, String sessionToken) {
        return productRepository.addToCart(userID, jsonParams, sessionToken);
    }
}

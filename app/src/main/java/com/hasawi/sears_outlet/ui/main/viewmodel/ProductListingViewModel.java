package com.hasawi.sears_outlet.ui.main.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.hasawi.sears_outlet.data.api.Resource;
import com.hasawi.sears_outlet.data.api.model.pojo.Category;
import com.hasawi.sears_outlet.data.api.model.pojo.FilterAttributeValues;
import com.hasawi.sears_outlet.data.api.model.pojo.Product;
import com.hasawi.sears_outlet.data.api.response.WishlistResponse;
import com.hasawi.sears_outlet.data.repository.ProductRepository;
import com.hasawi.sears_outlet.ui.main.view.dashboard.product.paging.ProductDataFactory;
import com.hasawi.sears_outlet.utils.NetworkState;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ProductListingViewModel extends ViewModel {
    //    private final MutableLiveData<Product> selectedProduct = new MutableLiveData<Product>();
//    private final MutableLiveData<Map<String, List<FilterAttributeValues>>> filterAttributesLiveData = new MutableLiveData<>();
//    MutableLiveData<Resource<ProductResponse>> generalMutsbleLiveData;
    private ProductRepository productRepository;
    //Pagination
    private Executor executor;
    private LiveData<NetworkState> networkState;
    private LiveData<PagedList<Product>> productPaginationLiveData;
    private LiveData<Map<String, List<FilterAttributeValues>>> filterAttributeMap;
    private LiveData<List<String>> sortStrings;
    private LiveData<List<Category>> categoryList;


    public ProductListingViewModel() {
        productRepository = new ProductRepository();
//        generalMutsbleLiveData = new MutableLiveData<>();
    }

//    public MutableLiveData<Resource<ProductResponse>> getProductsInfo(String selectedCategoryId, String page_no, JSONArray filterArray) {
//        return productRepository.getProductsInfo(selectedCategoryId, page_no, filterArray);
//    }

    public void fetchProductData(String categoryId, JSONArray filterArray, JSONArray brandArray, JSONArray colorArray, JSONArray sizeArray, String pageNo, String sortString, String userId, JSONObject productPayload) {
        if (executor == null)
            executor = Executors.newFixedThreadPool(5);

        ProductDataFactory productDataFactory = new ProductDataFactory(categoryId, filterArray, brandArray, colorArray, sizeArray, pageNo, sortString, userId, productPayload);
        networkState = Transformations.switchMap(productDataFactory.getMutableLiveData(),
                dataSource -> dataSource.getNetworkState());
        Transformations.switchMap(productDataFactory.getMutableLiveData(),
                dataSource -> dataSource.getFilterAttributeMap());
        filterAttributeMap = Transformations.switchMap(productDataFactory.getMutableLiveData(),
                dataSource -> dataSource.getFilterAttributeMap());
        sortStrings = Transformations.switchMap(productDataFactory.getMutableLiveData(),
                dataSource -> dataSource.getSortStrings());
        categoryList = Transformations.switchMap(productDataFactory.getMutableLiveData(),
                dataSource -> dataSource.getCategoryList());
        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setInitialLoadSizeHint(30)
                        .setPageSize(30).build();

        productPaginationLiveData = (new LivePagedListBuilder(productDataFactory, pagedListConfig))
                .setFetchExecutor(executor)
                .build();

    }

//    public MutableLiveData<Resource<ProductResponse>> callProductDataApi(String categoryId, String page_no) {
//
//        RequestBody body = ProductRepository.addInputParams(categoryId, null);
//        MutableLiveData<Resource<ProductResponse>> generalMutsbleLiveData = new MutableLiveData<>();
//        Call<ProductResponse> productsResponseCall = RetrofitApiClient.getInstance().getApiInterface().getProductsList(body, page_no);
//        productsResponseCall.enqueue(new Callback<ProductResponse>() {
//            @Override
//            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
//                if (response.code() == 200) {
//                    if (response.body() != null)
//                        generalMutsbleLiveData.setValue(Resource.success(response.body()));
//                } else {
//                    generalMutsbleLiveData.setValue(Resource.error("Network Error !", null));
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<ProductResponse> call, Throwable t) {
//                generalMutsbleLiveData.setValue(Resource.error(t.getMessage(), null));
//            }
//        });
//        return generalMutsbleLiveData;
//    }

    public LiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    public LiveData<PagedList<Product>> getProductPaginationLiveData() {
        return productPaginationLiveData;
    }

    public void invalidateProductLiveData() {
        productPaginationLiveData = null;
    }

    public LiveData<Map<String, List<FilterAttributeValues>>> getFilterAttributeMap() {
        return filterAttributeMap;
    }

    public LiveData<List<String>> getSortStrings() {
        return sortStrings;
    }

    public LiveData<List<Category>> getCategoryList() {
        return categoryList;
    }
    //    public MutableLiveData<Map<String, List<FilterAttributeValues>>> getFilterData() {
//        return filterAttributesLiveData;
//    }
//
//    public void setFilterData(Map<String, List<FilterAttributeValues>> filterData) {
//        this.filterAttributesLiveData.postValue(filterData);
//    }


    public MutableLiveData<Resource<WishlistResponse>> addToWishlist(String productID, String userID, String sessionToken) {
        return productRepository.addToWishlist(productID, userID, sessionToken);
    }

}
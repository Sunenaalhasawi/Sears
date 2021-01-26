package com.hasawi.sears.data.repository;

import android.util.ArrayMap;

import androidx.lifecycle.MutableLiveData;

import com.hasawi.sears.data.api.Resource;
import com.hasawi.sears.data.api.RetrofitApiClient;
import com.hasawi.sears.data.api.model.pojo.SearchProductListResponse;
import com.hasawi.sears.data.api.response.CartResponse;
import com.hasawi.sears.data.api.response.ProductDetailsResponse;
import com.hasawi.sears.data.api.response.ProductResponse;
import com.hasawi.sears.data.api.response.SearchedProductDetailsResponse;
import com.hasawi.sears.data.api.response.WishlistResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepository {

    public static RequestBody addInputParams(String categoryId, JSONArray filterArray) {
        JSONArray categoryIds = new JSONArray();
        categoryIds.put(categoryId);
        if (filterArray == null)
            filterArray = new JSONArray();
        JSONArray brandIds = new JSONArray();
        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("categoryIds", categoryIds);
        jsonParams.put("attributeIds", filterArray);
        jsonParams.put("brandIds", brandIds);


        return RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());
    }

    public MutableLiveData<Resource<ProductResponse>> getProductsInfo(String categoryId, String page_no, JSONArray filterArray) {
        MutableLiveData<Resource<ProductResponse>> mutableLiveDataProductResponse = new MutableLiveData<>();
        RequestBody body = addInputParams(categoryId, filterArray);

        Call<ProductResponse> productsResponseCall = RetrofitApiClient.getInstance().getApiInterface().getProductsList(body, page_no);
        productsResponseCall.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.code() != 200) {
                    mutableLiveDataProductResponse.setValue(Resource.error("Network Error !", null));
                } else if (response.body() != null) {
                    mutableLiveDataProductResponse.setValue(Resource.success(response.body()));

                }

            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                mutableLiveDataProductResponse.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return mutableLiveDataProductResponse;
    }

    public MutableLiveData<Resource<ProductDetailsResponse>> getProductDetails(String productName) {
        MutableLiveData<Resource<ProductDetailsResponse>> mutableLiveDataProductDetailsResponse = new MutableLiveData<>();
        Call<ProductDetailsResponse> productDetailsResponseCall = RetrofitApiClient.getInstance().getApiInterface().getProductDetails(productName);
        productDetailsResponseCall.enqueue(new Callback<ProductDetailsResponse>() {
            @Override
            public void onResponse(Call<ProductDetailsResponse> call, Response<ProductDetailsResponse> response) {
                if (response.code() != 200) {
                    mutableLiveDataProductDetailsResponse.setValue(Resource.error("Network Error !", null));
                } else if (response.body() != null) {
                    mutableLiveDataProductDetailsResponse.setValue(Resource.success(response.body()));

                }
            }

            @Override
            public void onFailure(Call<ProductDetailsResponse> call, Throwable t) {
                mutableLiveDataProductDetailsResponse.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return mutableLiveDataProductDetailsResponse;
    }

    public MutableLiveData<Resource<SearchProductListResponse>> searchProducts(String query) {
        MutableLiveData<Resource<SearchProductListResponse>> searchProductListResponseMutableLiveData = new MutableLiveData<>();
        Call<SearchProductListResponse> searchProductListResponseCall = RetrofitApiClient.getInstance().getApiInterface().searchProducts(query);
        searchProductListResponseCall.enqueue(new Callback<SearchProductListResponse>() {
            @Override
            public void onResponse(Call<SearchProductListResponse> call, Response<SearchProductListResponse> response) {
                if (response.code() != 200) {
                    searchProductListResponseMutableLiveData.setValue(Resource.error("Something Went Wrong. Please Try Again !", null));
                } else if (response.body() != null) {
                    searchProductListResponseMutableLiveData.setValue(Resource.success(response.body()));
                }
            }

            @Override
            public void onFailure(Call<SearchProductListResponse> call, Throwable t) {
                searchProductListResponseMutableLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return searchProductListResponseMutableLiveData;
    }

    public MutableLiveData<Resource<SearchedProductDetailsResponse>> getSearchedProductDetails(String productObjectID) {
        MutableLiveData<Resource<SearchedProductDetailsResponse>> searchedProductDetailsResponseMutableLiveData = new MutableLiveData<>();
        Call<SearchedProductDetailsResponse> searchedProductDetailsResponseCall = RetrofitApiClient.getInstance().getApiInterface().getSearchedProductDetails(productObjectID);
        searchedProductDetailsResponseCall.enqueue(new Callback<SearchedProductDetailsResponse>() {
            @Override
            public void onResponse(Call<SearchedProductDetailsResponse> call, Response<SearchedProductDetailsResponse> response) {
                if (response.code() != 200) {
                    searchedProductDetailsResponseMutableLiveData.setValue(Resource.error("Network Error !", null));
                } else if (response.body() != null) {
                    searchedProductDetailsResponseMutableLiveData.setValue(Resource.success(response.body()));

                }
            }

            @Override
            public void onFailure(Call<SearchedProductDetailsResponse> call, Throwable t) {
                searchedProductDetailsResponseMutableLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return searchedProductDetailsResponseMutableLiveData;
    }

    public MutableLiveData<Resource<WishlistResponse>> addToWishlist(String productId, String userID, String sessionToken) {

        MutableLiveData<Resource<WishlistResponse>> wishlistResponseMutableLiveData = new MutableLiveData<>();
        Call<WishlistResponse> wishlistResponseCall = RetrofitApiClient.getInstance().getApiInterface().addToWishlist(userID, productId, "Bearer " + sessionToken);
        wishlistResponseCall.enqueue(new Callback<WishlistResponse>() {
            @Override
            public void onResponse(Call<WishlistResponse> call, Response<WishlistResponse> response) {
                if (response.code() != 200) {
                    wishlistResponseMutableLiveData.setValue(Resource.error("Something Went Wrong. Please Try Again !", null));
                } else if (response.body() != null) {
                    wishlistResponseMutableLiveData.setValue(Resource.success(response.body()));
                }
            }

            @Override
            public void onFailure(Call<WishlistResponse> call, Throwable t) {

                wishlistResponseMutableLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return wishlistResponseMutableLiveData;
    }

    public MutableLiveData<Resource<WishlistResponse>> getWishListedProducts(String userID, String sessionToken) {

        MutableLiveData<Resource<WishlistResponse>> wishListItemsLiveData = new MutableLiveData<>();
        Call<WishlistResponse> wishlistResponseCall = RetrofitApiClient.getInstance().getApiInterface().getWishlistedProducts(userID, "Bearer " + sessionToken);
        wishlistResponseCall.enqueue(new Callback<WishlistResponse>() {
            @Override
            public void onResponse(Call<WishlistResponse> call, Response<WishlistResponse> response) {
                if (response.code() != 200) {
                    wishListItemsLiveData.setValue(Resource.error("Network Error !", null));
                } else if (response.body() != null) {
                    wishListItemsLiveData.setValue(Resource.success(response.body()));

                }
            }

            @Override
            public void onFailure(Call<WishlistResponse> call, Throwable t) {
                wishListItemsLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return wishListItemsLiveData;
    }

    public MutableLiveData<Resource<CartResponse>> addToCart(String userID, String jsonParams, String sessionToken) {
        MutableLiveData<Resource<CartResponse>> addToCartResponseMutableLiveData = new MutableLiveData<>();
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonParams);

        Call<CartResponse> addToCartResponseCall = RetrofitApiClient.getInstance().getApiInterface().addToCart(userID, requestBody, "Bearer " + sessionToken);
        addToCartResponseCall.enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                if (response.code() != 200) {
                    addToCartResponseMutableLiveData.setValue(Resource.error("Something Went Wrong. Please Try Again !", null));
                } else if (response.body() != null) {
                    addToCartResponseMutableLiveData.setValue(Resource.success(response.body()));
                }

            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                addToCartResponseMutableLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return addToCartResponseMutableLiveData;
    }

    public MutableLiveData<Resource<CartResponse>> updateCartItems(String userID, String jsonParams, String sessionToken) {
        MutableLiveData<Resource<CartResponse>> updateCartResponseMutableLiveData = new MutableLiveData<>();
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonParams);

        Call<CartResponse> updateCartResponseCall = RetrofitApiClient.getInstance().getApiInterface().updateCartItems(userID, requestBody, "Bearer " + sessionToken);
        updateCartResponseCall.enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                if (response.code() != 200) {
                    updateCartResponseMutableLiveData.setValue(Resource.error("Something Went Wrong. Please Try Again !", null));
                } else if (response.body() != null) {
                    updateCartResponseMutableLiveData.setValue(Resource.success(response.body()));
                }

            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                updateCartResponseMutableLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return updateCartResponseMutableLiveData;
    }

    public MutableLiveData<Resource<CartResponse>> removeFromCart(String userID, String shoppingCartItemId, String sessionToken) {
        MutableLiveData<Resource<CartResponse>> addToCartResponseMutableLiveData = new MutableLiveData<>();
        Call<CartResponse> addToCartResponseCall = RetrofitApiClient.getInstance().getApiInterface().removeFromCart(userID, shoppingCartItemId, "Bearer " + sessionToken);
        addToCartResponseCall.enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                if (response.code() != 200) {
                    addToCartResponseMutableLiveData.setValue(Resource.error("Something Went Wrong. Please Try Again !", null));
                } else if (response.body() != null) {
                    addToCartResponseMutableLiveData.setValue(Resource.success(response.body()));
                }

            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                addToCartResponseMutableLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return addToCartResponseMutableLiveData;
    }

    public MutableLiveData<Resource<CartResponse>> getCartItems(String userID, String sessionToken) {
        MutableLiveData<Resource<CartResponse>> cartItemsMutableLiveData = new MutableLiveData<>();
        Call<CartResponse> cartResponseCall = RetrofitApiClient.getInstance().getApiInterface().getCartItems(userID, "Bearer " + sessionToken);
        cartResponseCall.enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                if (response.code() != 200) {
                    cartItemsMutableLiveData.setValue(Resource.error("Network Error !", null));
                } else if (response.body() != null) {
                    cartItemsMutableLiveData.setValue(Resource.success(response.body()));

                }
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                cartItemsMutableLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return cartItemsMutableLiveData;
    }
}

package com.hasawi.sears.data.api;

import com.hasawi.sears.data.api.model.pojo.SearchProductListResponse;
import com.hasawi.sears.data.api.response.AddressResponse;
import com.hasawi.sears.data.api.response.CartResponse;
import com.hasawi.sears.data.api.response.CategoryResponse;
import com.hasawi.sears.data.api.response.ChangePasswordResponse;
import com.hasawi.sears.data.api.response.CheckoutResponse;
import com.hasawi.sears.data.api.response.DeleteAddressResponse;
import com.hasawi.sears.data.api.response.DynamicContentResponse;
import com.hasawi.sears.data.api.response.DynamicDataResponse;
import com.hasawi.sears.data.api.response.DynamicUiResponse;
import com.hasawi.sears.data.api.response.ForgotPasswordResponse;
import com.hasawi.sears.data.api.response.GetAllAddressResponse;
import com.hasawi.sears.data.api.response.LanguageResponse;
import com.hasawi.sears.data.api.response.LoginResponse;
import com.hasawi.sears.data.api.response.MainCategoryResponse;
import com.hasawi.sears.data.api.response.OrderHistoryResponse;
import com.hasawi.sears.data.api.response.OrderResponse;
import com.hasawi.sears.data.api.response.PaymentResponse;
import com.hasawi.sears.data.api.response.ProductDetailsResponse;
import com.hasawi.sears.data.api.response.ProductResponse;
import com.hasawi.sears.data.api.response.SearchedProductDetailsResponse;
import com.hasawi.sears.data.api.response.SignupResponse;
import com.hasawi.sears.data.api.response.UserProfileResponse;
import com.hasawi.sears.data.api.response.VerifyOtpResponse;
import com.hasawi.sears.data.api.response.WishlistResponse;

import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiInterface {

    @Headers("Content-Type: application/json")
    @POST("customers/authenticate")
    Call<LoginResponse> login(@Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @POST("customers")
    Call<SignupResponse> signup(@Body RequestBody requestBody);

    @FormUrlEncoded
    @POST("")
    Call<ForgotPasswordResponse> forgotPassword(@FieldMap HashMap<String, String> data);

    @PUT("customers/{customerId}/changePassword?")
    Call<ChangePasswordResponse> changePassword(@Path("customerId") String customerId, @Query("oldPassword") String oldPassword, @Query("newPassword") String newPassword, @Header("Authorization") String sessionToken);

    @FormUrlEncoded
    @POST("")
    Call<VerifyOtpResponse> verifyOtp();

    @GET("languages")
    Call<LanguageResponse> getLanguages();

    @GET("shop/landing")
    Call<DynamicDataResponse> getDynamicData();

    @GET("categories/tree")
    Call<MainCategoryResponse> getMainCategories();

    @GET("shop/landing/android/new")
    Call<DynamicUiResponse> getDynamicUiDataHomePage();

    @FormUrlEncoded
    @POST("")
    Call<CategoryResponse> getProductCategories(@FieldMap HashMap<String, String> data);

    @Headers("Content-Type: application/json")
    @POST("products/{page_no}/30?languageId=en")
    Call<ProductResponse> getProductsList(@Body RequestBody requestBody, @Path("page_no") String page_no);


    @GET("products/{product_name}")
    Call<ProductDetailsResponse> getProductDetails(@Path("product_name") String product_name);

    @GET("search/0/20?")
    Call<SearchProductListResponse> searchProducts(@Query("q") String query);

    @GET("products/search/{product_object_id}?languageId=en")
    Call<SearchedProductDetailsResponse> getSearchedProductDetails(@Path("product_object_id") String selectedProductObjectId);


    @POST("wishlists/customers/{user_id}/products/{product_id}")
    Call<WishlistResponse> addToWishlist(@Path("user_id") String userID, @Path("product_id") String productID, @Header("Authorization") String sessionToken);

    @GET("wishlists/customers/{user_id}")
    Call<WishlistResponse> getWishlistedProducts(@Path("user_id") String userID, @Header("Authorization") String sessionToken);


    @Headers("Content-Type: application/json")
    @POST("cart/customers/{customer_id}")
    Call<CartResponse> addToCart(@Path("customer_id") String userId, @Body RequestBody requestBody, @Header("Authorization") String sessionToken);

    @GET("cart/customers/{customer_id}/cartItem/{cart_item_id}")
    Call<CartResponse> removeFromCart(@Path("customer_id") String userId, @Path("cart_item_id") String cartItemId, @Header("Authorization") String sessionToken);

    @GET("cart/customers/{customer_id}")
    Call<CartResponse> getCartItems(@Path("customer_id") String userId, @Header("Authorization") String sessionToken);

    @Headers("Content-Type: application/json")
    @POST("customers/{customer_id}/address")
    Call<AddressResponse> addNewAddress(@Path("customer_id") String userId, @Body RequestBody requestBody, @Header("Authorization") String sessionToken);

    @GET("order/customers/{customerId}/cart/{cartId}/?")
    Call<CheckoutResponse> checkout(@Path("customerId") String customerID, @Path("cartId") String cartId, @Query("couponCode") String couponCode, @Header("Authorization") String sessionToken);

    @Headers("Content-Type: application/json")
    @POST("order/customers/{customerId}/address/{addressId}/cart/{cartId}")
    Call<OrderResponse> orderConfirmation(@Path("customerId") String customerID, @Path("addressId") String addressId, @Path("cartId") String cartId, @Header("Authorization") String sessionToken, @Body RequestBody requestBody);

    @GET("customers/{customerId}/address")
    Call<GetAllAddressResponse> getAddresses(@Path("customerId") String customerId, @Header("Authorization") String sessionToken);

    @Headers("Content-Type: application/json")
    @POST("address/{addressId}")
    Call<DeleteAddressResponse> deleteAddress(@Path("addressId") String addressId, @Header("Authorization") String sessionToken);

    @Headers("Content-Type: application/json")
    @POST("customers/{customerId}/address/{addressId}")
    Call<AddressResponse> editAddress(@Path("customerId") String customerId, @Path("addressId") String addressId, @Header("Authorization") String sessionToken, @Body RequestBody requestBody);

    @GET("customers/findByEmail?")
    Call<UserProfileResponse> userProfile(@Query("emailId") String emailId, @Header("Authorization") String sessionToken);

    @Headers("Content-Type: application/json")
    @PUT("customers/{customerId}")
    Call<UserProfileResponse> editUserProfile(@Path("customerId") String customerId, @Header("Authorization") String sessionToken, @Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @POST("order/customers/{customerId}")
    Call<OrderHistoryResponse> orderHistory(@Path("customerId") String customerId, @Header("Authorization") String sessionToken);

    @GET("consistent?")
    Call<DynamicContentResponse> getDynamicWebviewContent(@Query("name") String query);

    @GET
    Call<PaymentResponse> getPaymentSuccessResponse(@Url String url);

//    @GET("api/v{api_version}/events")
//    Call<RetrofitModelEvents> getEventsSearchPagination(
//            @Header("Authorization") String authHeader,
//            @Path("api_version") String api_version,
//            @Query("page") int page

//    http://preprod.acekuwait.com/api/v0/products/purple-off-shoulder-dress_ar
}

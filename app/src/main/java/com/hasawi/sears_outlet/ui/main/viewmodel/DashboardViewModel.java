package com.hasawi.sears_outlet.ui.main.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hasawi.sears_outlet.R;
import com.hasawi.sears_outlet.data.api.Resource;
import com.hasawi.sears_outlet.data.api.RetrofitApiClient;
import com.hasawi.sears_outlet.data.api.model.NavigationMenuItem;
import com.hasawi.sears_outlet.data.api.model.pojo.Product;
import com.hasawi.sears_outlet.data.api.model.pojo.SearchProductListResponse;
import com.hasawi.sears_outlet.data.api.response.CartResponse;
import com.hasawi.sears_outlet.data.api.response.DynamicUiResponse;
import com.hasawi.sears_outlet.data.api.response.MainCategoryResponse;
import com.hasawi.sears_outlet.data.api.response.ProductResponse;
import com.hasawi.sears_outlet.data.repository.DynamicDataRepository;
import com.hasawi.sears_outlet.data.repository.ProductRepository;
import com.hasawi.sears_outlet.utils.AppConstants;
import com.hasawi.sears_outlet.utils.PreferenceHandler;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardViewModel extends ViewModel {
    ArrayList<NavigationMenuItem> menuItemArrayList;
    MutableLiveData<List<Product>> wishListedProducts = new MutableLiveData<>();
    MutableLiveData<List<Product>> cartedProducts = new MutableLiveData<>();
    ArrayList<Product> wishlistItems = new ArrayList<>();
    ArrayList<Product> cartedItems = new ArrayList<>();
    ProductRepository productRepository;
    DynamicDataRepository dynamicDataRepository;
    MutableLiveData<SearchProductListResponse> searchProductListResponseMutableLiveData = new MutableLiveData<>();

    public DashboardViewModel() {
        this.productRepository = new ProductRepository();
        dynamicDataRepository = new DynamicDataRepository();
    }

    public MutableLiveData<Resource<SearchProductListResponse>> searchProducts(String query) {
        return productRepository.searchProducts(query);
    }

    public ArrayList<NavigationMenuItem> getMenuItemsList(Context context) {
        if (menuItemArrayList == null)
            menuItemArrayList = new ArrayList<>();
        PreferenceHandler preferenceHandler = new PreferenceHandler(context, PreferenceHandler.TOKEN_LOGIN);
        boolean isLoggedIn = preferenceHandler.getData(PreferenceHandler.LOGIN_STATUS, false);
        menuItemArrayList.add(new NavigationMenuItem(AppConstants.ID_MENU_HOME, context.getResources().getString(R.string.menu_home), context.getResources().getDrawable(R.drawable.home_side_menu), true));
        menuItemArrayList.add(new NavigationMenuItem(AppConstants.ID_MENU_NOTIFICATION, context.getResources().getString(R.string.menu_notifications), context.getResources().getDrawable(R.drawable.notification_side_menu), true));
        menuItemArrayList.add(new NavigationMenuItem(AppConstants.ID_MENU_FAQ, context.getResources().getString(R.string.faq), context.getResources().getDrawable(R.drawable.conversation), true));
        menuItemArrayList.add(new NavigationMenuItem(AppConstants.ID_MENU_ABOUT_US, context.getResources().getString(R.string.about_us), context.getResources().getDrawable(R.drawable.question), true));
        menuItemArrayList.add(new NavigationMenuItem(AppConstants.ID_MENU_PRIVACY_POLICY, context.getResources().getString(R.string.privacy_policy), context.getResources().getDrawable(R.drawable.contract), true));
        menuItemArrayList.add(new NavigationMenuItem(AppConstants.ID_MENU_CONTACT_US, context.getResources().getString(R.string.contact_us), context.getResources().getDrawable(R.drawable.headset), true));
        if (isLoggedIn) {
            menuItemArrayList.add(new NavigationMenuItem(AppConstants.ID_MENU_SIGNOUT, context.getResources().getString(R.string.menu_signout), context.getResources().getDrawable(R.drawable.logout), true));
        }
//        menuItemArrayList.add(new NavigationMenuItem(AppConstants.ID_MENU_BANK_DETAILS, context.getResources().getString(R.string.menu_bank_details)));
//        menuItemArrayList.add(new NavigationMenuItem(AppConstants.ID_MENU_RESET_PASSWORD, context.getResources().getString(R.string.menu_reset_password)));
//        menuItemArrayList.add(new NavigationMenuItem(AppConstants.ID_MENU_TRACK_ORDER, context.getResources().getString(R.string.menu_track_your_order)));
//        menuItemArrayList.add(new NavigationMenuItem(AppConstants.ID_MENU_CUSTOMER_SERVICE, context.getResources().getString(R.string.menu_customer_service)));
        return menuItemArrayList;
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
                    generalMutsbleLiveData.setValue(Resource.error(response.message(), null));
                }

            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                generalMutsbleLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return generalMutsbleLiveData;
    }


    public MutableLiveData<Resource<MainCategoryResponse>> getMainCateogries() {
        return dynamicDataRepository.getMainCategories();
    }

    public MutableLiveData<Resource<DynamicUiResponse>> getDynamicUiHome() {
        return dynamicDataRepository.getDynamicUiDataHome();
    }


    public MutableLiveData<Resource<CartResponse>> getCartItems(String userID, String sessiontoken) {
        return productRepository.getCartItems(userID, sessiontoken);
    }

}
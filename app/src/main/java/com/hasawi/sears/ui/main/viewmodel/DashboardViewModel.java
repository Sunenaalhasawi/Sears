package com.hasawi.sears.ui.main.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hasawi.sears.R;
import com.hasawi.sears.data.api.Resource;
import com.hasawi.sears.data.api.model.NavigationMenuItem;
import com.hasawi.sears.data.api.model.pojo.Content;
import com.hasawi.sears.data.api.model.pojo.SearchProductListResponse;
import com.hasawi.sears.data.repository.ProductRepository;
import com.hasawi.sears.utils.AppConstants;
import com.hasawi.sears.utils.PreferenceHandler;

import java.util.ArrayList;
import java.util.List;

public class DashboardViewModel extends ViewModel {
    ArrayList<NavigationMenuItem> menuItemArrayList;
    MutableLiveData<List<Content>> wishListedProducts = new MutableLiveData<>();
    MutableLiveData<List<Content>> cartedProducts = new MutableLiveData<>();
    ArrayList<Content> wishlistItems = new ArrayList<>();
    ArrayList<Content> cartedItems = new ArrayList<>();
    ProductRepository productRepository;
    MutableLiveData<SearchProductListResponse> searchProductListResponseMutableLiveData = new MutableLiveData<>();

    public DashboardViewModel() {
        this.productRepository = new ProductRepository();
    }

    public MutableLiveData<Resource<SearchProductListResponse>> searchProducts(String query) {
        return productRepository.searchProducts(query);
    }

    public ArrayList<NavigationMenuItem> getMenuItemsList(Context context) {
        if (menuItemArrayList == null)
            menuItemArrayList = new ArrayList<>();
        PreferenceHandler preferenceHandler = new PreferenceHandler(context, PreferenceHandler.TOKEN_LOGIN);
        boolean isLoggedIn = preferenceHandler.getData(PreferenceHandler.LOGIN_STATUS, false);
        if (isLoggedIn) {
            menuItemArrayList.add(new NavigationMenuItem(AppConstants.ID_MENU_HOME, context.getResources().getString(R.string.menu_home), true));
            menuItemArrayList.add(new NavigationMenuItem(AppConstants.ID_MENU_PROFILE, context.getResources().getString(R.string.menu_my_profile), true));
            menuItemArrayList.add(new NavigationMenuItem(AppConstants.ID_MENU_ORDERS, context.getResources().getString(R.string.menu_my_orders), true));
            menuItemArrayList.add(new NavigationMenuItem(AppConstants.ID_MENU_NOTIFICATION, context.getResources().getString(R.string.menu_notifications), true));
            menuItemArrayList.add(new NavigationMenuItem(AppConstants.ID_MENU_WISHLIST, context.getResources().getString(R.string.menu_my_wishlist), true));
            menuItemArrayList.add(new NavigationMenuItem(AppConstants.ID_MENU_ADDRESS_BOOK, context.getResources().getString(R.string.menu_my_address_book), false));
            menuItemArrayList.add(new NavigationMenuItem(AppConstants.ID_MENU_FAQ, context.getResources().getString(R.string.faq), false));
            menuItemArrayList.add(new NavigationMenuItem(AppConstants.ID_MENU_ABOUT_US, context.getResources().getString(R.string.about_us), false));
            menuItemArrayList.add(new NavigationMenuItem(AppConstants.ID_MENU_PRIVACY_POLICY, context.getResources().getString(R.string.privacy_policy), false));
            menuItemArrayList.add(new NavigationMenuItem(AppConstants.ID_MENU_CONTACT_US, context.getResources().getString(R.string.contact_us), false));
            menuItemArrayList.add(new NavigationMenuItem(AppConstants.ID_MENU_SIGNOUT, context.getResources().getString(R.string.menu_signout), true));
        } else {
            menuItemArrayList.add(new NavigationMenuItem(AppConstants.ID_MENU_HOME, context.getResources().getString(R.string.menu_home), true));
            menuItemArrayList.add(new NavigationMenuItem(AppConstants.ID_MENU_PROFILE, context.getResources().getString(R.string.menu_my_profile), false));
            menuItemArrayList.add(new NavigationMenuItem(AppConstants.ID_MENU_ORDERS, context.getResources().getString(R.string.menu_my_orders), false));
            menuItemArrayList.add(new NavigationMenuItem(AppConstants.ID_MENU_NOTIFICATION, context.getResources().getString(R.string.menu_notifications), false));
            menuItemArrayList.add(new NavigationMenuItem(AppConstants.ID_MENU_WISHLIST, context.getResources().getString(R.string.menu_my_wishlist), false));
            menuItemArrayList.add(new NavigationMenuItem(AppConstants.ID_MENU_ADDRESS_BOOK, context.getResources().getString(R.string.menu_my_address_book), false));
            menuItemArrayList.add(new NavigationMenuItem(AppConstants.ID_MENU_FAQ, context.getResources().getString(R.string.faq), false));
            menuItemArrayList.add(new NavigationMenuItem(AppConstants.ID_MENU_ABOUT_US, context.getResources().getString(R.string.about_us), false));
            menuItemArrayList.add(new NavigationMenuItem(AppConstants.ID_MENU_PRIVACY_POLICY, context.getResources().getString(R.string.privacy_policy), false));
            menuItemArrayList.add(new NavigationMenuItem(AppConstants.ID_MENU_CONTACT_US, context.getResources().getString(R.string.contact_us), false));
        }

//        menuItemArrayList.add(new NavigationMenuItem(AppConstants.ID_MENU_BANK_DETAILS, context.getResources().getString(R.string.menu_bank_details)));
//        menuItemArrayList.add(new NavigationMenuItem(AppConstants.ID_MENU_RESET_PASSWORD, context.getResources().getString(R.string.menu_reset_password)));
//        menuItemArrayList.add(new NavigationMenuItem(AppConstants.ID_MENU_TRACK_ORDER, context.getResources().getString(R.string.menu_track_your_order)));
//        menuItemArrayList.add(new NavigationMenuItem(AppConstants.ID_MENU_CUSTOMER_SERVICE, context.getResources().getString(R.string.menu_customer_service)));
        return menuItemArrayList;
    }

    public MutableLiveData<List<Content>> getWishListedProducts() {
        return wishListedProducts;
    }

    public void setWishListedProducts(Content product) {
        wishlistItems.add(product);
        wishListedProducts.setValue(wishlistItems);
    }

    public MutableLiveData<List<Content>> getCartedProducts() {
        return cartedProducts;
    }

    public void setCartedItems(Content product) {
        cartedItems.add(product);
        cartedProducts.setValue(cartedItems);
    }
}

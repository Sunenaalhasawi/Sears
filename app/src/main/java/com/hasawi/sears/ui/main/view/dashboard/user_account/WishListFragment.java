package com.hasawi.sears.ui.main.view.dashboard.user_account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.gson.Gson;
import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.pojo.Product;
import com.hasawi.sears.data.api.model.pojo.Wishlist;
import com.hasawi.sears.databinding.FragmentWishlistBinding;
import com.hasawi.sears.ui.base.BaseFragment;
import com.hasawi.sears.ui.main.adapters.WishListAdapter;
import com.hasawi.sears.ui.main.view.DashboardActivity;
import com.hasawi.sears.ui.main.view.dashboard.product.SelectedProductDetailsFragment;
import com.hasawi.sears.ui.main.view.signin.SigninActivity;
import com.hasawi.sears.ui.main.viewmodel.WishListViewModel;
import com.hasawi.sears.utils.PreferenceHandler;

import java.util.ArrayList;
import java.util.List;

public class WishListFragment extends BaseFragment {
    FragmentWishlistBinding fragmentWishlistBinding;
    WishListAdapter wishListAdapter;
    DashboardActivity dashboardActivity;
    WishListViewModel wishListViewModel;
    List<Wishlist> wishListItems = new ArrayList<>();
    String userID = "";
    String sessionToken;
    boolean isUserLoggedIn = false;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_wishlist;
    }

    @Override
    protected void setup() {
        fragmentWishlistBinding = (FragmentWishlistBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        dashboardActivity.handleActionBarIcons(false);
        wishListViewModel = new ViewModelProvider(dashboardActivity).get(WishListViewModel.class);
        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        userID = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
        sessionToken = preferenceHandler.getData(PreferenceHandler.LOGIN_TOKEN, "");
        isUserLoggedIn = preferenceHandler.getData(PreferenceHandler.LOGIN_STATUS, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        fragmentWishlistBinding.recyclerviewWishlist.setLayoutManager(gridLayoutManager);
        wishListAdapter = new WishListAdapter(getContext()) {
            @Override
            public void onWishListClicked(Product selectedProduct, boolean isChecked, int position) {
                callAddRemoveWishlistApi(selectedProduct, isChecked, position);
            }

            @Override
            public void onItemClicked(Product product) {
                Product selectedProduct = product;
                Gson gson = new Gson();
                String objectString = gson.toJson(selectedProduct);
                Bundle bundle = new Bundle();
                bundle.putString("selected_product_object", objectString);

                dashboardActivity.handleActionMenuBar(true, false, "");
                dashboardActivity.setTitle(selectedProduct.getDescriptions().get(0).getProductName());
                dashboardActivity.replaceFragment(R.id.fragment_replacer, new SelectedProductDetailsFragment(), bundle, true, false);

            }
        };
        fragmentWishlistBinding.recyclerviewWishlist.setAdapter(wishListAdapter);
        if (isUserLoggedIn)
            wishlistApi();
        else {
            fragmentWishlistBinding.cvGuestMissingWishlist.setVisibility(View.VISIBLE);
        }

        fragmentWishlistBinding.layoutMissingWishlist.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent redirectIntent = new Intent(dashboardActivity, SigninActivity.class);
                startActivity(redirectIntent);
                dashboardActivity.finish();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (isUserLoggedIn)
            wishlistApi();
    }

    public void wishlistApi() {
        fragmentWishlistBinding.progressBar.setVisibility(View.VISIBLE);
        wishListViewModel.getWishListedProducts(userID, sessionToken).observe(getActivity(), wishlistResponse -> {
            switch (wishlistResponse.status) {
                case SUCCESS:
                    wishListItems = wishlistResponse.data.getData();
                    if (wishListItems.size() == 0) {
                        fragmentWishlistBinding.cvLoggedUserNoWishlist.setVisibility(View.VISIBLE);
                    } else {
                        wishListAdapter.addAll(wishListItems);
                    }

                    break;
                case LOADING:
                    break;
                case ERROR:
                    Toast.makeText(dashboardActivity, wishlistResponse.message, Toast.LENGTH_SHORT).show();
                    break;
            }
            fragmentWishlistBinding.progressBar.setVisibility(View.GONE);

        });
    }

    public void callAddRemoveWishlistApi(Product product, boolean isWishlisted, int position) {
        wishListViewModel.addToWishlist(product.getProductId(), userID, sessionToken).observe(getActivity(), wishlistResponse -> {
            switch (wishlistResponse.status) {
                case SUCCESS:
                    if (!isWishlisted) {

                        wishListAdapter.removeItemFromWishList(product, position);

//                        wishListAdapter.addAll(wishlistResponse.data.getData());
//                        Toast.makeText(dashboardActivity, "Removed from Wishlist", Toast.LENGTH_SHORT).show();
                        wishlistApi();
                    }
                    dashboardActivity.showCustomWislistToast(isWishlisted);
                    break;
                case LOADING:
                    break;
                case ERROR:
                    Toast.makeText(dashboardActivity, wishlistResponse.message, Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

}

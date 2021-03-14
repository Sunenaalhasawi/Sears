package com.hasawi.sears_outlet.ui.main.view.checkout;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.appevents.AppEventsConstants;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.hasawi.sears_outlet.R;
import com.hasawi.sears_outlet.data.api.Resource;
import com.hasawi.sears_outlet.data.api.model.pojo.Product;
import com.hasawi.sears_outlet.data.api.model.pojo.ShoppingCartItem;
import com.hasawi.sears_outlet.data.api.response.CartResponse;
import com.hasawi.sears_outlet.databinding.FragmentCartBinding;
import com.hasawi.sears_outlet.ui.base.BaseFragment;
import com.hasawi.sears_outlet.ui.main.adapters.CartAdapter;
import com.hasawi.sears_outlet.ui.main.adapters.RecyclerItemTouchHelper;
import com.hasawi.sears_outlet.ui.main.listeners.RecyclerItemClickListener;
import com.hasawi.sears_outlet.ui.main.view.DashboardActivity;
import com.hasawi.sears_outlet.ui.main.view.dashboard.product.SelectedProductDetailsFragment;
import com.hasawi.sears_outlet.ui.main.view.signin.SigninActivity;
import com.hasawi.sears_outlet.ui.main.viewmodel.CartViewModel;
import com.hasawi.sears_outlet.utils.PreferenceHandler;
import com.hasawi.sears_outlet.utils.dialogs.GeneralDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.facebook.appevents.AppEventsConstants.EVENT_PARAM_CURRENCY;

public class MyCartFragment extends BaseFragment implements View.OnClickListener, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    FragmentCartBinding fragmentCartBinding;
    CartAdapter cartAdapter;
    CartViewModel cartViewModel;
    List<ShoppingCartItem> cartItemsList = new ArrayList<>();
    int cartCount = 0;
    String totalPrice = "", userID = "", username = "";
    String sessionToken;
    String cartId;
    boolean isUserLoggedIn = false;
    DashboardActivity dashboardActivity;
    List<ShoppingCartItem> availableItemList = new ArrayList<>();
    List<ShoppingCartItem> outOfStockItemList = new ArrayList<>();
    boolean isQuantityIncreased;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_cart;
    }

    @Override
    protected void setup() {
        fragmentCartBinding = (FragmentCartBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        dashboardActivity.handleActionBarIcons(false);
        Boolean isLoggedIn = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN).getData(PreferenceHandler.LOGIN_STATUS, false);
        if (isLoggedIn)
            dashboardActivity.handleActionMenuBar(true, false, "My Bag");
        else
            dashboardActivity.handleActionMenuBar(true, true, "My Bag");
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        cartAdapter = new CartAdapter(dashboardActivity) {
            @Override
            public void onItemDeleteClicked(ShoppingCartItem cartItem) {
                fragmentCartBinding.progressBar.setVisibility(View.VISIBLE);
                removeItemFromCartApi(cartItem);
            }

            @Override
            public void cartItemsUpdated(ShoppingCartItem cartItem, int quantity, boolean isIncreased) {
                updateCartItemsApi(cartItem.getProductId(), cartItem.getProductConfigurable().getRefSku(), quantity + "");
                isQuantityIncreased = isIncreased;
            }

            @Override
            public void onCartItemClicked(ShoppingCartItem cartItem) {
                try {
                    Product product = cartItem.getProduct();
                    Gson gson = new Gson();
                    String objectString = gson.toJson(product);
                    Bundle bundle = new Bundle();
                    bundle.putString("selected_product_object", objectString);
                    dashboardActivity.showToolBar();
                    dashboardActivity.handleActionMenuBar(true, false, product.getDescriptions().get(0).getProductName());
                    dashboardActivity.replaceFragment(R.id.fragment_replacer, new SelectedProductDetailsFragment(), bundle, true, false);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };
        fragmentCartBinding.tvProceedTocheckout.setOnClickListener(this);
        fragmentCartBinding.imageButtonBack.setOnClickListener(this);
        try {
            PreferenceHandler preferenceHandler = new PreferenceHandler(getActivity(), PreferenceHandler.TOKEN_LOGIN);
            userID = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
            sessionToken = preferenceHandler.getData(PreferenceHandler.LOGIN_TOKEN, "");
            username = preferenceHandler.getData(PreferenceHandler.LOGIN_USERNAME, "");
            isUserLoggedIn = preferenceHandler.getData(PreferenceHandler.LOGIN_STATUS, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (isUserLoggedIn)
            callMyCartApi();
        else {
            fragmentCartBinding.cvGuestUserEmptyCart.setVisibility(View.VISIBLE);
        }
        fragmentCartBinding.layoutNoItemsCart.btnShopNow.setOnClickListener(this);
        fragmentCartBinding.layoutGuestNoItemsCart.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(dashboardActivity, SigninActivity.class);
                startActivity(intent);
                dashboardActivity.finish();
            }
        });

        fragmentCartBinding.recyclerviewCart.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                try {
//                    ShoppingCartItem shoppingCartItem = cartItemsList.get(position);
//                    Product product = shoppingCartItem.getProduct();
//                    Gson gson = new Gson();
//                    String objectString = gson.toJson(product);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("selected_product_object", objectString);
//                    dashboardActivity.showToolBar();
//                    dashboardActivity.handleActionMenuBar(true, false, product.getDescriptions().get(0).getProductName());
//                    dashboardActivity.replaceFragment(R.id.fragment_replacer, new SelectedProductDetailsFragment(), bundle, true, false);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        }));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        fragmentCartBinding.recyclerviewCart.setLayoutManager(mLayoutManager);
        fragmentCartBinding.recyclerviewCart.setItemAnimator(new DefaultItemAnimator());
        fragmentCartBinding.recyclerviewCart.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        fragmentCartBinding.recyclerviewCart.setAdapter(cartAdapter);

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(fragmentCartBinding.recyclerviewCart);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback1 = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                cartAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());

                // Row is swiped from recycler view
                // remove it from adapter
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        // attaching the touch helper to recycler view
        new ItemTouchHelper(itemTouchHelperCallback1).attachToRecyclerView(fragmentCartBinding.recyclerviewCart);

//        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
//                final int position = viewHolder.getAdapterPosition();
//                if (direction == ItemTouchHelper.LEFT) {
//                    cartAdapter.removeItem(position);
//                }
//            }
//
//            @Override
//            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//                try {
//
//                    Bitmap icon;
//                    if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
//                        View itemView = viewHolder.itemView;
//                        float height = (float) itemView.getBottom() - (float) itemView.getTop();
//                        float width = height / 5;
//                        viewHolder.itemView.setTranslationX(dX / 5);
//                        Paint paint=new Paint();
//                        paint.setColor(getResources().getColor(R.color.red));
//                        RectF background = new RectF((float) itemView.getRight() + dX / 5, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
//                        c.drawRect(background, paint);
//                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_white);
//                        RectF icon_dest = new RectF((float) (itemView.getRight() + dX /7), (float) itemView.getTop()+width, (float) itemView.getRight()+dX/20, (float) itemView.getBottom()-width);
//                        c.drawBitmap(icon, null, icon_dest, paint);
//                    } else {
//                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        };
//
//        //        // attaching the touch helper to recycler view
//        new ItemTouchHelper(simpleCallback).attachToRecyclerView(fragmentCartBinding.recyclerviewCart);

    }

    private void setUiValues() {
        fragmentCartBinding.tvBagCount.setText(cartCount + "");
        fragmentCartBinding.tvTotalAmount.setText("KWD " + totalPrice);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvProceedTocheckout:
                if (outOfStockItemList.size() > 0) {
                    GeneralDialog generalDialog = new GeneralDialog("Error", "Please remove out of stock items from the bag to proceed?");
                    generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                } else {
                    dashboardActivity.showToolBar();
                    dashboardActivity.handleActionMenuBar(true, false, "Checkout");
                    dashboardActivity.replaceFragment(R.id.fragment_replacer, new CheckoutFragment(), null, true, false);
                }
                break;
            case R.id.btnShopNow:
                dashboardActivity.showToolBar();
                int fragmentCount = getParentFragmentManager().getBackStackEntryCount();
                for (int i = 0; i < fragmentCount; i++) {
                    getParentFragmentManager().popBackStackImmediate();
                }
                dashboardActivity.handleActionMenuBar(false, true, "");

                break;
            case R.id.imageButtonBack:
                dashboardActivity.onBackPressed();
                break;
            default:
                break;
        }
    }

    public void callMyCartApi() {
        fragmentCartBinding.progressBar.setVisibility(View.VISIBLE);
        cartViewModel.getCartItems(userID, sessionToken).observe(this, cartResponse -> {
            switch (cartResponse.status) {
                case SUCCESS:
                    updateCartValues(cartResponse);
                    try {
                        logViewCartEvent((ArrayList<ShoppingCartItem>) cartResponse.data.getCartData().getShoppingCartItems(), cartResponse.data.getCartData().getTotal());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case LOADING:
                    break;
                case ERROR:
                    Toast.makeText(dashboardActivity, cartResponse.message, Toast.LENGTH_SHORT).show();
                    break;
            }
            fragmentCartBinding.progressBar.setVisibility(View.GONE);
        });
    }

    private void updateCartValues(Resource<CartResponse> cartResponse) {
        try {
            cartItemsList = new ArrayList<>();
            availableItemList = cartResponse.data.getCartData().getAvailable();
            for (int i = 0; i < availableItemList.size(); i++) {
                availableItemList.get(i).setOutOfStock(false);
                cartItemsList.add(availableItemList.get(i));
            }
            outOfStockItemList = cartResponse.data.getCartData().getOutOfStock();
            for (int i = 0; i < outOfStockItemList.size(); i++) {
                outOfStockItemList.get(i).setOutOfStock(true);
                cartItemsList.add(outOfStockItemList.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (cartItemsList.size() == 0) {
            fragmentCartBinding.cvLoggedUserEmptyCart.setVisibility(View.VISIBLE);
        } else {
            try {
                cartId = cartResponse.data.getCartData().getShoppingCartId();
                PreferenceHandler preferenceHandler = new PreferenceHandler(dashboardActivity, PreferenceHandler.TOKEN_LOGIN);
                preferenceHandler.saveData(PreferenceHandler.LOGIN_USER_CART_ID, cartId);
                totalPrice = cartResponse.data.getCartData().getTotal() + "";
                cartCount = cartResponse.data.getCartData().getItemCount();
                cartAdapter.addAll(cartItemsList);
                setUiValues();
                dashboardActivity.setCartBadgeNumber(cartCount);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void removeItemFromCartApi(ShoppingCartItem shoppingCartItem) {

        cartViewModel.removeItemFromCart(userID, shoppingCartItem.getShoppingCartItemId(), sessionToken).observe(this, cartResponse -> {
            switch (cartResponse.status) {
                case SUCCESS:
                    Toast.makeText(dashboardActivity, "Removed item from cart successfully", Toast.LENGTH_SHORT).show();
                    cartCount = cartResponse.data.getCartData().getItemCount();
                    dashboardActivity.setCartBadgeNumber(cartCount);
//                    cartItemsList = cartResponse.data.getCartData().getShoppingCartItems();
//                    if (cartItemsList.size() == 0) {
//                        fragmentCartBinding.cvLoggedUserEmptyCart.setVisibility(View.VISIBLE);
//                    } else {
//                        totalPrice = cartResponse.data.getCartData().getSubTotal() + "";
//                        cartCount = cartResponse.data.getCartData().getShoppingCartItems().size();
//                        cartAdapter.addAll(cartItemsList);
//                        setUiValues();
//                    }
                    callMyCartApi();
                    break;
                case LOADING:
                    break;
                case ERROR:
                    Toast.makeText(dashboardActivity, cartResponse.message, Toast.LENGTH_SHORT).show();
                    break;
            }
            fragmentCartBinding.progressBar.setVisibility(View.GONE);
        });
    }

    private void updateCartItemsApi(String productId, String refSku, String quantity) {
        fragmentCartBinding.progressBar.setVisibility(View.VISIBLE);
        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("productId", productId);
        jsonParams.put("refSku", refSku);
        jsonParams.put("quantity", quantity);

        String jsonParamString = (new JSONObject(jsonParams)).toString();
        cartViewModel.updateCartItems(userID, jsonParamString, sessionToken).observe(getActivity(), cartResponseResource -> {
            fragmentCartBinding.progressBar.setVisibility(View.GONE);
            switch (cartResponseResource.status) {
                case SUCCESS:
                    updateCartValues(cartResponseResource);
                    Toast.makeText(dashboardActivity, "Cart Updated Successfully", Toast.LENGTH_SHORT).show();
                    break;
                case LOADING:
                    break;
                case ERROR:
                    cartAdapter.addAll(cartItemsList);
                    Toast.makeText(dashboardActivity, cartResponseResource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        dashboardActivity.hideToolBar();
        dashboardActivity.handleActionMenuBar(false, true, "");
    }

    public void showProgressbar(boolean shouldShowProgressbar) {
        if (shouldShowProgressbar)
            fragmentCartBinding.progressBar.setVisibility(View.VISIBLE);
        else
            fragmentCartBinding.progressBar.setVisibility(View.GONE);
    }

    private void logViewCartEvent(ArrayList<ShoppingCartItem> cartItemsList, Double cartValue) {
        ArrayList<Bundle> itemParcelableList = new ArrayList<>();
        int cartCount = cartItemsList.size();
        for (int i = 0; i < cartItemsList.size(); i++) {
            Bundle itemBundle = new Bundle();
            itemBundle.putString(FirebaseAnalytics.Param.ITEM_NAME, cartItemsList.get(i).getProduct().getDescriptions().get(0).getProductName());
            itemBundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, cartItemsList.get(i).getProduct().getCategories().get(0).getDescriptions().get(0).getCategoryName());
            itemBundle.putString(FirebaseAnalytics.Param.ITEM_BRAND, cartItemsList.get(i).getProduct().getManufature().getManufactureDescriptions().get(0).getName());
            itemBundle.putString("category_id", cartItemsList.get(i).getProduct().getCategories().get(0).getCategoryId());
            itemBundle.putString(FirebaseAnalytics.Param.ITEM_ID, cartItemsList.get(i).getProduct().getProductId());
            itemParcelableList.add(itemBundle);
        }


        Bundle analyticsBundle = new Bundle();
        analyticsBundle.putString(FirebaseAnalytics.Param.CURRENCY, "KWD");
        analyticsBundle.putDouble(FirebaseAnalytics.Param.VALUE, cartValue);
        analyticsBundle.putLong("item_count", cartCount);
        analyticsBundle.putParcelableArrayList(FirebaseAnalytics.Param.ITEMS, itemParcelableList);
        dashboardActivity.getmFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.VIEW_CART, analyticsBundle);
    }

    private void logViewCartFacebookEvent(ArrayList<ShoppingCartItem> cartItemsList, Double cartValue) {
        ArrayList<Bundle> itemParcelableList = new ArrayList<>();
        int cartCount = cartItemsList.size();
        for (int i = 0; i < cartItemsList.size(); i++) {
            Bundle itemBundle = new Bundle();
            itemBundle.putString(AppEventsConstants.EVENT_PARAM_CONTENT, cartItemsList.get(i).getProduct().getDescriptions().get(0).getProductName());
            itemBundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, cartItemsList.get(i).getProduct().getCategories().get(0).getDescriptions().get(0).getCategoryName());
            itemBundle.putString(FirebaseAnalytics.Param.ITEM_BRAND, cartItemsList.get(i).getProduct().getManufature().getManufactureDescriptions().get(0).getName());
            itemBundle.putString("category_id", cartItemsList.get(i).getProduct().getCategories().get(0).getCategoryId());
            itemBundle.putString(FirebaseAnalytics.Param.ITEM_ID, cartItemsList.get(i).getProduct().getProductId());
            itemParcelableList.add(itemBundle);
        }


        Bundle analyticsBundle = new Bundle();
        analyticsBundle.putString(EVENT_PARAM_CURRENCY, "KWD");
        analyticsBundle.putDouble(FirebaseAnalytics.Param.VALUE, cartValue);
        analyticsBundle.putLong("item_count", cartCount);
        analyticsBundle.putParcelableArrayList(FirebaseAnalytics.Param.ITEMS, itemParcelableList);
        dashboardActivity.getmFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.VIEW_CART, analyticsBundle);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CartAdapter.ViewHolder) {
            // get the removed item name to display it in snack bar
            String name = cartItemsList.get(viewHolder.getAdapterPosition()).getProduct().getDescriptions().get(0).getProductName();

            // backup of removed item for undo purpose
            final ShoppingCartItem deletedItem = cartItemsList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();
            fragmentCartBinding.progressBar.setVisibility(View.VISIBLE);
            removeItemFromCartApi(deletedItem);
            // remove the item from recycler view
            cartAdapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
//            Snackbar snackbar = Snackbar
//                    .make(fragmentCartBinding.cvFragment, name + " Removed from cart!", Snackbar.LENGTH_LONG);
//            snackbar.setAction("UNDO", new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    // undo is selected, restore the deleted item
//                    cartAdapter.restoreItem(deletedItem, deletedIndex);
//                }
//            });
//            snackbar.setActionTextColor(getResources().getColor(R.color.light_blue));
//            snackbar.show();
        }
    }
}

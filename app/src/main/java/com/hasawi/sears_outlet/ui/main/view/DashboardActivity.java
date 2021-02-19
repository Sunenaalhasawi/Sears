package com.hasawi.sears_outlet.ui.main.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.hasawi.sears_outlet.R;
import com.hasawi.sears_outlet.data.api.model.NavigationMenuItem;
import com.hasawi.sears_outlet.data.api.model.pojo.Category;
import com.hasawi.sears_outlet.data.api.model.pojo.Product;
import com.hasawi.sears_outlet.data.api.model.pojo.ProductSearch;
import com.hasawi.sears_outlet.data.api.response.DynamicUiResponse;
import com.hasawi.sears_outlet.databinding.ActivityDashboardBinding;
import com.hasawi.sears_outlet.databinding.LayoutToastWishlistNotificationBinding;
import com.hasawi.sears_outlet.ui.base.BaseActivity;
import com.hasawi.sears_outlet.ui.base.BaseFragment;
import com.hasawi.sears_outlet.ui.main.adapters.HomeTabsPagerAdapter;
import com.hasawi.sears_outlet.ui.main.adapters.NavigationDrawerAdapter;
import com.hasawi.sears_outlet.ui.main.adapters.SearchProductAdapter;
import com.hasawi.sears_outlet.ui.main.listeners.RecyclerItemClickListener;
import com.hasawi.sears_outlet.ui.main.view.checkout.CheckoutFragment;
import com.hasawi.sears_outlet.ui.main.view.checkout.MyCartFragment;
import com.hasawi.sears_outlet.ui.main.view.checkout.OrderFragment;
import com.hasawi.sears_outlet.ui.main.view.checkout.PaymentFragment;
import com.hasawi.sears_outlet.ui.main.view.dashboard.home.CategoryFragment;
import com.hasawi.sears_outlet.ui.main.view.dashboard.home.NotificationFragment;
import com.hasawi.sears_outlet.ui.main.view.dashboard.navigation_drawer_menu.AboutUsFragment;
import com.hasawi.sears_outlet.ui.main.view.dashboard.navigation_drawer_menu.ContactUsFragment;
import com.hasawi.sears_outlet.ui.main.view.dashboard.navigation_drawer_menu.FAQFragment;
import com.hasawi.sears_outlet.ui.main.view.dashboard.navigation_drawer_menu.PrivatePolicyFragment;
import com.hasawi.sears_outlet.ui.main.view.dashboard.product.SelectedProductDetailsFragment;
import com.hasawi.sears_outlet.ui.main.view.dashboard.user_account.UserAccountFragment;
import com.hasawi.sears_outlet.ui.main.view.dashboard.user_account.WishListFragment;
import com.hasawi.sears_outlet.ui.main.view.dashboard.user_account.order_history.OrderHistoryDetailFragment;
import com.hasawi.sears_outlet.ui.main.view.dashboard.user_account.order_history.OrderHistoryFragment;
import com.hasawi.sears_outlet.ui.main.view.dashboard.user_account.order_history.TrackOrderFragment;
import com.hasawi.sears_outlet.ui.main.view.dashboard.user_account.profile.UserProfileFragment;
import com.hasawi.sears_outlet.ui.main.view.signin.SigninActivity;
import com.hasawi.sears_outlet.ui.main.viewmodel.DashboardViewModel;
import com.hasawi.sears_outlet.utils.AppConstants;
import com.hasawi.sears_outlet.utils.PreferenceHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DashboardActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    // tags used to attach the fragments
    private static final String TAG_DASHBOARD = "dashboard";
    ActivityDashboardBinding activityDashboardBinding;
    ArrayList<NavigationMenuItem> menuItemArrayList = new ArrayList<>();
    ActionBarDrawerToggle actionBarDrawerToggle;
    Bundle dataBundle = new Bundle();
    SearchView searchView;
    MenuItem searchItem, notificationItem, shareItem, bagItem;
    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;
    private DashboardViewModel dashboardViewModel;
    private CharSequence mTitle;
    private List<ProductSearch> productSearchList = new ArrayList<>();
    private SearchProductAdapter searchProductAdapter;
    AppEventsLogger facebookEventsLogger;
    ActionBar actionBar;
    private FirebaseAnalytics mFirebaseAnalytics;
    private ArrayList<Category> mainCategoryList = new ArrayList<>();
    private HashMap<String, DynamicUiResponse.UiData> dynamicUiDataHashmap = new HashMap<>();
    private List<Category> allCategoryList = new ArrayList<>();
    private String searchQuery = "";
    boolean isUserLoggedin = false;
    private BadgeDrawable bottomBarBadgeDrawable, appBarBadgeDrawable;
    private String currentlyShowingProductId = "", currentlyShowingProductName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDashboardBinding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        facebookEventsLogger = AppEventsLogger.newLogger(this);
        setSupportActionBar(activityDashboardBinding.appBarMain.toolbar);
        try {
            dataBundle = getIntent().getExtras();
        } catch (Exception e) {
            e.printStackTrace();
        }
        getMainCategories();

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);
        setUpNavigationView();
        activityDashboardBinding.appBarMain.bottomNavigationView.setOnNavigationItemSelectedListener(this);
        activityDashboardBinding.appBarMain.bottomNavigationView.setItemIconTintList(null);


        PreferenceHandler preferenceHandler = new PreferenceHandler(this, PreferenceHandler.TOKEN_LOGIN);
        String username = preferenceHandler.getData(PreferenceHandler.LOGIN_USERNAME, "");
        isUserLoggedin = preferenceHandler.getData(PreferenceHandler.LOGIN_STATUS, false);
        String userId = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
        //Setting analytics default parameter
        if (isUserLoggedin) {
            Bundle parameters = new Bundle();
            parameters.putString("user_id", userId);
            mFirebaseAnalytics.setDefaultEventParameters(parameters);
        }
        if (isUserLoggedin)
            activityDashboardBinding.navigationDrawerHeaderInclude.tvUserName.setText(username);
        else
            activityDashboardBinding.navigationDrawerHeaderInclude.tvUserName.setText("Guest");
        activityDashboardBinding.navigationDrawerHeaderInclude.imageViewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Todo upload customer image
            }
        });

        retrieveFirebaseDynamicLinks();
    }

    private void retrieveFirebaseDynamicLinks() {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            String productId = deepLink.getQueryParameter("productId");
                            Bundle bundle = new Bundle();
                            bundle.putString("product_object_id", productId);
                            handleActionMenuBar(true, false, "");
                            replaceFragment(R.id.fragment_replacer, new SelectedProductDetailsFragment(), bundle, true, false);
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "getDynamicLink:onFailure", e);
                    }
                });
    }

    private void loadMenuFragments(int position) {
        NavigationMenuItem menuItem = menuItemArrayList.get(position);
        switch (menuItem.get_ID()) {
            case AppConstants.ID_MENU_HOME:
                handleActionMenuBar(false, true, "");
                int fragmentCount = getSupportFragmentManager().getBackStackEntryCount();
                for (int i = 0; i < fragmentCount; i++) {
                    getSupportFragmentManager().popBackStackImmediate();
                }
                closeDrawer();
                break;
            case AppConstants.ID_MENU_CONTACT_US:
                replaceFragment(R.id.fragment_replacer, new ContactUsFragment(), null, true, false);
                closeDrawer();
                break;
            case AppConstants.ID_MENU_ABOUT_US:
                replaceFragment(R.id.fragment_replacer, new AboutUsFragment(), null, true, false);
                activityDashboardBinding.appBarMain.toolbar.setVisibility(View.GONE);
                closeDrawer();
                break;
            case AppConstants.ID_MENU_PRIVACY_POLICY:
                replaceFragment(R.id.fragment_replacer, new PrivatePolicyFragment(), null, true, false);
                activityDashboardBinding.appBarMain.toolbar.setVisibility(View.GONE);
                closeDrawer();
                break;
            case AppConstants.ID_MENU_FAQ:
                replaceFragment(R.id.fragment_replacer, new FAQFragment(), null, true, false);
                activityDashboardBinding.appBarMain.toolbar.setVisibility(View.GONE);
                closeDrawer();
                break;
            case AppConstants.ID_MENU_SIGNOUT:
                if (isAlreadyLoggedinWithFacebbok())
                    disconnectFromFacebook();
                clearPreferences();
                closeDrawer();
                Intent intent = new Intent(DashboardActivity.this, SigninActivity.class);
                startActivity(intent);
                this.finish();
                break;
            default:
                break;


        }
    }

    private void clearPreferences() {
        PreferenceHandler preferenceHandler = new PreferenceHandler(getApplicationContext(), PreferenceHandler.TOKEN_LOGIN);
        preferenceHandler.saveData(PreferenceHandler.LOGIN_STATUS, false);
        preferenceHandler.saveData(PreferenceHandler.LOGIN_USERNAME, "");
        preferenceHandler.saveData(PreferenceHandler.LOGIN_EMAIL, "");
        preferenceHandler.saveData(PreferenceHandler.LOGIN_TOKEN, "");
        preferenceHandler.saveData(PreferenceHandler.LOGIN_ITEM_TO_BE_CARTED, "");
        preferenceHandler.saveData(PreferenceHandler.LOGIN_ITEM_TO_BE_WISHLISTED, "");
        preferenceHandler.saveData(PreferenceHandler.LOGIN_STATUS, false);
        preferenceHandler.saveData(PreferenceHandler.LOGIN_CONFIRM_PASSWORD, "");
        preferenceHandler.saveData(PreferenceHandler.LOGIN_PASSWORD, "");
        preferenceHandler.saveData(PreferenceHandler.LOGIN_USER_ID, "");
        preferenceHandler.saveData(PreferenceHandler.LOGIN_PHONENUMBER, "");
        preferenceHandler.saveData(PreferenceHandler.LOGIN_USER_SELECTED_ADDRESS_ID, "");
        preferenceHandler.saveData(PreferenceHandler.LOGIN_USER_CART_ID, "");
        preferenceHandler.saveData(PreferenceHandler.LOGIN_CATEGORY_NAME, "");
        preferenceHandler.saveData(PreferenceHandler.LOGIN_CATEGORY_ID, "");
        preferenceHandler.saveData(PreferenceHandler.LOGIN_NATIONALITY, "");
        preferenceHandler.saveData(PreferenceHandler.LOGIN_GENDER, "");
    }


    public void closeDrawer() {
        activityDashboardBinding.drawerLayout.closeDrawers();
    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {
//        activityDashboardBinding.navigationDrawerHeaderInclude.tvUserName.setText("");

        // loading header background image
//        Glide.with(this).load(urlNavHeaderBg)
//                .crossFade()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(imgNavHeaderBg);
//
//        // Loading profile image
//        Glide.with(this).load(urlProfileImg)
//                .crossFade()
//                .thumbnail(0.5f)
//                .bitmapTransform(new CircleTransform(this))
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(imgProfile);
    }

    private void setUpNavigationView() {
        actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(false);

        menuItemArrayList = dashboardViewModel.getMenuItemsList(this);
        activityDashboardBinding.listviewMenu.setAdapter(new NavigationDrawerAdapter(
                this,
                R.layout.layout_navigation_drawer_item,
                menuItemArrayList) {
            @Override
            public void onNotificationStatusChanged(boolean isNotificationEnabled) {
                if (isNotificationEnabled)
                    FirebaseMessaging.getInstance().subscribeToTopic(AppConstants.APP_NAME);
                else
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(AppConstants.APP_NAME);
            }
        });
        activityDashboardBinding.listviewMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                loadMenuFragments(position);
            }
        });
        loadNavHeader();
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, activityDashboardBinding.drawerLayout, activityDashboardBinding.appBarMain.toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };
        actionBarDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (actionBarDrawerToggle.isDrawerIndicatorEnabled()) {
                    activityDashboardBinding.drawerLayout.openDrawer(GravityCompat.START);
                } else {
                    onBackPressed();
                }
            }
        });

        //Setting the actionbarToggle to drawer layout
        activityDashboardBinding.drawerLayout.addDrawerListener(actionBarDrawerToggle);
        handleActionMenuBar(false, true, "");
        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard, menu);
        activityDashboardBinding.appBarMain.recyclerSearchProducts.setLayoutManager(new LinearLayoutManager(this));
        activityDashboardBinding.appBarMain.recyclerSearchProducts.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                try {
                    if (searchView != null) {
                        searchView.clearFocus();
                        searchItem.collapseActionView();
                    }
                    activityDashboardBinding.appBarMain.fragmentReplaceSearchView.setVisibility(View.GONE);
                    ProductSearch selectedProduct = productSearchList.get(position);
                    logSearchEvent(selectedProduct.getObjectID(), selectedProduct.getNameEn(), true);
                    Bundle bundle = new Bundle();
                    bundle.putString("product_object_id", selectedProduct.getObjectID());
                    handleActionMenuBar(true, false, selectedProduct.getNameEn());
                    replaceFragment(R.id.fragment_replacer, new SelectedProductDetailsFragment(), bundle, true, false);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
        searchItem = menu.findItem(R.id.action_search);
        notificationItem = menu.findItem(R.id.action_notifications);
        shareItem = menu.findItem(R.id.action_share);
        bagItem = menu.findItem(R.id.action_bag);
        shareItem.setVisible(false);
        bagItem.setVisible(false);
        searchItem.setVisible(true);
        notificationItem.setVisible(true);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("What are you looking for?");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery = query;
                Bundle analyticsBundle = new Bundle();
                analyticsBundle.putString(FirebaseAnalytics.Param.SEARCH_TERM, query);
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH, analyticsBundle);
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (!query.equalsIgnoreCase("")) {
                    if (activityDashboardBinding.appBarMain.fragmentReplaceSearchView.getVisibility() != View.VISIBLE) {
                        activityDashboardBinding.appBarMain.fragmentReplaceSearchView.setVisibility(View.VISIBLE);
                    }
                    dashboardViewModel.searchProducts(query).observe(DashboardActivity.this, searchProductListResponse -> {
                        switch (searchProductListResponse.status) {
                            case SUCCESS:
                                productSearchList.clear();
                                productSearchList = searchProductListResponse.data.getData().getProductSearchs();
                                searchProductAdapter = new SearchProductAdapter(getApplicationContext(), productSearchList);
                                activityDashboardBinding.appBarMain.recyclerSearchProducts.setAdapter(searchProductAdapter);
                                searchProductAdapter.notifyDataSetChanged();
                                break;
                            case LOADING:
                                break;
                            case ERROR:
                                Toast.makeText(DashboardActivity.this, searchProductListResponse.message, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    });
                }
                return false;
            }
        });

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                activityDashboardBinding.appBarMain.fragmentReplaceSearchView.setVisibility(View.GONE);
                return true;
            }
        });

        setCartBadges();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_notifications:
                handleActionMenuBar(true, true, "Notifications");
                replaceFragment(R.id.fragment_replacer, new NotificationFragment(), null, true, false);
                return true;
            case R.id.action_share:
                shareProduct(currentlyShowingProductId, currentlyShowingProductName);
                return true;
            default:
                super.onOptionsItemSelected(item);
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        BaseFragment currentFragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_replacer);
        int fragmentCount = getSupportFragmentManager().getBackStackEntryCount();
        for (int i = 0; i < fragmentCount; i++) {
            getSupportFragmentManager().popBackStack();
        }
        switch (item.getItemId()) {

            case R.id.navigation_home:
                activityDashboardBinding.appBarMain.framelayoutCategories.setVisibility(View.GONE);
                handleActionMenuBar(false, true, "");
                return true;
            case R.id.navigation_wishlist:
                if (currentFragment instanceof WishListFragment) {

                } else {
                    WishListFragment wishlistFragment = new WishListFragment();
                    replaceFragment(R.id.fragment_replacer, wishlistFragment, null, true, false);
                    handleActionMenuBar(true, true, "Wishlist");
                }
                return true;
            case R.id.navigation_categories:
                handleActionMenuBar(false, true, "");
//
//                int count = getSupportFragmentManager().getBackStackEntryCount();
//                for (int i = 0; i < count; i++) {
//                    getSupportFragmentManager().popBackStackImmediate();
//                }
                activityDashboardBinding.appBarMain.framelayoutCategories.setVisibility(View.VISIBLE);
                BaseFragment showingFragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.framelayout_categories);
                if (showingFragment instanceof CategoryFragment) {

                } else {
                    replaceFragment(R.id.framelayout_categories, new CategoryFragment(), null, true, false);
                }
                return true;
            case R.id.navigation_cart:
                if (currentFragment instanceof MyCartFragment) {

                } else {
                    MyCartFragment myCartFragment = new MyCartFragment();
                    replaceFragment(R.id.fragment_replacer, myCartFragment, null, true, false);
                    handleActionMenuBar(true, true, "My Cart");
                }
                return true;
            case R.id.navigation_profile:
                if (currentFragment instanceof UserAccountFragment) {

                } else {
                    UserAccountFragment userAccountFragment = new UserAccountFragment();
                    replaceFragment(R.id.fragment_replacer, userAccountFragment, null, true, false);
                    handleActionMenuBar(true, true, "My Account");
                }
                return true;
            default:
                return true;
        }

    }

    @Override
    public void setTitle(CharSequence title) {
        try {
            mTitle = title;
            getSupportActionBar().setTitle(mTitle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleActionMenuBar(boolean showBackbutton, boolean showBottomNavigationMenu, String title) {
//         Showing back arrow icon. true means show and false means do not show
        getSupportActionBar().setDisplayHomeAsUpEnabled(showBackbutton);
        //Showing title
        getSupportActionBar().setDisplayShowTitleEnabled(showBackbutton);
        // Showing or hiding ,menu icon
        actionBarDrawerToggle.setDrawerIndicatorEnabled(!showBackbutton);
        if (showBackbutton)
            activityDashboardBinding.appBarMain.imageSearsLogo.setVisibility(View.GONE);
        else
            activityDashboardBinding.appBarMain.imageSearsLogo.setVisibility(View.VISIBLE);
        if (showBottomNavigationMenu)
            activityDashboardBinding.appBarMain.bottomNavigationView.setVisibility(View.VISIBLE);
        else
            activityDashboardBinding.appBarMain.bottomNavigationView.setVisibility(View.GONE);
        if (showBackbutton) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_bronze);
        } else
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);
        if (showBackbutton)
            setTitle(title);
        else
            setTitle("");
        actionBarDrawerToggle.syncState();

    }

    @Override
    public void onBackPressed() {
        if (activityDashboardBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            activityDashboardBinding.drawerLayout.closeDrawer(GravityCompat.START);
        } else if (activityDashboardBinding.appBarMain.fragmentReplaceSearchView.getVisibility() == View.VISIBLE) {
            if (searchView != null) {
                searchView.setIconified(true);
                searchView.onActionViewCollapsed();
                searchView.clearFocus();
                activityDashboardBinding.appBarMain.toolbar.collapseActionView();
                activityDashboardBinding.appBarMain.fragmentReplaceSearchView.setVisibility(View.GONE);
            }

        } else {
            FragmentManager fm = getSupportFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                fm.popBackStack();
            } else {
                super.onBackPressed();
            }
            setTitle(getTitle());
            activityDashboardBinding.appBarMain.toolbar.setVisibility(View.VISIBLE);
            BaseFragment currentFragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_replacer);
            handleActionBarIcons(true);
            if (currentFragment instanceof WishListFragment) {
                handleActionMenuBar(false, true, "");
                activityDashboardBinding.appBarMain.framelayoutCategories.setVisibility(View.GONE);
                activityDashboardBinding.appBarMain.bottomNavigationView.getMenu().findItem(R.id.navigation_wishlist).setChecked(false);
            } else if (currentFragment instanceof MyCartFragment) {
                handleActionMenuBar(false, true, "");
                activityDashboardBinding.appBarMain.framelayoutCategories.setVisibility(View.GONE);
            } else if (currentFragment instanceof UserAccountFragment) {
                handleActionMenuBar(false, true, "");
                activityDashboardBinding.appBarMain.framelayoutCategories.setVisibility(View.GONE);
            } else if (currentFragment instanceof SelectedProductDetailsFragment) {
                handleActionMenuBar(false, true, "");
                activityDashboardBinding.appBarMain.framelayoutCategories.setVisibility(View.GONE);
            } else if (currentFragment instanceof PaymentFragment) {
                handleActionMenuBar(false, true, "");
                activityDashboardBinding.appBarMain.framelayoutCategories.setVisibility(View.GONE);
            } else if (currentFragment instanceof CheckoutFragment) {
                handleActionMenuBar(false, true, "");
                activityDashboardBinding.appBarMain.framelayoutCategories.setVisibility(View.GONE);
            } else if (currentFragment instanceof UserProfileFragment) {
                handleActionMenuBar(true, true, "My Account");
                activityDashboardBinding.appBarMain.framelayoutCategories.setVisibility(View.GONE);
            } else if (currentFragment instanceof OrderHistoryFragment) {
                handleActionMenuBar(true, true, "My Account");
                activityDashboardBinding.appBarMain.framelayoutCategories.setVisibility(View.GONE);
            } else if (currentFragment instanceof AboutUsFragment) {
                handleActionMenuBar(false, true, "");
                activityDashboardBinding.appBarMain.framelayoutCategories.setVisibility(View.GONE);
                activityDashboardBinding.appBarMain.toolbar.setVisibility(View.VISIBLE);
            } else if (currentFragment instanceof NotificationFragment) {
                handleActionMenuBar(false, true, "");
                activityDashboardBinding.appBarMain.framelayoutCategories.setVisibility(View.GONE);
            } else if (currentFragment instanceof TrackOrderFragment) {
                handleActionMenuBar(true, false, "Order Details");
                activityDashboardBinding.appBarMain.framelayoutCategories.setVisibility(View.GONE);
            } else if (currentFragment instanceof OrderHistoryDetailFragment) {
                handleActionMenuBar(true, false, "My Orders");
                activityDashboardBinding.appBarMain.framelayoutCategories.setVisibility(View.GONE);
            } else if (currentFragment instanceof OrderFragment) {
                int fragmentCount = getSupportFragmentManager().getBackStackEntryCount();
                for (int i = 0; i < fragmentCount; i++) {
                    getSupportFragmentManager().popBackStack();
                }
                activityDashboardBinding.appBarMain.framelayoutCategories.setVisibility(View.GONE);
                handleActionMenuBar(false, true, "");
            }

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Back arrow on click here
        onBackPressed();
        return true;
    }

    public void hideSearchPage() {
        activityDashboardBinding.appBarMain.fragmentReplaceSearchView.setVisibility(View.GONE);
    }

    public void showCustomWislistToast(boolean isWishlisted) {
        LayoutInflater inflater = getLayoutInflater();
        LayoutToastWishlistNotificationBinding toastWishlistNotificationBinding = DataBindingUtil.inflate(inflater, R.layout.layout_toast_wishlist_notification, null, true);
        if (isWishlisted) {
            toastWishlistNotificationBinding.checkBoxWishlistStatus.setChecked(true);
            toastWishlistNotificationBinding.tvWishlistStatus.setText("Added to");
        } else {
            toastWishlistNotificationBinding.checkBoxWishlistStatus.setChecked(false);
            toastWishlistNotificationBinding.tvWishlistStatus.setText("Removed from");
        }
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(toastWishlistNotificationBinding.getRoot());
        toast.show();
    }

    public FirebaseAnalytics getmFirebaseAnalytics() {
        return mFirebaseAnalytics;
    }

    public AppEventsLogger getFacebookEventsLogger() {
        return facebookEventsLogger;
    }

    private void getMainCategories() {
        dashboardViewModel.getMainCateogries().observe(this, mainCategoryResponseResource -> {
            switch (mainCategoryResponseResource.status) {
                case SUCCESS:
                    allCategoryList = mainCategoryResponseResource.data.getMainCategories();
                    for (int i = 0; i < allCategoryList.size(); i++) {
                        if (allCategoryList.get(i).getParentId() == null)
                            mainCategoryList.add(allCategoryList.get(i));
                    }
                    callDynamicUiApi();
                    break;
                case LOADING:
                    break;
                case ERROR:
                    Toast.makeText(this, mainCategoryResponseResource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    private void createDynamicHomeTabs(ArrayList<Category> mainCategoryList, HashMap<String, DynamicUiResponse.UiData> dynamicUiDataHashmap) {
        HomeTabsPagerAdapter adapter = new HomeTabsPagerAdapter
                (this, getSupportFragmentManager(), mainCategoryList.size(), mainCategoryList, dynamicUiDataHashmap);
        activityDashboardBinding.appBarMain.viewPagerHomeTabs.setAdapter(adapter);
        activityDashboardBinding.appBarMain.viewPagerHomeTabs.setOffscreenPageLimit(1);
        if (activityDashboardBinding.appBarMain.tabLayoutCategories.getTabCount() <= 5) {
            activityDashboardBinding.appBarMain.tabLayoutCategories.setTabMode(TabLayout.MODE_FIXED);
        } else {
            activityDashboardBinding.appBarMain.tabLayoutCategories.setTabMode(TabLayout.MODE_SCROLLABLE);
        }
        PreferenceHandler preferenceHandler = new PreferenceHandler(this, PreferenceHandler.TOKEN_LOGIN);
        String selectedMainCategory = preferenceHandler.getData(PreferenceHandler.LOGIN_CATEGORY_ID, "");
        for (int i = 0; i < mainCategoryList.size(); i++) {
            if (selectedMainCategory.equalsIgnoreCase(mainCategoryList.get(i).getCategoryId()))
                activityDashboardBinding.appBarMain.viewPagerHomeTabs.setCurrentItem(i);
//            TabLayout.Tab tab=activityDashboardBinding.appBarMain.
        }
        activityDashboardBinding.appBarMain.viewPagerHomeTabs.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Category selectedCategory = mainCategoryList.get(position);
                PreferenceHandler preferenceHandler = new PreferenceHandler(getBaseContext(), PreferenceHandler.TOKEN_LOGIN);
                preferenceHandler.saveData(PreferenceHandler.LOGIN_CATEGORY_ID, selectedCategory.getCategoryId());
                preferenceHandler.saveData(PreferenceHandler.LOGIN_CATEGORY_NAME, selectedCategory.getDescriptions().get(0).getCategoryName());

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        activityDashboardBinding.appBarMain.tabLayoutCategories.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                PreferenceHandler preferenceHandler = new PreferenceHandler(getBaseContext(), PreferenceHandler.TOKEN_LOGIN);
                activityDashboardBinding.appBarMain.viewPagerHomeTabs.setCurrentItem(tab.getPosition());
                Category selectedCategory = mainCategoryList.get(tab.getPosition());

                preferenceHandler.saveData(PreferenceHandler.LOGIN_CATEGORY_ID, selectedCategory.getCategoryId());
                preferenceHandler.saveData(PreferenceHandler.LOGIN_CATEGORY_NAME, selectedCategory.getDescriptions().get(0).getCategoryName());
                BaseFragment currentFragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.framelayout_categories);
                if (currentFragment instanceof CategoryFragment) {
                    replaceFragment(R.id.framelayout_categories, new CategoryFragment(), null, false, false);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        activityDashboardBinding.appBarMain.tabLayoutCategories.setupWithViewPager(activityDashboardBinding.appBarMain.viewPagerHomeTabs);


    }

    private void callDynamicUiApi() {
        activityDashboardBinding.appBarMain.progressBar.setVisibility(View.VISIBLE);
        dashboardViewModel.getDynamicUiHome().observe(this, dynamicUiResponseResource -> {
            switch (dynamicUiResponseResource.status) {
                case SUCCESS:
                    dynamicUiDataHashmap = dynamicUiResponseResource.data.getData();
                    createDynamicHomeTabs(mainCategoryList, dynamicUiDataHashmap);
                    break;
                case LOADING:
                    break;
                case ERROR:
                    Toast.makeText(this, dynamicUiResponseResource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
            activityDashboardBinding.appBarMain.progressBar.setVisibility(View.GONE);
        });
    }

    public void showMessageToast(String message, int duration) {
        Toast.makeText(DashboardActivity.this, message, duration).show();
    }

    public void handleSocialShare(boolean shouldShow) {
        if (shareItem != null) {
            if (shouldShow)
                shareItem.setVisible(true);
            else
                shareItem.setVisible(false);
        }
    }

    public void handleBag(boolean shouldShow) {
        if (bagItem != null) {
            if (shouldShow)
                bagItem.setVisible(true);
            else
                bagItem.setVisible(false);
        }
    }


    public void shareProduct(String productId, String currentlyShowingProductName) {
        handleSocialShare(true);
        String url = createDynamicLink(productId);
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, this.currentlyShowingProductName + "\n" +
                url);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    public void handleActionBarIcons(boolean showActionBaritems) {
        if (shareItem != null)
            shareItem.setVisible(false);
        if (bagItem != null)
            bagItem.setVisible(false);
        if (showActionBaritems) {
            if (searchItem != null)
                searchItem.setVisible(true);
            if (notificationItem != null)
                notificationItem.setVisible(true);
        } else {
            if (searchItem != null)
                searchItem.setVisible(false);
            if (notificationItem != null)
                notificationItem.setVisible(false);
        }
    }

    public void logAddToWishlistFirebaseEvent(Product product) {
        Bundle analyticsBundle = new Bundle();
        try {
            analyticsBundle.putString(FirebaseAnalytics.Param.CURRENCY, "KWD");
            analyticsBundle.putDouble(FirebaseAnalytics.Param.VALUE, product.getOriginalPrice());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bundle itemBundle = new Bundle();
        try {
            itemBundle.putString(FirebaseAnalytics.Param.ITEM_NAME, product.getDescriptions().get(0).getProductName());
            itemBundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, product.getCategories().get(0).getDescriptions().get(0).getCategoryName());
            itemBundle.putString("product_id", product.getProductId());
            ArrayList<Bundle> parcelabeList = new ArrayList<>();
            parcelabeList.add(itemBundle);
            analyticsBundle.putParcelableArrayList(FirebaseAnalytics.Param.ITEMS, parcelabeList);
            getmFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.ADD_TO_CART, analyticsBundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This function assumes logger is an instance of AppEventsLogger and has been
     * created using AppEventsLogger.newLogger() call.
     */
    public void logAddToWishlistEvent(Product currentSelectedProduct) {
        Bundle params = new Bundle();
        Gson gson = new Gson();
        String product = gson.toJson(currentSelectedProduct);
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, currentSelectedProduct.getCategories().get(0).getDescriptions().get(0).getCategoryName());
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, product);
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, currentSelectedProduct.getProductId());
        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "KWD");
        getFacebookEventsLogger().logEvent(AppEventsConstants.EVENT_NAME_ADDED_TO_WISHLIST, currentSelectedProduct.getOriginalPrice(), params);
    }

    /**
     * This function assumes logger is an instance of AppEventsLogger and has been
     * created using AppEventsLogger.newLogger() call.
     */
    public void logSearchEvent(String productID, String productName, boolean success) {
        Bundle params = new Bundle();
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, productName);
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, productID);
        params.putString(AppEventsConstants.EVENT_PARAM_SEARCH_STRING, searchQuery);
        params.putInt(AppEventsConstants.EVENT_PARAM_SUCCESS, success ? 1 : 0);
        getFacebookEventsLogger().logEvent(AppEventsConstants.EVENT_NAME_SEARCHED, params);
    }

    private void setAppBarBadge(int cartCount) {
        if (bagItem != null) {
            bagItem.setActionView(R.layout.layout_cart_badge);
            TextView textCartItemCount = (TextView) bagItem.getActionView().findViewById(R.id.cart_badge);
            if (textCartItemCount != null) {
                if (cartCount == 0) {
                    if (textCartItemCount.getVisibility() != View.GONE) {
                        textCartItemCount.setVisibility(View.GONE);
                    }
                } else {
                    textCartItemCount.setText(String.valueOf(Math.min(cartCount, 99)));
                    if (textCartItemCount.getVisibility() != View.VISIBLE) {
                        textCartItemCount.setVisibility(View.VISIBLE);
                    }
                }
            }
            FrameLayout fr_viewBag = (FrameLayout) bagItem.getActionView();

            fr_viewBag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleActionMenuBar(true, true, "My Bag");
                    replaceFragment(R.id.fragment_replacer, new MyCartFragment(), null, true, false);
                }
            });
        }


    }

    private void setCartBadges() {
        Menu menu = activityDashboardBinding.appBarMain.bottomNavigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.navigation_cart);
        bottomBarBadgeDrawable = activityDashboardBinding.appBarMain.bottomNavigationView.getOrCreateBadge(menuItem.getItemId());
        bottomBarBadgeDrawable.setBackgroundColor(getResources().getColor(R.color.bronze));
        bottomBarBadgeDrawable.setBadgeTextColor(getResources().getColor(R.color.white));
        bottomBarBadgeDrawable.setBadgeGravity(BadgeDrawable.TOP_END);
        setCartBadgeNumber(0);
        if (isUserLoggedin)
            callMyCartApi();
    }

    public void setCartBadgeNumber(int cartCount) {
        if (cartCount == 0) {
            bottomBarBadgeDrawable.clearNumber();
            bottomBarBadgeDrawable.setVisible(false);
        } else {
            bottomBarBadgeDrawable.setNumber(cartCount);
            bottomBarBadgeDrawable.setVisible(true);
        }
        setAppBarBadge(cartCount);


    }

    public void callMyCartApi() {
        PreferenceHandler preferenceHandler = new PreferenceHandler(this, PreferenceHandler.TOKEN_LOGIN);
        String userId = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
        String sessiontoken = preferenceHandler.getData(PreferenceHandler.LOGIN_TOKEN, "");
        activityDashboardBinding.appBarMain.progressBar.setVisibility(View.VISIBLE);
        dashboardViewModel.getCartItems(userId, sessiontoken).observe(this, cartResponse -> {
            switch (cartResponse.status) {
                case SUCCESS:
                    try {
                        int cartCount = cartResponse.data.getCartData().getItemCount();
                        setCartBadgeNumber(cartCount);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case LOADING:
                    break;
                case ERROR:
                    Toast.makeText(this, cartResponse.message, Toast.LENGTH_SHORT).show();
                    break;
            }
            activityDashboardBinding.appBarMain.progressBar.setVisibility(View.GONE);
        });
    }

    public String getCurrentlyShowingProductId() {
        return currentlyShowingProductId;
    }

    public void setCurrentlyShowingProductId(String currentlyShowingProductId) {
        this.currentlyShowingProductId = currentlyShowingProductId;
    }

    public String getCurrentlyShowingProductName() {
        return currentlyShowingProductName;
    }

    public void setCurrentlyShowingProductName(String currentlyShowingProductName) {
        this.currentlyShowingProductName = currentlyShowingProductName;
    }

    public String createDynamicLink(String productId) {
        String url = "https://searskuwait.com/products/?productId=" + productId;
        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(url))
                .setDomainUriPrefix("https://searsoutlet.page.link")
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder("com.hasawi.sears").build())
                .buildDynamicLink();
        Uri dynamicLinkUri = dynamicLink.getUri();
        return dynamicLinkUri.toString();
    }
}
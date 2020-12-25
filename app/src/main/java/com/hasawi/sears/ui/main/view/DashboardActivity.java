package com.hasawi.sears.ui.main.view;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.NavigationMenuItem;
import com.hasawi.sears.data.api.model.pojo.Banner;
import com.hasawi.sears.data.api.model.pojo.Category;
import com.hasawi.sears.data.api.model.pojo.ProductSearch;
import com.hasawi.sears.data.api.response.DynamicUiResponse;
import com.hasawi.sears.databinding.ActivityDashboardBinding;
import com.hasawi.sears.databinding.LayoutToastWishlistNotificationBinding;
import com.hasawi.sears.ui.base.BaseActivity;
import com.hasawi.sears.ui.base.BaseFragment;
import com.hasawi.sears.ui.main.adapters.HomeTabsPagerAdapter;
import com.hasawi.sears.ui.main.adapters.NavigationDrawerAdapter;
import com.hasawi.sears.ui.main.adapters.SearchProductAdapter;
import com.hasawi.sears.ui.main.adapters.SubCategoryAdapter;
import com.hasawi.sears.ui.main.listeners.RecyclerItemClickListener;
import com.hasawi.sears.ui.main.view.checkout.CheckoutFragment;
import com.hasawi.sears.ui.main.view.checkout.MyCartFragment;
import com.hasawi.sears.ui.main.view.checkout.PaymentFragment;
import com.hasawi.sears.ui.main.view.home.CategoryFragment;
import com.hasawi.sears.ui.main.view.home.SelectedProductDetailsFragment;
import com.hasawi.sears.ui.main.view.home.UserAccountFragment;
import com.hasawi.sears.ui.main.view.home.WishListFragment;
import com.hasawi.sears.ui.main.view.navigation_drawer_menu.order_history.OrderHistoryFragment;
import com.hasawi.sears.ui.main.view.navigation_drawer_menu.profile.UserProfileFragment;
import com.hasawi.sears.ui.main.view.signin.SigninActivity;
import com.hasawi.sears.ui.main.viewmodel.DashboardViewModel;
import com.hasawi.sears.utils.AppConstants;
import com.hasawi.sears.utils.PreferenceHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DashboardActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    // tags used to attach the fragments
    private static final String TAG_DASHBOARD = "dashboard";
    private static final String TAG_PROFILE = "profile";
    private static final String TAG_ORDERS = "orders";
    // index to identify current nav menu item
    public static int navItemIndex = 0;
    public static String CURRENT_TAG = TAG_DASHBOARD;
    ActivityDashboardBinding activityDashboardBinding;
    ArrayList<NavigationMenuItem> menuItemArrayList = new ArrayList<>();
    ActionBarDrawerToggle actionBarDrawerToggle;
    Bundle dataBundle = new Bundle();
    int cartCount = 0;
    SearchView searchView;
    MenuItem searchItem;
    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;
    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private DashboardViewModel dashboardViewModel;
    private CharSequence mTitle;
    private List<ProductSearch> productSearchList;
    private SearchProductAdapter searchProductAdapter;
    ActionBar actionBar;
    private FirebaseAnalytics mFirebaseAnalytics;
    private SubCategoryAdapter subCategoryAdapter;
    private ArrayList<Category> mainCategoryList = new ArrayList<>();
    private List<Banner> bannerList;
    private HashMap<String, DynamicUiResponse.UiData> dynamicUiDataHashmap = new HashMap<>();
    private ArrayList<Category>
            subCategoryArrayList = new ArrayList<>();
    private List<Category> allCategoryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDashboardBinding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        setSupportActionBar(activityDashboardBinding.appBarMain.toolbar);
        actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeAsUpIndicator(R.drawable.ic_back_bronze);
//        actionBar.setDisplayHomeAsUpEnabled(true);
        hideToolBarTitleShowLogo();

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
        activityDashboardBinding.appBarMain.searchView.recyclerSearchProducts.setLayoutManager(new LinearLayoutManager(this));
        activityDashboardBinding.appBarMain.searchView.recyclerSearchProducts.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                try {
                    ProductSearch selectedProduct = productSearchList.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putString("product_object_id", selectedProduct.getObjectID());
                    bundle.putBoolean("is_search", true);
                    showBackButton(true, false);
                    replaceFragment(R.id.fragment_replacer, new SelectedProductDetailsFragment(), bundle, true, false);
                    if (searchView != null)
                        searchView.clearFocus();
//                    if (searchItem != null)
//                        searchItem.collapseActionView();
                    activityDashboardBinding.appBarMain.fragmentReplaceSearchView.setVisibility(View.GONE);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));


        PreferenceHandler preferenceHandler = new PreferenceHandler(this, PreferenceHandler.TOKEN_LOGIN);
        String username = preferenceHandler.getData(PreferenceHandler.LOGIN_USERNAME, "");
        boolean isUserLoggedin = preferenceHandler.getData(PreferenceHandler.LOGIN_STATUS, false);
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


    }

    private void loadMenuFragments(int position) {
        NavigationMenuItem menuItem = menuItemArrayList.get(position);
        switch (menuItem.get_ID()) {
            case AppConstants.ID_MENU_HOME:
                closeDrawer();
                break;
            case AppConstants.ID_MENU_PROFILE:
                if (menuItem.isEnabled()) {
                    UserProfileFragment profileFragment = new UserProfileFragment();
                    replaceFragment(R.id.fragment_replacer, profileFragment, null, true, false);
                    setTitle("My Profile");
                    showBackButton(true, false);
                }
                closeDrawer();
                break;
            case AppConstants.ID_MENU_ORDERS:
                replaceFragment(R.id.fragment_replacer, new OrderHistoryFragment(), null, true, false);
                setTitle("My Orders");
                showBackButton(true, false);
                closeDrawer();
                break;
            case AppConstants.ID_MENU_WISHLIST:
                if (menuItem.isEnabled()) {
                    WishListFragment wishListFragment = new WishListFragment();
                    replaceFragment(R.id.fragment_replacer, wishListFragment, null, true, false);
                    setTitle("Wishlist");
                    showBackButton(true, true);
                }
                closeDrawer();
                break;
            case AppConstants.ID_MENU_SIGNOUT:
                if (menuItem.isEnabled()) {
                    if (isAlreadyLoggedinWithFacebbok())
                        LoginManager.getInstance().logOut();
                    clearPreferences();
                    closeDrawer();
                    Intent intent = new Intent(DashboardActivity.this, SigninActivity.class);
                    startActivity(intent);
                    this.finish();
                }
                break;
            default:
                break;


        }
    }

    private boolean isAlreadyLoggedinWithFacebbok() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        return isLoggedIn;
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
        activityDashboardBinding.navigationDrawerHeaderInclude.tvUserName.setText("John Smith");

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
//        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        showBackButton(false, false);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.menu, this.getTheme());
        actionBarDrawerToggle.setHomeAsUpIndicator(drawable);
//        actionBar=getSupportActionBar();
//        actionBar.setHomeAsUpIndicator(drawable);
        actionBarDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (actionBarDrawerToggle.isDrawerIndicatorEnabled()) {
                    activityDashboardBinding.drawerLayout.openDrawer(GravityCompat.START);
                } else {
                    onBackPressed();
                }
//                if (activityDashboardBinding.drawerLayout.isDrawerVisible(GravityCompat.START)) {
//                    activityDashboardBinding.drawerLayout.closeDrawer(GravityCompat.START);
//                } else {
//                    activityDashboardBinding.drawerLayout.openDrawer(GravityCompat.START);
//                }
            }
        });

        //Setting the actionbarToggle to drawer layout
        activityDashboardBinding.drawerLayout.addDrawerListener(actionBarDrawerToggle);
        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard, menu);
        searchItem = menu.findItem(R.id.action_search);
        searchView =
                (SearchView) searchItem.getActionView();
        searchView.setQueryHint("What are you looking for?");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Bundle analyticsBundle = new Bundle();
                analyticsBundle.putString(FirebaseAnalytics.Param.SEARCH_TERM, query);
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH, analyticsBundle);
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                activityDashboardBinding.appBarMain.fragmentReplaceSearchView.setVisibility(View.VISIBLE);
                activityDashboardBinding.appBarMain.searchView.shimmerViewContainer.startShimmer();
                activityDashboardBinding.appBarMain.searchView.shimmerViewContainer.setVisibility(View.VISIBLE);

                dashboardViewModel.searchProducts(query).observe(DashboardActivity.this, searchProductListResponse -> {
                    activityDashboardBinding.appBarMain.searchView.shimmerViewContainer.stopShimmer();
                    activityDashboardBinding.appBarMain.searchView.shimmerViewContainer.setVisibility(View.GONE);
                    switch (searchProductListResponse.status) {
                        case SUCCESS:
                            productSearchList = searchProductListResponse.data.getData().getProductSearchs();
                            searchProductAdapter = new SearchProductAdapter(getApplicationContext(), productSearchList);
                            activityDashboardBinding.appBarMain.fragmentReplaceSearchView.setVisibility(View.VISIBLE);
                            activityDashboardBinding.appBarMain.searchView.recyclerSearchProducts.setAdapter(searchProductAdapter);
                            break;
                        case LOADING:
                            break;
                        case ERROR:
                            Toast.makeText(DashboardActivity.this, searchProductListResponse.message, Toast.LENGTH_SHORT).show();
                            break;
                    }
                });
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                int fragmentCount = getSupportFragmentManager().getBackStackEntryCount();
                for (int i = 0; i < fragmentCount; i++) {
                    getSupportFragmentManager().popBackStackImmediate();
                }
//                callHomeFragment();
                return true;
            case R.id.navigation_wishlist:
                WishListFragment wishlistFragment = new WishListFragment();
                replaceFragment(R.id.fragment_replacer, wishlistFragment, null, true, false);
                showBackButton(true, true);
                setTitle("Wishlist");
                return true;
            case R.id.navigation_categories:
                replaceFragment(R.id.framelayout_categories, new CategoryFragment(), null, true, false);
                return true;
            case R.id.navigation_cart:
                MyCartFragment myCartFragment = new MyCartFragment();
                replaceFragment(R.id.fragment_replacer, myCartFragment, null, true, false);
                showBackButton(true, false);
                setTitle("My Cart");
                return true;
            case R.id.navigation_profile:
                UserAccountFragment userAccountFragment = new UserAccountFragment();
                replaceFragment(R.id.fragment_replacer, userAccountFragment, null, true, false);
                showBackButton(true, true);
                setTitle("My Account");
                return true;
            default:
                return true;
        }

    }

    public void hideBottomNavigationBar() {
        activityDashboardBinding.appBarMain.bottomNavigationView.setVisibility(View.GONE);
    }

    public void showBottomNavigationBar() {
        activityDashboardBinding.appBarMain.bottomNavigationView.setVisibility(View.VISIBLE);
    }

    public void hideToolBarTitleShowLogo() {

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        activityDashboardBinding.appBarMain.imageSearsLogo.setVisibility(View.VISIBLE);
    }

    public void hideSearsLogo() {
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        activityDashboardBinding.appBarMain.imageSearsLogo.setVisibility(View.GONE);
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

    void setupToolbar() {
        setSupportActionBar(activityDashboardBinding.appBarMain.toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    public void showBackButton(boolean isBack, boolean showBottomBar) {
        try {
            if (isBack) {
                hideSearsLogo();
            }
            if (showBottomBar)
                showBottomNavigationBar();
            else
                hideBottomNavigationBar();

            actionBarDrawerToggle.setDrawerIndicatorEnabled(!isBack);
            getSupportActionBar().setDisplayHomeAsUpEnabled(isBack);
            actionBarDrawerToggle.syncState();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        hideToolBarTitleShowLogo();

        if (activityDashboardBinding.appBarMain.fragmentReplaceSearchView.getVisibility() == View.VISIBLE)
            activityDashboardBinding.appBarMain.fragmentReplaceSearchView.setVisibility(View.GONE);
        if (activityDashboardBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            activityDashboardBinding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            setTitle(getTitle());
            BaseFragment currentFragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_replacer);
            if (currentFragment instanceof WishListFragment) {
                showBackButton(false, true);
            } else if (currentFragment instanceof MyCartFragment) {
                showBackButton(false, true);
            } else if (currentFragment instanceof UserAccountFragment) {
                showBackButton(false, true);
            } else if (currentFragment instanceof SelectedProductDetailsFragment) {
                showBackButton(false, true);
            } else if (currentFragment instanceof PaymentFragment) {
                showBackButton(true, false);
            } else if (currentFragment instanceof CheckoutFragment) {
                showBackButton(true, false);
            } else if (currentFragment instanceof UserProfileFragment) {
                showBackButton(false, true);
            } else if (currentFragment instanceof OrderHistoryFragment) {
                showBackButton(false, true);
            }
            FragmentManager fm = getSupportFragmentManager();
//            BaseFragment categoryFragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.framelayout_categories);
            if (fm.getBackStackEntryCount() > 0) {
//                if (categoryFragment instanceof CategoryFragment)
//                    super.onBackPressed();
//                else
                fm.popBackStack();
            } else {
                super.onBackPressed();
            }

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
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
                activityDashboardBinding.appBarMain.viewPagerHomeTabs.setCurrentItem(tab.getPosition());
                Category selectedCategory = mainCategoryList.get(tab.getPosition());
                PreferenceHandler preferenceHandler = new PreferenceHandler(getBaseContext(), PreferenceHandler.TOKEN_LOGIN);
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

}
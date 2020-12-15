package com.hasawi.sears.ui.main.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.LayerDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.pojo.Category;
import com.hasawi.sears.data.api.model.pojo.NavigationMenuItem;
import com.hasawi.sears.data.api.model.pojo.ProductSearch;
import com.hasawi.sears.databinding.ActivityDashboardBinding;
import com.hasawi.sears.databinding.LayoutToastWishlistNotificationBinding;
import com.hasawi.sears.ui.base.BaseActivity;
import com.hasawi.sears.ui.base.BaseFragment;
import com.hasawi.sears.ui.main.adapters.HomeTabsPagerAdapter;
import com.hasawi.sears.ui.main.adapters.NavigationDrawerAdapter;
import com.hasawi.sears.ui.main.adapters.SearchProductAdapter;
import com.hasawi.sears.ui.main.listeners.RecyclerItemClickListener;
import com.hasawi.sears.ui.main.view.checkout.CheckoutFragment;
import com.hasawi.sears.ui.main.view.checkout.MyCartFragment;
import com.hasawi.sears.ui.main.view.checkout.PaymentFragment;
import com.hasawi.sears.ui.main.view.home.SelectedProductDetailsFragment;
import com.hasawi.sears.ui.main.view.home.UserAccountFragment;
import com.hasawi.sears.ui.main.view.home.WishListFragment;
import com.hasawi.sears.ui.main.view.navigation_drawer_menu.profile.UserProfileFragment;
import com.hasawi.sears.ui.main.view.signin.SigninActivity;
import com.hasawi.sears.ui.main.viewmodel.DashboardViewModel;
import com.hasawi.sears.utils.AppConstants;
import com.hasawi.sears.utils.PreferenceHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DashboardActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener, LocationListener {

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
    private Handler mHandler;
    private DashboardViewModel dashboardViewModel;
    private CharSequence mTitle;
    private List<ProductSearch> productSearchList;
    private SearchProductAdapter searchProductAdapter;
    private FirebaseAnalytics mFirebaseAnalytics;
    private LocationManager locationManager;
    private Location currentLocation;
    private ArrayList<Category> mainCategoryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDashboardBinding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        setSupportActionBar(activityDashboardBinding.appBarMain.toolbar);
        hideToolBarTitleShowLogo();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        getCurrentLocation();
        try {
            dataBundle = getIntent().getExtras();

        } catch (Exception e) {

        }
        getMainCategories();

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_DASHBOARD;
//            loadHomeFragment();
        }


        activityDashboardBinding.appBarMain.bottomNavigationView.setOnNavigationItemSelectedListener(this);
        dashboardViewModel.getCartedProducts().observe(this, contents -> {
            if (contents != null) {
                setCartCount(contents.size());
            }
        });
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

//        activityDashboardBinding.appBarMain.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // perform whatever you want on back arrow click
////                if(activityDashboardBinding.appBarMain.fragmentReplaceSearchView.getVisibility()==View.VISIBLE)
//                activityDashboardBinding.appBarMain.fragmentReplaceSearchView.setVisibility(View.GONE);
//            }
//        });

        PreferenceHandler preferenceHandler = new PreferenceHandler(this, PreferenceHandler.TOKEN_LOGIN);
        String username = preferenceHandler.getData(PreferenceHandler.LOGIN_USERNAME, "");
        boolean isUserLoggedin = preferenceHandler.getData(PreferenceHandler.LOGIN_STATUS, false);
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

    private boolean getCurrentLocation() {
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return true;
        }
        Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        }
        return false;
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
                closeDrawer();
                break;
//            case AppConstants.ID_MENU_ADDRESS_BOOK:
//                closeDrawer();
//                break;
//            case AppConstants.ID_MENU_BANK_DETAILS:
//                closeDrawer();
//                break;
            case AppConstants.ID_MENU_WISHLIST:
                if (menuItem.isEnabled()) {
                    WishListFragment wishListFragment = new WishListFragment();
                    replaceFragment(R.id.fragment_replacer, wishListFragment, null, true, false);
                    setTitle("Wishlist");
                    showBackButton(true, true);
                }
                closeDrawer();
                break;
//            case AppConstants.ID_MENU_RESET_PASSWORD:
//                closeDrawer();
//                break;
//            case AppConstants.ID_MENU_TRACK_ORDER:
//                closeDrawer();
//                break;
//            case AppConstants.ID_MENU_CUSTOMER_SERVICE:
//                closeDrawer();
//                break;
            case AppConstants.ID_MENU_SIGNOUT:
                if (menuItem.isEnabled()) {
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
                if (isNotificationEnabled) {
                    FirebaseMessaging.getInstance().subscribeToTopic(AppConstants.APP_NAME);
                    mFirebaseAnalytics.setUserProperty("notification_status", "enabled");

                } else {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(AppConstants.APP_NAME);
                    mFirebaseAnalytics.setUserProperty("notification_status", "disabled");
                }
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

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
        setCartCount(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);

//        MenuItem searchItem = menu.findItem(R.id.action_search);

        // Associate searchable configuration with the SearchView
//        SearchManager searchManager =
//                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchItem = menu.findItem(R.id.action_search);
        searchView =
                (SearchView) searchItem.getActionView();
        searchView.setQueryHint("What are you looking for?");


//        searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(getComponentName()));
//        SearchActivity searchActivity = new SearchActivity();
//        searchActivity.setSearchItemSelectedListener(this);

//        MenuItem searchViewItem = menu.findItem(R.id.action_search);
//        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
             /*   if(list.contains(query)){
                    adapter.getFilter().filter(query);
                }else{
                    Toast.makeText(MainActivity.this, "No Match found",Toast.LENGTH_LONG).show();
                }*/

                //use the query to search
//                Map<Object,Object> jsonParams=new ArrayMap<>();
//                jsonParams.put("q",query);


                return false;

            }

            @Override
            public boolean onQueryTextChange(String query) {
//                adapter.getFilter().filter(newText);
//                Bundle bundle = new Bundle();
//                bundle.putString("search_query", newText);
//                replaceFragment(R.id.fragment_replace_search_view, new SearchFragment(), bundle, true, false);
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

    public void setCartCount(int count) {
        cartCount += count;
        MenuItem itemCart = activityDashboardBinding.appBarMain.bottomNavigationView.getMenu().findItem(R.id.navigation_cart);
        LayerDrawable icon = (LayerDrawable) itemCart.getIcon();
//        setBadgeCount(this, icon, cartCount + "");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
//                Intent intent = new Intent(DashboardActivity.this, SelectUserDetailsActivity.class);
//                intent.putExtra("redirect_from_home", true);
//                startActivity(intent);
//                this.finish();
                return true;
            case R.id.navigation_wishlist:
                WishListFragment wishlistFragment = new WishListFragment();
                replaceFragment(R.id.fragment_replacer, wishlistFragment, null, true, false);
                showBackButton(true, true);
                setTitle("Wishlist");
                return true;
            case R.id.navigation_cart:
                MyCartFragment myCartFragment = new MyCartFragment();
                replaceFragment(R.id.fragment_replacer, myCartFragment, null, true, false);
                showBackButton(true, false);
                setTitle("My Cart");
//                Intent cartIntent = new Intent(DashboardActivity.this, MyCartActivity.class);
//                startActivity(cartIntent);
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

//    @Override
//    public void onBackPressed() {
////        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
////
////            getSupportFragmentManager().popBackStackImmediate();
////
////        } else {
////
////            super.onBackPressed();
////
////        }
//    }

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
            FragmentManager fm = getSupportFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                fm.popBackStack();
            } else {
                super.onBackPressed();
            }
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
            }


        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

//    @Override
//    public void searchItemSelected(String productId) {
//        Bundle bundle = new Bundle();
//        bundle.putString("product_object_id", productId);
//        bundle.putBoolean("is_search_query", true);
//        SelectedProductDetailsFragment selectedProductDetailsFragment = new SelectedProductDetailsFragment();
//        replaceFragment(R.id.fragment_replacer, selectedProductDetailsFragment, bundle, true, false);
//
//    }

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

    @Override
    public void onLocationChanged(@NonNull Location location) {
        currentLocation = location;
        int latitude = (int) (location.getLatitude());
        int longitude = (int) (location.getLongitude());
        mFirebaseAnalytics.setUserProperty("latitude", latitude + "");
        mFirebaseAnalytics.setUserProperty("longitude", longitude + "");
        try {

            Geocoder geo = new Geocoder(this.getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(latitude, longitude, 1);
            if (addresses.isEmpty()) {
                Log.e("Location", "Getting location name");
            } else {
                String locationName = addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName();
                mFirebaseAnalytics.setUserProperty("location", locationName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    private void getMainCategories() {
        dashboardViewModel.getMainCategories().observe(this, mainCategoryResponseResource -> {
            switch (mainCategoryResponseResource.status) {
                case SUCCESS:
                    mainCategoryList = (ArrayList<Category>) mainCategoryResponseResource.data.getMainCategories();
                    createDynamicHomeTabs(mainCategoryList);
                    break;
                case LOADING:
                    break;
                case ERROR:
                    Toast.makeText(this, mainCategoryResponseResource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
//            userPreferenceActivity.showProgressBar(false);
        });
    }

    private void createDynamicHomeTabs(ArrayList<Category> mainCategoryList) {
        for (int k = 0; k < 10; k++) {
            try {
                activityDashboardBinding.appBarMain.tabLayoutCategories.addTab(activityDashboardBinding.appBarMain.tabLayoutCategories.newTab().setText(mainCategoryList.get(k).getDescriptions().get(0).getCategoryName()));
            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        HomeTabsPagerAdapter adapter = new HomeTabsPagerAdapter
                (this, getSupportFragmentManager(), activityDashboardBinding.appBarMain.tabLayoutCategories.getTabCount(), mainCategoryList);
        activityDashboardBinding.appBarMain.viewPagerHomeTabs.setAdapter(adapter);
        activityDashboardBinding.appBarMain.viewPagerHomeTabs.setOffscreenPageLimit(1);
        activityDashboardBinding.appBarMain.viewPagerHomeTabs.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(activityDashboardBinding.appBarMain.tabLayoutCategories));
//Bonus Code : If your tab layout has more than 2 tabs then tab will scroll other wise they will take whole width of the screen
        if (activityDashboardBinding.appBarMain.tabLayoutCategories.getTabCount() <= 5) {
            activityDashboardBinding.appBarMain.tabLayoutCategories.setTabMode(TabLayout.MODE_FIXED);
        } else {
            activityDashboardBinding.appBarMain.tabLayoutCategories.setTabMode(TabLayout.MODE_SCROLLABLE);
        }
    }


//    private void createDynamicHomeTabs(ArrayList<Category> mainCategoryList) {
//        for (int k = 0; k < mainCategoryList.size(); k++) {
//            activityDashboardBinding.appBarMain.tabLayoutCategories.addTab(activityDashboardBinding.appBarMain.tabLayoutCategories.newTab().setText(mainCategoryList.get(k).getDescriptions().get(0).getCategoryName()));
//        }
////        activityDashboardBinding.appBarMain.tabLayoutCategories.setTabTextColors(R.color.txt_clr_blue,R.color.txt_clr_blue);
//        HomeTabsPagerAdapter adapter = new HomeTabsPagerAdapter
//                (this,getSupportFragmentManager(), activityDashboardBinding.appBarMain.tabLayoutCategories.getTabCount(),mainCategoryList);
//        activityDashboardBinding.appBarMain.viewPagerHomeTabs.setAdapter(adapter);
//        activityDashboardBinding.appBarMain.viewPagerHomeTabs.setOffscreenPageLimit(1);
//        activityDashboardBinding.appBarMain.tabLayoutCategories.setupWithViewPager(activityDashboardBinding.appBarMain.viewPagerHomeTabs);
////        activityDashboardBinding.appBarMain.viewPagerHomeTabs.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
////            @Override
////            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
////
////            }
////
////            @Override
////            public void onPageSelected(int position) {
//////                 Category selectedCategory=mainCategoryList.get(position);
//////                 PreferenceHandler preferenceHandler=new PreferenceHandler(getBaseContext(),PreferenceHandler.TOKEN_LOGIN);
//////                 preferenceHandler.saveData(PreferenceHandler.LOGIN_CATEGORY_ID,selectedCategory.getCategoryId());
//////                 preferenceHandler.saveData(PreferenceHandler.LOGIN_CATEGORY_NAME,selectedCategory.getDescriptions().get(0).getCategoryName());
////
////            }
////
////            @Override
////            public void onPageScrollStateChanged(int state) {
////
////            }
////        });
//        activityDashboardBinding.appBarMain.viewPagerHomeTabs.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(activityDashboardBinding.appBarMain.tabLayoutCategories));
////Bonus Code : If your tab layout has more than 2 tabs then tab will scroll other wise they will take whole width of the screen
//        if (activityDashboardBinding.appBarMain.tabLayoutCategories.getTabCount() == 2) {
//            activityDashboardBinding.appBarMain.tabLayoutCategories.setTabMode(TabLayout.MODE_FIXED);
//        } else {
//            activityDashboardBinding.appBarMain.tabLayoutCategories.setTabMode(TabLayout.MODE_SCROLLABLE);
//        }
//    }
}
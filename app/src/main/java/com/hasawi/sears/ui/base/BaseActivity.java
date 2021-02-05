package com.hasawi.sears.ui.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.hasawi.sears.R;
import com.hasawi.sears.utils.LoadingIndicator;
import com.hasawi.sears.utils.dialogs.ProgressBarDialog;

import java.util.ArrayList;

import static com.hasawi.sears.utils.AppConstants.SEARS_EMAIL;
import static com.hasawi.sears.utils.AppConstants.SEARS_PHONE;

public abstract class BaseActivity extends AppCompatActivity {

    static boolean isAppRunning = true;
    private static boolean isAppStopped = false;
    protected LayoutInflater inflater;
    //    LoadingIndicator loadingIndicator;
    ProgressBarDialog progressBarDialog;
    private InputMethodManager inputMethodManager;
    private boolean isForceUpdate = false;
    private LoadingIndicator loadingIndicator;
    private ProgressDialog progressBar;

    public static void hideSoftKeyboard(Activity activity) {
        try {
            if (activity != null) {
                final InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                if (inputMethodManager.isActive()) {
                    if (activity.getCurrentFocus() != null) {
                        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static boolean isIsAppStopped() {
        return isAppStopped;
    }

    @Override
    public void onPause() {
        super.onPause();
        isAppRunning = false;
        isAppStopped = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        isAppRunning = true;
        isAppStopped = false;

    }


    @Override
    public void onStop() {
        super.onStop();
        isAppStopped = true;
        isAppRunning = false;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isAppStopped = true;
        isAppRunning = false;
    }

    @Override
    public void onBackPressed() {
        if (!isForceUpdate) {
            super.onBackPressed();
        }
    }

    /*Method called  To hide keyboard*/
    public boolean hideSoftKeyboard() {
        try {
            if (getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /*Method called  To check Internet Connection */
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null
                && activeNetworkInfo.isConnectedOrConnecting();
    }

    /**
     * This method is used to replace fragment .
     *
     * @param newFragment :replace an existing fragment with new fragment.
     * @param args        :pass bundle data from one fragment to another fragment
     */
    public void replaceFragment(int frame_layout, Fragment newFragment, Bundle args, boolean toBeAddedInStack, boolean clearAllHistory) {
        replaceFragment(frame_layout, newFragment, args, toBeAddedInStack, clearAllHistory, R.anim.slide_in_up, R.anim.slide_out_up, null);
    }

    public void replaceFragment(int frame_layout, Fragment newFragment, Bundle args, boolean toBeAddedInStack, boolean clearAllHistory, int enter, int exit) {
        replaceFragment(frame_layout, newFragment, args, toBeAddedInStack, clearAllHistory, enter, exit, null);
    }

    public void replaceFragment(int frame_layout, Fragment newFragment, Bundle args, boolean toBeAddedInStack, boolean clearAllHistory, int enter, int exit, ArrayList<View> viewList) {
        hideSoftKeyboard(newFragment.getActivity());
        FragmentManager manager = getSupportFragmentManager();
//        manager.popBackStack();
        if (clearAllHistory) {
            if (manager.getBackStackEntryCount() != 0) {
                FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
                manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }
        if (args != null)
            newFragment.setArguments(args);
        FragmentTransaction transaction = manager.beginTransaction();
        if (viewList != null) {
            for (int i = 0; i < viewList.size(); i++) {
                transaction.addSharedElement(viewList.get(i), "name" + viewList.get(i));
            }
        }
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
//        if (newFragment instanceof SearchPickupFragment || newFragment instanceof SearchDropFragment)
        transaction.setCustomAnimations(enter, exit, enter, exit);
        transaction.replace(frame_layout, newFragment);
        if (toBeAddedInStack)
            transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

    }

    public void popBackStack() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() != 0) {
            manager.popBackStack();
        }
    }

    public Animation inFromBottomAnimation(long durationMillis) {

        Animation inFromBottom = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, +1.0f, Animation.RELATIVE_TO_PARENT, 0.0f
        );
        inFromBottom.setDuration(durationMillis);
        return inFromBottom;
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
    }

    public Activity getActivity(Context context) {
        if (context == null) {
            return null;
        } else if (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            } else {
                return getActivity(((ContextWrapper) context).getBaseContext());
            }
        }

        return null;
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //todo clean onCreate on every activity - ie oncreate should not be override....
        isAppRunning = true;
        isAppStopped = false;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        float scale = 1;
        adjustFontScale(getResources().getConfiguration(), scale);
//        if (loadingIndicator == null)
//            loadingIndicator = new LoadingIndicator();


    }

    // To change size of font in settings according to the convenience of the user. This function determines the scaling
    public void adjustFontScale(Configuration configuration, float scale) {

        float systemScale = Settings.System.getFloat(getContentResolver(), Settings.System.FONT_SCALE, 1f);
        configuration.fontScale = scale * systemScale;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;
        getBaseContext().getResources().updateConfiguration(configuration, metrics);

    }

    public boolean isAlreadyLoggedinWithFacebbok() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        return isLoggedIn;
    }


    public void disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();

            }
        }).executeAsync();
    }

//    public void showProgressBarDialog() {

//        if (loadingIndicator == null)
//            loadingIndicator = new LoadingIndicator();
//        loadingIndicator.showDialog(this, null);
//        progressBarDialog = ProgressBarDialog.newInstance();
//            progressBarDialog.show(getSupportFragmentManager(), "progress");
//    }

    //    public void hideProgressBarDialog() {
//        try {
////            loadingIndicator.hideDialog();
//            Fragment prev = getSupportFragmentManager().findFragmentByTag("progress");
//            if (prev != null) {
//                DialogFragment df = (DialogFragment) prev;
//                df.dismiss();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    public void sendEmailToCustomerCare() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{SEARS_EMAIL});
//        i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
//        i.putExtra(Intent.EXTRA_TEXT   , "body of email");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void callSearsCustomerCare() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + SEARS_PHONE));
        startActivity(callIntent);
    }
}

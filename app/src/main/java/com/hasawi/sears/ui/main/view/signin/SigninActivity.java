package com.hasawi.sears.ui.main.view.signin;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.hasawi.sears.R;
import com.hasawi.sears.databinding.ActivitySigninBinding;
import com.hasawi.sears.ui.base.BaseActivity;
import com.hasawi.sears.ui.main.view.signin.login.LoginFragment;

public class SigninActivity extends BaseActivity {

    ActivitySigninBinding activitySigninBinding;
    private GoogleSignInClient mGoogleSignInClient;
    FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySigninBinding = DataBindingUtil.setContentView(this, R.layout.activity_signin);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        LoginFragment loginFragment = new LoginFragment();
        replaceFragment(loginFragment, null);
    }

    public GoogleSignInAccount checksForAnExistingUser() {
        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        return account;
    }

    public GoogleSignInClient getmGoogleSignInClient() {
        return mGoogleSignInClient;
    }

    public void replaceFragment(Fragment fragment, Bundle bundle) {
        replaceFragment(R.id.fragment_replacer_signin, fragment, bundle, true, false);
    }

    public void showProgressBar(boolean shouldShow) {
        if (shouldShow)
            activitySigninBinding.progressBar.setVisibility(View.VISIBLE);
        else
            activitySigninBinding.progressBar.setVisibility(View.GONE);
    }

    public void hideFragment() {
        activitySigninBinding.fragmentReplacerSignin.setVisibility(View.GONE);
    }

    public FirebaseAnalytics getmFirebaseAnalytics() {
        return mFirebaseAnalytics;
    }
}
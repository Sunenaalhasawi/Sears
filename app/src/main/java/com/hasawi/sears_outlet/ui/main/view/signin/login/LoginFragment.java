package com.hasawi.sears_outlet.ui.main.view.signin.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.hasawi.sears_outlet.R;
import com.hasawi.sears_outlet.databinding.FragmentLoginBinding;
import com.hasawi.sears_outlet.ui.base.BaseFragment;
import com.hasawi.sears_outlet.ui.main.view.DashboardActivity;
import com.hasawi.sears_outlet.ui.main.view.signin.SigninActivity;
import com.hasawi.sears_outlet.ui.main.view.signin.signup.SignupFragment;
import com.hasawi.sears_outlet.ui.main.viewmodel.LoginViewModel;
import com.hasawi.sears_outlet.utils.PreferenceHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Map;

public class LoginFragment extends BaseFragment implements View.OnClickListener {

    private static final int GOOGLE_SIGN_IN = 100;
    private static final String TAG = "SIGN IN ACTIVITY";
    SigninActivity signinActivity;
    private LoginViewModel loginViewModel;
    private FragmentLoginBinding fragmentLoginBinding;
    CallbackManager callbackManager;
    AccessToken facebookAccessToken;
    GraphRequest facebookGraphRequest;
    FacebookCallback<LoginResult> facebookCallback;
    private AccessTokenTracker accessTokenTracker;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void setup() {
        fragmentLoginBinding = (FragmentLoginBinding) viewDataBinding;
        signinActivity = (SigninActivity) getActivity();
        loginViewModel = new ViewModelProvider(getActivity()).get(LoginViewModel.class);
        fragmentLoginBinding.btnLogin.setOnClickListener(this);
        fragmentLoginBinding.imageViewGoogle.setOnClickListener(this);
        fragmentLoginBinding.imageViewFacebook.setOnClickListener(this);
        fragmentLoginBinding.btnSignup.setOnClickListener(this);
        fragmentLoginBinding.tvForgotPswd.setOnClickListener(this);

        //Facbook Signin
        FacebookSdk.sdkInitialize(getActivity());
        callbackManager = CallbackManager.Factory.create();
        FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        facebookCallback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                // Application code
                                try {
                                    Log.d("tttttt", object.getString("id"));
                                    String birthday = "";
                                    if (object.has("birthday")) {
                                        birthday = object.getString("birthday"); // 01/31/1980 format
                                    }

                                    String first_name = object.getString("first_name");
                                    String last_name = object.getString("last_name");
                                    String mail = object.getString("email");
                                    String gender = object.getString("gender");
                                    String fid = object.getString("id");
                                    String picture = "https://graph.facebook.com/" + fid + "/picture?type=large";

                                    userRegistration(first_name, last_name, mail);
//                                    tvdetails.setText("Name: "+fnm+" "+lnm+" \n"+"Email: "+mail+" \n"+"Gender: "+gender+" \n"+"ID: "+fid+" \n"+"Birth Date: "+birthday);
//                                    aQuery.id(ivpic).image("https://graph.facebook.com/" + fid + "/picture?type=large");
//                                    https://graph.facebook.com/143990709444026/picture?type=large
                                    Log.d("aswwww", "https://graph.facebook.com/" + fid + "/picture?type=large");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email, gender, birthday, location");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        };
        LoginManager.getInstance().registerCallback(callbackManager, facebookCallback);
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageViewGoogle:
                Intent signInIntent = signinActivity.getmGoogleSignInClient().getSignInIntent();
                startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
                break;
            case R.id.imageViewFacebook:
                if (signinActivity.isAlreadyLoggedinWithFacebbok()) {

                } else
                    facebookSignin();
                break;
            case R.id.btn_login:
                hideSoftKeyboard(signinActivity);
                userAuthentication();
                break;
            case R.id.btn_signup:
                signinActivity.showFragment();
                signinActivity.replaceFragment(new SignupFragment(), null);
                break;
            case R.id.tv_forgot_pswd:
                signinActivity.replaceFragment(new ForgotPasswordFragment(), null);
                break;
            default:
                break;


        }
    }

    private void userAuthentication() {
        signinActivity.showProgressBar(true);
        String username = fragmentLoginBinding.edtEmail.getText().toString().trim();
        String password = fragmentLoginBinding.edtPassword.getText().toString().trim();
        if (!username.equals("") && !password.equals("")) {
            Map<String, Object> jsonParams = new ArrayMap<>();
            jsonParams.put("emailId", username);
            jsonParams.put("password", password);
            loginViewModel.userLogin(jsonParams).observe(getActivity(), loginResponse -> {
                signinActivity.showProgressBar(false);
                switch (loginResponse.status) {
                    case SUCCESS:
                        if (loginResponse != null && loginResponse.data.getStatusCode() == 200) {
                            Toast.makeText(signinActivity, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
                            preferenceHandler.saveData(PreferenceHandler.LOGIN_TOKEN, loginResponse.data.getData().getToken());
                            preferenceHandler.saveData(PreferenceHandler.LOGIN_USER_ID, loginResponse.data.getData().getCustomer().getCustomerId());
                            preferenceHandler.saveData(PreferenceHandler.LOGIN_USERNAME, loginResponse.data.getData().getCustomer().getCustomerFirstName());
                            preferenceHandler.saveData(PreferenceHandler.LOGIN_EMAIL, loginResponse.data.getData().getCustomer().getEmailId());
                            preferenceHandler.saveData(PreferenceHandler.LOGIN_GENDER, loginResponse.data.getData().getCustomer().getGender());
                            preferenceHandler.saveData(PreferenceHandler.LOGIN_PHONENUMBER, loginResponse.data.getData().getCustomer().getMobileNo());
                            preferenceHandler.saveData(PreferenceHandler.LOGIN_PASSWORD, loginResponse.data.getData().getCustomer().getPassword());
                            preferenceHandler.saveData(PreferenceHandler.LOGIN_CONFIRM_PASSWORD, loginResponse.data.getData().getCustomer().getConfirmPassword());
                            preferenceHandler.saveData(PreferenceHandler.LOGIN_STATUS, true);
                            redirectToHomePage();

                        } else if (loginResponse.data.getStatusCode() == 400) {
                            Toast.makeText(signinActivity, "Incorrect email or password", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case ERROR:
                        Toast.makeText(getActivity(), loginResponse.message, Toast.LENGTH_SHORT).show();
                        break;
                }

            });
        } else {
            Toast.makeText(getContext(), "Incorrect username or password", Toast.LENGTH_LONG).show();
        }

    }

    private void redirectToHomePage() {
        signinActivity.hideFragment();
        Intent intent = new Intent(signinActivity, DashboardActivity.class);
        startActivity(intent);
        signinActivity.finish();
    }

    @Override
    public void onActivityResult(int resultCode, int requestCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == GOOGLE_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGoogleSignInResult(task);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);

        }
    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            redirectToHomePage();
            String email = account.getEmail();
            String name = account.getGivenName();
            String lastName = account.getFamilyName();
            String id = account.getId();
            userRegistration(name, lastName, email);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(signinActivity, "Signin Failed. Please Try Again", Toast.LENGTH_SHORT).show();
//            updateUI(null);
        }
    }

    private void facebookSignin() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile", "user_birthday"));
    }


    private void userRegistration(String firstName, String lastName, String email) {

        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("customerFirstName", firstName);
        jsonParams.put("customerLastName", lastName);
//                jsonParams.put("mobileNo", phone);
        jsonParams.put("active", true);
        jsonParams.put("emailId", email);
//                jsonParams.put("gender", selectedGender);
//                jsonParams.put("nationality", selectedNationality);
//                jsonParams.put("dob", selectedDate);
        signinActivity.showProgressBar(true);
        loginViewModel.userRegistration(jsonParams).observe(getActivity(), signupResponse -> {
            signinActivity.showProgressBar(false);
            switch (signupResponse.status) {
                case SUCCESS:
                    PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
                    preferenceHandler.saveData(PreferenceHandler.LOGIN_TOKEN, signupResponse.data.getData().getToken());
                    preferenceHandler.saveData(PreferenceHandler.LOGIN_USER_ID, signupResponse.data.getData().getuser().getCustomerId());
                    preferenceHandler.saveData(PreferenceHandler.LOGIN_USERNAME, signupResponse.data.getData().getuser().getCustomerFirstName());
                    preferenceHandler.saveData(PreferenceHandler.LOGIN_EMAIL, signupResponse.data.getData().getuser().getEmailId());
                    preferenceHandler.saveData(PreferenceHandler.LOGIN_PASSWORD, signupResponse.data.getData().getuser().getPassword());
                    preferenceHandler.saveData(PreferenceHandler.LOGIN_CONFIRM_PASSWORD, signupResponse.data.getData().getuser().getConfirmPassword());
                    preferenceHandler.saveData(PreferenceHandler.LOGIN_PHONENUMBER, signupResponse.data.getData().getuser().getMobileNo());
                    preferenceHandler.saveData(PreferenceHandler.LOGIN_STATUS, true);

                    signinActivity.getmFirebaseAnalytics().setUserProperty("email", signupResponse.data.getData().getuser().getEmailId());
                    signinActivity.getmFirebaseAnalytics().setUserProperty("country", signupResponse.data.getData().getuser().getNationality());
                    signinActivity.getmFirebaseAnalytics().setUserProperty("gender", signupResponse.data.getData().getuser().getGender());
                    signinActivity.getmFirebaseAnalytics().setUserProperty("date_of_birth", signupResponse.data.getData().getuser().getDob().toString());
                    signinActivity.getmFirebaseAnalytics().setUserProperty("phone", signupResponse.data.getData().getuser().getMobileNo());

                    Intent intent = new Intent(signinActivity, DashboardActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                    break;
                case LOADING:
                    break;
                case ERROR:
                    Toast.makeText(signinActivity, signupResponse.message, Toast.LENGTH_SHORT).show();
                    break;
            }
        });

    }

}




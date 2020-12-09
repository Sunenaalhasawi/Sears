package com.hasawi.sears.ui.main.view.signin.login;

import android.content.Intent;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.hasawi.sears.R;
import com.hasawi.sears.databinding.FragmentLoginBinding;
import com.hasawi.sears.ui.base.BaseFragment;
import com.hasawi.sears.ui.main.view.DashboardActivity;
import com.hasawi.sears.ui.main.view.signin.SigninActivity;
import com.hasawi.sears.ui.main.view.signin.signup.SignupFragment;
import com.hasawi.sears.ui.main.viewmodel.LoginViewModel;
import com.hasawi.sears.utils.PreferenceHandler;

import java.util.Map;

public class LoginFragment extends BaseFragment implements View.OnClickListener {

    private static final int RC_SIGN_IN = 100;
    private static final String TAG = "SIGN IN ACTIVITY";
    SigninActivity signinActivity;
    private LoginViewModel loginViewModel;
    private FragmentLoginBinding fragmentLoginBinding;

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageViewGoogle:
                Intent signInIntent = signinActivity.getmGoogleSignInClient().getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
            case R.id.imageViewFacebook:
                //Todo facebook integration here
                break;
            case R.id.btn_login:
                userAuthentication();
            case R.id.btn_signup:
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
        String username = fragmentLoginBinding.edtEmail.getText().toString();
        String password = fragmentLoginBinding.edtPassword.getText().toString();
        if (!username.equals("") && !password.equals("")) {
            Map<String, Object> jsonParams = new ArrayMap<>();
            jsonParams.put("emailId", username);
            jsonParams.put("password", password);
            loginViewModel.userLogin(jsonParams).observe(getActivity(), loginResponse -> {
                signinActivity.showProgressBar(false);
                switch (loginResponse.status) {
                    case SUCCESS:
                        if (loginResponse != null) {
                            Toast.makeText(signinActivity, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
                            preferenceHandler.saveData(PreferenceHandler.LOGIN_TOKEN, loginResponse.data.getData().getToken());
                            preferenceHandler.saveData(PreferenceHandler.LOGIN_USER_ID, loginResponse.data.getData().getCustomer().getCustomerId());
                            preferenceHandler.saveData(PreferenceHandler.LOGIN_USERNAME, loginResponse.data.getData().getCustomer().getCustomerFirstName());
                            preferenceHandler.saveData(PreferenceHandler.LOGIN_EMAIL, loginResponse.data.getData().getCustomer().getEmailId());
                            preferenceHandler.saveData(PreferenceHandler.LOGIN_PHONENUMBER, loginResponse.data.getData().getCustomer().getMobileNo());
                            preferenceHandler.saveData(PreferenceHandler.LOGIN_PASSWORD, loginResponse.data.getData().getCustomer().getPassword());
                            preferenceHandler.saveData(PreferenceHandler.LOGIN_CONFIRM_PASSWORD, loginResponse.data.getData().getCustomer().getConfirmPassword());
                            preferenceHandler.saveData(PreferenceHandler.LOGIN_STATUS, true);
                            redirectToHomePage();

                        }
                        break;
                    case LOADING:
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            redirectToHomePage();
            String email = account.getEmail();
            String name = account.getGivenName();
            String id = account.getId();
            PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
            preferenceHandler.saveData(PreferenceHandler.LOGIN_USERNAME, name);
            preferenceHandler.saveData(PreferenceHandler.LOGIN_EMAIL, email);
            preferenceHandler.saveData(PreferenceHandler.LOGIN_STATUS, true);

            // Signed in successfully, show authenticated UI.
//            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(signinActivity, "Signin Failed. Please Try Again", Toast.LENGTH_SHORT).show();
//            updateUI(null);
        }
    }
}
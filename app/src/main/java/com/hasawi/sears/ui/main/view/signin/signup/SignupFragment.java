package com.hasawi.sears.ui.main.view.signin.signup;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.util.ArrayMap;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.hasawi.sears.R;
import com.hasawi.sears.databinding.FragmentSignupBinding;
import com.hasawi.sears.ui.base.BaseFragment;
import com.hasawi.sears.ui.main.view.DashboardActivity;
import com.hasawi.sears.ui.main.view.signin.SigninActivity;
import com.hasawi.sears.ui.main.viewmodel.SignupViewModel;
import com.hasawi.sears.utils.PreferenceHandler;
import com.rilixtech.widget.countrycodepicker.Country;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.Calendar;
import java.util.Map;

public class SignupFragment extends BaseFragment implements View.OnClickListener {

    FragmentSignupBinding fragmentSignupBinding;
    SigninActivity signinActivity;
    String selectedCountryCode = "", selectedNationality = "";
    SignupViewModel signupViewModel;
    String selectedDate = "";
    private String selectedGender = "M";
    private DatePickerDialog datePickerDialog;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_signup;
    }

    @Override
    protected void setup() {
        fragmentSignupBinding = (FragmentSignupBinding) viewDataBinding;
        signinActivity = (SigninActivity) getActivity();
        signupViewModel = new ViewModelProvider(getActivity()).get(SignupViewModel.class);
        fragmentSignupBinding.btnSignup.setOnClickListener(this);
        fragmentSignupBinding.layoutSignup.edtBirthday.setOnClickListener(this);
        fragmentSignupBinding.layoutSignup.constraintLayoutMale.setOnClickListener(this);
        fragmentSignupBinding.layoutSignup.constraintLayoutFemale.setOnClickListener(this);

        fragmentSignupBinding.layoutSignup.pickerCountryCode.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                selectedCountryCode = selectedCountry.getPhoneCode();
            }
        });
        fragmentSignupBinding.layoutSignup.pickerNationality.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                selectedNationality = selectedCountry.getName();
            }
        });

    }

    private void userRegistration() {

        String firstName = fragmentSignupBinding.layoutSignup.edtFirstname.getText().toString();
        String lastName = fragmentSignupBinding.layoutSignup.edtLastname.getText().toString();
        String email = fragmentSignupBinding.layoutSignup.edtEmail.getText().toString();
        String phone = fragmentSignupBinding.layoutSignup.edtMobile.getText().toString();
        String password = fragmentSignupBinding.layoutSignup.edtPassword.getText().toString();
        String dob = fragmentSignupBinding.layoutSignup.edtBirthday.getText().toString();
        String confirmPassword = fragmentSignupBinding.layoutSignup.edtConfirmPassword.getText().toString();

        if (firstName.equals(""))
            fragmentSignupBinding.layoutSignup.tvFirstNameError.setVisibility(View.VISIBLE);
        if (lastName.equals(""))
            fragmentSignupBinding.layoutSignup.tvLastNameError.setVisibility(View.VISIBLE);
        else if (email.equals(""))
            fragmentSignupBinding.layoutSignup.tvEmailError.setVisibility(View.VISIBLE);
        else if (phone.equals(""))
            fragmentSignupBinding.layoutSignup.tvMobileError.setVisibility(View.VISIBLE);
        else if (password.equals(""))
            fragmentSignupBinding.layoutSignup.tvPasswordError.setVisibility(View.VISIBLE);
        else if (confirmPassword.equals(""))
            fragmentSignupBinding.layoutSignup.tvConfirmPasswordError.setVisibility(View.VISIBLE);
        else {
            if (password.equals(confirmPassword)) {
                Map<String, Object> jsonParams = new ArrayMap<>();
                jsonParams.put("customerFirstName", firstName);
                jsonParams.put("customerLastName", lastName);
                jsonParams.put("mobileNo", phone);
                jsonParams.put("password", password);
                jsonParams.put("confirmPassword", confirmPassword);
                jsonParams.put("active", true);
                jsonParams.put("emailId", email);
                jsonParams.put("gender", selectedGender);
                jsonParams.put("nationality", selectedNationality);
                jsonParams.put("dob", selectedDate);
                signinActivity.showProgressBar(true);
                signupViewModel.userRegistration(jsonParams).observe(getActivity(), signupResponse -> {
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

            } else {
                Toast.makeText(signinActivity, "Password did not match", Toast.LENGTH_SHORT).show();
            }
        }


    }

    private void selectMale() {
        fragmentSignupBinding.layoutSignup.constraintLayoutMale.setBackground(getActivity().getResources().getDrawable(R.drawable.blue_rounded_rectangle_12dp_corner));
        fragmentSignupBinding.layoutSignup.tvMale.setTextColor(getResources().getColor(R.color.white));
        fragmentSignupBinding.layoutSignup.constraintLayoutFemale.setBackground(getActivity().getResources().getDrawable(R.drawable.blue_outlined_rounded_rectangle_12dp));
        fragmentSignupBinding.layoutSignup.txtFemale.setTextColor(getResources().getColor(R.color.txt_clr_blue));
        selectedGender = "M";
    }

    private void selectFemale() {
        fragmentSignupBinding.layoutSignup.constraintLayoutFemale.setBackground(getActivity().getResources().getDrawable(R.drawable.blue_rounded_rectangle_12dp_corner));
        fragmentSignupBinding.layoutSignup.txtFemale.setTextColor(getResources().getColor(R.color.white));
        fragmentSignupBinding.layoutSignup.constraintLayoutMale.setBackground(getActivity().getResources().getDrawable(R.drawable.blue_outlined_rounded_rectangle_12dp));
        fragmentSignupBinding.layoutSignup.tvMale.setTextColor(getResources().getColor(R.color.txt_clr_blue));
        selectedGender = "F";
    }

    private void showDatePickerDialog() {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        selectedDate = year + "-" + monthOfYear + "-" + dayOfMonth;
                        fragmentSignupBinding.layoutSignup.edtBirthday.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

//    @Override
//    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//        // Create a Calender instance
//
//        Calendar mCalender = Calendar.getInstance();
//
//        // Set static variables of Calender instance
//
//        mCalender.set(Calendar.YEAR, year);
//
//        mCalender.set(Calendar.MONTH, month);
//
//        mCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//
//        // Get the date in form of string
//
//        String selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(mCalender.getTime());
//
//        // Set the textview to the selectedDate String
//
//        fragmentSignupBinding.layoutSignup.edtBirthday.setText(selectedDate);
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edt_birthday:
                showDatePickerDialog();
                break;
            case R.id.btn_signup:
                userRegistration();
                break;
            case R.id.constraintLayoutMale:
                selectMale();
                break;
            case R.id.constraintLayoutFemale:
                selectFemale();
                break;
            default:
                break;
        }
    }
}

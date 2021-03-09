package com.hasawi.sears_outlet.ui.main.view.signin.signup;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.facebook.appevents.AppEventsLogger;
import com.hasawi.sears_outlet.R;
import com.hasawi.sears_outlet.databinding.FragmentSignupBinding;
import com.hasawi.sears_outlet.ui.base.BaseFragment;
import com.hasawi.sears_outlet.ui.main.view.signin.SigninActivity;
import com.hasawi.sears_outlet.ui.main.viewmodel.SignupViewModel;
import com.hasawi.sears_outlet.utils.DateTimeUtils;
import com.hasawi.sears_outlet.utils.PreferenceHandler;
import com.hasawi.sears_outlet.utils.dialogs.GeneralDialog;
import com.hasawi.sears_outlet.utils.dialogs.RegistrationSuccessDialog;
import com.rilixtech.widget.countrycodepicker.Country;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.Map;

public class SignupFragment extends BaseFragment implements View.OnClickListener {

    FragmentSignupBinding fragmentSignupBinding;
    SigninActivity signinActivity;
    String selectedCountryCode = "", selectedNationality = "";
    SignupViewModel signupViewModel;
    String selectedDate = "";
    private String selectedGender = "M";
    private DatePickerDialog datePickerDialog;
    AppEventsLogger logger;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_signup;
    }

    @Override
    protected void setup() {
        fragmentSignupBinding = (FragmentSignupBinding) viewDataBinding;
        signinActivity = (SigninActivity) getActivity();
        logger = AppEventsLogger.newLogger(signinActivity);
        signupViewModel = new ViewModelProvider(getActivity()).get(SignupViewModel.class);
        fragmentSignupBinding.layoutSignup.btnSignup.setOnClickListener(this);
//        fragmentSignupBinding.layoutSignup.edtBirthday.setOnClickListener(this);
        fragmentSignupBinding.layoutSignup.constraintLayoutMale.setOnClickListener(this);
        fragmentSignupBinding.layoutSignup.constraintLayoutFemale.setOnClickListener(this);
        fragmentSignupBinding.cvBackground.setOnClickListener(this);

        fragmentSignupBinding.layoutSignup.pickerCountryCode.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                selectedCountryCode = selectedCountry.getPhoneCode();
            }
        });
//        fragmentSignupBinding.layoutSignup.pickerNationality.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
//            @Override
//            public void onCountrySelected(Country selectedCountry) {
////                selectedNationality = selectedCountry.getName();
//            }
//        });

    }

    private void userRegistration() {


        String firstName = fragmentSignupBinding.layoutSignup.edtFirstname.getText().toString().trim();
        String lastName = fragmentSignupBinding.layoutSignup.edtLastname.getText().toString().trim();
        String email = fragmentSignupBinding.layoutSignup.edtEmail.getText().toString().trim();
        String phone = fragmentSignupBinding.layoutSignup.edtMobile.getText().toString().trim();
        String password = fragmentSignupBinding.layoutSignup.edtPassword.getText().toString().trim();
//        String dob = fragmentSignupBinding.layoutSignup.edtBirthday.getText().toString().trim();
        String confirmPassword = fragmentSignupBinding.layoutSignup.edtConfirmPassword.getText().toString().trim();

        if (firstName.equals(""))
            fragmentSignupBinding.layoutSignup.tvFirstNameError.setVisibility(View.VISIBLE);
        if (lastName.equals(""))
            fragmentSignupBinding.layoutSignup.tvLastNameError.setVisibility(View.VISIBLE);
        if (email.equals(""))
            fragmentSignupBinding.layoutSignup.tvEmailError.setVisibility(View.VISIBLE);
        if (phone.equals(""))
            fragmentSignupBinding.layoutSignup.tvMobileError.setVisibility(View.VISIBLE);
        if (password.equals(""))
            fragmentSignupBinding.layoutSignup.tvPasswordError.setVisibility(View.VISIBLE);
        if (confirmPassword.equals(""))
            fragmentSignupBinding.layoutSignup.tvConfirmPasswordError.setVisibility(View.VISIBLE);
        if (!firstName.equals("") && !lastName.equals("") && !email.equals("") && !phone.equals("") && !password.equals("") && !confirmPassword.equals(""))
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
                jsonParams.put("nationality", "");
                jsonParams.put("dob", "");
                signinActivity.showProgressBar(true);
                signupViewModel.userRegistration(jsonParams).observe(getActivity(), signupResponse -> {
                    signinActivity.showProgressBar(false);
                    switch (signupResponse.status) {
                        case SUCCESS:
                            PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
                            try {
                                if (signupResponse.data.getData().getToken() != null)
                                    preferenceHandler.saveData(PreferenceHandler.LOGIN_TOKEN, signupResponse.data.getData().getToken());
                                if (signupResponse.data.getData().getuser() != null) {
                                    preferenceHandler.saveData(PreferenceHandler.LOGIN_USER_ID, signupResponse.data.getData().getuser().getCustomerId());
                                    preferenceHandler.saveData(PreferenceHandler.LOGIN_USERNAME, signupResponse.data.getData().getuser().getCustomerFirstName());
                                    preferenceHandler.saveData(PreferenceHandler.LOGIN_EMAIL, signupResponse.data.getData().getuser().getEmailId());
                                    preferenceHandler.saveData(PreferenceHandler.LOGIN_PASSWORD, signupResponse.data.getData().getuser().getPassword());
                                    preferenceHandler.saveData(PreferenceHandler.LOGIN_GENDER, signupResponse.data.getData().getuser().getGender());
                                    preferenceHandler.saveData(PreferenceHandler.LOGIN_CONFIRM_PASSWORD, signupResponse.data.getData().getuser().getConfirmPassword());
                                    preferenceHandler.saveData(PreferenceHandler.LOGIN_PHONENUMBER, signupResponse.data.getData().getuser().getMobileNo());
                                    preferenceHandler.saveData(PreferenceHandler.LOGIN_STATUS, true);
                                    if (signupResponse.data.getData().getuser() != null) {
                                        signinActivity.getmFirebaseAnalytics().setUserProperty("email", signupResponse.data.getData().getuser().getEmailId());
                                        signinActivity.getmFirebaseAnalytics().setUserProperty("country", signupResponse.data.getData().getuser().getNationality());
                                        signinActivity.getmFirebaseAnalytics().setUserProperty("gender", signupResponse.data.getData().getuser().getGender());
                                        signinActivity.getmFirebaseAnalytics().setUserProperty("date_of_birth", signupResponse.data.getData().getuser().getDob());
                                        signinActivity.getmFirebaseAnalytics().setUserProperty("phone", signupResponse.data.getData().getuser().getMobileNo());
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            try {
                                String date = DateTimeUtils.getCurrentStringDateTime();
                                String country = "", gender = "";
                                if (signupResponse.data.getData().getuser() != null) {
                                    country = signupResponse.data.getData().getuser().getNationality();
                                    gender = signupResponse.data.getData().getuser().getGender();
                                }
                                logREGISTRATION_SUCCESSEvent(date, country, gender);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            RegistrationSuccessDialog registrationSuccessDialog = new RegistrationSuccessDialog(signinActivity);
                            registrationSuccessDialog.show(getParentFragmentManager(), "REG_SUCCESS_DIALOG");

                            break;
                        case LOADING:
                            break;
                        case ERROR:
                            String error = signupResponse.message;
                            String date = "";
                            try {
                                date = DateTimeUtils.getCurrentStringDateTime();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            logREGISTRATION_FAILEDEvent(error, date);
                            Toast.makeText(signinActivity, signupResponse.message, Toast.LENGTH_SHORT).show();
                            GeneralDialog generalDialog = new GeneralDialog("Error", error);
                            generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                            break;
                    }
                });

            } else {
                Toast.makeText(signinActivity, "Password did not match", Toast.LENGTH_SHORT).show();
            }

    }


    private void selectMale() {
        hideSoftKeyboard(signinActivity);
        fragmentSignupBinding.layoutSignup.constraintLayoutMale.setBackground(getActivity().getResources().getDrawable(R.drawable.bronze_gradient_rounded_rectangle_12dp));
        fragmentSignupBinding.layoutSignup.tvMale.setTextColor(getResources().getColor(R.color.white));
        fragmentSignupBinding.layoutSignup.constraintLayoutFemale.setBackground(getActivity().getResources().getDrawable(R.drawable.blue_outlined_rounded_rectangle_12dp));
        fragmentSignupBinding.layoutSignup.txtFemale.setTextColor(getResources().getColor(R.color.txt_clr_blue));
        selectedGender = "M";
    }

    private void selectFemale() {
        hideSoftKeyboard(signinActivity);
        fragmentSignupBinding.layoutSignup.constraintLayoutFemale.setBackground(getActivity().getResources().getDrawable(R.drawable.bronze_gradient_rounded_rectangle_12dp));
        fragmentSignupBinding.layoutSignup.txtFemale.setTextColor(getResources().getColor(R.color.white));
        fragmentSignupBinding.layoutSignup.constraintLayoutMale.setBackground(getActivity().getResources().getDrawable(R.drawable.blue_outlined_rounded_rectangle_12dp));
        fragmentSignupBinding.layoutSignup.tvMale.setTextColor(getResources().getColor(R.color.txt_clr_blue));
        selectedGender = "F";
    }

//    private void showDatePickerDialog() {
//        final Calendar cldr = Calendar.getInstance();
//        int day = cldr.get(Calendar.DAY_OF_MONTH);
//        int month = cldr.get(Calendar.MONTH);
//        int year = cldr.get(Calendar.YEAR);
//        // date picker dialog
//        datePickerDialog = new DatePickerDialog(getActivity(),
//                new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//
//                        selectedDate = year + "-" + monthOfYear + "-" + dayOfMonth;
//                        fragmentSignupBinding.layoutSignup.edtBirthday.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
//                    }
//                }, year, month, day);
//        datePickerDialog.getDatePicker().setMaxDate((long) (System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 365.25 * 16)));
//        datePickerDialog.show();
//    }

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
//                showDatePickerDialog();
                break;
            case R.id.btn_signup:
                hideSoftKeyboard(signinActivity);
                userRegistration();
                break;
            case R.id.constraintLayoutMale:
                selectMale();
                break;
            case R.id.constraintLayoutFemale:
                selectFemale();
                break;
            case R.id.cv_background:
                hideSoftKeyboard(signinActivity);
                break;
            default:
                break;
        }
    }

    /**
     * This function assumes logger is an instance of AppEventsLogger and has been
     * created using AppEventsLogger.newLogger() call.
     */
    public void logREGISTRATION_SUCCESSEvent(String registration_date, String country, String gender) {
        Bundle params = new Bundle();
        params.putString("registration_date", registration_date);
        params.putString("country", country);
        params.putString("gender", gender);
        logger.logEvent("REGISTRATION_SUCCESS", params);
        signinActivity.getmFirebaseAnalytics().logEvent("REGISTRATION_SUCCESS", params);
    }

    /**
     * This function assumes logger is an instance of AppEventsLogger and has been
     * created using AppEventsLogger.newLogger() call.
     */
    public void logREGISTRATION_FAILEDEvent(String error, String registration_date) {
        Bundle params = new Bundle();
        params.putString("error", error);
        params.putString("registration_date", registration_date);
        logger.logEvent("REGISTRATION_FAILED", params);
        signinActivity.getmFirebaseAnalytics().logEvent("REGISTRATION_FAILED", params);
    }
}

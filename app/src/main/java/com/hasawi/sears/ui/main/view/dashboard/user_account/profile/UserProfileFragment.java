package com.hasawi.sears.ui.main.view.dashboard.user_account.profile;

import android.app.DatePickerDialog;
import android.util.ArrayMap;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.hasawi.sears.R;
import com.hasawi.sears.databinding.FragmentUserProfileBinding;
import com.hasawi.sears.ui.base.BaseFragment;
import com.hasawi.sears.ui.main.view.DashboardActivity;
import com.hasawi.sears.ui.main.viewmodel.UserProfileViewModel;
import com.hasawi.sears.utils.PreferenceHandler;
import com.rilixtech.widget.countrycodepicker.Country;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.Calendar;
import java.util.Map;

public class UserProfileFragment extends BaseFragment {

    UserProfileViewModel userProfileViewModel;
    FragmentUserProfileBinding fragmentUserProfileBinding;
    DatePickerDialog datePickerDialog;
    DashboardActivity dashboardActivity;
    private String selectedGender, selectedNationality, selectedDob;
    private String sessionToken, customerId;
    private String selectedCountryCode;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_user_profile;
    }

    @Override
    protected void setup() {
        fragmentUserProfileBinding = (FragmentUserProfileBinding) viewDataBinding;
        userProfileViewModel = new ViewModelProvider(getActivity()).get(UserProfileViewModel.class);
        dashboardActivity = (DashboardActivity) getActivity();
        dashboardActivity.handleActionMenuBar(true, false, "My Profile");
        try {
            PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
            String name = preferenceHandler.getData(PreferenceHandler.LOGIN_USERNAME, "");
            String email = preferenceHandler.getData(PreferenceHandler.LOGIN_EMAIL, "");
            String phone = preferenceHandler.getData(PreferenceHandler.LOGIN_PHONENUMBER, "");
            String password = preferenceHandler.getData(PreferenceHandler.LOGIN_PASSWORD, "");
            sessionToken = preferenceHandler.getData(PreferenceHandler.LOGIN_TOKEN, "");
            customerId = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");

            fragmentUserProfileBinding.layoutProfile.edtFirstname.setText(name);
            fragmentUserProfileBinding.layoutProfile.edtEmail.setText(email);
            fragmentUserProfileBinding.layoutProfile.edtMobile.setText(phone);
            fragmentUserProfileBinding.tvUsernameTop.setText(name);

            callUserProfileApi(email, sessionToken);

        } catch (Exception e) {
            e.printStackTrace();
        }

        fragmentUserProfileBinding.layoutProfile.pickerCountryCode.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                selectedCountryCode = selectedCountry.getPhoneCode();
            }
        });
        fragmentUserProfileBinding.layoutProfile.pickerNationality.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                selectedNationality = selectedCountry.getName();
            }
        });

        fragmentUserProfileBinding.layoutProfile.edtBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();

            }
        });

        fragmentUserProfileBinding.layoutProfile.constraintLayoutFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFemale();
            }
        });
        fragmentUserProfileBinding.layoutProfile.constraintLayoutMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMale();
            }
        });

        fragmentUserProfileBinding.layoutProfile.btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardActivity.replaceFragment(R.id.fragment_replacer, new ChangePasswordFragment(), null, true, false);
                dashboardActivity.handleActionMenuBar(true, false, "Change Password");
            }
        });

        fragmentUserProfileBinding.layoutProfile.tvSaveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String firstName = fragmentUserProfileBinding.layoutProfile.edtFirstname.getText().toString();
                    String lastName = fragmentUserProfileBinding.layoutProfile.edtLastname.getText().toString();
                    String mobileNo = fragmentUserProfileBinding.layoutProfile.edtMobile.getText().toString();
                    String emailId = fragmentUserProfileBinding.layoutProfile.edtEmail.getText().toString();

                    if (firstName.equals(""))
                        fragmentUserProfileBinding.layoutProfile.tvFirstNameError.setVisibility(View.VISIBLE);
                    if (lastName.equals(""))
                        fragmentUserProfileBinding.layoutProfile.tvLastNameError.setVisibility(View.VISIBLE);
                    if (emailId.equals(""))
                        fragmentUserProfileBinding.layoutProfile.tvEmailError.setVisibility(View.VISIBLE);
                    if (mobileNo.equals(""))
                        fragmentUserProfileBinding.layoutProfile.tvMobileError.setVisibility(View.VISIBLE);
                    else {
                        Map<String, Object> inputParams = new ArrayMap<>();
                        inputParams.put("customerFirstName", firstName);
                        inputParams.put("customerLastName", lastName);
                        inputParams.put("emailId", emailId);
                        inputParams.put("gender", selectedGender);
                        inputParams.put("nationality", selectedNationality);
                        inputParams.put("dob", selectedDob);
                        inputParams.put("mobileNo", mobileNo);
                        callEditUserProfileApi(customerId, inputParams, sessionToken);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void callUserProfileApi(String emailId, String sessionToken) {
        userProfileViewModel.userProfile(emailId, sessionToken).observe(getActivity(), userProfileResponseResource -> {
            switch (userProfileResponseResource.status) {
                case SUCCESS:
                    if (userProfileResponseResource != null) {
                        fragmentUserProfileBinding.layoutProfile.edtFirstname.setText(userProfileResponseResource.data.getData().getUser().getCustomerFirstName());
                        fragmentUserProfileBinding.layoutProfile.edtLastname.setText(userProfileResponseResource.data.getData().getUser().getCustomerLastName());
                        fragmentUserProfileBinding.layoutProfile.edtEmail.setText(userProfileResponseResource.data.getData().getUser().getEmailId());
                        if (userProfileResponseResource.data.getData().getUser().getDob() != null)
                            fragmentUserProfileBinding.layoutProfile.edtBirthday.setText(userProfileResponseResource.data.getData().getUser().getDob());
                        if (userProfileResponseResource.data.getData().getUser().getGender() != null) {
                            if (userProfileResponseResource.data.getData().getUser().getGender().equalsIgnoreCase("f"))
                                selectFemale();
                            else if (userProfileResponseResource.data.getData().getUser().getGender().equalsIgnoreCase("m"))
                                selectMale();
                        }
                        fragmentUserProfileBinding.tvUsernameTop.setText(userProfileResponseResource.data.getData().getUser().getCustomerFirstName() + " " + userProfileResponseResource.data.getData().getUser().getCustomerLastName());
                        if (userProfileResponseResource.data.getData().getUser().getNationality() != null) {
                            fragmentUserProfileBinding.layoutProfile.pickerNationality.setCountryForNameCode(userProfileResponseResource.data.getData().getUser().getNationality());
                        }
                    }
                    break;
                case LOADING:
                    break;
                case ERROR:
                    Toast.makeText(dashboardActivity, userProfileResponseResource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
            fragmentUserProfileBinding.progressBar.setVisibility(View.GONE);
        });
    }


    private void callEditUserProfileApi(String customerId, Map<String, Object> inputParams, String sessionToken) {
        fragmentUserProfileBinding.progressBar.setVisibility(View.VISIBLE);
        userProfileViewModel.editUserProfile(customerId, sessionToken, inputParams).observe(getActivity(), userProfileResponseResource -> {
            switch (userProfileResponseResource.status) {
                case SUCCESS:
                    Toast.makeText(getActivity(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                    if (userProfileResponseResource != null) {
                        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
                        preferenceHandler.saveData(PreferenceHandler.LOGIN_USERNAME, userProfileResponseResource.data.getData().getUser().getCustomerFirstName());
                        preferenceHandler.saveData(PreferenceHandler.LOGIN_EMAIL, userProfileResponseResource.data.getData().getUser().getEmailId());
                        preferenceHandler.saveData(PreferenceHandler.LOGIN_PASSWORD, userProfileResponseResource.data.getData().getUser().getPassword());
                        preferenceHandler.saveData(PreferenceHandler.LOGIN_CONFIRM_PASSWORD, userProfileResponseResource.data.getData().getUser().getConfirmPassword());
                        preferenceHandler.saveData(PreferenceHandler.LOGIN_PHONENUMBER, userProfileResponseResource.data.getData().getUser().getMobileNo());
                        preferenceHandler.saveData(PreferenceHandler.LOGIN_GENDER, userProfileResponseResource.data.getData().getUser().getGender());
                        preferenceHandler.saveData(PreferenceHandler.LOGIN_NATIONALITY, userProfileResponseResource.data.getData().getUser().getNationality());

                        dashboardActivity.getmFirebaseAnalytics().setUserProperty("email", userProfileResponseResource.data.getData().getUser().getEmailId());
                        dashboardActivity.getmFirebaseAnalytics().setUserProperty("country", userProfileResponseResource.data.getData().getUser().getNationality());
                        dashboardActivity.getmFirebaseAnalytics().setUserProperty("gender", userProfileResponseResource.data.getData().getUser().getGender());
                        dashboardActivity.getmFirebaseAnalytics().setUserProperty("date_of_birth", userProfileResponseResource.data.getData().getUser().getDob());
                        dashboardActivity.getmFirebaseAnalytics().setUserProperty("phone", userProfileResponseResource.data.getData().getUser().getMobileNo());


                    }
                    break;
                case LOADING:
                    break;
                case ERROR:
                    Toast.makeText(getActivity(), userProfileResponseResource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
            fragmentUserProfileBinding.progressBar.setVisibility(View.GONE);
        });
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
                        fragmentUserProfileBinding.layoutProfile.edtBirthday.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        selectedDob = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    private void selectMale() {
        fragmentUserProfileBinding.layoutProfile.constraintLayoutMale.setBackground(getActivity().getResources().getDrawable(R.drawable.blue_rounded_rectangle_12dp_corner));
        fragmentUserProfileBinding.layoutProfile.tvMale.setTextColor(getResources().getColor(R.color.white));
        fragmentUserProfileBinding.layoutProfile.constraintLayoutFemale.setBackground(getActivity().getResources().getDrawable(R.drawable.blue_outlined_rounded_rectangle_12dp));
        fragmentUserProfileBinding.layoutProfile.txtFemale.setTextColor(getResources().getColor(R.color.txt_clr_blue));
        selectedGender = "Male";
    }

    private void selectFemale() {
        fragmentUserProfileBinding.layoutProfile.constraintLayoutFemale.setBackground(getActivity().getResources().getDrawable(R.drawable.blue_rounded_rectangle_12dp_corner));
        fragmentUserProfileBinding.layoutProfile.txtFemale.setTextColor(getResources().getColor(R.color.white));
        fragmentUserProfileBinding.layoutProfile.constraintLayoutMale.setBackground(getActivity().getResources().getDrawable(R.drawable.blue_outlined_rounded_rectangle_12dp));
        fragmentUserProfileBinding.layoutProfile.tvMale.setTextColor(getResources().getColor(R.color.txt_clr_blue));
        selectedGender = "Female";
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
//        fragmentUserProfileBinding.layoutProfile.edtBirthday.setText(selectedDate);
//    }
}

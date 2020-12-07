package com.hasawi.sears.ui.main.view.navigation_drawer_menu.profile;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.DatePicker;

import androidx.lifecycle.ViewModelProvider;

import com.hasawi.sears.R;
import com.hasawi.sears.databinding.FragmentUserProfileBinding;
import com.hasawi.sears.ui.base.BaseFragment;
import com.hasawi.sears.ui.main.viewmodel.UserProfileViewModel;
import com.hasawi.sears.utils.PreferenceHandler;

import java.text.DateFormat;
import java.util.Calendar;

public class UserProfileFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener {

    UserProfileViewModel userProfileViewModel;
    FragmentUserProfileBinding fragmentUserProfileBinding;
    DatePickerDialog datePickerDialog;
    private String selectedGender;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_user_profile;
    }

    @Override
    protected void setup() {
        fragmentUserProfileBinding = (FragmentUserProfileBinding) viewDataBinding;
        userProfileViewModel = new ViewModelProvider(getActivity()).get(UserProfileViewModel.class);

        try {
            PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
            String name = preferenceHandler.getData(PreferenceHandler.LOGIN_USERNAME, "");
            String email = preferenceHandler.getData(PreferenceHandler.LOGIN_EMAIL, "");
            String phone = preferenceHandler.getData(PreferenceHandler.LOGIN_PHONENUMBER, "");
            String password = preferenceHandler.getData(PreferenceHandler.LOGIN_PASSWORD, "");

            fragmentUserProfileBinding.layoutProfile.edtFirstname.setText(name);
            fragmentUserProfileBinding.layoutProfile.edtEmail.setText(email);
            fragmentUserProfileBinding.layoutProfile.edtMobile.setText(phone);
//            fragmentUserProfileBinding.layoutCommonSignupProfile.setText(password);
//            fragmentUserProfileBinding.edtConfirmPassword.setText(password);
        } catch (Exception e) {
            e.printStackTrace();
        }

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

//        final Observer<User> userObserver = new Observer<User>() {
//            @Override
//            public void onChanged(User user) {
//              // Set ui values here
//            }
//        };
//        userProfileViewModel.getUser().observe(getActivity(), userObserver);
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

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // Create a Calender instance

        Calendar mCalender = Calendar.getInstance();

        // Set static variables of Calender instance

        mCalender.set(Calendar.YEAR, year);

        mCalender.set(Calendar.MONTH, month);

        mCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        // Get the date in form of string

        String selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(mCalender.getTime());

        // Set the textview to the selectedDate String

        fragmentUserProfileBinding.layoutProfile.edtBirthday.setText(selectedDate);
    }
}

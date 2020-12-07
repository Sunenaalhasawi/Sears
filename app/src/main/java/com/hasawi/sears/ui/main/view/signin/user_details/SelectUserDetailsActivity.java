package com.hasawi.sears.ui.main.view.signin.user_details;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.hasawi.sears.R;
import com.hasawi.sears.databinding.ActivitySelectUserDetailsBinding;
import com.hasawi.sears.ui.base.BaseActivity;
import com.hasawi.sears.ui.main.viewmodel.SelectUserDetailsViewModel;
import com.hasawi.sears.utils.DialogGeneral;

public class SelectUserDetailsActivity extends BaseActivity {

    SelectUserDetailsViewModel selectUserDetailsViewModel;
    ActivitySelectUserDetailsBinding activitySelectUserDetailsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user_details);
        activitySelectUserDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_select_user_details);
        selectUserDetailsViewModel = new ViewModelProvider(this).get(SelectUserDetailsViewModel.class);
        if (getIntent() != null && getIntent().getBooleanExtra("redirect_from_home", false))
            replaceFragment(R.id.fragment_replacer_user_details, new SelectCategoryFragment(), null, true, false);
        else
            replaceFragment(R.id.fragment_replacer_user_details, new SelectLanguageFragment(), null, true, false);

        if (isNetworkAvailable())
            callDynamicDataApi();
        else {
            DialogGeneral dialogGeneral = new DialogGeneral();
            dialogGeneral.showDialog(this);
            dialogGeneral.setClickListener(new DialogGeneral.DialogGeneralInterface() {
                @Override
                public void positiveClick() {
                    callDynamicDataApi();
                }

                @Override
                public void negativeClick() {

                }
            });
        }

    }

    private void callDynamicDataApi() {
        try {
//            Bundle bundle=getArguments();
//            String selectedLanguage=bundle.getString("languageId");
            activitySelectUserDetailsBinding.progressBar.setVisibility(View.VISIBLE);
            selectUserDetailsViewModel.getDynamicData().observe(this, dynamicDataResponseResource -> {
                switch (dynamicDataResponseResource.status) {
                    case SUCCESS:
                        selectUserDetailsViewModel.setDynamicData(new MutableLiveData<>(dynamicDataResponseResource.data.getData()));
                        break;
                    case LOADING:
                        break;
                    case ERROR:
                        Toast.makeText(this, dynamicDataResponseResource.message, Toast.LENGTH_SHORT).show();
                        break;
                }
                activitySelectUserDetailsBinding.progressBar.setVisibility(View.GONE);

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void replaceFragment(Fragment fragment, Bundle bundle) {
        replaceFragment(R.id.fragment_replacer_user_details, fragment, bundle, true, false);
    }

    public SelectUserDetailsViewModel getSelectUserDetailsViewModel() {
        if (selectUserDetailsViewModel == null)
            selectUserDetailsViewModel = new ViewModelProvider(this).get(SelectUserDetailsViewModel.class);
        return selectUserDetailsViewModel;
    }
}

package com.hasawi.sears.ui.main.view.signin.user_details;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.Language;
import com.hasawi.sears.databinding.FragmentSelectLanguageBinding;
import com.hasawi.sears.ui.base.BaseFragment;
import com.hasawi.sears.ui.main.adapters.SelectUserLanguageAdapter;
import com.hasawi.sears.ui.main.listeners.RecyclerviewSingleChoiceClickListener;
import com.hasawi.sears.ui.main.viewmodel.SelectUserDetailsViewModel;

import java.util.ArrayList;

public class SelectLanguageFragment extends BaseFragment implements RecyclerviewSingleChoiceClickListener, View.OnClickListener {

    SelectUserDetailsActivity selectUserDetailsActivity;
    FragmentSelectLanguageBinding fragmentSelectLanguageBinding;
    ArrayList<Language> languageArrayList = new ArrayList<>();
    SelectUserLanguageAdapter selectUserLanguageAdapter;
    LinearLayoutManager horizontalLayoutManager;
    SelectUserDetailsViewModel selectUserDetailsViewModel;
    String selectedLanguage;
    Bundle dataBundle = new Bundle();

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_select_language;
    }

    @Override
    protected void setup() {
        fragmentSelectLanguageBinding = (FragmentSelectLanguageBinding) viewDataBinding;
        selectUserDetailsActivity = (SelectUserDetailsActivity) getActivity();
        selectUserDetailsViewModel = selectUserDetailsActivity.getSelectUserDetailsViewModel();
        fragmentSelectLanguageBinding.btnContinue.setOnClickListener(this);
        fragmentSelectLanguageBinding.imageButtonNext.setOnClickListener(this);
        fragmentSelectLanguageBinding.tvLanguage.setOnClickListener(this);
        fragmentSelectLanguageBinding.tvGender.setOnClickListener(this);
        fragmentSelectLanguageBinding.tvProductCategory.setOnClickListener(this);
        fragmentSelectLanguageBinding.tvSize.setOnClickListener(this);


        horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        fragmentSelectLanguageBinding.recyclerViewLanguage.setLayoutManager(horizontalLayoutManager);

        selectUserLanguageAdapter = new SelectUserLanguageAdapter(getContext(), languageArrayList);
        fragmentSelectLanguageBinding.recyclerViewLanguage.setAdapter(selectUserLanguageAdapter);
        selectUserLanguageAdapter.setOnItemClickListener(this);


        callLanguageApi();
//        TripHistory tripHistory = tripHistoryArrayList.get(position);
//                Gson gson = new Gson();
//                String objectString = gson.toJson(tripHistory);
//                Intent tripDetailIntent = new Intent(getActivity(), ActiveTripDetailsActivity.class);
//                tripDetailIntent.putExtra("trip_history_object", objectString);
//                startActivity(tripDetailIntent);
//

    }


    @Override
    public void onItemClickListener(int position, View view) {
        selectUserLanguageAdapter.selectedItem();
        Language item = languageArrayList.get(position);
        selectedLanguage = item.getCode();
    }

    @Override
    public void onClick(View v) {
        dataBundle.putString("languageId", selectedLanguage);
        switch (v.getId()) {
            case R.id.btnContinue:
                selectUserDetailsActivity.replaceFragment(new SelectCategoryFragment(), dataBundle);
                break;
            case R.id.imageButtonNext:
                if (horizontalLayoutManager.findLastCompletelyVisibleItemPosition() < (selectUserLanguageAdapter.getItemCount() - 1)) {
                    horizontalLayoutManager.scrollToPosition(horizontalLayoutManager.findLastCompletelyVisibleItemPosition() + 1);
                }
                break;
            case R.id.tvLanguage:
                selectUserDetailsActivity.replaceFragment(new SelectLanguageFragment(), dataBundle);
                break;
            case R.id.tvGender:
                selectUserDetailsActivity.replaceFragment(new SelectGenderFragment(), dataBundle);
                break;
            case R.id.tv_product_category:
                selectUserDetailsActivity.replaceFragment(new SelectCategoryFragment(), dataBundle);
                break;
            case R.id.tv_size:
                selectUserDetailsActivity.replaceFragment(new SelectSizeFragment(), dataBundle);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        selectTextView();
    }

    public void selectTextView() {
        fragmentSelectLanguageBinding.tvLanguage.setBackground(getResources().getDrawable(R.drawable.blue_rounded_rectangle_8dp));
        fragmentSelectLanguageBinding.tvLanguage.setTextColor(getResources().getColor(R.color.white));
        fragmentSelectLanguageBinding.tvLanguage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.translation_white, 0, 0, 0);
    }

    public void callLanguageApi() {
        selectUserDetailsViewModel.getLanguages().observe(getActivity(), languageResponseResource -> {
            switch (languageResponseResource.status) {
                case SUCCESS:
                    languageArrayList = new ArrayList<>();
                    languageArrayList = (ArrayList<Language>) languageResponseResource.data.getData();
                    selectUserLanguageAdapter = new SelectUserLanguageAdapter(getContext(), languageArrayList);
                    fragmentSelectLanguageBinding.recyclerViewLanguage.setAdapter(selectUserLanguageAdapter);
                    selectUserLanguageAdapter.notifyDataSetChanged();
                    break;
                case LOADING:
                    break;
                case ERROR:
                    Toast.makeText(selectUserDetailsActivity, languageResponseResource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
            fragmentSelectLanguageBinding.progressBar.setVisibility(View.GONE);
        });
    }
}

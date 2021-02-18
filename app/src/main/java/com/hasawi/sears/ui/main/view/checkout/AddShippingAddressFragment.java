package com.hasawi.sears.ui.main.view.checkout;

import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.pojo.Address;
import com.hasawi.sears.databinding.FragmentAddNewAddressBinding;
import com.hasawi.sears.ui.base.BaseFragment;
import com.hasawi.sears.ui.main.view.DashboardActivity;
import com.hasawi.sears.ui.main.viewmodel.ShippingAddressViewModel;
import com.hasawi.sears.utils.PreferenceHandler;
import com.hasawi.sears.utils.dialogs.GeneralDialog;
import com.rilixtech.widget.countrycodepicker.Country;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.ArrayList;
import java.util.Map;

public class AddShippingAddressFragment extends BaseFragment implements AdapterView.OnItemSelectedListener {
    FragmentAddNewAddressBinding fragmentAddAddressBinding;
    ShippingAddressViewModel shippingAddressViewModel;
    boolean isEdit = false;
    Address addressObj;
    DashboardActivity dashboardActivity;
    ArrayList<String> addressType = new ArrayList<>();
    //    String[] addressType = { "Building", "House",
//            "Office"};
//    String[] area = {"Building", "House",
//            "Office"};
    String selectedCountryName = "KUWAIT";

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_add_new_address;
    }

    @Override
    protected void setup() {
        fragmentAddAddressBinding = (FragmentAddNewAddressBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        shippingAddressViewModel = new ViewModelProvider(getActivity()).get(ShippingAddressViewModel.class);
        dashboardActivity.handleActionBarIcons(false);

        fragmentAddAddressBinding.spinnerAddressType.setOnItemSelectedListener(this);
        fragmentAddAddressBinding.spinnerArea.setOnItemSelectedListener(this);
        addressType.add("Building");
        addressType.add("House");
        addressType.add("Office");


        ArrayAdapter<String> addressTypeAdapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, addressType);
        addressTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fragmentAddAddressBinding.spinnerAddressType.setAdapter(addressTypeAdapter);

        ArrayAdapter<String> areaAdapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, shippingAddressViewModel.getAreaList());
        areaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fragmentAddAddressBinding.spinnerArea.setAdapter(areaAdapter);
        try {
            Bundle bundle = getArguments();
            if (bundle != null) {
                String address = bundle.getString("address");
                Gson gson = new Gson();
                addressObj = gson.fromJson(address, Address.class);
                if (addressObj != null) {
                    isEdit = true;
                    fragmentAddAddressBinding.edtFirstName.setText(addressObj.getFirstName());
                    fragmentAddAddressBinding.edtLastName.setText(addressObj.getLastName());
                    fragmentAddAddressBinding.edtStreet.setText(addressObj.getStreet());
//                    fragmentAddAddressBinding.spinnerAddressType.setPrompt(addressObj.get);
//                    fragmentAddAddressBinding.spinnerArea.setText(addressObj.getArea());
                    fragmentAddAddressBinding.edtFlatNumber.setText(addressObj.getFlat());
                    fragmentAddAddressBinding.edtBlock.setText(addressObj.getBlock());
                    fragmentAddAddressBinding.edtMobileNumber.setText(addressObj.getMobile());
                    fragmentAddAddressBinding.pickerNationality.setCountryForNameCode(addressObj.getCountry());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragmentAddAddressBinding.btnContinueShippingAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit)
                    callEditAddressApi(addressObj);
                else
                    callAddNewAddressApi();
            }
        });

        fragmentAddAddressBinding.pickerNationality.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                selectedCountryName = selectedCountry.getName();
            }
        });
    }


    private void callAddNewAddressApi() {

        String street = fragmentAddAddressBinding.edtStreet.getText().toString();
        String firstName = fragmentAddAddressBinding.edtFirstName.getText().toString();
        String lastName = fragmentAddAddressBinding.edtLastName.getText().toString();
        String mobile = fragmentAddAddressBinding.edtMobileNumber.getText().toString();
//        String area = fragmentAddAddressBinding.edtArea.getText().toString();
        String flat = fragmentAddAddressBinding.edtFlatNumber.getText().toString();
        String block = fragmentAddAddressBinding.edtBlock.getText().toString();
//        String country = fragmentAddAddressBinding.edtCountry.getText().toString();
        if (!firstName.equalsIgnoreCase("") && !lastName.equalsIgnoreCase("") && !mobile.equalsIgnoreCase("")
                && !street.equalsIgnoreCase("") && !flat.equalsIgnoreCase("") && !block.equalsIgnoreCase("") && !selectedCountryName.equalsIgnoreCase("")) {
            fragmentAddAddressBinding.progressBar.setVisibility(View.VISIBLE);
            PreferenceHandler preferenceHandler = new PreferenceHandler(getActivity(), PreferenceHandler.TOKEN_LOGIN);
            String userID = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
            String sessionToken = preferenceHandler.getData(PreferenceHandler.LOGIN_TOKEN, "");
            Map<String, Object> jsonParams = new ArrayMap<>();
            jsonParams.put("firstName", firstName);
            jsonParams.put("lastName", lastName);
            jsonParams.put("mobile", mobile);
            jsonParams.put("street", street);
            jsonParams.put("area", "");
            jsonParams.put("flat", flat);
            jsonParams.put("block", block);
            jsonParams.put("country", selectedCountryName);
            jsonParams.put("customerId", userID);

            shippingAddressViewModel.addNewAddress(userID, jsonParams, sessionToken).observe(getActivity(), addressResponseResource -> {
                switch (addressResponseResource.status) {
                    case SUCCESS:
                        Toast.makeText(getActivity(), "Address Added Successfully", Toast.LENGTH_SHORT).show();
                        getParentFragmentManager().popBackStackImmediate();
                        break;
                    case LOADING:
                        break;
                    case ERROR:
                        Toast.makeText(getActivity(), addressResponseResource.message, Toast.LENGTH_SHORT).show();
                        break;
                }

                fragmentAddAddressBinding.progressBar.setVisibility(View.GONE);
            });
        } else {
            GeneralDialog generalDialog = new GeneralDialog("Error", "Please enter required fields to proceed?");
            generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
        }


    }

    private void callEditAddressApi(Address address) {
        fragmentAddAddressBinding.progressBar.setVisibility(View.VISIBLE);
        String firstName = fragmentAddAddressBinding.edtFirstName.getText().toString();
        String lastName = fragmentAddAddressBinding.edtLastName.getText().toString();
        String mobile = fragmentAddAddressBinding.edtMobileNumber.getText().toString();
        String street = fragmentAddAddressBinding.edtStreet.getText().toString();
//        String area = fragmentAddAddressBinding.edtArea.getText().toString();
        String flat = fragmentAddAddressBinding.edtFlatNumber.getText().toString();
        String block = fragmentAddAddressBinding.edtBlock.getText().toString();
//        String country = fragmentAddAddressBinding.edtCountry.getText().toString();
        PreferenceHandler preferenceHandler = new PreferenceHandler(getActivity(), PreferenceHandler.TOKEN_LOGIN);
        String userID = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
        String sessionToken = preferenceHandler.getData(PreferenceHandler.LOGIN_TOKEN, "");
        Map<String, Object> jsonParams = new ArrayMap<>();

        jsonParams.put("addressId", address.getAddressId());
        jsonParams.put("firstName", firstName);
        jsonParams.put("lastName", lastName);
        jsonParams.put("mobile", mobile);
        jsonParams.put("street", street);
        jsonParams.put("area", "");
        jsonParams.put("flat", flat);
        jsonParams.put("block", block);
        jsonParams.put("country", selectedCountryName);
        jsonParams.put("customerId", userID);

        shippingAddressViewModel.editAddress(userID, addressObj.getAddressId(), sessionToken, jsonParams).observe(getActivity(), addressResponseResource -> {
            switch (addressResponseResource.status) {
                case SUCCESS:
                    Toast.makeText(getActivity(), "Address Added Successfully", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().popBackStackImmediate();
                    break;
                case LOADING:
                    break;
                case ERROR:
                    Toast.makeText(getActivity(), addressResponseResource.message, Toast.LENGTH_SHORT).show();
                    break;
            }

            fragmentAddAddressBinding.progressBar.setVisibility(View.GONE);
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        if(view.getId()==R.id.spinnerAddressType)
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

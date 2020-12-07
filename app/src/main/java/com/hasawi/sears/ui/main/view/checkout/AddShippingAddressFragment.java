package com.hasawi.sears.ui.main.view.checkout;

import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.pojo.Address;
import com.hasawi.sears.databinding.FragmentAddAddressBinding;
import com.hasawi.sears.ui.base.BaseFragment;
import com.hasawi.sears.ui.main.viewmodel.ShippingAddressViewModel;
import com.hasawi.sears.utils.PreferenceHandler;

import java.util.Map;

public class AddShippingAddressFragment extends BaseFragment {
    FragmentAddAddressBinding fragmentAddAddressBinding;
    ShippingAddressViewModel shippingAddressViewModel;
    boolean isEdit = false;
    Address addressObj;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_add_address;
    }

    @Override
    protected void setup() {
        fragmentAddAddressBinding = (FragmentAddAddressBinding) viewDataBinding;
        shippingAddressViewModel = new ViewModelProvider(getActivity()).get(ShippingAddressViewModel.class);
        try {
            Bundle bundle = getArguments();
            if (bundle != null) {
                String address = bundle.getString("address");
                Gson gson = new Gson();
                addressObj = gson.fromJson(address, Address.class);
                if (addressObj != null) {
                    isEdit = true;
                    fragmentAddAddressBinding.edtStreet.setText(addressObj.getStreet());
                    fragmentAddAddressBinding.edtArea.setText(addressObj.getArea());
                    fragmentAddAddressBinding.edtFlat.setText(addressObj.getFlat());
                    fragmentAddAddressBinding.edtBlock.setText(addressObj.getBlock());
                    fragmentAddAddressBinding.edtCountry.setText(addressObj.getCountry());
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
    }


    private void callAddNewAddressApi() {
        fragmentAddAddressBinding.progressBar.setVisibility(View.VISIBLE);
        String street = fragmentAddAddressBinding.edtStreet.getText().toString();
        String area = fragmentAddAddressBinding.edtArea.getText().toString();
        String flat = fragmentAddAddressBinding.edtFlat.getText().toString();
        String block = fragmentAddAddressBinding.edtBlock.getText().toString();
        String country = fragmentAddAddressBinding.edtCountry.getText().toString();
        PreferenceHandler preferenceHandler = new PreferenceHandler(getActivity(), PreferenceHandler.TOKEN_LOGIN);
        String userID = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
        String sessionToken = preferenceHandler.getData(PreferenceHandler.LOGIN_TOKEN, "");
        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("street", street);
        jsonParams.put("area", area);
        jsonParams.put("flat", flat);
        jsonParams.put("block", block);
        jsonParams.put("country", country);

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

    }

    private void callEditAddressApi(Address address) {
        fragmentAddAddressBinding.progressBar.setVisibility(View.VISIBLE);
        String street = fragmentAddAddressBinding.edtStreet.getText().toString();
        String area = fragmentAddAddressBinding.edtArea.getText().toString();
        String flat = fragmentAddAddressBinding.edtFlat.getText().toString();
        String block = fragmentAddAddressBinding.edtBlock.getText().toString();
        String country = fragmentAddAddressBinding.edtCountry.getText().toString();
        PreferenceHandler preferenceHandler = new PreferenceHandler(getActivity(), PreferenceHandler.TOKEN_LOGIN);
        String userID = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
        String sessionToken = preferenceHandler.getData(PreferenceHandler.LOGIN_TOKEN, "");
        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("addressId", address.getAddressId());
        jsonParams.put("street", street);
        jsonParams.put("area", area);
        jsonParams.put("flat", flat);
        jsonParams.put("block", block);
        jsonParams.put("country", country);
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
}

package com.hasawi.sears.ui.main.view.dashboard.user_account.address_book;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.hasawi.sears.R;
import com.hasawi.sears.data.api.model.pojo.Address;
import com.hasawi.sears.databinding.FragmentAddressbookBinding;
import com.hasawi.sears.ui.base.BaseFragment;
import com.hasawi.sears.ui.main.adapters.ShippingAddressAdapter;
import com.hasawi.sears.ui.main.listeners.RecyclerviewSingleChoiceClickListener;
import com.hasawi.sears.ui.main.view.DashboardActivity;
import com.hasawi.sears.ui.main.view.checkout.AddShippingAddressFragment;
import com.hasawi.sears.ui.main.viewmodel.ShippingAddressViewModel;
import com.hasawi.sears.utils.AppConstants;
import com.hasawi.sears.utils.PreferenceHandler;

import java.util.ArrayList;
import java.util.List;

public class AddressBookFragment extends BaseFragment implements RecyclerviewSingleChoiceClickListener {
    FragmentAddressbookBinding fragmentAddressbookBinding;
    ShippingAddressAdapter shippingAddressAdapter;
    List<Address> addressList = new ArrayList<>();
    DashboardActivity dashboardActivity;
    ShippingAddressViewModel shippingAddressViewModel;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_addressbook;
    }

    @Override
    protected void setup() {
        fragmentAddressbookBinding = (FragmentAddressbookBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        shippingAddressViewModel = new ViewModelProvider(this).get(ShippingAddressViewModel.class);
        fragmentAddressbookBinding.recyclerviewAddress.setLayoutManager(new LinearLayoutManager(getContext()));
        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        String userId = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
        String token = preferenceHandler.getData(PreferenceHandler.LOGIN_TOKEN, "");
        callGetAddressApi(userId, token);
    }

    private void callGetAddressApi(String userId, String sessionToken) {
        fragmentAddressbookBinding.progressBar.setVisibility(View.VISIBLE);
        shippingAddressViewModel.getAddresses(userId, sessionToken).observe(getActivity(), getAllAddressResponseResource -> {
            switch (getAllAddressResponseResource.status) {
                case SUCCESS:
                    try {
                        addressList = getAllAddressResponseResource.data.getData().getAddresses();
                        if (addressList == null || addressList.size() == 0) {
                            fragmentAddressbookBinding.constrainLayoutNoData.setVisibility(View.VISIBLE);
                        } else {
                            fragmentAddressbookBinding.constrainLayoutNoData.setVisibility(View.GONE);
                            shippingAddressAdapter = new ShippingAddressAdapter(getContext(), (ArrayList<Address>) addressList, AppConstants.ADDRESS_VIEW_TYPE_ADDRESSBOOK) {
                                @Override
                                public void onEditClicked(Address address) {
                                    dashboardActivity.handleActionMenuBar(true, false, "");
                                    Gson gson = new Gson();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("address", gson.toJson(address));
                                    dashboardActivity.replaceFragment(R.id.fragment_replacer, new AddShippingAddressFragment(), bundle, true, false);
                                }

                                @Override
                                public void onDeleteClicked(Address address) {

                                }
                            };
                            shippingAddressAdapter.setOnItemClickListener(this);
                            fragmentAddressbookBinding.recyclerviewAddress.setAdapter(shippingAddressAdapter);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case LOADING:
                    break;
                case ERROR:
                    Toast.makeText(getActivity(), getAllAddressResponseResource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
            fragmentAddressbookBinding.progressBar.setVisibility(View.GONE);
        });
    }

    @Override
    public void onItemClickListener(int position, View view) {
        shippingAddressAdapter.selectedItem();
    }
}

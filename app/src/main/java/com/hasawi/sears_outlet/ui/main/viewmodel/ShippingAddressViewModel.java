package com.hasawi.sears_outlet.ui.main.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hasawi.sears_outlet.data.api.Resource;
import com.hasawi.sears_outlet.data.api.response.AddressResponse;
import com.hasawi.sears_outlet.data.api.response.GetAllAddressResponse;
import com.hasawi.sears_outlet.data.repository.UserAccountRepository;

import java.util.ArrayList;
import java.util.Map;

public class ShippingAddressViewModel extends ViewModel {
    UserAccountRepository userAccountRepository;

    public ShippingAddressViewModel() {
        userAccountRepository = new UserAccountRepository();
    }

    public MutableLiveData<Resource<AddressResponse>> addNewAddress(String userID, Map<String, Object> jsonParams, String sessionToken) {
        return userAccountRepository.addNewAddress(userID, jsonParams, sessionToken);
    }

    public MutableLiveData<Resource<AddressResponse>> editAddress(String userId, String addressId, String sessionToken, Map<String, Object> jsonParams) {
        return userAccountRepository.editAddress(userId, addressId, sessionToken, jsonParams);
    }

    public MutableLiveData<Resource<GetAllAddressResponse>> getAddresses(String userId, String sessiontoken) {
        return userAccountRepository.getAddresses(userId, sessiontoken);
    }

    public ArrayList<String> getAreaList() {
        ArrayList<String> areaList = new ArrayList<>();
        areaList.add("Abdullah AL Salim suburb");
        areaList.add(" Abdullah Al Mubarak");
        areaList.add(" Abu Alhassaniyah");
        areaList.add("Abu Fatera Coast Strip B");
        areaList.add("Abu Halifa");
        areaList.add("Abu ftaira");
        areaList.add("Adailiya");
        areaList.add("Adan");
        areaList.add("Ahmadi");
        areaList.add("Airport");
        areaList.add("Al Bidea");
        areaList.add("Al Masayel");
        areaList.add("Al Mubarakiya (Block 15) Bayan");
        areaList.add("Al Subahiya South");
        areaList.add("Ali as-Salim suburb");
        areaList.add("Almasela");
        areaList.add(" Andalous");
        areaList.add("Ardhiya");
        areaList.add(" Ardhiya Storage Zone");
        areaList.add(" Ardihya Industrial");
        areaList.add("AzZour");
        areaList.add("Bayan");
        areaList.add("Bneid il Gar");
        areaList.add("Coast Strip A");
        areaList.add("Dasma");
        areaList.add("Dasman");
        areaList.add("Deiya");
        areaList.add("Dhaher");
        areaList.add("Dhajeej South Farwaniya");
        areaList.add("Doha Residential");
        areaList.add("East Taima");
        areaList.add("Egaila");
        areaList.add("Eshbilya");
        areaList.add("Exhibits - South Khitan");
        areaList.add("Fahad Al Ahmad");
        areaList.add("Fahaheel");
        areaList.add("Faihaa");
        areaList.add("Farwaniya");
        areaList.add("Ferdous");
        areaList.add("Fintas");
        areaList.add("Fnaitees");
        areaList.add("Fnaitees Coast Strip B");
        areaList.add("Free Zone Phaze 2");
        areaList.add("Ghirnata");
        areaList.add("Hadiya");
        areaList.add("Hawalli");
        areaList.add("Hiteen");
        areaList.add("Jaber Al -Ali");
        areaList.add("Jabir al -Ahmad City");
        areaList.add("Jabriya");
        areaList.add("Jahra - Naeem");
        areaList.add("Jahra - Nasseem");
        areaList.add("Jahra - Old Jahra");
        areaList.add(" Jahra - Oyoun");
        areaList.add("Jahra - Qasr");
        areaList.add("Jahra - Saad Al Abdullah");
        areaList.add("Jahra - Taima");
        areaList.add("Jahra - Waha");
        areaList.add("Jibla");
        areaList.add("Jleeb Al Shiyoukh");
        areaList.add("Keifan");
        areaList.add("Khaitan");
        areaList.add("Khaldiya");
        areaList.add("Magwa");
        areaList.add("Mahboula");
        areaList.add("Maidan Hawalli");
        areaList.add("Mangaf");
        areaList.add("Mansuriya");
        areaList.add("Mirgab");
        areaList.add("Mishrif");
        areaList.add("Mubarak aj -Jabir suburb");
        areaList.add("Mubarak al Kabeer");
        areaList.add("Nahda");
        areaList.add("North West Jahra");
        areaList.add("North west sulaibikhat");
        areaList.add("Nuzha");
        areaList.add("Omariya");
        areaList.add("Qadsiya");
        areaList.add("Qairawan");
        areaList.add("Qurain");
        areaList.add("Qurtuba");
        areaList.add("Qusour");
        areaList.add("Rabia");
        areaList.add("Rai");
        areaList.add("Rawda");
        areaList.add("Riggae");
        areaList.add("Rihab - South Rabia");
        areaList.add("Riqqa");
        areaList.add("Rumaithiya");
        areaList.add("Sabah Al Salem University City");
        areaList.add("Sabah Al salem");
        areaList.add("Sabah Al -Nasser");
        areaList.add("Sabah al -Ahmad City");
        areaList.add("Sadiq");
        areaList.add("Salam");
        areaList.add("Salmiya");
        areaList.add("Salwa");
        areaList.add("Shaab");
        areaList.add("Shaab Al Bahri Block 8");
        areaList.add("Shamiya");
        areaList.add("Sharq");
        areaList.add("Shuhadaa");
        areaList.add("Shuwaikh");
        areaList.add("Shuwaikh Administration");
        areaList.add("Shuwaikh Educational");
        areaList.add("Shuwaikh Health");
        areaList.add("Shuwaikh Industrial 1");
        areaList.add(" Shuwaikh Industrial 2");
        areaList.add("Shuwaikh Industrial 3");
        areaList.add("Subah Al Ahmad Residential");
        areaList.add("Subayhiyah");
        areaList.add("Sulaibikhat");
        areaList.add("Sulibiya Residential");
        areaList.add("Surra");
        areaList.add("The Sea Front");
        areaList.add(" Wafra");
        areaList.add("West AbuFatera Industrial");
        areaList.add("Yarmuk");
        areaList.add("Zahra");
        return areaList;
    }
}

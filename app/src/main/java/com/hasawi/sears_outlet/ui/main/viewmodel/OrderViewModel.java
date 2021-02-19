package com.hasawi.sears_outlet.ui.main.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hasawi.sears_outlet.data.api.Resource;
import com.hasawi.sears_outlet.data.api.response.OrderResponse;
import com.hasawi.sears_outlet.data.api.response.PaymentResponse;
import com.hasawi.sears_outlet.data.repository.CheckoutRepository;
import com.hasawi.sears_outlet.data.repository.OrderRepository;

import java.util.Map;

public class OrderViewModel extends ViewModel {

    private OrderRepository orderRepository;
    private CheckoutRepository checkoutRepository;


    public OrderViewModel() {
        orderRepository = new OrderRepository();
        checkoutRepository = new CheckoutRepository();
    }

    public MutableLiveData<Resource<OrderResponse>> orderConfirmation(String userId, String addressId, String cartId, String sessiontoken, Map<String, Object> jsonParams) {
        return orderRepository.orderConfirmation(userId, addressId, cartId, sessiontoken, jsonParams);
    }

    public MutableLiveData<Resource<PaymentResponse>> getPaymentSuccess(String url) {
        return checkoutRepository.getPaymentSuccess(url);
    }
}

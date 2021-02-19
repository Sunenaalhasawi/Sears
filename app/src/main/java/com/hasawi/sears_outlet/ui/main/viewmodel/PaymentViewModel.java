package com.hasawi.sears_outlet.ui.main.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hasawi.sears_outlet.data.api.Resource;
import com.hasawi.sears_outlet.data.api.model.pojo.PaymentMode;
import com.hasawi.sears_outlet.data.api.response.OrderResponse;
import com.hasawi.sears_outlet.data.repository.OrderRepository;

import java.util.Map;

public class PaymentViewModel extends ViewModel {
    PaymentMode selectedPaymentMode;
    OrderRepository orderRepository;

    public PaymentViewModel() {
        orderRepository = new OrderRepository();
    }

    public void setSelectedPaymentMode(PaymentMode selectedPaymentMode) {
        this.selectedPaymentMode = selectedPaymentMode;
    }

    public MutableLiveData<Resource<OrderResponse>> orderConfirmation(String userId, String addressId, String cartId, String sessiontoken, Map<String, Object> jsonParams) {
        return orderRepository.orderConfirmation(userId, addressId, cartId, sessiontoken, jsonParams);
    }
}
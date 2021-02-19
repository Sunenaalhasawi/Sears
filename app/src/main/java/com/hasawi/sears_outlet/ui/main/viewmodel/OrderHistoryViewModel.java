package com.hasawi.sears_outlet.ui.main.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hasawi.sears_outlet.data.api.Resource;
import com.hasawi.sears_outlet.data.api.response.OrderHistoryResponse;
import com.hasawi.sears_outlet.data.repository.OrderRepository;

public class OrderHistoryViewModel extends ViewModel {

    OrderRepository orderRepository;

    public OrderHistoryViewModel() {
        orderRepository = new OrderRepository();
    }

    public MutableLiveData<Resource<OrderHistoryResponse>> getOrderHistory(String customerId, String sessionToken) {
        return orderRepository.orderHistory(customerId, sessionToken);
    }
}

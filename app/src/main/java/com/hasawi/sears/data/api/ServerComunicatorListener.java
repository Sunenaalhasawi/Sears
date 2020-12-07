package com.hasawi.sears.data.api;

import retrofit2.Call;

public interface ServerComunicatorListener {
    void onResponse(Call call, Object response);

    void onFailure(Call call, Throwable t);
}

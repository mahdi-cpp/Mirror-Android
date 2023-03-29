package com.mahdi.car.server.https;

import retrofit2.Response;

public interface ServerDelegate {
    void response(Response<Integer> response);
}

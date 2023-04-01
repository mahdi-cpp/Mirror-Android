package com.mahdi.car.server;


import com.mahdi.car.server.dtos.ReportDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;


public interface ReportController {

    @PUT("/report/v1.0/add_report")
    Call<Long> addReport(@Body ReportDTO reportDTO);

}
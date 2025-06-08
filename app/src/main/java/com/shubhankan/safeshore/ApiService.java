package com.shubhankan.safeshore;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ApiService {

    // New method for fetching beach data with dynamic URL
    @GET
    Call<BeachDataResponse> getBeachData(@Url String url);


    @POST("/predict")
    Call<BeachPredictionResponse> predictBeachSafety(@Body Map<String, Object> requestBody);
}
package com.dantsu.thermalprinter;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("commanda/full/")
    Call<List<ApiResponseItem>> getData();
}

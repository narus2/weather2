package com.weather.narus.weather;

import com.weather.narus.weather.model.model;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by com.weather.narus.weather on 03.08.2015.
 */
public interface JsonApiService {

    @GET("/data/2.5/weather")
    void  get_countries(@Query("q") String city, Callback<model> callback);

    @GET("/data/2.5/weather")

    void  get_countries_LatLng(@Query("lat") double lat,@Query("lon") double lon, Callback<model> callback);


}

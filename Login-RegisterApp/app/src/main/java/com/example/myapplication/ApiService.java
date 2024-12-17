package com.example.myapplication;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/register")
    Call<Void> registerUser(@Body Users user);

    @POST("/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @GET("/profile")
    Call<UserProfile> getUserProfile(@Header("Authorization") String token);
}

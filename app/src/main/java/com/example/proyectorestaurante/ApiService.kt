package com.example.proyectorestaurante

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("filter.php")
    suspend fun filterByCategory(
        @Query("c") category: String
    ): MealsResponse

    @GET("lookup.php")
    suspend fun getMealById(
        @Query("i") id: String
    ): MealsResponse
}
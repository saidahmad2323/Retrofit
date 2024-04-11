package com.example.retrofittest

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("character/{id}")
    suspend fun getCharacterById(@Path("id") id: Int): Character
}

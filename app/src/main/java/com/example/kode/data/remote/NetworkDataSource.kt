package com.example.kode.data.remote

import retrofit2.http.GET

interface NetworkDataSource {
    @GET(value = "users")
    suspend fun getItems(): Items
}
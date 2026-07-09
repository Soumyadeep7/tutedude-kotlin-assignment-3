package com.example.data.remote.api

import com.example.data.model.FakeStoreProduct
import retrofit2.http.GET

interface FakeStoreService {
    @GET("products")
    suspend fun getProducts(): List<FakeStoreProduct>
}

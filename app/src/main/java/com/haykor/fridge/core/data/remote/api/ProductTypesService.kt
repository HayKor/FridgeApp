package com.haykor.fridge.core.data.remote.api

import com.haykor.fridge.core.data.remote.models.ProductTypeDto
import com.haykor.fridge.core.data.remote.models.Result
import retrofit2.http.GET

interface ProductTypesService {

    companion object {
        const val ROUTE = "product_types/"
    }

    @GET(ROUTE)
    fun getProductTypes(): Result<List<ProductTypeDto>>
}
package com.catnip.rullfood.data.network.api.datasource

import com.catnip.rullfood.data.network.api.model.category.CategoryResponse
import com.catnip.rullfood.data.network.api.model.menu.MenuResponse
import com.catnip.rullfood.data.network.api.model.order.OrderRequest
import com.catnip.rullfood.data.network.api.model.order.OrderResponse
import com.catnip.rullfood.data.network.api.service.RestaurantService

interface RestaurantDataSource {
    suspend fun getMenus(category: String?= null) : MenuResponse
    suspend fun getCategory(): CategoryResponse
    suspend fun createOrder(orderRequest: OrderRequest): OrderResponse

}

class RestaurantDataSourceImpl(
    private val service: RestaurantService
): RestaurantDataSource{
    override suspend fun getMenus(category: String?): MenuResponse {
        return service.getMenus(category)
    }

    override suspend fun getCategory(): CategoryResponse {
        return service.getCategory()
    }

    override suspend fun createOrder(orderRequest: OrderRequest): OrderResponse {
        return service.createOrder(orderRequest)
    }


}
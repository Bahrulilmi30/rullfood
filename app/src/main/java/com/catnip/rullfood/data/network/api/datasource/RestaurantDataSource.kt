package com.catnip.rullfood.data.network.api.datasource

import com.catnip.rullfood.data.network.api.model.category.CategoryResponse
import com.catnip.rullfood.data.network.api.model.menu.MenuResponse
import com.catnip.rullfood.data.network.api.service.RestaurantService

interface RestaurantDataSource {
    suspend fun getMenus(category: String?= null) : MenuResponse
    suspend fun getCategory(): CategoryResponse
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

}
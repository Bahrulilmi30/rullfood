package com.catnip.rullfood.data.repository

import com.catnip.rullfood.data.network.api.datasource.RestaurantDataSource
import com.catnip.rullfood.data.network.api.model.category.toCategoryList
import com.catnip.rullfood.data.network.api.model.menu.toMenuList
import com.catnip.rullfood.model.Category
import com.catnip.rullfood.model.Menu

import com.catnip.rullfood.utils.ResultWrapper
import com.catnip.rullfood.utils.proceedFlow
import kotlinx.coroutines.flow.Flow

interface MenuRepository {
     suspend fun getMenus(category : String? = null): Flow<ResultWrapper<List<Menu>>>
     suspend fun getCategory(): Flow<ResultWrapper<List<Category>>>

}

class MenuRepositoryImpl(
    private val dataSource: RestaurantDataSource
): MenuRepository{
    override suspend fun getMenus(category: String?): Flow<ResultWrapper<List<Menu>>> {
        return proceedFlow {
            dataSource.getMenus(category).data?.toMenuList() ?: emptyList()
        }
    }

    override suspend fun getCategory(): Flow<ResultWrapper<List<Category>>> {
        return proceedFlow {
            dataSource.getCategory().data?.toCategoryList()?: emptyList()
        }
    }

}
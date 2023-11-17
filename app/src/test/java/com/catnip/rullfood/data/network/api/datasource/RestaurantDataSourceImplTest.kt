package com.catnip.rullfood.data.network.api.datasource

import com.catnip.rullfood.data.network.api.model.category.CategoryResponse
import com.catnip.rullfood.data.network.api.model.menu.MenuResponse
import com.catnip.rullfood.data.network.api.model.order.OrderRequest
import com.catnip.rullfood.data.network.api.model.order.OrderResponse
import com.catnip.rullfood.data.network.api.service.RestaurantService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class RestaurantDataSourceImplTest {

    @MockK
    lateinit var service: RestaurantService
    private lateinit var dataSource: RestaurantDataSourceImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dataSource = RestaurantDataSourceImpl(service)
    }

    @Test
    fun getMenus() {
        runTest {
            val mockResponse = mockk<MenuResponse>(relaxed = true)
            // memanggil data
            coEvery { service.getMenus(any()) } returns mockResponse
            val response = dataSource.getMenus("food")
            // memverify data apakah data sudah terpanggil
            coVerify { service.getMenus(any()) }
            // mencocok kan data
            assertEquals(response, mockResponse)
        }
    }

    @Test
    fun getCategory() {
        runTest {
            val mockResponse = mockk<CategoryResponse>(relaxed = true)
            // memanggil data
            coEvery { service.getCategory() } returns mockResponse
            val response = dataSource.getCategory()
            // memverify data apakah data sudah terpanggil
            coVerify { service.getCategory() }
            // mencocok kan data
            assertEquals(response, mockResponse)
        }
    }

    @Test
    fun createOrder() {
        runTest {
            val mockResponse = mockk<OrderResponse>(relaxed = true)
            val mockRequest = mockk<OrderRequest>(relaxed = true)
            // memanggil data
            coEvery { service.createOrder(any()) } returns mockResponse
            val response = dataSource.createOrder(mockRequest)
            // memanggil data
            coEvery { service.createOrder(mockRequest) }
            // memverify data apakah data sudah terpanggil
            coVerify { service.createOrder(any()) }
            // mencocok kan data
            assertEquals(response, mockResponse)
        }
    }
}

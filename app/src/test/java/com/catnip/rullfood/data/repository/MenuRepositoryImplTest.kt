package com.catnip.rullfood.data.repository

import app.cash.turbine.test
import com.catnip.rullfood.data.network.api.datasource.RestaurantDataSource
import com.catnip.rullfood.data.network.api.model.category.CategoryItemResponse
import com.catnip.rullfood.data.network.api.model.category.CategoryResponse
import com.catnip.rullfood.data.network.api.model.menu.MenuItemResponse
import com.catnip.rullfood.data.network.api.model.menu.MenuResponse
import com.catnip.rullfood.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MenuRepositoryImplTest {

    @MockK
    lateinit var remoteDataSource: RestaurantDataSource

    private lateinit var repository: MenuRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = MenuRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `get categories, with result loading`() {
        val mockCategoryResponse = mockk<CategoryResponse>()
        runTest {
            coEvery { remoteDataSource.getCategory() } returns mockCategoryResponse
            repository.getCategory().map {
                delay(100)
                it
            }.test {
                delay(110)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { remoteDataSource.getCategory() }
            }
        }
    }

    @Test
    fun `get categories, with result success`() {
        val fakeCategoryResponse = CategoryItemResponse(
            id = 124,
            imgUrlCategory = "url",
            nameOfCategory = "name",
            slug = "slug"
        )
        val fakeCategoriesResponse = CategoryResponse(
            code = 200,
            status = true,
            message = "success",
            data = listOf(fakeCategoryResponse)
        )
        runTest {
            coEvery { remoteDataSource.getCategory() } returns fakeCategoriesResponse
            repository.getCategory().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                assertEquals(data.payload?.size, 1)
                assertEquals(data.payload?.get(0)?.id, 124)
                coVerify { remoteDataSource.getCategory() }
            }
        }
    }

    @Test
    fun `get categories, with result empty`() {
        val fakeCategoriesResponse = CategoryResponse(
            code = 200,
            status = true,
            message = "success but empty",
            data = emptyList()
        )
        runTest {
            coEvery { remoteDataSource.getCategory() } returns fakeCategoriesResponse
            repository.getCategory().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Empty)
                coVerify { remoteDataSource.getCategory() }
            }
        }
    }

    @Test
    fun `get categories, with result error`() {
        runTest {
            coEvery { remoteDataSource.getCategory() } throws IllegalAccessException("Mock error")
            repository.getCategory().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                coVerify { remoteDataSource.getCategory() }
            }
        }
    }

    @Test
    fun `get menus, with result loading`() {
        val fakeMenuResponse = mockk<MenuResponse>()
        runTest {
            coEvery { remoteDataSource.getMenus(any()) } returns fakeMenuResponse
            repository.getMenus("makanan").map {
                delay(100)
                it
            }.test {
                delay(110)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { remoteDataSource.getMenus(any()) }
            }
        }
    }

    @Test
    fun `get menus, with result success`() {
        val fakeMenuItemResponse = MenuItemResponse(
            id = 124,
            descOfMenu = "description",
            locationName = "bandung",
            locationUrl = "url",
            name = "name",
            price = 20000.0,
            productImgUrl = "urlImage"
        )
        val fakeMenusResponse = MenuResponse(
            code = 200,
            status = true,
            message = "success",
            data = listOf(fakeMenuItemResponse)
        )
        runTest {
            coEvery { remoteDataSource.getMenus(any()) } returns fakeMenusResponse
            repository.getMenus("makanan").map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                assertEquals(data.payload?.size, 1)
                assertEquals(data.payload?.get(0)?.id, 124)
                coVerify { remoteDataSource.getMenus(any()) }
            }
        }
    }

    @Test
    fun `get menus, with result empty`() {
        val fakeMenusResponse = MenuResponse(
            code = 200,
            status = true,
            message = "success but empty",
            data = emptyList()
        )
        runTest {
            coEvery { remoteDataSource.getMenus(any()) } returns fakeMenusResponse
            repository.getMenus().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Empty)
                coVerify { remoteDataSource.getMenus(any()) }
            }
        }
    }

    @Test
    fun `get menus, with result error`() {
        runTest {
            coEvery { remoteDataSource.getMenus(any()) } throws IllegalAccessException("Mock error")
            repository.getMenus("makanan").map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                coVerify { remoteDataSource.getMenus(any()) }
            }
        }
    }
}

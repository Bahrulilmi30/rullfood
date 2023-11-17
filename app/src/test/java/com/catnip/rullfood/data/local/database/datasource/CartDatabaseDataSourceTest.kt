package com.catnip.rullfood.data.local.database.datasource

import app.cash.turbine.test
import com.catnip.rullfood.data.local.database.dao.CartDao
import com.catnip.rullfood.data.local.database.entity.CartEntity
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CartDatabaseDataSourceTest {

    @MockK
    lateinit var cartDao: CartDao
    private lateinit var cartDataSource: CartDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        cartDataSource = CartDatabaseDataSource(cartDao)
    }

    @Test
    fun getAllCarts() {
        val mockItemEntity1 = mockk<CartEntity>()
        val mockItemEntity2 = mockk<CartEntity>()
        val mockListEntity = listOf(mockItemEntity1, mockItemEntity2)
        val mockFlow = flow {
            emit(mockListEntity)
        }
        coEvery { cartDao.getAllCarts() } returns mockFlow
        runTest {
            cartDataSource.getAllCarts().test {
                val result = awaitItem()
                assertEquals(mockListEntity, result)
                assertEquals(mockListEntity.size, result.size)
                assertEquals(mockItemEntity1, result[0])
                assertEquals(mockItemEntity2, result[1])
                awaitComplete()
            }
        }
    }

    @Test
    fun getCartById() {
        val mockItemEntity = mockk<CartEntity>()
        val mockFlow = flow {
            emit(mockItemEntity)
        }
        coEvery { cartDao.getCartById(any()) } returns mockFlow
        runTest {
            cartDataSource.getCartById(1).test {
                assertEquals(mockItemEntity, awaitItem())
                awaitComplete()
            }
        }
    }

    @Test
    fun insertCart() {
        runTest {
            val mockCartEntity = mockk<CartEntity>(relaxed = true)
            coEvery { cartDao.insertCart(any()) } returns 1
            val result = cartDataSource.insertCart(mockCartEntity)
            coVerify { cartDao.insertCart(any()) }
            assertEquals(result, 1)
        }
    }

    @Test
    fun deleteCart() {
        runTest {
            val mockCartEntity = mockk<CartEntity>(relaxed = true)
            coEvery { cartDao.deleteCart(any()) } returns 1
            val result = cartDataSource.deleteCart(mockCartEntity)
            coVerify { cartDao.deleteCart(any()) }
            assertEquals(result, 1)
        }
    }

    @Test
    fun updateCart() {
        runTest {
            val mockCartEntity = mockk<CartEntity>(relaxed = true)
            coEvery { cartDao.updateCart(any()) } returns 1
            val result = cartDataSource.updateCart(mockCartEntity)
            coVerify { cartDao.updateCart(any()) }
            assertEquals(result, 1)
        }
    }

    @Test
    fun deleteAll() {
        runTest {
            coEvery { cartDao.deleteAll() } returns Unit
            val result = cartDataSource.deleteAll()
            coVerify { cartDao.deleteAll() }
            assertEquals(result, Unit)
        }
    }
}

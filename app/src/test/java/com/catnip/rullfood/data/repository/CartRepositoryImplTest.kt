package com.catnip.rullfood.data.repository

import app.cash.turbine.test
import com.catnip.rullfood.data.local.database.datasource.CartDataSource
import com.catnip.rullfood.data.local.database.entity.CartEntity
import com.catnip.rullfood.data.network.api.datasource.RestaurantDataSource
import com.catnip.rullfood.data.network.api.model.order.OrderResponse
import com.catnip.rullfood.model.Cart
import com.catnip.rullfood.model.Menu
import com.catnip.rullfood.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.lang.IllegalStateException

class CartRepositoryImplTest {

    @MockK
    lateinit var localDataSource: CartDataSource

    @MockK
    lateinit var remoteDataSource: RestaurantDataSource
    private lateinit var repository: CartRepository

    private val fakeCartList = listOf(
        CartEntity(
            id = 1,
            productId = 1,
            productName = "sate kambing",
            productPrice = 12000.0,
            productImgUrl = "url",
            itemQuantity = 2,
            itemNotes = "notes"
        ),
        CartEntity(
            id = 2,
            productId = 1,
            productName = "sate ayam",
            productPrice = 14000.0,
            productImgUrl = "url",
            itemQuantity = 2,
            itemNotes = "notes"
        )
    )
    private val mockCart = Cart(
        id = 1,
        productId = 1,
        productName = "sate",
        productPrice = 12000.0,
        productImgUrl = "url",
        itemQuantity = 0,
        itemNotes = "notes"
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = CartRepositoryImpl(localDataSource, remoteDataSource)
    }

    @Test
    fun deleteAll() {
        coEvery { localDataSource.deleteAll() } returns Unit
        runTest {
            val result = repository.deleteAll()
            coVerify { localDataSource.deleteAll() }
            assertEquals(result, Unit)
        }
    }

    @Test
    fun `get user cart data, result success`() {
        every { localDataSource.getAllCarts() } returns flow {
            emit(fakeCartList)
        }
        runTest {
            repository.getUserCardData().map {
                delay(100)
                it
            }.test {
                delay(2201)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                assertEquals(data.payload?.first?.size, 2)
                assertEquals(data.payload?.second, 52000.0)
                verify { localDataSource.getAllCarts() }
            }
        }
    }

    @Test
    fun `get user cart data, result loading`() {
        every { localDataSource.getAllCarts() } returns flow {
            emit(fakeCartList)
        }
        runTest {
            repository.getUserCardData().map {
                delay(100)
                it
            }.test {
                delay(2101)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                verify { localDataSource.getAllCarts() }
            }
        }
    }

    @Test
    fun `get user card data, result error`() {
        every { localDataSource.getAllCarts() } returns flow {
            throw IllegalStateException("Mock Error")
        }
        runTest {
            repository.getUserCardData().map {
                delay(100)
                it
            }.test {
                delay(2201)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                verify { localDataSource.getAllCarts() }
            }
        }
    }

    @Test
    fun `get user cart data, result empty`() {
        every { localDataSource.getAllCarts() } returns flow {
            emit(emptyList())
        }
        runTest {
            repository.getUserCardData().map {
                delay(100)
                it
            }.test {
                delay(2201)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Empty)
                verify { localDataSource.getAllCarts() }
            }
        }
    }

    @Test
    fun `create cart loading, product id not null`() {
        runTest {
            val mockMenu = mockk<Menu>(relaxed = true)
            coEvery { localDataSource.insertCart(any()) } returns 1
            repository.createCart(mockMenu, 1)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(110)
                    val result = expectMostRecentItem()
                    assertTrue(result is ResultWrapper.Loading)
                    coVerify { localDataSource.insertCart(any()) }
                }
        }
    }

    @Test
    fun `create cart success, product id not null`() {
        runTest {
            val mockMenu = mockk<Menu>(relaxed = true)
            coEvery { localDataSource.insertCart(any()) } returns 1
            repository.createCart(mockMenu, 1)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(210)
                    val result = expectMostRecentItem()
                    assertTrue(result is ResultWrapper.Success)
                    assertEquals(result.payload, true)
                    coVerify { localDataSource.insertCart(any()) }
                }
        }
    }

    @Test
    fun `create cart error, product id not null`() {
        runTest {
            val mockMenu = mockk<Menu>(relaxed = true)
            coEvery { localDataSource.insertCart(any()) } throws java.lang.IllegalStateException("Mock Error")
            repository.createCart(mockMenu, 1)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(210)
                    val result = expectMostRecentItem()
                    assertTrue(result is ResultWrapper.Error)
                    coVerify { localDataSource.insertCart(any()) }
                }
        }
    }

//    @Test
//    fun `create cart error, product id null`() {
//        runTest {
//            val mockMenu = mockk<Menu>(relaxed = true)
// //            {
// //                every { id } returns null
// //            }
//            coEvery { localDataSource.insertCart(any()) } returns 1
//            repository.createCart(mockMenu, 1)
//                .map {
//                    delay(100)
//                    it
//                }.test {
//                    delay(210)
//                    val result = expectMostRecentItem()
//                    assertTrue(result is ResultWrapper.Error)
//                    assertEquals(result.exception?.message, "Product ID not Found")
//                    coVerify(atLeast = 0) { localDataSource.insertCart(any()) }
//                }
//        }
//    }

    @Test
    fun `decrease cart when quantity less than or equals 0`() {
        coEvery { localDataSource.deleteCart(any()) } returns 1
        coEvery { localDataSource.updateCart(any()) } returns 1
        runTest {
            repository.decreaseCart(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val result = expectMostRecentItem()
                assertEquals(result.payload, true)
                coVerify(atLeast = 1) { localDataSource.deleteCart(any()) }
                coVerify(atLeast = 0) { localDataSource.updateCart(any()) }
            }
        }
    }

    @Test
    fun `decrease cart when quantity more than 0`() {
        coEvery { localDataSource.deleteCart(any()) } returns 1
        coEvery { localDataSource.updateCart(any()) } returns 1
        runTest {
            repository.decreaseCart(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val result = expectMostRecentItem()
                assertEquals(result.payload, true)
                coVerify(atLeast = 1) { localDataSource.deleteCart(any()) }
                coVerify(atLeast = 0) { localDataSource.updateCart(any()) }
            }
        }
    }

    @Test
    fun `increase cart`() {
        coEvery { localDataSource.updateCart(any()) } returns 1
        runTest {
            repository.increaseCart(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val result = expectMostRecentItem()
                assertEquals(result.payload, true)
                coVerify(atLeast = 0) { localDataSource.updateCart(any()) }
            }
        }
    }

    @Test
    fun `set cart notes`() {
        coEvery { localDataSource.updateCart(any()) } returns 1
        runTest {
            repository.setCartNotes(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val result = expectMostRecentItem()
                assertEquals(result.payload, true)
                coVerify(atLeast = 0) { localDataSource.updateCart(any()) }
            }
        }
    }

    @Test
    fun `delete cart`() {
        coEvery { localDataSource.deleteCart(any()) } returns 1
        runTest {
            repository.deleteCart(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val result = expectMostRecentItem()
                assertEquals(result.payload, true)
                coVerify(atLeast = 0) { localDataSource.deleteCart(any()) }
            }
        }
    }

    @Test
    fun `test order`() {
        runTest {
            val mockCart = listOf(
                Cart(
                    id = 1,
                    productId = 1,
                    productName = "sate",
                    productPrice = 12000.0,
                    productImgUrl = "url",
                    itemQuantity = 2,
                    itemNotes = "notes"
                ),
                Cart(
                    id = 2,
                    productId = 2,
                    productName = "sate kambing",
                    productPrice = 12000.0,
                    productImgUrl = "url",
                    itemQuantity = 2,
                    itemNotes = "notes"
                )
            )
            coEvery { remoteDataSource.createOrder(any()) } returns OrderResponse(
                code = 200,
                message = "Success",
                status = true
            )
            repository.order(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val result = expectMostRecentItem()
                assertTrue(result is ResultWrapper.Success)
            }
        }
    }
}

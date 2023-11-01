package com.catnip.rullfood.data.repository

import com.catnip.rullfood.data.database.datasource.CartDataSource
import com.catnip.rullfood.data.database.entity.CartEntity
import com.catnip.rullfood.data.database.mapper.toCartEntity
import com.catnip.rullfood.data.database.mapper.toCartList
import com.catnip.rullfood.data.network.api.datasource.RestaurantDataSource
import com.catnip.rullfood.data.network.api.model.order.OrderItemRequest
import com.catnip.rullfood.data.network.api.model.order.OrderRequest
import com.catnip.rullfood.model.Cart
import com.catnip.rullfood.model.Menu
import com.catnip.rullfood.utils.ResultWrapper
import com.catnip.rullfood.utils.proceed
import com.catnip.rullfood.utils.proceedFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

interface CartRepository {

    fun getUserCardData(): Flow<ResultWrapper<Pair<List<Cart>, Double>>>

    suspend fun createCart(menu: Menu, totalQuantity: Int): Flow<ResultWrapper<Boolean>>

    suspend fun decreaseCart(item: Cart): Flow<ResultWrapper<Boolean>>

    suspend fun increaseCart(item: Cart): Flow<ResultWrapper<Boolean>>

    suspend fun setCartNotes(item: Cart): Flow<ResultWrapper<Boolean>>

    suspend fun deleteCart(item: Cart): Flow<ResultWrapper<Boolean>>

    suspend fun order(items: List<Cart>): Flow<ResultWrapper<Boolean>>

    suspend fun deleteAll()
}

class CartRepositoryImpl(
    private val dataSource: CartDataSource,
    private val restaurantDataSource: RestaurantDataSource
) : CartRepository{
    override fun getUserCardData(): Flow<ResultWrapper<Pair<List<Cart>, Double>>> {
        return dataSource.getAllCarts()
            .map<List<CartEntity>, ResultWrapper<Pair<List<Cart>, Double>>> {
                proceed {
                    val result = it.toCartList()
                    val totalPrice = result.sumOf {
                        val pricePerItem = it.productPrice
                        val quantity = it.itemQuantity
                        pricePerItem * quantity
                    }
                    Pair(result, totalPrice)
                }
            }.map {
                if (it.payload?.first?.isEmpty() == true)
                    ResultWrapper.Empty(it.payload)
                else
                    it
            }
            .onStart {
                emit(ResultWrapper.Loading())
                delay(2000)
            }
    }

    override suspend fun createCart(
        menu: Menu,
        totalQuantity: Int
    ): Flow<ResultWrapper<Boolean>> {
        return menu.id.let { menuId ->
            proceedFlow {
                val affecttedRow = dataSource.insertCart(
                    CartEntity(
                        productId = menuId,
                        itemQuantity = totalQuantity,
                        productImgUrl = menu.productImgUrl,
                        productName = menu.name,
                        productPrice = menu.price
                    )
                )
                affecttedRow > 0
            }
        }?: flow {
            emit(ResultWrapper.Error(IllegalStateException("Product ID not found")))
        }
    }

    override suspend fun decreaseCart(item: Cart): Flow<ResultWrapper<Boolean>> {
        val modifiedCart = item.copy().apply {
            itemQuantity -= 1
        }
        return if (modifiedCart.itemQuantity <= 0) {
            proceedFlow { dataSource.deleteCart(modifiedCart.toCartEntity()) > 0 }
        } else {
            proceedFlow { dataSource.updateCart(modifiedCart.toCartEntity()) > 0 }
        }
    }

    override suspend fun increaseCart(item: Cart): Flow<ResultWrapper<Boolean>> {
        val modifiedCart = item.copy().apply {
            itemQuantity += 1
        }
        return proceedFlow { dataSource.updateCart(modifiedCart.toCartEntity()) > 0 }
    }

    override suspend fun setCartNotes(item: Cart): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { dataSource.updateCart(item.toCartEntity()) > 0 }
    }

    override suspend fun deleteCart(item: Cart): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { dataSource.deleteCart(item.toCartEntity()) > 0 }
    }

    override suspend fun order(items: List<Cart>): Flow<ResultWrapper<Boolean>> {
        return proceedFlow {
            val orderItems = items.map {
                OrderItemRequest(it.itemNotes, it.productId, it.itemQuantity)
            } // xxx -> ppp
            val orderRequest = OrderRequest(orderItems)
            restaurantDataSource.createOrder(orderRequest).status == true
        }
    }

    override suspend fun deleteAll() {
        dataSource.deleteAll()
    }
}
package com.catnip.rullfood.data.database.datasource

import com.catnip.rullfood.data.database.dao.CartDao
import com.catnip.rullfood.data.database.entity.CartEntity
import kotlinx.coroutines.flow.Flow

interface CartDataSource {

    fun getAllCarts(): Flow<List<CartEntity>>

    fun getCartById(cartId: Int): Flow<CartEntity>

    suspend fun insertCart(cart: CartEntity): Long

    suspend fun deleteCart(cart: CartEntity): Int

    suspend fun updateCart(cart: CartEntity): Int

    suspend fun deleteAll()
}

class CartDatabaseDataSource(
    private val cartDao: CartDao
) : CartDataSource {
    override fun getAllCarts(): Flow<List<CartEntity>> {
        return cartDao.getAllCarts()
    }

    override fun getCartById(cartId: Int): Flow<CartEntity> {
        return cartDao.getCartById(cartId)
    }

    override suspend fun insertCart(cart: CartEntity): Long {
        return cartDao.insertCart(cart)
    }

    override suspend fun deleteCart(cart: CartEntity): Int {
        return cartDao.deleteCart(cart)
    }

    override suspend fun updateCart(cart: CartEntity): Int {
        return cartDao.updateCart(cart)
    }

    override suspend fun deleteAll() {
        cartDao.deleteAll()
    }
}

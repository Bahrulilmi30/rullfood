package com.catnip.rullfood.presentation.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.catnip.rullfood.data.repository.CartRepository
import com.catnip.rullfood.model.Cart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CartViewModel(
    private val repository : CartRepository
) : ViewModel() {

    val cartList = repository.getUserCardData().asLiveData(Dispatchers.IO)

    fun decreaseCart(menu : Cart){
        viewModelScope.launch {
            repository.decreaseCart(menu).collect {
                Log.d("CartViewModel", " : Decrease Cart -> $it ${it.payload} ${it.exception}")
            }
        }
    }
    fun increaseCart(menu: Cart){
        viewModelScope.launch {
            repository.increaseCart(menu).collect {
                Log.d("CartViewModel", " : Increase Cart -> $it ${it.payload} ${it.exception}")
            }
        }
    }
    fun removeCart(menu: Cart){
        viewModelScope.launch {
            repository.deleteCart(menu).collect {
                Log.d("CartViewModel", " : Remove Cart -> $it ${it.payload} ${it.exception}")
            }
        }
    }
    fun setCartNotes(menu: Cart){
        viewModelScope.launch { repository.setCartNotes(menu).collect() }
    }
}
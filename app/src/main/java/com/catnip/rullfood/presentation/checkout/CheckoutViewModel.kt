package com.catnip.rullfood.presentation.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.catnip.rullfood.data.repository.CartRepository
import com.catnip.rullfood.utils.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CheckoutViewModel(
    private val cartRepository: CartRepository
) : ViewModel() {
    val cartList = cartRepository.getUserCardData().asLiveData(Dispatchers.IO)

    private val _checkoutresult = MutableLiveData<ResultWrapper<Boolean>>()
    val checkoutResult: LiveData<ResultWrapper<Boolean>>
        get() = _checkoutresult

    fun order() {
        viewModelScope.launch(Dispatchers.IO) {
            val carts = cartList.value?.payload?.first ?: return@launch
            cartRepository.order(carts).collect {
                _checkoutresult.postValue(it)
            }
        }
    }

    fun clearCart() {
        viewModelScope.launch(Dispatchers.IO) {
            cartRepository.deleteAll()
        }
    }
}

package com.catnip.rullfood.presentation.details

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catnip.rullfood.data.repository.CartRepository
import com.catnip.rullfood.model.Menu
import com.catnip.rullfood.utils.ResultWrapper
import kotlinx.coroutines.launch

class DetailViewModel (
    private val cartRepository: CartRepository,
    private val extras : Bundle?
): ViewModel(){
    val menu = extras?.getParcelable<Menu>(DetailActivity.EXTRA_FOOD)
    val priceLivedata = MutableLiveData<Double>().apply {
        postValue(0.0)
    }
    val productCountLiveData = MutableLiveData<Int>().apply {
        postValue(0)
    }
    private val _addToCartResult = MutableLiveData<ResultWrapper<Boolean>>()
    val addToCartResult : LiveData<ResultWrapper<Boolean>>
        get() = _addToCartResult

    fun add(){
        val count = (productCountLiveData.value ?: 0) + 1
        productCountLiveData.postValue(count)
        priceLivedata.postValue(menu?.price?.times(count)?: 0.0)
    }

    fun minus(){
        if ((productCountLiveData.value ?: 0) > 0) {
            val count = (productCountLiveData.value ?: 0) - 1
            productCountLiveData.postValue(count)
            priceLivedata.postValue(menu?.price?.times(count) ?: 0.0)
        }
    }

    fun addToCart() {
        viewModelScope.launch {
            val productQuantity =
                if ((productCountLiveData.value ?: 0) <= 0) 1 else productCountLiveData.value ?: 0
            menu?.let {
                cartRepository.createCart(it, productQuantity).collect { result ->
                    _addToCartResult.postValue(result)
                }
            }
        }
    }
}
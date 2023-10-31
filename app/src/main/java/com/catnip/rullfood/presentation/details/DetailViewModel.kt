package com.catnip.rullfood.presentation.details

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.catnip.rullfood.data.repository.MenuRepository
import com.catnip.rullfood.model.Product
import com.catnip.rullfood.utils.ResultWrapper

class DetailViewModel (
    private val menuRepository: MenuRepository,
    private val extras : Bundle?
): ViewModel(){
//    val product = extras?.getParcelable<Product>(DetailActivity.EXTRA_FOOD)
//    val priceLivedata = MutableLiveData<Double>().apply {
//        postValue(0.0)
//    }
//    private val productCountLiveData = MutableLiveData<Int>().apply {
//        postValue(0)
//    }
//    private val _addToCartResult = MutableLiveData<ResultWrapper<Boolean>>()
//    val addToCartResult : LiveData<ResultWrapper<Boolean>>
//        get() = _addToCartResult
//
//    fun add(){
//        val count = (productCountLiveData.value ?: 0) + 1
//        productCountLiveData.postValue(count)
//        priceLivedata.postValue(product?.price?.times(count)?: 0.0)
//    }
//
//    fun minus(){
//        if((productCountLiveData.value?: 0)<=0) 1 else productCountLiveData.value?: 0
//        product?.let {
//            menuRepository.
//        }
//    }
}
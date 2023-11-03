package com.catnip.rullfood.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catnip.rullfood.data.repository.MenuRepository
import com.catnip.rullfood.model.Category
import com.catnip.rullfood.model.Menu
import com.catnip.rullfood.utils.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: MenuRepository
) : ViewModel() {
    private val _menu = MutableLiveData<ResultWrapper<List<Menu>>>()
    val menu: LiveData<ResultWrapper<List<Menu>>>
        get() = _menu

    private val _category = MutableLiveData<ResultWrapper<List<Category>>>()
    val category: LiveData<ResultWrapper<List<Category>>>
        get() = _category

    fun getMenus(category: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getMenus(category).collect() {
                _menu.postValue(it)
            }
        }
    }

    fun getCategory() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCategory().collect() {
                _category.postValue(it)
            }
        }
    }
}

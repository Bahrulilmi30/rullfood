package com.catnip.rullfood.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catnip.rullfood.data.repository.MenuRepository
import com.catnip.rullfood.data.repository.UserRepository
import com.catnip.rullfood.model.Category
import com.catnip.rullfood.model.Menu
import com.catnip.rullfood.model.User
import com.catnip.rullfood.utils.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(
    private val menuRepository: MenuRepository,
    private val userRepository: UserRepository

) : ViewModel() {

    private val _menu = MutableLiveData<ResultWrapper<List<Menu>>>()
    val menu: LiveData<ResultWrapper<List<Menu>>>
        get() = _menu

    private val _category = MutableLiveData<ResultWrapper<List<Category>>>()
    val category: LiveData<ResultWrapper<List<Category>>>
        get() = _category

    private val _userProfile = MutableLiveData<User?>()
    val userProfile: LiveData<User?>
        get() = _userProfile

    fun getCurrentUser() {
        val result = userRepository.getCurrentUser()
        _userProfile.postValue(result)
    }

    fun getMenus(category: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            menuRepository.getMenus(category).collect() {
                _menu.postValue(it)
            }
        }
    }

    fun getCategory() {
        viewModelScope.launch(Dispatchers.IO) {
            menuRepository.getCategory().collect() {
                _category.postValue(it)
            }
        }
    }
}

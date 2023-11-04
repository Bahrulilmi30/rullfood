package com.catnip.rullfood.presentation.changepforile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catnip.rullfood.data.repository.UserRepository
import com.catnip.rullfood.model.User
import com.catnip.rullfood.utils.ResultWrapper
import kotlinx.coroutines.launch

class ProfileViewModel1(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _userProfile = MutableLiveData<User?>()
    val userProfile: LiveData<User?>
        get() = _userProfile

    private val _updateResultProfile = MutableLiveData<ResultWrapper<Boolean>>()
    val updateResultProfile: LiveData<ResultWrapper<Boolean>>
        get() = _updateResultProfile

    fun getCurrentUser() {
        val result = userRepository.getCurrentUser()
        _userProfile.postValue(result)
    }
    fun updateProfile(fullName: String) {
        viewModelScope.launch {
            userRepository.updateProfile(fullName).collect {
                _updateResultProfile.postValue(it)
            }
        }
    }
}

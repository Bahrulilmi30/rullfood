package com.catnip.rullfood.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.catnip.rullfood.data.repository.UserRepository
import com.catnip.rullfood.model.User
import com.catnip.rullfood.utils.ResultWrapper


class ProfileViewModel(
    private val userRepository: UserRepository
) : ViewModel(){
    private val _userProfile = MutableLiveData<User?>()
    val userProfile : LiveData<User?>
        get() = _userProfile

    fun getCurrentUser(){
        val result = userRepository.getCurrentUser()
        _userProfile.postValue(result)
    }
}

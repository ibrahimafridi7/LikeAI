package com.likeai.ecommerce.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.likeai.ecommerce.data.repository.AuthRepository
import com.likeai.ecommerce.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : BaseViewModel() {

    private val _signupSuccess = MutableLiveData<Unit>()
    val signupSuccess: LiveData<Unit> = _signupSuccess

    fun signup(name: String, email: String, password: String) {
        launchDataLoad(
            execution = { authRepository.signUp(email, password, name) },
            onSuccess = { _signupSuccess.value = Unit }
        )
    }
} 
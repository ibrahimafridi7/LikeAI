package com.likeai.ecommerce.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    protected fun showLoading() {
        _loading.value = true
    }

    protected fun hideLoading() {
        _loading.value = false
    }

    protected fun showError(message: String) {
        _error.value = message
    }

    protected fun <T> launchDataLoad(
        execution: suspend () -> Result<T>,
        onSuccess: (T) -> Unit,
        onError: ((Exception) -> Unit)? = null
    ) {
        viewModelScope.launch {
            try {
                showLoading()
                val result = execution()
                hideLoading()
                result.fold(
                    onSuccess = { onSuccess(it) },
                    onFailure = { e ->
                        onError?.invoke(e as Exception) ?: showError(e.message ?: "Unknown error")
                    }
                )
            } catch (e: Exception) {
                hideLoading()
                onError?.invoke(e) ?: showError(e.message ?: "Unknown error")
            }
        }
    }

    protected fun <T> launchFlowCollection(
        flow: Flow<T>,
        onCollect: suspend (T) -> Unit,
        onError: ((Exception) -> Unit)? = null
    ) {
        viewModelScope.launch {
            try {
                flow.collect { value ->
                    onCollect(value)
                }
            } catch (e: Exception) {
                onError?.invoke(e) ?: showError(e.message ?: "Unknown error")
            }
        }
    }
} 
package com.cleteci.redsolidaria.viewModels


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable


abstract class BaseViewModel : ViewModel() {

    val compositeDisposable by lazy { CompositeDisposable() }
    val status = MutableLiveData<QueryStatus>()

    enum class QueryStatus {
        NOTIFY_LOADING,
        NOTIFY_SUCCESS,
        NOTIFY_FAILURE,
        NOTIFY_UNKNOWN_HOST_FAILURE,
        ORGANIZATION_NOT_FOUND,
        EMAIL_ALREADY_REGISTERED }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}
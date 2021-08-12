package com.cleteci.redsolidaria.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {
    val compositeDisposable by lazy { CompositeDisposable() }
    val status = MutableLiveData<QueryStatus>()
    enum class QueryStatus { NOTIFY_LOADING, NOTIFY_SUCCESS, NOTIFY_FAILURE, ORGANIZATION_NOT_FOUND }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}
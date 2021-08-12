package com.cleteci.redsolidaria.viewModels

import android.util.Base64
import android.util.Log
import com.apollographql.apollo.api.Response
import com.cleteci.redsolidaria.BaseApp
import com.cleteci.redsolidaria.LoginUserMutation
import com.cleteci.redsolidaria.models.User
import com.cleteci.redsolidaria.network.GraphQLController
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.nio.charset.Charset


class UserAccountViewModel(private val graphQLController: GraphQLController) : BaseViewModel() {
    private var email: String? = null
    private var password: String? = null

    fun login(email: String?, password: String?) {
        status.value = QueryStatus.NOTIFY_LOADING
        this.email = email
        this.password = password
        compositeDisposable.add(
            graphQLController.login(email!!, password!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { dataResponse: Response<LoginUserMutation.Data>, throwable: Throwable? ->
                    if (dataResponse.data == null || dataResponse.hasErrors()) {
                        status.value = QueryStatus.NOTIFY_FAILURE
                        if (throwable != null) Log.d(TAG, throwable.message)

                    } else {
                        val token = dataResponse.data?.login().toString()
                        val user = getUser(token)
                        BaseApp.prefs.login_later = false
                        BaseApp.prefs.is_provider_service = user.role == "admin"
                        BaseApp.prefs.user_saved = user.id
                        BaseApp.prefs.token = token

                        if (user.role == "admin") {
                            //getInfoOrganization()
                        }
                        status.value = QueryStatus.NOTIFY_SUCCESS
                    }
                })
    }

    private fun getUser(response: String): User {
        val token: List<String> = response.split(".")
        val payload = token[1]
        val decodedBytes: ByteArray = Base64.decode(payload, Base64.URL_SAFE)
        val decodedData = String(decodedBytes, Charset.forName("UTF-8"))
        val formatter = Gson()
        return formatter.fromJson(decodedData, User::class.java)
    }

    companion object {
        const val TAG: String = "UserAccountViewModel"

    }

}
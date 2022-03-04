package com.cleteci.redsolidaria.viewModels


import android.util.Base64
import android.util.Log
import com.apollographql.apollo.api.Response
import com.cleteci.redsolidaria.*
import com.cleteci.redsolidaria.models.User
import com.cleteci.redsolidaria.network.GraphQLController
import com.facebook.FacebookSdk.getApplicationContext
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.net.UnknownHostException
import java.nio.charset.Charset


class UserAccountViewModel(private val graphQLController: GraphQLController) : BaseViewModel() {

    fun createUser(name: String, lastName: String, email: String, password: String){
        status.value = QueryStatus.NOTIFY_LOADING
        compositeDisposable.add(
            graphQLController.createUser(name, lastName, email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<RegisterUserMutation.Data> ->
                   if (response.data?.CreateUser() == true) {
                       status.value = QueryStatus.NOTIFY_SUCCESS
                   } else {
                       status.value = QueryStatus.EMAIL_ALREADY_REGISTERED
                   }
                }, {
                    status.value = QueryStatus.NOTIFY_FAILURE
                    Log.d(TAG, it.message)
                })
        )
    }

    fun login(email: String?, password: String?) {
        status.value = QueryStatus.NOTIFY_LOADING
        compositeDisposable.add(
            graphQLController.login(email!!, password!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ dataResponse: Response<LoginUserMutation.Data> ->
                    if (dataResponse.data == null || dataResponse.hasErrors()) {
                        status.value = QueryStatus.NOTIFY_FAILURE
                    } else {
                        val token = dataResponse.data?.login().toString()
                        val user = getUser(token)
                        BaseApp.sharedPreferences.loginLater = false
                        BaseApp.sharedPreferences.isProviderService = user.role == "admin"
                        BaseApp.sharedPreferences.userSaved = user.id
                        BaseApp.sharedPreferences.userInfoToDisplay = getApplicationContext()
                            .getString(R.string.user_info_to_display, user.id, user.name, user.lastName?:"", email)
                        BaseApp.sharedPreferences.token = token

                        status.value = QueryStatus.NOTIFY_SUCCESS
                    }
                },
                    {
                        Log.d(TAG, it.message)
                        status.value = if (it.cause is UnknownHostException) {
                            QueryStatus.NOTIFY_UNKNOWN_HOST_FAILURE
                        } else {
                            QueryStatus.NOTIFY_FAILURE
                        }
                    })
        )
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
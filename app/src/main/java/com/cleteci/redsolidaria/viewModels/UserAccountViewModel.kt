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

    fun createUser(name: String, lastName: String, email: String, password: String) {
        status.value = QueryStatus.NOTIFY_LOADING
        compositeDisposable.add(
            graphQLController.createUser(name, lastName, email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<RegisterUserMutation.Data> ->
                    if (response.hasErrors()) {
                        Log.e(TAG, "GraphQL register errors: ${response.errors?.joinToString("; ") { it.message }}")
                    }
                    if (response.data?.CreateUser() == true) {
                        status.value = QueryStatus.NOTIFY_SUCCESS
                    } else {
                        status.value = QueryStatus.EMAIL_ALREADY_REGISTERED
                    }
                }, {
                    status.value = QueryStatus.NOTIFY_FAILURE
                    Log.e(TAG, "Failed to create user: ${it.message}", it)
                })
        )
    }

    fun login(email: String?, password: String?) {
        if (email.isNullOrBlank() || password.isNullOrBlank()) {
            Log.e(TAG, "Login aborted: email or password is blank")
            status.value = QueryStatus.NOTIFY_FAILURE
            return
        }

        status.value = QueryStatus.NOTIFY_LOADING
        compositeDisposable.add(
            graphQLController.login(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ dataResponse: Response<LoginUserMutation.Data> ->
                    if (dataResponse.data == null || dataResponse.hasErrors()) {
                        Log.e(TAG, "Login GraphQL errors: ${dataResponse.errors?.joinToString("; ") { it.message }}")
                        status.value = QueryStatus.NOTIFY_FAILURE
                    } else {
                        val token = dataResponse.data?.login().orEmpty()
                        val user = getUser(token)
                        if (user == null || user.id.isNullOrBlank()) {
                            Log.e(TAG, "Failed to decode valid user from auth token")
                            status.value = QueryStatus.NOTIFY_FAILURE
                            return@subscribe
                        }

                        BaseApp.sharedPreferences.loginLater = false
                        BaseApp.sharedPreferences.isProviderService = user.role == "admin"
                        BaseApp.sharedPreferences.userSaved = user.id
                        BaseApp.sharedPreferences.userInfoToDisplay = getApplicationContext()
                            .getString(R.string.user_info_to_display, user.id, user.name, user.lastName ?: "", email)
                        BaseApp.sharedPreferences.token = token

                        status.value = QueryStatus.NOTIFY_SUCCESS
                    }
                }, {
                    Log.e(TAG, "Login network/query error: ${it.message}", it)
                    status.value = if (it.cause is UnknownHostException || it is UnknownHostException) {
                        QueryStatus.NOTIFY_UNKNOWN_HOST_FAILURE
                    } else {
                        QueryStatus.NOTIFY_FAILURE
                    }
                })
        )
    }

    private fun getUser(response: String): User? {
        return try {
            val token: List<String> = response.split(".")
            if (token.size > 1) {
                val payload = token[1]
                val decodedBytes: ByteArray = Base64.decode(payload, Base64.URL_SAFE)
                val decodedData = String(decodedBytes, Charset.forName("UTF-8"))
                Gson().fromJson(decodedData, User::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error decoding JWT token payload", e)
            null
        }
    }

    companion object {
        const val TAG: String = "UserAccountViewModel"
    }
}

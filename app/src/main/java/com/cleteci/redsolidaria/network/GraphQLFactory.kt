package com.cleteci.redsolidaria.network

import com.apollographql.apollo.ApolloClient
import com.cleteci.redsolidaria.BuildConfig
import com.cleteci.redsolidaria.util.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit


class GraphQLFactory {

    val graphQLAppServicesInstance: ApolloClient by lazy {
        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.NONE
            ))
            .build()

        getGraphQLInstance(client)
    }

    private fun getGraphQLInstance(okHttpClient: OkHttpClient): ApolloClient {
        return ApolloClient.builder()
            .serverUrl(Constants.BASE_URL + Constants.SLASH)
            .okHttpClient(okHttpClient)
            .build()
    }
}
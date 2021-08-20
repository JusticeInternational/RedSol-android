package com.cleteci.redsolidaria.network


import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.cache.CacheHeaders
import com.apollographql.apollo.rx2.rxMutate
import com.apollographql.apollo.rx2.rxQuery
import com.cleteci.redsolidaria.*
import io.reactivex.Observable
import io.reactivex.Single


class GraphQLController(private val apolloClient: ApolloClient) {

    fun login(email: String, password: String): Single<Response<LoginUserMutation.Data>> {
        val loginMutation = LoginUserMutation(email, password)
        return apolloClient.rxMutate(loginMutation) {
            cacheHeaders(CacheHeaders.NONE)
        }
    }

    fun getOrganization(id: String): Observable<Response<GetOrganizationInfoQuery.Data>> {
        val query = GetOrganizationInfoQuery(id)
        return apolloClient.rxQuery(query) {
            cacheHeaders(CacheHeaders.NONE)
        }
    }

    fun getOrganizationServicesAndCategories(id: String): Observable<Response<GetOrganizationServicesAndCategoriesQuery.Data>> {
        val query = GetOrganizationServicesAndCategoriesQuery(id)
        return apolloClient.rxQuery(query) {
            cacheHeaders(CacheHeaders.NONE)
        }
    }

    fun getUsedCategories(): Observable<Response<GetUsedCategoriesQuery.Data>> {
        val query = GetUsedCategoriesQuery()
        return apolloClient.rxQuery(query) {
            cacheHeaders(CacheHeaders.NONE)
        }
    }

    fun getOrganizationsByCategory(id: String, keyWord: String): Observable<Response<GetOrganizationsByCategoryQuery.Data>> {
        val query = GetOrganizationsByCategoryQuery(id, keyWord)
        return apolloClient.rxQuery(query) {
            cacheHeaders(CacheHeaders.NONE)
        }
    }

}
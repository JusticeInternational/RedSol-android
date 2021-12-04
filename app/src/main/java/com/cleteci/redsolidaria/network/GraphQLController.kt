package com.cleteci.redsolidaria.network

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.cache.CacheHeaders
import com.apollographql.apollo.rx2.rxMutate
import com.apollographql.apollo.rx2.rxQuery
import com.cleteci.redsolidaria.*
import com.cleteci.redsolidaria.viewModels.OrganizationViewModel
import io.reactivex.Observable
import io.reactivex.Single

class GraphQLController(private val apolloClient: ApolloClient) {

    fun createUser(name: String, lastName: String, email: String, password: String):
        Single<Response<RegisterUserMutation.Data>> {
        val mutation = RegisterUserMutation(name, lastName, email, password)
        return apolloClient.rxMutate(mutation) {
            cacheHeaders(CacheHeaders.NONE)
        }
    }

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

    fun getCategories(): Observable<Response<GetCategoriesQuery.Data>> {
        val query = GetCategoriesQuery()
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

    fun getOrganizationsByCategory(id: String, keyWord: String):Observable<Response<GetOrganizationsByCategoryQuery.Data>> {
        val query = GetOrganizationsByCategoryQuery(id, keyWord)
        return apolloClient.rxQuery(query) {
            cacheHeaders(CacheHeaders.NONE)
        }
    }

    fun getTotalCategoryAttentions(categoryId: String): Observable<Response<GetTotalCategoryAttentionsQuery.Data>> {
        val query = GetTotalCategoryAttentionsQuery(categoryId,
            BaseApp.sharedPreferences.currentOrganizationId.toString())
        return apolloClient.rxQuery(query) {
            cacheHeaders(CacheHeaders.NONE)
        }
    }

    fun getTotalServiceAttentions(serviceId: String): Observable<Response<GetTotalServiceAttentionsQuery.Data>> {
        val query = GetTotalServiceAttentionsQuery(serviceId,
            BaseApp.sharedPreferences.currentOrganizationId.toString())
        return apolloClient.rxQuery(query) {
            cacheHeaders(CacheHeaders.NONE)
        }
    }

    fun createAttentionCategory(request: OrganizationViewModel.CreateAttentionRequest):
        Single<Response<ProvideAtentionUnregisteredCategoryMutation.Data>> {
        val mutation = ProvideAtentionUnregisteredCategoryMutation(
            BaseApp.sharedPreferences.currentOrganizationId.toString(),
            request.categoryId,
            request.name,
            request.lastName,
            request.identification,
            request.gender,
            request.country,
            request.age,
            request.phone,
            request.email,
            request.otherInfo
        )
        return apolloClient.rxMutate(mutation) {
            cacheHeaders(CacheHeaders.NONE)
        }
    }

    fun createAttentionService(request: OrganizationViewModel.CreateAttentionRequest):
        Single<Response<ProvideAtentionUnregisteredServiceMutation.Data>> {
        val mutation = ProvideAtentionUnregisteredServiceMutation(
            BaseApp.sharedPreferences.currentOrganizationId.toString(),
            request.serviceId,
            request.name,
            request.lastName,
            request.identification,
            request.gender,
            request.country,
            request.age,
            request.phone,
            request.email,
            request.otherInfo
        )
        return apolloClient.rxMutate(mutation) {
            cacheHeaders(CacheHeaders.NONE)
        }
    }
}
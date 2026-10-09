package com.cleteci.redsolidaria.network

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.cache.CacheHeaders
import com.apollographql.apollo.rx2.rxMutate
import com.apollographql.apollo.rx2.rxQuery
import com.cleteci.redsolidaria.*
import com.cleteci.redsolidaria.type.UserGroup
import com.cleteci.redsolidaria.viewModels.OrganizationViewModel
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

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

    fun getOrganizationByUserId(userId: String): Observable<Response<GetOrganizationByUserIdQuery.Data>> {
        val query = GetOrganizationByUserIdQuery(userId)
        return apolloClient.rxQuery(query) {
            cacheHeaders(CacheHeaders.NONE)
        }
    }

    fun getOrganizationById(organizationId: String): Observable<Response<GetOrganizationByIdQuery.Data>> {
        val query = GetOrganizationByIdQuery(organizationId)
        return apolloClient.rxQuery(query) {
            cacheHeaders(CacheHeaders.NONE)
        }
    }

    fun getServiceCategories(): Observable<Response<GetServiceCategoriesQuery.Data>> {
        val query = GetServiceCategoriesQuery()
        return apolloClient.rxQuery(query) {
            cacheHeaders(CacheHeaders.NONE)
        }
    }

    fun getServices(): Observable<Response<GetServicesQuery.Data>> {
        val query = GetServicesQuery()
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
        val orgId = BaseApp.sharedPreferences.currentOrganizationId ?: ""
        val query = GetTotalCategoryAttentionsQuery(categoryId, orgId)
        return apolloClient.rxQuery(query) {
            cacheHeaders(CacheHeaders.NONE)
        }
    }

    fun getTotalServiceAttentions(serviceId: String): Observable<Response<GetTotalServiceAttentionsQuery.Data>> {
        val orgId = BaseApp.sharedPreferences.currentOrganizationId ?: ""
        val query = GetTotalServiceAttentionsQuery(serviceId, orgId)
        return apolloClient.rxQuery(query) {
            cacheHeaders(CacheHeaders.NONE)
        }
    }

    fun createAttentionCategory(request: OrganizationViewModel.CreateAttentionRequest):
        Single<Response<ProvideAtentionUnregisteredCategoryMutation.Data>> {
        val orgId = BaseApp.sharedPreferences.currentOrganizationId ?: ""
        val mutation = ProvideAtentionUnregisteredCategoryMutation(
            orgId,
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
        val orgId = BaseApp.sharedPreferences.currentOrganizationId ?: ""
        val mutation = ProvideAtentionUnregisteredServiceMutation(
            orgId,
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

    fun createOrganization(request: OrganizationViewModel.CreateOrganizationRequest):
            Single<Response<CreateOrganizationMutation.Data>> {
        val mutation = CreateOrganizationMutation(
            request.id,
            request.name,
            request.webPage,
            request.phone,
            request.ranking,
            request.hourHand,
            request.description,
            request.servicesDesc,
            request.iconName,
            request.urlIcon
        )
        return apolloClient.rxMutate(mutation) {
            cacheHeaders(CacheHeaders.NONE)
        }
    }

    fun associateOrganizationToUser(userId: String, organizationId: String):
        Single<Response<AssociateOrganizationToUserMutation.Data>> {
        val mutation = AssociateOrganizationToUserMutation(
            userId,
            organizationId
        )
        return apolloClient.rxMutate(mutation) {
            cacheHeaders(CacheHeaders.NONE)
        }
    }

    fun updateUserRoleToAdmin(userId: String):
        Single<Response<UpdateUserRoleMutation.Data>> {
        val mutation = UpdateUserRoleMutation(
            userId,
            UserGroup.ADMIN
        )
        return apolloClient.rxMutate(mutation) {
            cacheHeaders(CacheHeaders.NONE)
        }
    }

    fun createServiceDirect(
        name: String,
        description: String,
        isGeneral: Boolean,
        organizationId: String,
        serviceCategoryId: String,
        callback: (success: Boolean, errorMessage: String?) -> Unit
    ) {
        val client = okhttp3.OkHttpClient.Builder()
            .connectTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .build()

        val jsonBody = org.json.JSONObject().apply {
            put(
                "query",
                "mutation CreateService(\$name: String!, \$description: String!, \$isGeneral: Boolean, \$organizationId: ID!, \$serviceCategoryId: ID!) { CreateService(name: \$name, description: \$description, isGeneral: \$isGeneral, organizationId: \$organizationId, serviceCategoryId: \$serviceCategoryId) { id name description } }"
            )
            val vars = org.json.JSONObject().apply {
                put("name", name)
                put("description", description)
                put("isGeneral", isGeneral)
                put("organizationId", organizationId)
                put("serviceCategoryId", serviceCategoryId)
            }
            put("variables", vars)
        }

        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = jsonBody.toString().toRequestBody(mediaType)
        val request = okhttp3.Request.Builder()
            .url(com.cleteci.redsolidaria.util.Constants.BASE_URL)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                android.util.Log.e("GraphQLController", "Failed to create service", e)
                callback(false, e.message)
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                response.use { res ->
                    if (!res.isSuccessful) {
                        callback(false, "Server error: ${res.code}")
                        return
                    }
                    val bodyString = res.body?.string().orEmpty()
                    try {
                        val json = org.json.JSONObject(bodyString)
                        if (json.has("errors")) {
                            val errorsArray = json.getJSONArray("errors")
                            val firstMsg = if (errorsArray.length() > 0) errorsArray.getJSONObject(0).optString("message") else "Unknown error"
                            callback(false, firstMsg)
                        } else if (json.has("data") && !json.isNull("data")) {
                            val dataObj = json.getJSONObject("data")
                            if (dataObj.has("CreateService") && !dataObj.isNull("CreateService")) {
                                callback(true, null)
                            } else {
                                callback(false, "Failed to create service")
                            }
                        } else {
                            callback(false, "Invalid response from server")
                        }
                    } catch (e: Exception) {
                        android.util.Log.e("GraphQLController", "Failed to parse CreateService response", e)
                        callback(false, e.message)
                    }
                }
            }
        })
    }
}
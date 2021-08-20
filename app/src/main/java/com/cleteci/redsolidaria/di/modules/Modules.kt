package com.cleteci.redsolidaria.di.modules

import com.cleteci.redsolidaria.network.GraphQLController
import com.cleteci.redsolidaria.network.GraphQLFactory
import com.cleteci.redsolidaria.viewModels.GeneralViewModel
import com.cleteci.redsolidaria.viewModels.OrganizationViewModel
import com.cleteci.redsolidaria.viewModels.UserAccountViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module


private const val GRAPH_QL_CLIENT = "graphQLClient"

val appModule = module {
    //NetworkClients
    single(GRAPH_QL_CLIENT) { GraphQLFactory().graphQLAppServicesInstance }
}

val controllersModule = module {
    single { GraphQLController(get(GRAPH_QL_CLIENT)) }
}

//Don't forget to add the constructor dependencies to viewModels (Crash will occur on release build)
val viewModelsModule = module {
    viewModel { GeneralViewModel(get()) }
    viewModel { UserAccountViewModel(get()) }
    viewModel { OrganizationViewModel(get()) }
}
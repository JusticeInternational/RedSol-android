package com.cleteci.redsolidaria.di.modules

import com.cleteci.redsolidaria.network.GraphQLController
import com.cleteci.redsolidaria.network.GraphQLFactory
import com.cleteci.redsolidaria.viewModels.BeneficiaryViewModel
import com.cleteci.redsolidaria.viewModels.GeneralViewModel
import com.cleteci.redsolidaria.viewModels.OrganizationViewModel
import com.cleteci.redsolidaria.viewModels.UserAccountViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module


private const val GRAPH_QL_CLIENT = "graphQLClient"

val appModule = module {
    //NetworkClients
    single(named(GRAPH_QL_CLIENT)) { GraphQLFactory().graphQLAppServicesInstance }
}

val controllersModule = module {
    single { GraphQLController(get(named(GRAPH_QL_CLIENT))) }
}

//Don't forget to add the constructor dependencies to viewModels (Crash will occur on release build)
val viewModelsModule = module {
    viewModel { BeneficiaryViewModel(get()) }
    viewModel { GeneralViewModel(get()) }
    viewModel { OrganizationViewModel(get()) }
    viewModel { UserAccountViewModel(get()) }

}
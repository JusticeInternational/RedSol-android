package com.cleteci.redsolidaria.ui.fragments.createService

import com.cleteci.redsolidaria.network.GraphQLController
import io.reactivex.disposables.CompositeDisposable

class CreateServicePresenter(
    private val graphQLController: GraphQLController
) : CreateServiceContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: CreateServiceContract.View

    override fun subscribe() {}

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: CreateServiceContract.View) {
        this.view = view
        view.init()
    }

    override fun createService(
        name: String,
        description: String,
        isGeneral: Boolean,
        organizationId: String,
        serviceCategoryId: String
    ) {
        view.onServiceLoading(true)
        graphQLController.createServiceDirect(
            name,
            description,
            isGeneral,
            organizationId,
            serviceCategoryId
        ) { success, errorMessage ->
            view.onServiceLoading(false)
            if (success) {
                view.onServiceCreatedSuccess("Servicio creado exitosamente")
            } else {
                view.onServiceCreatedError(errorMessage ?: "Error al crear el servicio")
            }
        }
    }
}

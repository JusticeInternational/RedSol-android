package com.cleteci.redsolidaria.ui.fragments.infoService

import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.cleteci.redsolidaria.*
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ogulcan on 07/02/2018.
 */
class ScanNoUserPresenter: ScanNoUserContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: ScanNoUserContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: ScanNoUserContract.View) {
        this.view = view
        view.init() // as default
    }

    override fun validateAtentionUnregisteredCategory(
        orgID: String,
        categoryID: String,
        name: String?,
        lastName: String?,
        identification: String?,
        gender: Int?,
        country: String?,
        age: Int?,
        phone: String?,
        email: String?,
        otherInfo: String?,
        nameCat: String?
    ) {

        //var flat=true

        if (name==null || name.isEmpty()){
            //    flat=false
            Toast.makeText(BaseApp.instance, BaseApp.instance.getString(R.string.invalid_name), Toast.LENGTH_SHORT).show()
            return
        }

        if (lastName==null || lastName.isEmpty()){
            //  flat=false
            Toast.makeText(BaseApp.instance, BaseApp.instance.getString(R.string.invalid_lastname), Toast.LENGTH_SHORT).show()
            return
        }



        if (email!=null && !email.isEmpty() && !email.isValidEmail() ){
            Toast.makeText(BaseApp.instance, BaseApp.instance.getString(R.string.invalid_email), Toast.LENGTH_SHORT).show()
            return
        }

        if (email!=null && !email.isEmpty() && !email.isValidPhone() ){
            Toast.makeText(BaseApp.instance, BaseApp.instance.getString(R.string.invalid_phone), Toast.LENGTH_SHORT).show()
            return
        }


        atentionUnregisteredCategory(
            orgID,
            categoryID,
            name,
            lastName,
            identification,
            gender,
            country,
            age,
            phone,
            email,
            otherInfo,
            nameCat

        )
    }

    override fun validateAtentionUnregisteredService(
        orgID: String,
        serviceID: String,
        name: String?,
        lastName: String?,
        identification: String?,
        gender: Int?,
        country: String?,
        age: Int?,
        phone: String?,
        email: String?,
        otherInfo: String?,
        nameService: String?,
        isGeneric: Boolean
    ) {

       //var flat=true

        if (name==null || name.isEmpty()){
        //    flat=false
            Toast.makeText(BaseApp.instance, BaseApp.instance.getString(R.string.invalid_name), Toast.LENGTH_SHORT).show()
            return
        }

        if (lastName==null || lastName.isEmpty()){
          //  flat=false
            Toast.makeText(BaseApp.instance, BaseApp.instance.getString(R.string.invalid_lastname), Toast.LENGTH_SHORT).show()
            return
        }



        if (email!=null && !email.isEmpty() && !email.isValidEmail() ){
            Toast.makeText(BaseApp.instance, BaseApp.instance.getString(R.string.invalid_email), Toast.LENGTH_SHORT).show()
            return
        }

       /* if (email!=null && !email.isEmpty() && !email.isValidPhone() ){
            Toast.makeText(BaseApp.instance, BaseApp.instance.getString(R.string.invalid_phone), Toast.LENGTH_SHORT).show()
            return
        }*/


         atentionUnregisteredService(
            orgID,
            serviceID,
            name,
            lastName,
            identification,
            gender,
            country,
            age,
            phone,
            email,
            otherInfo,
            nameService,
            isGeneric
        )



    }

    fun CharSequence?.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

    fun CharSequence?.isValidPhone() = !isNullOrEmpty() && Patterns.PHONE.matcher(this).matches()


    override fun atentionUnregisteredCategory(
        orgID: String,
        categoryID: String,
        name: String,
        lastName: String,
        identification: String?,
        gender: Int?,
        country: String?,
        age: Int?,
        phone: String?,
        email: String?,
        otherInfo: String?,
        nameCat:String?
    ) {

        BaseApp.apolloClient.mutate(
            ProvideAtentionUnregisteredCategoryMutation.builder()
                .orgID(orgID)
                .categoryID(categoryID)
                .name(name)
                .lastName(lastName)
                .identification(identification)
                .gender(gender).country(country)
                .age(age).phone(phone)
                .email(email)
                .otherInfo(otherInfo)
                .build()
        ).enqueue(object : ApolloCall.Callback<ProvideAtentionUnregisteredCategoryMutation.Data>() {
            override fun onResponse(response: Response<ProvideAtentionUnregisteredCategoryMutation.Data>) {
                if (response.data() != null) {

                    view.loadDataSuccess( String.format(BaseApp.instance.getResources().getString(R.string.posted_category),name,nameCat ))

                } else {
                    view.loadDataError( BaseApp.instance.getResources().getString(R.string.error_posted_category))
                }
            }

            override fun onFailure(e: ApolloException) {
                view.loadDataError( BaseApp.instance.getResources().getString(R.string.error_posted_category))
            }
        })

    }

    override fun atentionUnregisteredService(
        orgID: String,
        serviceID: String,
        name: String,
        lastName: String,
        identification: String?,
        gender: Int?,
        country: String?,
        age: Int?,
        phone: String?,
        email: String?,
        otherInfo: String?,
        nameService:String?,
        isGeneric:Boolean
    ) {




        BaseApp.apolloClient.mutate(
            ProvideAtentionUnregisteredServiceMutation.builder()
                .orgID(orgID)
                .serviceID(serviceID)
                .name(name)
                .lastName(lastName)
                .identification(identification)
                .gender(gender).country(country)
                .age(age).phone(phone)
                .email(email)
                .otherInfo(otherInfo)
                .build()
        ).enqueue(object : ApolloCall.Callback<ProvideAtentionUnregisteredServiceMutation.Data>() {
            override fun onResponse(response: Response<ProvideAtentionUnregisteredServiceMutation.Data>) {
                if (response.data() != null) {

                    if (isGeneric){
                        view.loadDataSuccess( String.format(BaseApp.instance.getResources().getString(R.string.posted_service_2),name ))
                    }else{
                        view.loadDataError( String.format(BaseApp.instance.getResources().getString(R.string.posted_service),name, nameService ))
                    }

                } else {
                    view.loadDataError( BaseApp.instance.getResources().getString(R.string.error_posted_service))
                }
            }

            override fun onFailure(e: ApolloException) {
                view.loadDataError( BaseApp.instance.getResources().getString(R.string.error_posted_service))
            }
        })



    }




}
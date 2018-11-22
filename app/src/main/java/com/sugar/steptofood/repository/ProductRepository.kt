package com.sugar.steptofood.repository

import android.arch.lifecycle.*
import com.sugar.steptofood.utils.extension.smartSubscribe
import com.sugar.steptofood.model.fullinfo.*
import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.utils.NetworkState

class ProductRepository(private val api: ApiService) : BaseStatusRepository() {

    fun getAllProducts(): LiveData<List<FullProductInfo>> {
        status.postValue(NetworkState.LOADING)
        val products = MutableLiveData<List<FullProductInfo>>()
        api.getAllProducts()
                .smartSubscribe({
                    status.postValue(NetworkState.LOADED)
                    products.postValue(it)
                }, onError())
        return products
    }

    fun searchProducts(name: String): LiveData<List<FullProductInfo>> {
        status.postValue(NetworkState.LOADING)
        val products = MutableLiveData<List<FullProductInfo>>()
        api.searchProducts(name)
                .smartSubscribe({
                    status.postValue(NetworkState.LOADED)
                    products.postValue(it)
                }, onError())
        return products
    }
}
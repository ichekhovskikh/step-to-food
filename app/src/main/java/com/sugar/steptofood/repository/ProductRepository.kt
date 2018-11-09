package com.sugar.steptofood.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.sugar.steptofood.extension.customSubscribe
import com.sugar.steptofood.model.Product
import com.sugar.steptofood.rest.ApiService

class ProductRepository(private val api: ApiService) : BaseRepository() {

    fun getAllProducts(): LiveData<List<Product>> {
        liveStatus.postValue(LoadingStatus.LOADING)
        val products = MutableLiveData<List<Product>>()
        api.getAllProducts()
                .customSubscribe({
                    liveStatus.postValue(LoadingStatus.LOADED)
                    products.postValue(it)
                }, { errorMessage.postValue(it) })
        return products
    }

    fun searchProducts(name: String): LiveData<List<Product>> {
        liveStatus.postValue(LoadingStatus.LOADING)
        val products = MutableLiveData<List<Product>>()
        api.searchProducts(name)
                .customSubscribe({
                    liveStatus.postValue(LoadingStatus.LOADED)
                    products.postValue(it)
                }, { errorMessage.postValue(it) })
        return products
    }
}
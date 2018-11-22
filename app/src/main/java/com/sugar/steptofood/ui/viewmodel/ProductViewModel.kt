package com.sugar.steptofood.ui.viewmodel

import android.arch.lifecycle.*
import com.sugar.steptofood.App
import com.sugar.steptofood.model.fullinfo.FullProductInfo
import com.sugar.steptofood.repository.ProductRepository
import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.utils.NetworkState
import javax.inject.Inject

class ProductViewModel : ViewModel() {

    @Inject
    lateinit var api: ApiService
        protected set

    private val productRepository by lazy { ProductRepository(api) }

    init {
        App.appComponent.inject(this)
    }

    fun getAll(): LiveData<List<FullProductInfo>> {
        return productRepository.getAllProducts()
    }

    fun search(name: String): LiveData<List<FullProductInfo>> {
        return productRepository.searchProducts(name)
    }

    fun getLoadingStatus(): LiveData<NetworkState> {
        return productRepository.loadingStatus()
    }
}
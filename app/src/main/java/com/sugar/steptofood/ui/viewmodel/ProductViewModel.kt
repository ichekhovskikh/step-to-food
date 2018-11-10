package com.sugar.steptofood.ui.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.sugar.steptofood.App
import com.sugar.steptofood.model.Product
import com.sugar.steptofood.repository.BaseRepository
import com.sugar.steptofood.repository.ProductRepository
import com.sugar.steptofood.rest.ApiService
import javax.inject.Inject

class ProductViewModel : ViewModel() {

    @Inject
    lateinit var api: ApiService
        protected set

    private val productRepository by lazy { ProductRepository(api) }

    init {
        App.appComponent.inject(this)
    }

    fun getAll(): LiveData<List<Product>> {
        return productRepository.getAllProducts()
    }

    fun search(name: String): LiveData<List<Product>> {
        return productRepository.searchProducts(name)
    }

    fun getLoadingStatus(): LiveData<BaseRepository.LoadingStatus> {
        return productRepository.loadingStatus()
    }

    fun getErrorMessage(): LiveData<String> {
        return productRepository.error()
    }
}
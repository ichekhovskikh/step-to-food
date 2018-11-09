package com.sugar.steptofood.ui.viewmodel

import android.arch.lifecycle.ViewModel
import com.sugar.steptofood.repository.ProductRepository
import com.sugar.steptofood.rest.ApiService
import javax.inject.Inject

class ProductViewModel : ViewModel() {

    @Inject
    lateinit var api: ApiService

    private val productRepository by lazy { ProductRepository(api) }
}
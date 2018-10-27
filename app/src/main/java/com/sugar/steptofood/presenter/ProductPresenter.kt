package com.sugar.steptofood.presenter

import com.sugar.steptofood.extension.customSubscribe
import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.ui.view.ProductView

class ProductPresenter(view: ProductView,
                       api: ApiService) : BasePresenter<ProductView>(view, api) {

    fun getAllProducts() {
        view.onShowLoading()
        api.getAllProducts()
                .customSubscribe({
                    view.onHideLoading()
                    view.refreshProducts(it)
                }, defaultError())
    }

    fun searchProducts(name: String) {
        view.onShowLoading()
        api.searchProducts(name)
                .customSubscribe({
                    view.onHideLoading()
                    view.refreshProducts(it)
                }, defaultError())
    }
}
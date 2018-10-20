package com.sugar.steptofood.presenter

import com.sugar.steptofood.extension.customSubscribe
import com.sugar.steptofood.model.Product
import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.ui.FoodView

class ProductPresenter(view: FoodView,
                       api: ApiService) : BasePresenter<FoodView>(view, api) {

    fun getAllProducts(onSuccess: ((List<Product>) -> Unit)) {
//        view.onShowLoading()
        api.getAllProducts()
                .customSubscribe({
                    //view.onHideLoading()
                    onSuccess.invoke(it)
                }, defaultError())
    }
}
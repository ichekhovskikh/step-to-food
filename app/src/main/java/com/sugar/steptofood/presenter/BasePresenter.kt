package com.sugar.steptofood.presenter

import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.ui.FoodView

open class BasePresenter<V : FoodView>(protected open val view: V,
                                       protected open val api: ApiService) {

    protected fun defaultError(block: () -> Unit = {}) = { error: String ->
        /*view.onHideLoading()
        view.onShowError(error)
        block.invoke()*/
    }
}
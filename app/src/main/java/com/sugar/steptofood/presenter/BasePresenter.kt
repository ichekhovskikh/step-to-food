package com.sugar.steptofood.presenter

import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.ui.view.BaseView

open class BasePresenter<V : BaseView>(protected open val view: V,
                                       protected open val api: ApiService) {

    protected fun defaultError(block: () -> Unit = {}) = { error: String ->
        /*view.onHideLoading()
        view.onShowError(error)
        block.invoke()*/
    }
}
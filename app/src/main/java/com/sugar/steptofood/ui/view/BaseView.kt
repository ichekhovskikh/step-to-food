package com.sugar.steptofood.ui.view

@Deprecated("using view model")
interface BaseView {
    fun onShowLoading()
    fun onHideLoading()
    fun onShowError(error: String)
}
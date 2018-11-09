package com.sugar.steptofood.ui.view

import com.sugar.steptofood.model.Product

@Deprecated("using view model")
interface ProductView : BaseView {
    fun refreshProducts(products: List<Product>)
}
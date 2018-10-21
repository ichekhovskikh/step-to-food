package com.sugar.steptofood.ui.view

import com.sugar.steptofood.model.Product

interface ProductView : BaseView {
    fun refreshProducts(products: List<Product>)
}
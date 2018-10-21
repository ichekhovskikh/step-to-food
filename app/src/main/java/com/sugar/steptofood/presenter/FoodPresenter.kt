package com.sugar.steptofood.presenter

import com.sugar.steptofood.extension.customSubscribe
import com.sugar.steptofood.model.Food
import com.sugar.steptofood.model.Product
import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.ui.view.FoodView

class FoodPresenter(view: FoodView,
                    api: ApiService) : BasePresenter<FoodView>(view, api) {

    fun getRecommendedFoods(userId: Int) {
        //view.onShowLoading()
        api.getRecommendedFood(userId)
                .customSubscribe({
                    //view.onHideLoading()
                    view.refreshFoods(it)
                }, defaultError())
    }

    fun getLikeFoods(userId: Int) {
        //view.onShowLoading()
        api.getLikeFood(userId)
                .customSubscribe({
                    //view.onHideLoading()
                    view.refreshFoods(it)
                }, defaultError())
    }

    fun getAddedFoods(userId: Int) {
        //view.onShowLoading()
        api.getAddedFood(userId)
                .customSubscribe({
                    //view.onHideLoading()
                    view.refreshFoods(it)
                }, defaultError())
    }

    fun searchFoodsByName(searchField: String) {
//        view.onShowLoading()
        api.searchFoods(searchField)
                .customSubscribe({
                    //view.onHideLoading()
                    view.refreshFoods(it)
                }, defaultError())
    }

    fun searchFoodsByProduct(products: List<Product>) {
//        view.onShowLoading()
        api.searchFoodsByProduct(products)
                .customSubscribe({
                    //view.onHideLoading()
                    view.refreshFoods(it)
                }, defaultError())
    }

    fun getFood(foodId: Int, onSuccess: (food: Food) -> Unit) {
        //view.onShowLoading()
        api.getFood(foodId)
                .customSubscribe({
                    //view.onHideLoading()
                    onSuccess.invoke(it)
                }, defaultError())
    }

    fun addFood(food: Food, onSuccess: () -> Unit) {
        //view.onShowLoading()
        api.addFood(food)
                .customSubscribe({
                    //view.onHideLoading()
                    onSuccess.invoke()
                }, defaultError())
    }

    fun removeFood(foodId: Int) {
        api.removeFood(foodId)
                .customSubscribe({}, defaultError())
    }

    fun setLikeFood(foodId: Int, hasLike: Boolean) {
        api.setLike(foodId, hasLike)
                .customSubscribe({}, defaultError())
    }
}
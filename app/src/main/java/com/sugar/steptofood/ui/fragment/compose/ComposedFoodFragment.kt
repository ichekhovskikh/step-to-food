package com.sugar.steptofood.ui.fragment.compose

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.sugar.steptofood.R
import com.sugar.steptofood.model.Food
import com.sugar.steptofood.ui.fragment.BaseFragment
import com.sugar.steptofood.ui.view.FoodView
import kotlinx.android.synthetic.main.button_like.*
import kotlinx.android.synthetic.main.button_remove.*
import kotlinx.android.synthetic.main.fragment_composed_foods.*

class ComposedFoodFragment : FoodView, BaseFragment() {

    companion object {
        fun getInstance() = ComposedFoodFragment()
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        /*view?.intent?.getSerializableExtra(PRODUCTS)*/
        initAllProductsCards(view)
        addProductsCard(composedFoodContainer) //TODO test
    }

    override fun getLayout(): Int = R.layout.fragment_composed_foods

    override fun refreshFoods(foods: List<Food>) {
    }

    private fun initAllProductsCards(view: View) {
        /* TODO
        for with pagination food in foods
        addProductsCard(contentContainer, food)
        */
    }

    private fun addProductsCard(container: ViewGroup) {
        //TODO user, food, products
        //TODO replace on adapter
        /*val productsCard = CardViewFactory.createProductsCardView(inflater)
        container.addView(productsCard)
        addButtonInCornerListener()

        foodImageView.setOnClickListener {
            val intent = Intent(activity, FoodActivity::class.java)
            //TODO putExtra(food)
            startActivity(intent)
        }

        textUserNameView.setOnClickListener {
            val intent = Intent(activity, AnotherUserActivity::class.java)
            //TODO putExtra(user)
            startActivity(intent)
        }*/
    }

    private fun addProducts(container: ViewGroup) {
        //TODO user, food, products
    }

    private fun addButtonInCornerListener() {
        addLikeListenerIfButtonExists()
        addRemoveListenerIfButtonExists()
    }

    private fun addLikeListenerIfButtonExists() {
        buttonLike?.setOnCheckedChangeListener { buttonView, isChecked ->
            //TODO add/remove from db
        }
    }

    private fun addRemoveListenerIfButtonExists() {
        buttonRemove?.setOnClickListener {
            //TODO remove from db
        }
    }
}

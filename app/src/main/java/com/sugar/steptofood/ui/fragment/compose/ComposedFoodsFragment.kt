package com.sugar.steptofood.ui.fragment.compose

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.sugar.steptofood.R
import com.sugar.steptofood.ui.factory.CardViewFactory
import com.sugar.steptofood.ui.activity.AnotherUserActivity
import com.sugar.steptofood.ui.activity.FoodActivity
import com.sugar.steptofood.ui.fragment.BaseFragment
import kotlinx.android.synthetic.main.button_like.*
import kotlinx.android.synthetic.main.button_remove.*
import kotlinx.android.synthetic.main.fragment_composed_foods.*
import kotlinx.android.synthetic.main.item_small_food_info.*

class ComposedFoodsFragment : BaseFragment() {

    companion object {
        fun getInstance() = ComposedFoodsFragment()
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        /*activity?.intent?.getSerializableExtra(PRODUCTS)*/
        initAllProductsCards(view)
        addProductsCard(composedFoodsContainer) //TODO test
    }

    override fun getLayout(): Int = R.layout.fragment_composed_foods

    private fun initAllProductsCards(view: View) {
        /* TODO
        for with pagination food in foods
        addProductsCard(contentContainer, food)
        */
    }

    private fun addProductsCard(container: ViewGroup) {
        //TODO user, food, products
        val productsCard = CardViewFactory.createProductsCardView(inflater)
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
        }
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

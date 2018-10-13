package com.sugar.steptofood.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.CardView
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ToggleButton
import com.sugar.steptofood.R
import com.sugar.steptofood.ui.activity.FoodActivity
import kotlinx.android.synthetic.main.fragment_composed_foods.*

class ComposedFoodsFragment : BaseFragment() {

    companion object {
        fun getInstance(): ComposedFoodsFragment {
            return ComposedFoodsFragment()
        }
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

    @SuppressLint("InflateParams")
    private fun addProductsCard(container: ViewGroup) {
        //TODO user, food, products
        val productsCard = inflater?.inflate(R.layout.item_products_card, null) as CardView
        addLikeButton(productsCard)
        addFoodImage(productsCard)
        //TODO onclick userName -> user
        //productsCard?.tag = "id"
        //addProducts colored
        container.addView(productsCard)
    }

    @SuppressLint("InflateParams")
    fun addProducts(container: ViewGroup) {
        //TODO user, food, products
    }

    private fun addLikeButton(card: CardView) {
        val button: ToggleButton = inflater?.inflate(R.layout.button_like, null) as ToggleButton
        //TODO set initial value like
        button.setOnCheckedChangeListener { buttonView, isChecked ->
            //TODO add/remove from db
        }
        val buttonContainer = card.findViewById<FrameLayout>(R.id.buttonContainer)
        buttonContainer?.addView(button)
    }

    private fun addFoodImage(card: CardView) {
        val foodImageView = card.findViewById<ImageView>(R.id.foodImageView)
        foodImageView.setOnClickListener {
            val intent = Intent(activity, FoodActivity::class.java)
            //TODO putExtra(food)
            startActivity(intent)
        }
    }
}

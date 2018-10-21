package com.sugar.steptofood.di

import com.sugar.steptofood.di.module.ContextModule
import com.sugar.steptofood.di.module.NetworkModule
import com.sugar.steptofood.ui.activity.*
import com.sugar.steptofood.ui.fragment.recipes.RecipesFragment
import com.sugar.steptofood.ui.fragment.user.UserFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ContextModule::class, NetworkModule::class))
interface AppComponent {

    fun inject(startActivity: StartActivity)
    fun inject(foodActivity: FoodActivity)
    fun inject(addFoodActivity: AddFoodActivity)
    fun inject(searchProductActivity: SearchProductActivity)
    fun inject(recipesFragment: RecipesFragment)
    fun inject(userFragment: UserFragment)
    fun inject(userItemActivity: UserItemActivity)
}
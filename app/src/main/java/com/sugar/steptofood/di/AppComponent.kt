package com.sugar.steptofood.di

import com.sugar.steptofood.di.module.ContextModule
import com.sugar.steptofood.di.module.NetworkModule
import com.sugar.steptofood.ui.activity.*
import com.sugar.steptofood.ui.fragment.recipe.RecipeFragment
import com.sugar.steptofood.ui.fragment.user.UserFragment
import com.sugar.steptofood.ui.viewmodel.ProductViewModel
import com.sugar.steptofood.ui.viewmodel.RecipeViewModel
import com.sugar.steptofood.ui.viewmodel.UserViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ContextModule::class, NetworkModule::class])
interface AppComponent {
    fun inject(tabsActivity: TabsActivity)
    fun inject(userViewModel: UserViewModel)
    fun inject(recipeViewModel: RecipeViewModel)
    fun inject(productViewModel: ProductViewModel)
}
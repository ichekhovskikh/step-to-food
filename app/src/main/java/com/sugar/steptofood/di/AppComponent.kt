package com.sugar.steptofood.di

import com.sugar.steptofood.di.module.ContextModule
import com.sugar.steptofood.di.module.NetworkModule
import com.sugar.steptofood.ui.activity.*
import com.sugar.steptofood.ui.fragment.recipes.RecipeFragment
import com.sugar.steptofood.ui.fragment.user.UserFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ContextModule::class, NetworkModule::class])
interface AppComponent {

    fun inject(startActivity: StartActivity)
    fun inject(tabsActivity: TabsActivity)
    fun inject(recipeActivity: RecipeActivity)
    fun inject(recipeCreationActivity: RecipeCreationActivity)
    fun inject(searchProductActivity: SearchProductActivity)
    fun inject(recipeFragment: RecipeFragment)
    fun inject(userFragment: UserFragment)
    fun inject(recipeListActivity: RecipeListActivity)
}
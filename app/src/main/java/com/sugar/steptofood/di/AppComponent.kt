package com.sugar.steptofood.di

import com.sugar.steptofood.di.module.*
import com.sugar.steptofood.ui.viewmodel.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ContextModule::class, NetworkModule::class])
interface AppComponent {
    fun inject(userViewModel: UserViewModel)
    fun inject(recipeViewModel: RecipeViewModel)
    fun inject(productViewModel: ProductViewModel)
}
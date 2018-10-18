package com.sugar.steptofood.di

import com.sugar.steptofood.di.module.ContextModule
import com.sugar.steptofood.ui.activity.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ContextModule::class))
interface AppComponent {

    fun inject(dialogsActivity: AddFoodActivity)
    fun inject(searchActivity: AnotherUserActivity)
    fun inject(settingsActivity: FoodActivity)
    fun inject(chatActivity: SearchProductActivity)
    fun inject(loginActivity: StartActivity)
    fun inject(loginActivity: TabsActivity)
    fun inject(loginActivity: UserItemActivity)
}
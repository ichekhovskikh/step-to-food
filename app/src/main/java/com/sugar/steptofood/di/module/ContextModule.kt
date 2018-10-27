package com.sugar.steptofood.di.module

import android.app.Application
import android.content.Context
import com.sugar.steptofood.Session
import com.sugar.steptofood.db.SQLiteHelper
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton

@Module
class ContextModule(private val app: Application) {

    @Provides
    @Singleton
    fun provideContext(): Context = app

    @Provides
    @Singleton
    fun provideSession(context: Context): Session = Session(context)

    @Provides
    @Singleton
    fun provideDbHelper(context: Context): SQLiteHelper = SQLiteHelper(context)

    @Provides
    fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()
}
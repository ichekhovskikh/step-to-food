package com.sugar.steptofood.di.module

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import com.sugar.steptofood.Session
import com.sugar.steptofood.db.AppDatabase
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
    fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()

    @Provides
    fun provideAppDatabase(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "database").allowMainThreadQueries().fallbackToDestructiveMigration().build()
}
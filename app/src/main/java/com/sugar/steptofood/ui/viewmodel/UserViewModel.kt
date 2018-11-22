package com.sugar.steptofood.ui.viewmodel

import android.app.Application
import android.arch.lifecycle.*
import android.net.Uri
import com.sugar.steptofood.*
import com.sugar.steptofood.repository.UserRepository
import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.utils.NetworkState
import javax.inject.Inject

class UserViewModel(app: Application) : AndroidViewModel(app) {

    @Inject
    lateinit var session: Session
        protected set

    @Inject
    lateinit var api: ApiService
        protected set

    private val userRepository by lazy { UserRepository(api, session, getApplication()) }

    init {
        App.appComponent.inject(this)
    }

    fun register(name: String, login: String, password: String, onSuccess: () -> Unit) {
        userRepository.register(name, login, password, onSuccess)
    }

    fun login(login: String, password: String, onSuccess: () -> Unit) {
        userRepository.login(login, password, onSuccess)
    }

    fun login(onSuccess: () -> Unit) {
        userRepository.login(onSuccess)
    }

    fun getUserName(userId: Int): LiveData<String> {
        return userRepository.getUserName(userId)
    }

    fun setAvatar(uri: Uri) {
        userRepository.setAvatar(uri)
    }

    fun terminate() {
        userRepository.terminate()
    }

    fun getLoadingStatus(): LiveData<NetworkState> {
        return userRepository.loadingStatus()
    }
}
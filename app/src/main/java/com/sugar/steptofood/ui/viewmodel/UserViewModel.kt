package com.sugar.steptofood.ui.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.sugar.steptofood.Session
import com.sugar.steptofood.repository.UserRepository
import com.sugar.steptofood.rest.ApiService
import javax.inject.Inject

class UserViewModel(application: Application) : AndroidViewModel(application) {

    @Inject
    lateinit var session: Session

    @Inject
    lateinit var api: ApiService

    private val userRepository by lazy { UserRepository(api, session, application) }
}
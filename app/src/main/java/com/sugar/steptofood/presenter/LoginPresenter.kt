package com.sugar.steptofood.presenter

import com.sugar.steptofood.Session
import com.sugar.steptofood.extension.customSubscribe
import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.ui.view.LoginView

class LoginPresenter(view: LoginView,
                     api: ApiService,
                     private val session: Session) : BasePresenter<LoginView>(view, api) {

    fun register(name: String, login: String, password: String) {
        //view.onShowLoading()
        api.register(name, login, password)
                .customSubscribe({
                    login(name, password)
                }, defaultError())
    }

    fun login(login: String, password: String) {
        //view.onShowLoading()
        api.login(login, password)
                .customSubscribe({ response ->
                    session.token = response.token
                    session.userId = response.id!!
                    //view.onHideLoading()
                    view.login()
                }, defaultError())
    }
}
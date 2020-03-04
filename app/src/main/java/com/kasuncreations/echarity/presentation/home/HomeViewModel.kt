package com.kasuncreations.echarity.presentation.home

import android.app.Application
import android.view.View
import androidx.lifecycle.ViewModel
import com.kasuncreations.echarity.data.repository.UserRepository

class HomeViewModel(
    private val repository: UserRepository,
    application: Application
) : ViewModel() {

    val user by lazy {
        repository.getCurrentUser()
    }

    fun logOut(view: View) {
        repository.logout()
        //view.context.startL
    }
}
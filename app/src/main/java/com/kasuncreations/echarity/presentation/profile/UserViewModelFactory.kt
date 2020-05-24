package com.kasuncreations.echarity.presentation.profile

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kasuncreations.echarity.data.repository.UserRepository

class UserViewModelFactory(
    private val userRepository: UserRepository,
    var application: Application
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UserViewModel(userRepository, application) as T
    }
}
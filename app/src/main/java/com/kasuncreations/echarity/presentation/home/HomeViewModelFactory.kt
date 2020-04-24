package com.kasuncreations.echarity.presentation.home

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.kasuncreations.echarity.data.repository.UserRepository


class HomeViewModelFactory(
    private val repository: UserRepository,
    var application: Application

) : ViewModelProvider.NewInstanceFactory() {
//
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        return HomeViewModel(repository, application) as T
//    }

}
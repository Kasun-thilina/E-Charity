package com.kasuncreations.echarity.presentation.post

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kasuncreations.echarity.data.repository.PostsRepository
import com.kasuncreations.echarity.data.repository.UserRepository

class PostViewModelFactory(
    private val userRepository: UserRepository,
    private val postsRepository: PostsRepository,
    var application: Application
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PostViewModel(userRepository, postsRepository, application) as T
    }
}
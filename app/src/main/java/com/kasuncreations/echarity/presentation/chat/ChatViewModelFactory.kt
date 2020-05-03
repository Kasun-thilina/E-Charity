package com.kasuncreations.echarity.presentation.chat

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kasuncreations.echarity.data.repository.ChatRepository

class ChatViewModelFactory(
    private val chatRepository: ChatRepository,
    var application: Application
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ChatViewModel(chatRepository, application) as T
    }
}
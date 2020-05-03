package com.kasuncreations.echarity.data.repository

import com.kasuncreations.echarity.data.di.ChatFunctions
import com.kasuncreations.echarity.data.models.Chat

class ChatRepository(
    private val chatFunctions: ChatFunctions
) {
    fun saveMessage(chat: Chat) = chatFunctions.saveMessage(chat)
}
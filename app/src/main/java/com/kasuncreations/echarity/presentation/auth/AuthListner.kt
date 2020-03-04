package com.kasuncreations.echarity.presentation.auth

interface AuthListner {
    fun onStarted()
    fun onSuccess()
    fun onError(msg: String)
}
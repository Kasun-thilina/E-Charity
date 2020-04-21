package com.kasuncreations.echarity.presentation.auth

interface Listner {
    fun onStarted()
    fun onSuccess()
    fun onError(msg: String)
}
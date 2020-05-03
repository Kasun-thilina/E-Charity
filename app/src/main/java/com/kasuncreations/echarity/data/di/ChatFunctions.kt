package com.kasuncreations.echarity.data.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.kasuncreations.echarity.data.models.Chat
import io.reactivex.Completable

class ChatFunctions {
    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val firebaseDatabase: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }

    fun saveMessage(chat: Chat) = Completable.create { emitter ->
        val msgID = System.currentTimeMillis() / 1000
        chat.timeStamp = msgID
        firebaseDatabase.getReference("messages")
            .child(chat.id.toString())
            .child(msgID.toString())
            .setValue(chat).addOnCompleteListener { saveUser ->
                if (saveUser.isSuccessful) {
                    firebaseDatabase.getReference("users")
                        .child(firebaseAuth.currentUser!!.uid)
                        .child("messages")
                        .child(chat.id.toString())
                        .setValue(chat).addOnCompleteListener { saveMsg ->
                            if (saveMsg.isSuccessful) {
                                firebaseDatabase.getReference("users")
                                    .child(chat.receiverID.toString())
                                    .child("messages")
                                    .child(chat.id.toString())
                                    .setValue(chat)
                                emitter.onComplete()
                            } else {
                                emitter.onError(saveMsg.exception!!)
                            }
                        }
                } else {
                    emitter.onError(saveUser.exception!!)
                }
            }
    }

}
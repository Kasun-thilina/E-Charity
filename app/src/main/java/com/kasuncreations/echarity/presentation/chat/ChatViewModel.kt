package com.kasuncreations.echarity.presentation.chat

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kasuncreations.echarity.data.di.FirebaseLiveData
import com.kasuncreations.echarity.data.models.Chat
import com.kasuncreations.echarity.data.repository.ChatRepository
import com.kasuncreations.echarity.presentation.auth.Listner
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ChatViewModel(
    private val chatRepository: ChatRepository,
    val application: Application
) : ViewModel() {
    private val disposables = CompositeDisposable()
    var listner: Listner? = null

    private val firebaseDatabase: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }
    private lateinit var postsReference: DatabaseReference
    private lateinit var liveData: FirebaseLiveData

    fun setQuery(id: String) {
        postsReference = firebaseDatabase.getReference("messages").child(id)
        liveData = FirebaseLiveData(postsReference)
    }

    fun getDataSnapshotLiveData(): LiveData<DataSnapshot?>? {
        return liveData
    }

    fun saveMessage(chat: Chat) {
        listner?.onStarted()
        val disposable = chatRepository.saveMessage(chat)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    listner?.onSuccess()
                },
                {
                    listner?.onError(it.message!!)
                }
            )
        disposables.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}
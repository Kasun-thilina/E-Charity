package com.kasuncreations.echarity.presentation.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kasuncreations.echarity.data.di.FirebaseLiveData

class MessagesViewModel : ViewModel() {

    private val firebaseDatabase: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }

    private lateinit var postsReference: DatabaseReference
    private lateinit var liveData: FirebaseLiveData

    fun setQuery(id: String) {
        postsReference = firebaseDatabase.getReference("users").child(id).child("messages")
        liveData = FirebaseLiveData(postsReference)
    }

    fun getDataSnapshotLiveData(): LiveData<DataSnapshot?>? {
        return liveData
    }

}
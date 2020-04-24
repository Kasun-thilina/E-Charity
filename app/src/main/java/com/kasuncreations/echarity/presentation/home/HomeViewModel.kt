package com.kasuncreations.echarity.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kasuncreations.echarity.data.firebase.FirebaseQueryLiveData

class HomeViewModel : ViewModel() {

    private val firebaseDatabase: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }

    private val postsReference: DatabaseReference = firebaseDatabase.getReference("posts")
    private val liveData = FirebaseQueryLiveData(postsReference)

    fun getDataSnapshotLiveData(): LiveData<DataSnapshot?>? {
        return liveData
    }

}
package com.kasuncreations.echarity.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.kasuncreations.echarity.data.di.FirebaseLiveData

class HomeViewModel : ViewModel() {

    private val firebaseDatabase: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }

    private val postsReference: Query =
        firebaseDatabase.getReference("posts").orderByChild("voteCount")
    private val liveData = FirebaseLiveData(postsReference)


    fun getDataSnapshotLiveData(): LiveData<DataSnapshot?>? {
        return liveData
    }

}
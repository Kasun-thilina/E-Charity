package com.kasuncreations.echarity.presentation.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.kasuncreations.echarity.data.di.FirebaseLiveData

class MapViewModel : ViewModel() {
    private val firebaseDatabase: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }

    private val postsReference: Query = firebaseDatabase.getReference("posts")
        .orderByChild("longitude").startAt("8")
    private val liveData = FirebaseLiveData(postsReference)


    fun getDataSnapshotLiveData(): LiveData<DataSnapshot?>? {
        return liveData
    }

}
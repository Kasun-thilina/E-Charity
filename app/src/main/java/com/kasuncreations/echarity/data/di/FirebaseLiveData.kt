package com.kasuncreations.echarity.data.di

import androidx.lifecycle.LiveData
import com.google.firebase.database.*


class FirebaseLiveData : LiveData<DataSnapshot?> {
    private val query: Query
    private val listener =
        MyValueEventListener()

    constructor(query: Query) {
        this.query = query
    }

    constructor(ref: DatabaseReference) {
        query = ref
    }

    override fun onActive() {
        query.addValueEventListener(listener)
    }

    override fun onInactive() {
        query.removeEventListener(listener)
    }

    private inner class MyValueEventListener : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            value = dataSnapshot
        }

        override fun onCancelled(databaseError: DatabaseError) {
            println(databaseError.toException())
        }
    }
}
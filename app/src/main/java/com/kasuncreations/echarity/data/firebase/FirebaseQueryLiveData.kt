package com.kasuncreations.echarity.data.firebase

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.database.*


class FirebaseQueryLiveData : LiveData<DataSnapshot?> {
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
        println("onActive")
        query.addValueEventListener(listener)
    }

    override fun onInactive() {
        println("onInactive")
        query.removeEventListener(listener)
    }

    private inner class MyValueEventListener : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            value = dataSnapshot
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.e(
                "error",
                "Can't listen to query $query",
                databaseError.toException()
            )

            //value=databaseError
        }
    }


}
package com.kasuncreations.echarity.data.di

import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.kasuncreations.echarity.data.models.User
import com.kasuncreations.echarity.utils.CONSTANTS
import com.kasuncreations.echarity.utils.CONSTANTS.IS_ADMIN
import com.kasuncreations.echarity.utils.CONSTANTS.USER_ID
import io.reactivex.Completable

/**
 * Created by Kasun Thilina on 26 February 2020
 * kasun.thilina.t@gmail.com
 *
 * firebase initialization and related functions
 */
class FirebaseFunctions(val pref: SharedPreferences) {

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val firebaseDatabase: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }

    fun login(email: String, password: String) = Completable.create { emitter ->
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (!emitter.isDisposed) {
                if (it.isSuccessful) {
                    pref.edit().putString(USER_ID, firebaseAuth.currentUser!!.uid).apply()
                    println(email)
                    if (email == "admin@g.com") {
                        pref.edit().putBoolean(IS_ADMIN, true).apply()
                    } else {
                        pref.edit().putBoolean(IS_ADMIN, false).apply()
                    }
                    emitter.onComplete()
                } else
                    emitter.onError(it.exception!!)
            }
        }
    }

    fun signUp(email: String, password: String, firstName: String, lastName: String) =
        Completable.create { emitter ->
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (!emitter.isDisposed) {
                if (it.isSuccessful) {
                    //Saving Extra data for user if initial auth sign up is successful
                    val user = User(firstName, lastName)
                    firebaseDatabase.getReference(CONSTANTS.USER_DATA)
                        .child(firebaseAuth.currentUser!!.uid)
                        .setValue(user).addOnCompleteListener { dbTask ->
                            if (dbTask.isSuccessful) {
                                emitter.onComplete()
                            } else {
                                emitter.onError(it.exception!!)
                            }
                        }
                } else
                    emitter.onError(it.exception!!)
            }
        }

        }

    fun signOut() = firebaseAuth.signOut()
    fun currentUser() = firebaseAuth.currentUser
}
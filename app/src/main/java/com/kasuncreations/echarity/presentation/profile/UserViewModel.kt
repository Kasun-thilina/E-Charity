package com.kasuncreations.echarity.presentation.profile

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kasuncreations.echarity.data.di.FirebaseLiveData
import com.kasuncreations.echarity.data.repository.UserRepository
import com.kasuncreations.echarity.presentation.auth.Listner
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class UserViewModel(
    val userRepository: UserRepository,
    val application: Application
) : ViewModel() {
    private lateinit var postsReference: DatabaseReference
    private lateinit var liveData: FirebaseLiveData
    var listner: Listner? = null
    private val disposables = CompositeDisposable()

    private val firebaseDatabase: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }

    fun setQuery(id: String) {
        postsReference = firebaseDatabase.getReference("users").child(id)
        liveData = FirebaseLiveData(postsReference)
    }

    fun getDataSnapshotLiveData(): LiveData<DataSnapshot?>? {
        return liveData
    }

    fun updateDP(uri: Uri) {
        listner?.onStarted()
        val disposable = userRepository.updateDP(uri)
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
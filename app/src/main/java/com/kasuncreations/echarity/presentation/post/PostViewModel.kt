package com.kasuncreations.echarity.presentation.post

import android.app.Application
import androidx.lifecycle.ViewModel
import com.kasuncreations.echarity.data.models.Post
import com.kasuncreations.echarity.data.repository.PostsRepository
import com.kasuncreations.echarity.data.repository.UserRepository
import com.kasuncreations.echarity.presentation.auth.Listner
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PostViewModel(
    val userRepository: UserRepository,
    val postsRepository: PostsRepository,
    val application: Application
) : ViewModel() {
    var title: String? = null
    var description: String? = null
    var latLng: String? = null
    var image: String? = null

    var listner: Listner? = null

    private val disposables = CompositeDisposable()


    val user by lazy {
        userRepository.getCurrentUser()
    }

    fun savePost(post: Post) {
        listner?.onStarted()

        val disposable = postsRepository.savePost(post)
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

    fun loadPost() {
        listner?.onStarted()
        val disposable = postsRepository.loadPost()
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
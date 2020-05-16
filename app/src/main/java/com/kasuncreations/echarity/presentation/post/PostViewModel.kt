package com.kasuncreations.echarity.presentation.post

import android.app.Application
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.kasuncreations.echarity.data.models.Post
import com.kasuncreations.echarity.data.models.Vote
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

    var listner: Listner? = null

    private val disposables = CompositeDisposable()

    fun savePost(post: Post, uri: Uri?) {
        listner?.onStarted()

        val disposable = postsRepository.savePost(post, uri)
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

    fun updatePost(count: Int, ID: Long, vote: Vote) {
        listner?.onStarted()
        val disposable = postsRepository.updatePost(count, ID, vote)
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
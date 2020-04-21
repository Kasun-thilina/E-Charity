package com.kasuncreations.echarity.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.kasuncreations.echarity.data.models.Post
import io.reactivex.Completable

class PostFunctions {
    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val firebaseDatabase: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }

    fun savePost(post: Post) = Completable.create { emitter ->
        post.userId = firebaseAuth.currentUser!!.uid
        val postid = System.currentTimeMillis() / 1000
        post.postId = postid
        firebaseDatabase.getReference("posts")
            .child(postid.toString())
            .setValue(post).addOnCompleteListener { dbTask ->
                if (dbTask.isSuccessful) {
                    emitter.onComplete()
                } else {
                    emitter.onError(dbTask.exception!!)
                }
            }

    }


}
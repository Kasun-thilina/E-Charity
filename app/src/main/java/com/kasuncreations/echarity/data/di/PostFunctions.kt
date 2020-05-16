package com.kasuncreations.echarity.data.di

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.kasuncreations.echarity.data.models.Post
import com.kasuncreations.echarity.data.models.Vote
import io.reactivex.Completable
import io.reactivex.CompletableEmitter

class PostFunctions {
    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val firebaseDatabase: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }

    private val firebaseStorage: FirebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }

    private val storageReference = firebaseStorage.reference

    fun savePost(post: Post, uri: Uri?) = Completable.create { emitter ->
        val imageRef =
            storageReference.child("${firebaseAuth.currentUser!!.uid}/${uri!!.lastPathSegment}")
        println(uri)
        val uploadTask = imageRef.putFile(uri)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    emitter.onError(it)
                }
            }
            imageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                saveToDB(downloadUri.toString(), post, emitter)
            } else {
                emitter.onError(task.exception?.cause!!)
            }
        }
    }

    fun savePost(post: Post) = Completable.create { emitter ->
        saveToDB("", post, emitter)
    }

    private fun saveToDB(
        uri: String,
        post: Post,
        emitter: CompletableEmitter
    ) {
        post.userId = firebaseAuth.currentUser!!.uid
        val postId = System.currentTimeMillis() / 1000
        post.postId = postId
        post.imageUri = uri
        firebaseDatabase.getReference("posts")
            .child(postId.toString())
            .setValue(post).addOnCompleteListener { dbTask ->
                if (dbTask.isSuccessful) {
                    emitter.onComplete()
                } else {
                    emitter.onError(dbTask.exception!!)
                }
            }
    }

    fun updateDB(
        count: Int, ID: Long, vote: Vote
    ) = Completable.create { emitter ->
        val userId = firebaseAuth.currentUser!!.uid
        firebaseDatabase.getReference("posts").child(ID.toString())
            .child("voteCount").setValue(count).addOnCompleteListener { saveVote ->
                if (saveVote.isSuccessful) {
                    firebaseDatabase.getReference("posts").child(ID.toString())
                        .child("vote").child(userId).setValue(vote)
                        .addOnCompleteListener { saveDetails ->
                            if (saveDetails.isSuccessful) {
                                emitter.onComplete()
                            } else {
                                emitter.onError(saveDetails.exception!!)
                            }
                        }
                } else {
                    emitter.onError(saveVote.exception!!)
                }
            }
    }


}
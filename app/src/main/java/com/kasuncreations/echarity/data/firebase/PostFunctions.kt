package com.kasuncreations.echarity.data.firebase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kasuncreations.echarity.data.models.Post
import io.reactivex.Completable
import io.reactivex.Single

class PostFunctions {
    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val firebaseDatabase: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }

    fun savePost(post: Post) = Completable.create { emitter ->
        post.userId = firebaseAuth.currentUser!!.uid
        val postId = System.currentTimeMillis() / 1000
        post.postId = postId
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

    val single = Single.create<String> { it.onSuccess("result") }
    fun loadPosts() = Completable.create { emitter ->
        val databaseReference = firebaseDatabase.getReference("posts")
        val posts = mutableListOf<Post>()
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                emitter.onError(p0.toException())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.map { post ->
                    posts.add(post.value as Post)
                }
                emitter.onComplete()
            }

        })
    }

    var posts2: MutableLiveData<MutableList<Post>> = MutableLiveData()
    fun load(): LiveData<MutableList<Post>> {
        val databaseReference = firebaseDatabase.getReference("posts")
        val posts = mutableListOf<Post>()
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.map { post ->
                    posts.add(post.value as Post)
                }
                posts2.postValue(posts)
            }
        })
        return posts2
    }
//
//    fun getPosts():(MutableList<Post>?){
//        val databaseReference = firebaseDatabase.getReference("posts")
//        val posts = mutableListOf<Post>()
//        databaseReference.addValueEventListener(object : ValueEventListener {
//            override fun onCancelled(p0: DatabaseError) {
//            }
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//                snapshot.children.map { post ->
//                    println(post.value as Post)
//                    posts.add(post.value as Post)
//                }
//            }
//
//        })
//        return posts
//    }
//
}
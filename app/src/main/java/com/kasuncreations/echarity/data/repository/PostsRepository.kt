package com.kasuncreations.echarity.data.repository

import android.net.Uri
import com.kasuncreations.echarity.data.di.PostFunctions
import com.kasuncreations.echarity.data.models.Post
import com.kasuncreations.echarity.data.models.Vote

class PostsRepository(
    private val postFunctions: PostFunctions
) {
    fun savePost(post: Post, uri: Uri?) = postFunctions.savePost(post, uri)
    fun savePost(post: Post) = postFunctions.savePost(post)
    fun updatePost(count: Int, ID: Long, vote: Vote) =
        postFunctions.updateDB(count, ID, vote)
}
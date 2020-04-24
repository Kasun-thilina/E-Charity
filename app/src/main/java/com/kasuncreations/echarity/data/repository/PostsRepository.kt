package com.kasuncreations.echarity.data.repository

import com.kasuncreations.echarity.data.firebase.PostFunctions
import com.kasuncreations.echarity.data.models.Post

class PostsRepository(
    private val postFunctions: PostFunctions
) {
    fun savePost(post: Post) = postFunctions.savePost(post)
    fun loadPost() = postFunctions.loadPosts()
    //fun load()=postFunctions.load()
}
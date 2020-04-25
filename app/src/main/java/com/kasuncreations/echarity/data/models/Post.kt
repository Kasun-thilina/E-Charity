package com.kasuncreations.echarity.data.models

/**
 * Created by Kasun Thilina on 13th April 2020
 * @author kasun.thilina.t@gmail.com
 *
 * Data class for Posts
 */
data class Post(
    var userId: String? = null,
    var postId: Long? = null,
    var tittle: String? = null,
    var description: String? = null,
    var category: Int? = null,
    var latitude: Long? = null,
    var upVote: Int? = null,
    var downVote: Int? = null,
    var longtitude: Long? = null,
    var imageUri: String? = null
)
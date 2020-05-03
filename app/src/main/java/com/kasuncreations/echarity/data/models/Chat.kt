package com.kasuncreations.echarity.data.models

/**
 * Created by Kasun Thilina on 13th April 2020
 * @author kasun.thilina.t@gmail.com
 *
 * Data class for Chat
 */
data class Chat(
    val id: String? = null,
    val message: String? = null,
    val senderID: String? = null,
    val receiverID: String? = null,
    val receiverProfileUrl: String? = null,
    var timeStamp: Long? = null
)
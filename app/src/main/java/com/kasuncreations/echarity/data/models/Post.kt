package com.kasuncreations.echarity.data.models

import android.media.Image
import com.google.android.gms.maps.model.LatLng

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
    var latLng: LatLng? = null,
    var image: Image? = null
)
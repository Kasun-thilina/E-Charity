package com.kasuncreations.echarity.presentation.map

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kasuncreations.echarity.R
import com.kasuncreations.echarity.data.models.Post
import kotlinx.android.synthetic.main.item_post_layout.view.*

class MapDetailsAdapter(
    var mContext: Context,
    var postList: MutableList<Post>
) :
    RecyclerView.Adapter<MapDetailsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_post_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(position: Int) {
            itemView.cl_bottom.visibility = View.INVISIBLE
            itemView.tv_post_title.text = postList[position].tittle.toString()
            itemView.post_description.text = postList[position].description.toString()
            if (postList[position].imageUri.isNullOrBlank()) {
                var imageUri: Int? = null
                if (postList[position].category == 0) {
                    imageUri = R.drawable.help_me
                } else if (postList[position].category == 1) {
                    imageUri = R.drawable.can_help

                }
                Glide.with(mContext).load(imageUri).into(itemView.iv_post_banner)
            } else {
                Glide.with(mContext).load(postList[position].imageUri).into(itemView.iv_post_banner)
            }

            itemView.tv_location.text = postList[position].locationName.toString()
        }
    }
}
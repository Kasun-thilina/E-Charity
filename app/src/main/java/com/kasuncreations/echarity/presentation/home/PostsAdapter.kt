package com.kasuncreations.echarity.presentation.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kasuncreations.echarity.R

/**
 * RecyclerView adpter for Posts view
 * @author kasun.thilina.t@gmail.com
 * @since 23rd April 2020
 */
class PostsAdapter(var postList: ArrayList<String>?) :
    RecyclerView.Adapter<PostsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_post_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 20
    }

    override fun onBindViewHolder(holder: PostsAdapter.ViewHolder, position: Int) {
        holder.onBind(position)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun onBind(position: Int) {

        }
    }
}
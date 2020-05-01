package com.kasuncreations.echarity.presentation.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kasuncreations.echarity.R
import com.kasuncreations.echarity.data.models.Post
import kotlinx.android.synthetic.main.item_post_layout.view.*

/**
 * RecyclerView adpter for Posts view
 * This Adapter is using a Kotlin Lambda Function to callback to the Activity class. Its similar
 * to a interface but with less code :). Lambda function is returning the required values back and that's it.
 * @author kasun.thilina.t@gmail.com
 * @since 23rd April 2020
 */
class PostsAdapter(
    var mContext: Context,
    val userID: String,
    var postList: MutableList<Post>,
    val onVoteCast: (Int, Long, Int) -> Unit
) :
    RecyclerView.Adapter<PostsAdapter.ViewHolder>() {


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
        var clickedUp = false
        var clickedDown = false
        var alreadyClicked = false
        if (postList[position].userId == userID) {
            if (postList[position].voteType == 0) {
                holder.itemView.post_vote_down.setImageDrawable(
                    ContextCompat.getDrawable(
                        mContext,
                        R.drawable.down_selected
                    )
                )
                clickedDown = true
                alreadyClicked = true
            } else if (postList[position].voteType == 1) {
                holder.itemView.post_vote_up.setImageDrawable(
                    ContextCompat.getDrawable(
                        mContext,
                        R.drawable.up_selected
                    )
                )
                clickedUp = true
                alreadyClicked = true
            }
        }
        var count: Int = postList[position].vote!!
        holder.itemView.tv_vote_counter.text = count.toString()
        holder.itemView.post_vote_up.setOnClickListener {
            clickedUp = if (!clickedUp) {
                holder.itemView.post_vote_up.setImageDrawable(
                    ContextCompat.getDrawable(
                        mContext,
                        R.drawable.up_selected
                    )
                )
                holder.itemView.post_vote_down.setImageDrawable(
                    ContextCompat.getDrawable(
                        mContext,
                        R.drawable.down
                    )
                )
                if (clickedDown && !alreadyClicked) {
                    count += 2
                    alreadyClicked = false
                } else {
                    count++
                }
                holder.itemView.tv_vote_counter.text = count.toString()
                onVoteCast(count, postList[position].postId!!, 1)
                clickedDown = false
                true
            } else {
                holder.itemView.post_vote_up.setImageDrawable(
                    ContextCompat.getDrawable(
                        mContext,
                        R.drawable.up
                    )
                )
                holder.itemView.post_vote_down.setImageDrawable(
                    ContextCompat.getDrawable(
                        mContext,
                        R.drawable.down
                    )
                )
                count--
                holder.itemView.tv_vote_counter.text = count.toString()
                onVoteCast(count, postList[position].postId!!, 2)
                false
            }

        }

        holder.itemView.post_vote_down.setOnClickListener {
            clickedDown = if (!clickedDown) {
                holder.itemView.post_vote_down.setImageDrawable(
                    ContextCompat.getDrawable(
                        mContext,
                        R.drawable.down_selected
                    )
                )
                holder.itemView.post_vote_up.setImageDrawable(
                    ContextCompat.getDrawable(
                        mContext,
                        R.drawable.up
                    )
                )
                if (clickedUp && !alreadyClicked) {
                    count -= 2
                    alreadyClicked = false
                } else {
                    count--
                }
                holder.itemView.tv_vote_counter.text = count.toString()
                onVoteCast(count, postList[position].postId!!, 0)
                clickedUp = false
                true
            } else {
                holder.itemView.post_vote_down.setImageDrawable(
                    ContextCompat.getDrawable(
                        mContext,
                        R.drawable.down
                    )
                )
                holder.itemView.post_vote_up.setImageDrawable(
                    ContextCompat.getDrawable(
                        mContext,
                        R.drawable.up
                    )
                )
                count++
                holder.itemView.tv_vote_counter.text = count.toString()
                onVoteCast(count, postList[position].postId!!, 2)
                false
            }
        }


    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(position: Int) {
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
package com.kasuncreations.echarity.presentation.chat

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.kasuncreations.echarity.R
import com.kasuncreations.echarity.data.models.Chat
import com.kasuncreations.echarity.data.models.User
import com.kasuncreations.echarity.presentation.profile.UserViewModel
import com.kasuncreations.echarity.utils.CONSTANTS
import com.kasuncreations.echarity.utils.CONSTANTS.USER_NAME
import kotlinx.android.synthetic.main.item_message.view.*

/**
 * RecyclerView adpter for conversations view
 * @author kasun.thilina.t@gmail.com
 * @since 24th March 2020
 */
class MessageViewAdapter(
    var mContext: Context,
    var msgList: MutableList<Chat>,
    var userViewModel: UserViewModel,
    var userLiveData: LiveData<DataSnapshot?>?,
    var viewLifecycleOwner: LifecycleOwner,
    var userID: String
) :
    RecyclerView.Adapter<MessageViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return msgList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
        val ids = msgList[position].id!!.split("_").toTypedArray()
        if (ids[0] != userID) {
            userViewModel.setQuery(ids[0])
        } else {
            userViewModel.setQuery(ids[1])
        }
        userLiveData = userViewModel.getDataSnapshotLiveData()
        var userName = ""
        userLiveData!!.observe(viewLifecycleOwner, Observer {
            val user = it!!.getValue(User::class.java)!!
            userName = "${user.first_name} ${user.last_name}"
            holder.itemView.tv_title.text = userName
            if (!user.avatar.isNullOrEmpty()) {
                Glide.with(mContext).load(user.avatar).into(holder.itemView.iv_profile_image)
            }
        })
        holder.itemView.tv_subtitle.text = msgList[position].message.toString()


        holder.itemView.cl_parent.setOnClickListener {
            val intent = Intent(mContext, ConversationViewActivity::class.java)
            if (userID == msgList[position].receiverID) {
                intent.putExtra(CONSTANTS.USER_ID, msgList[position].senderID)
            } else {
                intent.putExtra(CONSTANTS.USER_ID, msgList[position].receiverID)
            }
            intent.putExtra(USER_NAME, userName)
            mContext.startActivity(intent)
        }

    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun onBind(position: Int) {

        }
    }
}

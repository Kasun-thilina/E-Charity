package com.kasuncreations.echarity.presentation.chat

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kasuncreations.echarity.R
import com.kasuncreations.echarity.data.models.Chat
import com.kasuncreations.echarity.utils.CONSTANTS
import kotlinx.android.synthetic.main.item_message.view.*

/**
 * RecyclerView adpter for conversations view
 * @author kasun.thilina.t@gmail.com
 * @since 24th March 2020
 */
class MessageViewAdapter(
    var mContext: Context,
    var msgList: MutableList<Chat>,
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
        holder.itemView.tv_title.text = "Jhon Snow"
        holder.itemView.tv_subtitle.text = msgList[position].message.toString()

        holder.itemView.cl_parent.setOnClickListener {
            val intent = Intent(mContext, ConversationViewActivity::class.java)
            if (userID == msgList[position].receiverID) {
                intent.putExtra(CONSTANTS.USER_ID, msgList[position].senderID)
            } else {
                intent.putExtra(CONSTANTS.USER_ID, msgList[position].receiverID)
            }
            mContext.startActivity(intent)
        }

    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun onBind(position: Int) {

        }
    }
}

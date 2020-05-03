package com.kasuncreations.echarity.presentation.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kasuncreations.echarity.R
import com.kasuncreations.echarity.data.models.Chat
import kotlinx.android.synthetic.main.item_chat.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * RecyclerView adapter for Conversation view
 *
 * @author kasun.thilina.t@gmail.com
 * @since 3rd May 2020
 */
class ConversationViewAdapter(var msgList: MutableList<Chat>, var userID: String) :
    RecyclerView.Adapter<ConversationViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return msgList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
        val sdf = SimpleDateFormat("dd-MM-yyyy hh:mm:ss a")
        //sdf.timeZone = TimeZone.getTimeZone("Etc/UTC");
        val dateTemp = Date((msgList[position].timeStamp!! * 1000))
        val date = sdf.format(dateTemp)
        if (msgList[position].senderID == userID) {
            holder.itemView.cl_receiver.visibility = View.GONE
            holder.itemView.tv_sender.text = msgList[position].message
            holder.itemView.tv_timestamp_sender.text = date.toString()
        } else {
            holder.itemView.cl_sender.visibility = View.GONE
            holder.itemView.tv_receiver.text = msgList[position].message
            holder.itemView.tv_timestamp_receiver.text = date.toString()
        }

    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun onBind(position: Int) {

        }
    }
}
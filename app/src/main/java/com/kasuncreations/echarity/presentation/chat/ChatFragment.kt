package com.kasuncreations.echarity.presentation.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.intentservice.chatui.ChatView
import com.kasuncreations.echarity.R
import com.kasuncreations.echarity.utils.BaseFragment
import io.kommunicate.KmConversationBuilder
import io.kommunicate.Kommunicate
import io.kommunicate.callbacks.KmCallback

class ChatFragment : BaseFragment() {

    var chatView: ChatView? = null
    var btnAIChat: Button? = null
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var rvConversations: RecyclerView


    companion object {
        const val TAG = "chat"
        fun newInstance() = ChatFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat, null)
        //chatView=view.findViewById(R.id.chat_view)
        rvConversations = view.findViewById(R.id.rv_conversations)
        btnAIChat = view.findViewById(R.id.btn_chatbot)
        init()
        return view
    }

    private fun init() {
        var con: ArrayList<String>? = null
        con?.add("")
        con?.add("")
        messageAdapter = MessageAdapter(con)
        mLayoutManager = LinearLayoutManager(context!!)
        rvConversations.layoutManager = mLayoutManager
        rvConversations.adapter = messageAdapter

        btnAIChat?.setOnClickListener {
            Kommunicate.init(context, getString(R.string.KOMMUNICATE_APP_ID))
            showProgress()
            KmConversationBuilder(context)
                .launchConversation(object : KmCallback {
                    override fun onSuccess(message: Any) {
                        println("Success : $message")
                    }

                    override fun onFailure(error: Any) {
                        println("Failure : $error")
                        hideProgress()
                    }
                })
        }


        /*chatView?.setOnSentMessageListener {

            true
        }*/
    }

    override fun onPause() {
        super.onPause()
        hideProgress()
    }

    override fun onResume() {
        super.onResume()
        hideProgress()
    }

}
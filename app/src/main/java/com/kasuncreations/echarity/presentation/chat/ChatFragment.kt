package com.kasuncreations.echarity.presentation.chat

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.kasuncreations.echarity.R
import com.kasuncreations.echarity.data.models.Chat
import com.kasuncreations.echarity.presentation.auth.Listner
import com.kasuncreations.echarity.utils.BaseFragment
import com.kasuncreations.echarity.utils.CONSTANTS
import com.kasuncreations.echarity.utils.showToastLong
import io.kommunicate.KmConversationBuilder
import io.kommunicate.Kommunicate
import io.kommunicate.callbacks.KmCallback
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class ChatFragment : BaseFragment(), KodeinAware, Listner {

    var btnAIChat: Button? = null
    override val kodein by lazy { (context as KodeinAware).kodein }
    private lateinit var messageViewAdapter: MessageViewAdapter
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var rvConversations: RecyclerView
    private val sharedPreferences: SharedPreferences by instance(arg = CONSTANTS.PREF_NAME)
    private lateinit var messagesViewModel: MessagesViewModel
    private var liveData: LiveData<DataSnapshot?>? = null
    private lateinit var userID: String

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
        messagesViewModel = ViewModelProviders.of(this).get(MessagesViewModel::class.java)
        userID = sharedPreferences.getString(CONSTANTS.USER_ID, "")!!

        messagesViewModel.setQuery(userID)
        initView()
        loadData()
        return view
    }

    private fun initView() {

        messageViewAdapter = MessageViewAdapter(context!!, mutableListOf(), userID)
        mLayoutManager = LinearLayoutManager(context!!)
        rvConversations.layoutManager = mLayoutManager
        rvConversations.adapter = messageViewAdapter

        btnAIChat?.setOnClickListener {
            Kommunicate.init(context, getString(R.string.KOMMUNICATE_APP_ID))
            //showProgress()
            KmConversationBuilder(context)
                .launchConversation(object : KmCallback {
                    override fun onSuccess(message: Any) {
                        println("Success : $message")
                    }

                    override fun onFailure(error: Any) {
                        println("Failure : $error")
                        //  hideProgress()
                    }
                })
        }
    }

    private fun loadData() {
        liveData = messagesViewModel.getDataSnapshotLiveData()
        liveData!!.observe(viewLifecycleOwner, Observer {
            messageViewAdapter.msgList.clear()
            it!!.children.map { chat ->
                messageViewAdapter.msgList.add(chat.getValue(Chat::class.java)!!)
            }
            messageViewAdapter.notifyDataSetChanged()
        })
    }

    override fun onPause() {
        super.onPause()
        //hideProgress()
    }

    override fun onResume() {
        super.onResume()
        //  hideProgress()
    }

    override fun onStarted() {

    }

    override fun onSuccess() {

    }

    override fun onError(msg: String) {
        context.showToastLong("Vote Casting Failed:$msg")
    }

}
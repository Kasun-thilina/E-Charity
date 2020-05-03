package com.kasuncreations.echarity.presentation.chat

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.firebase.database.DataSnapshot
import com.kasuncreations.echarity.R
import com.kasuncreations.echarity.data.models.Chat
import com.kasuncreations.echarity.presentation.auth.Listner
import com.kasuncreations.echarity.utils.BaseActivity
import com.kasuncreations.echarity.utils.CONSTANTS
import com.kasuncreations.echarity.utils.CONSTANTS.USER_ID
import com.kasuncreations.echarity.utils.showToastLong
import kotlinx.android.synthetic.main.activity_chat_view.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class ChatViewActivity : BaseActivity(), KodeinAware, Listner {

    override val kodein by kodein()
    private val factory: ChatViewModelFactory by instance()
    private val sharedPreferences: SharedPreferences by instance(arg = CONSTANTS.PREF_NAME)
    private lateinit var viewModel: ChatViewModel
    private lateinit var receiverID: String
    private lateinit var senderID: String
    private lateinit var sorted: List<String>
    private var liveData: LiveData<DataSnapshot?>? = null
    private lateinit var conversationViewAdapter: ConversationViewAdapter
    private lateinit var mLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_view)
        setSupportActionBar(toolbar_chat_view)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //supportActionBar?.title = getString(R.string.btn_label_add_post)
        ButterKnife.bind(this)
        viewModel = ViewModelProviders.of(this, factory).get(ChatViewModel::class.java)
        viewModel.listner = this
        receiverID = intent.getStringExtra(USER_ID)!!
        senderID = sharedPreferences.getString(USER_ID, "")!!
        initView()
        init()
    }

    private fun initView() {
        conversationViewAdapter = ConversationViewAdapter(mutableListOf(), senderID)
        mLayoutManager = LinearLayoutManager(this)
        rv_chat_view.layoutManager = mLayoutManager
        rv_chat_view.adapter = conversationViewAdapter
    }

    private fun init() {
        //showProgress()
        val temp = listOf(senderID, receiverID)
        sorted = temp.sortedDescending()
        viewModel.setQuery("${sorted[0]}_${sorted[1]}")
        liveData = viewModel.getDataSnapshotLiveData()
        liveData!!.observe(this, Observer {
            conversationViewAdapter.msgList.clear()
            it!!.children.map { chat ->
                conversationViewAdapter.msgList.add(chat.getValue(Chat::class.java)!!)
            }
            conversationViewAdapter.notifyDataSetChanged()
            // hideProgress()
        })
    }


    @OnClick(
        R.id.btn_send
    )
    internal fun click(view: View) {
        when (view.id) {
            R.id.btn_send -> {
                if (!et_msg.text.isNullOrBlank()) {
                    val chat = Chat(
                        id = "${sorted[0]}_${sorted[1]}",
                        message = et_msg.text.trim().toString(),
                        senderID = senderID,
                        receiverID = receiverID
                    )
                    viewModel.saveMessage(chat)
                }
            }
        }
    }

    override fun onStarted() {
        // showProgress()
    }

    override fun onSuccess() {
        // hideProgress()
        et_msg.text.clear()
    }

    override fun onError(msg: String) {
        // hideProgress()
        showToastLong(msg)
    }
}

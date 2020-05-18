package com.kasuncreations.echarity.presentation.fcm

import android.os.Bundle
import com.kasuncreations.echarity.R
import com.kasuncreations.echarity.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_send_push_notification.*

class SendPushNotificationActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_push_notification)
        setSupportActionBar(toolbar_add_fcm)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Send Push Notification"
    }
}

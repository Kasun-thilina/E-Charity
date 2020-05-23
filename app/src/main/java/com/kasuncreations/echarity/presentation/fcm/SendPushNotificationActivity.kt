package com.kasuncreations.echarity.presentation.fcm

import android.os.Bundle
import android.view.View
import butterknife.ButterKnife
import butterknife.OnClick
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.kasuncreations.echarity.R
import com.kasuncreations.echarity.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_send_push_notification.*
import org.json.JSONObject

class SendPushNotificationActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_push_notification)
        setSupportActionBar(toolbar_add_fcm)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Send Push Notification"
        ButterKnife.bind(this)

    }

    @OnClick(
        R.id.btn_addpost
    )
    internal fun click(view: View) {
        when (view.id) {
            R.id.btn_addpost -> {
                val notificationBody = JSONObject()
                    .put("title", et_title.text)
                    .put("body", et_description.text)
                val body = JSONObject()
                    .put("to", "/topics/all")
                    .put("notification", notificationBody)

                showProgress()
                AndroidNetworking.post("https://fcm.googleapis.com/fcm/send")
                    .addJSONObjectBody(body)
                    .setTag("test")
                    .addHeaders(
                        "Authorization",
                        "key=AAAAohHAYpI:APA91bE4PTsDCBkOTE8yOinMAZI7VmgTo61xCvlkFjLt9f6qqHC2tFCDQkPzYOJSvRPglct-v9eWV5wcFMYm1AsgRKe-QwfeRPegb23aRnwEVuGK20XC9rZyPo0YwLTEzsDMTTbqsA1g"
                    )
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject) { // do anything with response
                            hideProgress()
                            finish()
                        }

                        override fun onError(error: ANError) { // handle error
                            hideProgress()
                        }
                    })

            }

        }
    }
}

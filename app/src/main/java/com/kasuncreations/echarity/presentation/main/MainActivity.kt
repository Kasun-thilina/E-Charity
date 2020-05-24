package com.kasuncreations.echarity.presentation.main

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.kasuncreations.echarity.R
import com.kasuncreations.echarity.presentation.chat.ChatFragment
import com.kasuncreations.echarity.presentation.fcm.SendPushNotificationActivity
import com.kasuncreations.echarity.presentation.home.HomeFragment
import com.kasuncreations.echarity.presentation.map.MapFragment
import com.kasuncreations.echarity.presentation.post.AddPostActivity
import com.kasuncreations.echarity.presentation.profile.ProfileFragment
import com.kasuncreations.echarity.utils.BaseActivity
import com.kasuncreations.echarity.utils.CONSTANTS
import com.kasuncreations.echarity.utils.showToastLong
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class MainActivity : BaseActivity(), KodeinAware {
    private lateinit var bottomAppBar: BottomAppBar
    override val kodein by kodein()
    private val sharedPreferences: SharedPreferences by instance(arg = CONSTANTS.PREF_NAME)
    private var isAdmin: Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        setUpBottomNavigation()
        isAdmin = sharedPreferences.getBoolean(CONSTANTS.IS_ADMIN, false)
        if (isAdmin!!) {
            showToastLong("Logged in as Administrator")
        }
//        setStatusBarColor(R.color.colorWhite)
        initFCM()
    }

    private fun setUpBottomNavigation() {
        bottomAppBar = findViewById(R.id.bottom_nav)
        setSupportActionBar(bottomAppBar)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, HomeFragment.newInstance(), "home")
            .commit()
        // bottomAppBar.replaceMenu(R.menu.app_bar_menu)
        /*bottomAppBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.nav_btn_profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container,ProfileFragment.newInstance(),"profile")
                        .commit()

                }
            }
            false
        }*/
    }

    @OnClick(
        R.id.nav_btn_home,
        R.id.nav_btn_chat,
        R.id.nav_btn_map,
        R.id.nav_btn_profile,
        R.id.btn_fab
    )
    internal fun click(view: View) {
        //Fragment navigation
        when (view.id) {
            R.id.nav_btn_home -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, HomeFragment.newInstance(), "home")
                    .commit()
            }
            R.id.nav_btn_chat -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ChatFragment.newInstance(), "chat")
                    .commit()
            }
            R.id.nav_btn_map -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, MapFragment.newInstance(), "map")
                    .commit()
            }
            R.id.nav_btn_profile -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ProfileFragment.newInstance(), "profile")
                    .commit()
            }
            R.id.btn_fab -> {
                showOptions()
                showToastLong("PUSH NOTIFICATION option is enabled for all users for testing purposes!")
//                if (isAdmin!!) {
//                    showOptions()
//                } else {
//                    Intent(this, AddPostActivity::class.java).also {
//                        this.startActivity(it)
//                    }
//                }
            }
        }
    }

    private fun showOptions() {
        val options =
            arrayOf<CharSequence>("Add New Post", "Send Push Notification", "Cancel")

        val builder = AlertDialog.Builder(this)
        //builder.setTitle("Select an image for your post")

        builder.setItems(options) { dialog, item ->
            when {
                options[item] == "Add New Post" -> {
                    Intent(this, AddPostActivity::class.java).also {
                        this.startActivity(it)
                    }
                }
                options[item] == "Send Push Notification" -> {
                    Intent(this, SendPushNotificationActivity::class.java).also {
                        this.startActivity(it)
                    }
                }
                options[item] == "Cancel" -> {
                    dialog.dismiss()
                }
            }
        }
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun initFCM() {
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            val devicePushToken = it.token
            sharedPreferences.edit().putString(CONSTANTS.PUSH_TOKEN, devicePushToken).apply()
            Log.e("push", it?.token.toString())
        }

        //FCM notifications
        FirebaseMessaging.getInstance().subscribeToTopic("all")
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    showToastLong("Subscription to Emergency Notification Service failed")
                }
                showToastLong("Successfully Subscribed to Emergency Notification Service")

            }
    }

}

package com.kasuncreations.echarity.presentation.auth

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.kasuncreations.echarity.R
import com.kasuncreations.echarity.databinding.ActivityLoginBinding
import com.kasuncreations.echarity.presentation.main.MainActivity
import com.kasuncreations.echarity.utils.BaseActivity
import com.kasuncreations.echarity.utils.CONSTANTS
import com.kasuncreations.echarity.utils.CONSTANTS.PUSH_TOKEN
import com.kasuncreations.echarity.utils.hideKeyboard
import com.kasuncreations.echarity.utils.showToastLong
import kotlinx.android.synthetic.main.activity_login.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class LoginActivity : BaseActivity(), Listner, KodeinAware {

    private lateinit var auth: FirebaseAuth
    override val kodein by kodein()
    private val factory: AuthViewModelFactory by instance()
    private lateinit var viewModel: AuthViewModel
    private lateinit var binding: ActivityLoginBinding
    private val sharedPreferences: SharedPreferences by instance(arg = CONSTANTS.PREF_NAME)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
//        setTheme(R.style.AppTheme_Login)
        ButterKnife.bind(this)
        //auth = FirebaseAuth.getInstance()
        init()
        initFCM()
    }

    private fun init() {
        //bottom_sheet.visibility = View.GONE
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_login)
        viewModel = ViewModelProviders.of(this, factory).get(AuthViewModel::class.java)
        //.of(this,factory).get(AuthViewModel::class.java)
        binding.viewModel = viewModel
        viewModel.authListner = this

        btn_login.setOnClickListener {
            viewModel.login()
        }


    }

    @OnClick(
        R.id.tv_signup,
        R.id.btn_login
    )
    internal fun click(view: View) {
        when (view.id) {
            R.id.tv_signup -> {
                // startActivity(Intent(this, SignUpActivity::class.java))
            }
            R.id.btn_login -> {


            }

        }
    }

    fun hideKeyBoard(layout: View) {
        hideKeyboard()
    }

    override fun onStarted() {
        showProgress()
    }

    override fun onSuccess() {
        hideProgress()
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun onError(msg: String) {
        hideProgress()
        showToastLong(msg)
    }

    override fun onStart() {
        super.onStart()
        viewModel.user?.let {
            Intent(this, MainActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun initFCM() {
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            val devicePushToken = it.token
            sharedPreferences.edit().putString(PUSH_TOKEN, devicePushToken).apply()
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

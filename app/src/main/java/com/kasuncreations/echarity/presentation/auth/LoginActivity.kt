package com.kasuncreations.echarity.presentation.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.firebase.auth.FirebaseAuth
import com.kasuncreations.echarity.R
import com.kasuncreations.echarity.databinding.ActivityLoginBinding
import com.kasuncreations.echarity.presentation.main.MainActivity
import com.kasuncreations.echarity.utils.BaseActivity
import com.kasuncreations.echarity.utils.showToastLong
import kotlinx.android.synthetic.main.activity_login.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class LoginActivity : BaseActivity(), AuthListner, KodeinAware {

    private lateinit var auth: FirebaseAuth
    override val kodein by kodein()
    private val factory: AuthViewModelFactory by instance()
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ButterKnife.bind(this)
        //auth = FirebaseAuth.getInstance()
        init()
    }

    private fun init() {
        //bottom_sheet.visibility = View.GONE
        val binding: ActivityLoginBinding =
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
                startActivity(Intent(this, SignUpActivity::class.java))
            }
            R.id.btn_login -> {
                startActivity(Intent(this, MainActivity::class.java))
            }

        }
    }

    override fun onStarted() {
        showProgress()
    }

    override fun onSuccess() {
        hideProgress()
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

}
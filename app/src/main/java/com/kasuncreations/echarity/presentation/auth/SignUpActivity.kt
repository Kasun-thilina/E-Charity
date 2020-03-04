package com.kasuncreations.echarity.presentation.auth

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.kasuncreations.echarity.R
import com.kasuncreations.echarity.databinding.ActivitySignUpBinding
import com.kasuncreations.echarity.presentation.main.MainActivity
import com.kasuncreations.echarity.utils.BaseActivity
import com.kasuncreations.echarity.utils.showToastLong
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class SignUpActivity : BaseActivity(), AuthListner, KodeinAware {

    override val kodein by kodein()
    private val factory: AuthViewModelFactory by instance()
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        val binding: ActivitySignUpBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        viewModel = ViewModelProviders.of(this, factory).get(AuthViewModel::class.java)
        binding.viewModel = viewModel

        btn_signup.setOnClickListener {
            viewModel.signUp()
        }
        viewModel.authListner = this
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.label_sign_up)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStarted() {
        showProgress()
        Intent(this, MainActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(it)
        }
    }

    override fun onSuccess() {
        hideProgress()
        Intent(this, LoginActivity::class.java).also {
            startActivity(it)
        }
    }

    override fun onError(msg: String) {
        hideProgress()
        showToastLong(msg)
    }

}

package com.kasuncreations.echarity.presentation.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.firebase.auth.FirebaseAuth
import com.kasuncreations.echarity.R
import com.kasuncreations.echarity.presentation.home.MainActivity
import com.kasuncreations.echarity.utils.BaseActivity

class LoginActivity : BaseActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ButterKnife.bind(this)
        auth = FirebaseAuth.getInstance()

        init()
    }

    private fun init() {
        //bottom_sheet.visibility = View.GONE
/*
        val bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        tv_signup.setOnClickListener {
            showToastLong("Clicked!")
            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
            } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
            }

        }*/
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
}

package com.kasuncreations.echarity

import android.content.Intent
import android.os.Bundle
import android.view.View
import butterknife.ButterKnife
import butterknife.OnClick

class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ButterKnife.bind(this)

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

    @OnClick(R.id.tv_q_signup)
    internal fun click(view: View) {
        when (view.id) {
            R.id.tv_signup -> {
                startActivity(Intent(this, SignUpActivity::class.java))
            }

        }
    }
}

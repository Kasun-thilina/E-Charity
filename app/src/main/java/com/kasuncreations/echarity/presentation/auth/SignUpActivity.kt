package com.kasuncreations.echarity.presentation.auth

import android.os.Bundle
import com.kasuncreations.echarity.R
import com.kasuncreations.echarity.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.label_sign_up)

    }


}

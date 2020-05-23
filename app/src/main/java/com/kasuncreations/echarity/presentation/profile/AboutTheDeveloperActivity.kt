package com.kasuncreations.echarity.presentation.profile

import android.os.Bundle
import com.kasuncreations.echarity.R
import com.kasuncreations.echarity.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_about_the_developer.*

class AboutTheDeveloperActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_the_developer)
        setSupportActionBar(toolbar_about)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "About The Application"
    }
}

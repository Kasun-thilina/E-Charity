package com.kasuncreations.echarity.presentation.post

import android.os.Bundle
import android.widget.ArrayAdapter
import com.kasuncreations.echarity.R
import com.kasuncreations.echarity.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_add_post.*

class AddPostActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)
        setSupportActionBar(toolbar_addpost)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.label_add_post)
        init()
    }

    private fun init() {
        val categories = arrayOf("Requesting For Help", "Willing To Help")
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_category!!.adapter = aa
    }
}

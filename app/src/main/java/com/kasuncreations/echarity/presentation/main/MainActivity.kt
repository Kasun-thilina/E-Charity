package com.kasuncreations.echarity.presentation.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.android.material.bottomappbar.BottomAppBar
import com.kasuncreations.echarity.R
import com.kasuncreations.echarity.presentation.chat.ChatFragment
import com.kasuncreations.echarity.presentation.home.HomeFragment
import com.kasuncreations.echarity.presentation.map.MapFragment
import com.kasuncreations.echarity.presentation.post.AddPostActivity
import com.kasuncreations.echarity.presentation.profile.ProfileFragment

class MainActivity : AppCompatActivity() {
    private lateinit var bottomAppBar: BottomAppBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        setUpBottomNavigation()
    }

    private fun setUpBottomNavigation() {
        bottomAppBar = findViewById(R.id.bottom_nav)
        setSupportActionBar(bottomAppBar)
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
        R.id.btn_floating
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
            R.id.btn_floating -> {
                Intent(this, AddPostActivity::class.java).also {
                    this.startActivity(it)
                }
            }
        }
    }


}

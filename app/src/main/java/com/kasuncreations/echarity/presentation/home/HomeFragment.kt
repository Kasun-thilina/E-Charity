package com.kasuncreations.echarity.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kasuncreations.echarity.R
import com.kasuncreations.echarity.utils.BaseFragment

class HomeFragment : BaseFragment() {


    companion object {
        const val TAG = "chat"
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, null)
        init()
        return view
    }

    private fun init() {

    }

    override fun onPause() {
        super.onPause()
        //hideProgress()
    }

    override fun onResume() {
        super.onResume()
        //  hideProgress()
    }

}
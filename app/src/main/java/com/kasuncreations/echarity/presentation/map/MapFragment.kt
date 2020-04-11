package com.kasuncreations.echarity.presentation.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kasuncreations.echarity.R
import com.kasuncreations.echarity.presentation.home.HomeFragment
import com.kasuncreations.echarity.utils.BaseFragment

class MapFragment : BaseFragment() {


    companion object {
        const val TAG = "chat"
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, null)
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
package com.kasuncreations.echarity.presentation.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.kasuncreations.echarity.R
import com.kasuncreations.echarity.utils.*
import com.kasuncreations.echarity.utils.dialog.alertDialog

class MapFragment : BaseFragment(), OnMapReadyCallback {


    companion object {
        const val TAG = "map"
        fun newInstance() = MapFragment()
    }

    private lateinit var mMap: GoogleMap
    private lateinit var mMapView: MapView
    private var paused = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, null)
        mMapView = view.findViewById(R.id.mv_map)
        mMapView.onCreate(savedInstanceState)
        mMapView.getMapAsync(this)
        return view
    }

    @SuppressLint("ResourceType")
    private fun initLocation() {
        if (activity!!.isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            if (activity!!.isProviderEnabled()) {
                mMap.isMyLocationEnabled = true
                val locationButton = mMapView.findViewById(2) as ImageView
                locationButton.setImageResource(R.drawable.btn_user_location)
                val rlp = locationButton.layoutParams as RelativeLayout.LayoutParams
                rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
                rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
                rlp.setMargins(0, 0, 180, 280)
            } else {
                alertDialog(
                    getString(R.string.msg_gps_dialog_title),
                    getString(R.string.msg_gps_provider)
                ) {
                    positiveButton("OK") {
                        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    }
                }
            }
        } else {
            activity!!.requestPermission(
                Manifest.permission.ACCESS_FINE_LOCATION,
                CONSTANTS.REQUEST_CODE_LOCATION_PERMISSION
            )
        }

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!
        initLocation()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        mMapView.onPause()
        paused = true
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView.onLowMemory()
    }

    override fun onResume() {
        super.onResume()
        mMapView.onResume()
        if (paused) {
            initLocation()
            paused = false
        }

    }
}
package com.kasuncreations.echarity.presentation.map

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.kasuncreations.echarity.R
import com.kasuncreations.echarity.utils.BaseActivity
import com.kasuncreations.echarity.utils.CONSTANTS.LOCATION_COORDINATES
import com.kasuncreations.echarity.utils.CONSTANTS.LOCATION_NAME
import com.kasuncreations.echarity.utils.CONSTANTS.REQUEST_CODE_LOCATION_PERMISSION
import com.kasuncreations.echarity.utils.dialog.alertDialog
import com.kasuncreations.echarity.utils.isPermissionGranted
import com.kasuncreations.echarity.utils.isProviderEnabled
import com.kasuncreations.echarity.utils.requestPermission
import kotlinx.android.synthetic.main.activity_location_picker.*

class LocationPickerActivity : BaseActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mMapView: MapView
    private var paused = false
    private var isSelected = false
    private lateinit var locationName: String
    private lateinit var locationData: LatLng
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_picker)
        setSupportActionBar(toolbar_location_picker)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.label_location_picker)
        mMapView = findViewById(R.id.mv_location_picker)
        mMapView.onCreate(savedInstanceState)
        mMapView.getMapAsync(this)
    }

    @SuppressLint("ResourceType")
    private fun initLocation() {
        if (isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            if (isProviderEnabled()) {
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
                        startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    }
                }
            }
        } else {
            requestPermission(
                Manifest.permission.ACCESS_FINE_LOCATION,
                REQUEST_CODE_LOCATION_PERMISSION
            )
        }
        btn_addpost.setOnClickListener {
            if (isSelected) {
                val intent = Intent()
                intent.putExtra(LOCATION_NAME, locationName)
                intent.putExtra(LOCATION_COORDINATES, locationData)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        initLocation()
        mMap.setOnMapClickListener { latLng ->
            mMap.clear()
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
            mMap.addMarker(MarkerOptions().position(latLng))
            val geocoder = Geocoder(this)
            val res = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 3)
            locationData = latLng
            println(res[0].adminArea)

            /**
             * Places API is not used because its paid
             */
            /*Places.initialize(applicationContext, getString(R.string.google_api_key))
            val placesClient = Places.createClient(this)
            val placeFields: List<Place.Field> = listOf(Place.Field.NAME)
            val request =FindCurrentPlaceRequest.builder(placeFields).build()

            placesClient.findCurrentPlace(request).addOnSuccessListener {response->
                for(placelikehood in response.placeLikelihoods){
                    Log.i("TAG", String.format("Place '%s' has likelihood: %f",
                        placelikehood.place.name,
                        placelikehood.likelihood
                    ));
                }
            }.addOnFailureListener{
                Log.e("error", "Place not found: $it");
            }*/


            btn_location.text = res[0].getAddressLine(0)
            locationName = res[0].getAddressLine(0)
            btn_location.visibility = View.VISIBLE
            btn_location.isSelected = true
            isSelected = true
            btn_addpost.setBackgroundResource(R.drawable.bg_rounded_border_btn)
        }
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

package com.kasuncreations.echarity.presentation.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.firebase.database.DataSnapshot
import com.kasuncreations.echarity.R
import com.kasuncreations.echarity.data.models.Post
import com.kasuncreations.echarity.presentation.home.HomeViewModel
import com.kasuncreations.echarity.presentation.profile.UserViewModel
import com.kasuncreations.echarity.presentation.profile.UserViewModelFactory
import com.kasuncreations.echarity.utils.*
import com.kasuncreations.echarity.utils.dialog.alertDialog
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

/**
 * Map Fragment to display posts inside a map within a 10Km radius to the user location
 * Clicking the map will show a recyclerview with the selected markers relevant post
 * Future closer markers can be merged to one and make the recyclerview scrollable horizontally
 * @author kasun.thilina.t@gmail.com
 * @since 2nd MAY 2020
 */
class MapFragment : BaseFragment(), OnMapReadyCallback, LocationListener, KodeinAware,
    GoogleMap.OnMarkerClickListener {


    companion object {
        const val TAG = "map"
        fun newInstance() = MapFragment()
    }

    override val kodein by lazy { (context as KodeinAware).kodein }
    private lateinit var mMap: GoogleMap
    private lateinit var mMapView: MapView
    private var paused = false
    private lateinit var mapDetailsAdapter: MapDetailsAdapter
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var rvPosts: RecyclerView
    private lateinit var mapViewModel: HomeViewModel
    private var liveData: LiveData<DataSnapshot?>? = null
    private lateinit var postsList: MutableList<Post>
    private lateinit var sortedPostsList: MutableList<Post>
    private lateinit var locationManager: LocationManager
    private lateinit var sortedPostsMap: Map<Marker, Post>
    private val userFactory: UserViewModelFactory by instance()
    private lateinit var userViewModel: UserViewModel
    private var userLiveData: LiveData<DataSnapshot?>? = null
    private val RADIUS = 50000.0 //10km

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, null)
        mMapView = view.findViewById(R.id.mv_map)
        mMapView.onCreate(savedInstanceState)
        mMapView.getMapAsync(this)
        rvPosts = view.findViewById(R.id.rv_details)
        userViewModel = ViewModelProviders.of(this, userFactory).get(UserViewModel::class.java)
        postsList = mutableListOf()
        sortedPostsMap = HashMap()
        sortedPostsList = mutableListOf()
        return view
    }


    private fun loadData(latLng: LatLng) {
        showProgress()
        if (!this.isDetached) {
            mapViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        }
        liveData = mapViewModel.getDataSnapshotLiveData()

        liveData!!.observe(viewLifecycleOwner, Observer {
            postsList.clear()
            it!!.children.map { post ->
                postsList.add(post.getValue(Post::class.java)!!)
                //setPins(post.getValue(Post::class.java)!!)
                // println(post)
            }
            sortData(latLng)
            hideProgress()
        })
        context.showToastLong("Displaying Posts within a 50Km radius")
        //drawCircle(latLng)
    }

    private fun drawCircle(latLng: LatLng) {
        val circleOptions = CircleOptions()
            .center(latLng)
            .radius(RADIUS)
            .strokeWidth(1F)
            .fillColor(R.color.colorWhite)

        mMap.addCircle(circleOptions)
    }

    private fun sortData(latLng: LatLng) {
        val result = FloatArray(1)
        var distanceBetween: Float
        postsList.map { post ->
            Location.distanceBetween(
                latLng.latitude,
                latLng.longitude,
                post.latitude!!,
                post.longitude!!,
                result
            )
            distanceBetween = result[0]
            if (distanceBetween < RADIUS) {//10km
                setPins(post)
            }
        }
        // println(sortedPostsList)
    }

    private fun setPins(post: Post) {
        val markerOptions = MarkerOptions()
        markerOptions.position(LatLng(post.latitude!!, post.longitude!!))
        markerOptions.title(post.tittle)
        if (post.category == 0) {
            markerOptions.icon(bitMapFromVector(R.drawable.ic_location_marker_help))
        } else {
            markerOptions.icon(bitMapFromVector(R.drawable.ic_location_marker_support))
        }
        markerOptions.position
        val marker = mMap.addMarker(markerOptions)
        (sortedPostsMap as HashMap<Marker, Post>)[marker] = post
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
                locationManager = context!!.getSystemService(LOCATION_SERVICE) as LocationManager
                val mCriteria = Criteria()
                mCriteria.accuracy = Criteria.ACCURACY_MEDIUM
                mCriteria.powerRequirement = Criteria.POWER_LOW
                mCriteria.isSpeedRequired = false
                mCriteria.isAltitudeRequired = false
                mCriteria.isBearingRequired = false
                mCriteria.isCostAllowed = false
                val provider = locationManager.getBestProvider(mCriteria, true)
                locationManager.requestLocationUpdates(provider!!, 10000, 10000F, this)
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



        googleMap.setOnMarkerClickListener(this@MapFragment)
        googleMap.setOnMapClickListener {
            rvPosts.visibility = View.GONE
        }

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

    private fun setLocation(latLng: LatLng) {
        //mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13F))
        loadData(latLng)
        // mMap.addMarker(MarkerOptions().position(latLng))
    }

    override fun onLocationChanged(location: Location?) {
        //context.showToastLong(location.toString())
        setLocation(LatLng(location!!.latitude, location.longitude))
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        context.showToastLong("Status Changed")
    }

    override fun onProviderEnabled(provider: String?) {
        context.showToastLong("Provider Enabled")
    }

    override fun onProviderDisabled(provider: String?) {
        context.showToastLong("Provider Disabled")

    }

    private fun bitMapFromVector(vectorResID: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context!!, vectorResID)
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable!!.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        println(marker)
        rvPosts.visibility = View.VISIBLE
        sortedPostsList.clear()
        sortedPostsList.add(sortedPostsMap[marker]!!)
        mapDetailsAdapter = MapDetailsAdapter(
            context!!,
            sortedPostsList,
            userViewModel,
            userLiveData,
            viewLifecycleOwner
        )
        mLayoutManager = LinearLayoutManager(context!!, LinearLayoutManager.HORIZONTAL, false)
        rvPosts.layoutManager = mLayoutManager
        rvPosts.adapter = mapDetailsAdapter
        return false
    }
}
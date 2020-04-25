package com.kasuncreations.echarity.presentation.post

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProviders
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.kasuncreations.echarity.R
import com.kasuncreations.echarity.data.models.Post
import com.kasuncreations.echarity.presentation.auth.Listner
import com.kasuncreations.echarity.presentation.map.LocationPickerActivity
import com.kasuncreations.echarity.utils.*
import com.kasuncreations.echarity.utils.CONSTANTS.LOCATION_NAME
import com.kasuncreations.echarity.utils.CONSTANTS.REQUEST_CODE_CAMERA
import com.kasuncreations.echarity.utils.CONSTANTS.REQUEST_CODE_CAMERA_PERMISSION
import com.kasuncreations.echarity.utils.CONSTANTS.REQUEST_CODE_GALLERY
import com.kasuncreations.echarity.utils.CONSTANTS.REQUEST_CODE_LOCATION_PICKER
import com.kasuncreations.echarity.utils.CONSTANTS.REQUEST_CODE_WRITE_PERMISSION
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.activity_add_post.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.io.File

class AddPostActivity : BaseActivity(), OnMapReadyCallback, KodeinAware, Listner {

    override val kodein by kodein()
    private val factory: PostViewModelFactory by instance()
    private lateinit var viewModel: PostViewModel

    private lateinit var mMap: GoogleMap
    private lateinit var mMapView: MapView
    private var file: File? = null
    private var fileUri: Uri? = null
    private var imageUri: Uri? = null
    private var location: LatLng? = null
    private var locationName: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)
        setSupportActionBar(toolbar_addpost)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.btn_label_add_post)
        ButterKnife.bind(this)
        init()
        mMapView = findViewById(R.id.fl_mapview)
        mMapView.onCreate(savedInstanceState)
        mMapView.getMapAsync(this)

        viewModel = ViewModelProviders.of(this, factory).get(PostViewModel::class.java)
        viewModel.listner = this
    }

    private fun init() {
        val categories = arrayOf("Requesting For Help", "Willing To Help")
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_category!!.adapter = aa
        println(System.currentTimeMillis() / 1000)

    }

    private fun selectImage() {
        val options =
            arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select an image for your post")

        builder.setItems(options) { dialog, item ->
            when {
                options[item] == "Take Photo" -> {
                    if (isPermissionGranted(Manifest.permission.CAMERA)) {
                        pickFromCamera()
                    } else {
                        requestPermission(
                            Manifest.permission.CAMERA,
                            REQUEST_CODE_CAMERA_PERMISSION
                        )
                    }
                }
                options[item] == "Choose from Gallery" -> {
                    if (isPermissionGranted(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                    ) {
                        pickFromGallery()
                    } else {
                        requestPermission(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            REQUEST_CODE_WRITE_PERMISSION
                        )
                    }
                }
                options[item] == "Cancel" -> {
                    dialog.dismiss()
                }
            }
        }
        builder.show()
    }

    @OnClick(
        R.id.iv_image, R.id.et_location, R.id.btn_addpost, R.id.add_post_parent
    )
    internal fun click(view: View) {
        when (view.id) {
            R.id.iv_image -> {
                selectImage()
            }
            R.id.et_location -> {
                val intent = Intent(this, LocationPickerActivity::class.java)
                startActivityForResult(intent, REQUEST_CODE_LOCATION_PICKER)
            }
            R.id.btn_addpost -> {
                verifyInputs()
            }
            R.id.add_post_parent -> {
                hideKeyboard()
            }
        }
    }

    private fun verifyInputs() {
        if (et_title.text.isNullOrEmpty()) {
            et_title.error = "Title Should Not Be Empty!"
            return
        }
        if (et_description.text.isNullOrEmpty()) {
            et_description.error = "Description Should Not Be Empty!"
            return
        }
        if (et_location.text.isNullOrEmpty()) {
            et_location.error = "You Should Select A Location!"
            return
        }
        val post = Post()
        post.description = et_description.text.toString()
        post.tittle = et_title.text.toString()
        post.latitude = location!!.latitude.toLong()
        post.longitude = location!!.longitude.toLong()
        post.category = spinner_category.selectedItemId.toInt()
        post.locationName = locationName
        if (imageUri != null) {
            viewModel.savePost(post, imageUri)
        } else {
            viewModel.savePost(post)
        }
    }

    private fun pickFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png")
        galleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY)
    }

    private fun pickFromCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            file = File(
                getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS + "/temp")!!.path,
                System.currentTimeMillis().toString() + ".jpg"
            )
            fileUri = FileProvider.getUriForFile(
                this,
                this.applicationContext.packageName + ".provider",
                file!!
            )
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }
            startActivityForResult(takePictureIntent, REQUEST_CODE_CAMERA)
        }

    }


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_CANCELED) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                when (requestCode) {
                    REQUEST_CODE_CAMERA -> {
                        UCrop.of(fileUri!!, Uri.fromFile(File.createTempFile("Image", "")))
                            .withAspectRatio(8F, 3.2F)
                            .start(this)
                        file?.delete()
                    }
                    REQUEST_CODE_GALLERY -> {
                        val selectedImage: Uri = data.data!!
                        UCrop.of(selectedImage, Uri.fromFile(File.createTempFile("Image", "")))
                            .withAspectRatio(8F, 3.2F)
                            .start(this)
                    }
                    UCrop.REQUEST_CROP -> {
                        iv_image.setImageURI(UCrop.getOutput(data))
                        imageUri = UCrop.getOutput(data)
                        println(UCrop.getOutput(data))
                    }
                    REQUEST_CODE_WRITE_PERMISSION -> {
                        selectImage()
                    }
                    REQUEST_CODE_LOCATION_PICKER -> {
                        locationName = data.getStringExtra(LOCATION_NAME)
                        et_location.text = locationName
                        location = data.getParcelableExtra(CONSTANTS.LOCATION_COORDINATES)
                        setLocation(location!!)
                    }
                }
            }
        }
    }

    private fun setLocation(latLng: LatLng) {
        fl_mapview.visibility = View.VISIBLE
        //mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13F))
        mMap.addMarker(MarkerOptions().position(latLng))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_WRITE_PERMISSION -> {
                pickFromGallery()
            }
            REQUEST_CODE_CAMERA_PERMISSION -> {
                pickFromCamera()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        mMapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView.onLowMemory()
    }

    override fun onResume() {
        super.onResume()
        mMapView.onResume()

    }

    override fun onStarted() {
        showProgress()
    }

    override fun onSuccess() {
        hideProgress()
        finish()
    }

    override fun onError(msg: String) {
        hideProgress()
        showToastLong(msg)
    }

}

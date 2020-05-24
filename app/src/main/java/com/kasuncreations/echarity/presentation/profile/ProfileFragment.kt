package com.kasuncreations.echarity.presentation.profile

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.kasuncreations.echarity.R
import com.kasuncreations.echarity.data.models.User
import com.kasuncreations.echarity.databinding.FragmentProfileBinding
import com.kasuncreations.echarity.presentation.auth.AuthViewModel
import com.kasuncreations.echarity.presentation.auth.AuthViewModelFactory
import com.kasuncreations.echarity.presentation.auth.Listner
import com.kasuncreations.echarity.presentation.auth.LoginActivity
import com.kasuncreations.echarity.utils.*
import com.kasuncreations.echarity.utils.dialog.alertDialog
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.io.File

class ProfileFragment : BaseFragment(), KodeinAware, Listner {
    private val factory: AuthViewModelFactory by instance()
    private val userFactory: UserViewModelFactory by instance()
    private lateinit var viewModel: AuthViewModel
    override val kodein: Kodein by kodein()
    private val sharedPreferences: SharedPreferences by instance(arg = CONSTANTS.PREF_NAME)
    private lateinit var userViewModel: UserViewModel
    private var liveData: LiveData<DataSnapshot?>? = null
    private var file: File? = null
    private var fileUri: Uri? = null
    private var imageUri: Uri? = null
    private lateinit var userID: String
    private lateinit var user: User

    companion object {
        const val TAG = "profile"
        fun newInstance() = ProfileFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentProfileBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        val view = binding.root
        viewModel = ViewModelProviders.of(this, factory).get(AuthViewModel::class.java)
        userViewModel = ViewModelProviders.of(this, userFactory).get(UserViewModel::class.java)
        userID = sharedPreferences.getString(CONSTANTS.USER_ID, "")!!
        userViewModel.setQuery(userID)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        loadData()

        view.btn_logout.setOnClickListener {
            //viewModel.logOut()
            this.alertDialog("Logout", getString(R.string.msg_sign_out)) {
                positiveButton("Yes") {
                    viewModel.logOut()
                    val mainIntent = Intent(activity, LoginActivity::class.java)
                    mainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    mainIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(mainIntent)
                }
                negativeButton("No") {

                }
            }

        }
        view.btn_about_the_developer.setOnClickListener {
            context.startActivity<AboutTheDeveloperActivity>()
        }

        view.btn_settings.setOnClickListener {
            context.showToastLong("Under Development")
        }

        view.iv_btn_camera_user_profile.setOnClickListener {
            selectImage()
        }
        return view
    }

    private fun loadData() {
        liveData = userViewModel.getDataSnapshotLiveData()
        showProgress()
        liveData!!.observe(viewLifecycleOwner, Observer {
            user = it!!.getValue(User::class.java)!!
            tv_up_user_name.text = "${user.first_name} ${user.last_name}"
            if (!user.avatar.isNullOrEmpty()) {
                Glide.with(context!!).load(user.avatar).into(iv_user_profile_image)
            }
            hideProgress()
        })

    }

    private fun selectImage() {
        val options =
            arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Select an image for your post")

        builder.setItems(options) { dialog, item ->
            when {
                options[item] == "Take Photo" -> {
                    if (activity!!.isPermissionGranted(Manifest.permission.CAMERA)) {
                        pickFromCamera()
                    } else {
                        activity!!.requestPermission(
                            Manifest.permission.CAMERA,
                            CONSTANTS.REQUEST_CODE_CAMERA_PERMISSION
                        )
                    }
                }
                options[item] == "Choose from Gallery" -> {
                    if (activity!!.isPermissionGranted(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                    ) {
                        pickFromGallery()
                    } else {
                        activity!!.requestPermission(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            CONSTANTS.REQUEST_CODE_WRITE_PERMISSION
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

    private fun pickFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png")
        galleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        startActivityForResult(galleryIntent, CONSTANTS.REQUEST_CODE_GALLERY)
    }

    private fun pickFromCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(activity!!.packageManager) != null) {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE, "New Picture")
            values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
            fileUri = activity!!.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values
            )
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }
            startActivityForResult(takePictureIntent, CONSTANTS.REQUEST_CODE_CAMERA)
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
                    CONSTANTS.REQUEST_CODE_CAMERA -> {
                        UCrop.of(fileUri!!, Uri.fromFile(File.createTempFile("Image", "")))
                            .withAspectRatio(1F, 1F)
                            .start(context!!, this)
                        file?.delete()
                    }
                    CONSTANTS.REQUEST_CODE_GALLERY -> {
                        val selectedImage: Uri = data.data!!
                        UCrop.of(selectedImage, Uri.fromFile(File.createTempFile("Image", "")))
                            .withAspectRatio(1F, 1F)
                            .start(context!!, this)
                    }
                    UCrop.REQUEST_CROP -> {
                        iv_user_profile_image.setImageURI(UCrop.getOutput(data))
                        imageUri = UCrop.getOutput(data)
                        userViewModel.updateDP(imageUri!!)
                        println(UCrop.getOutput(data))
                    }
                    CONSTANTS.REQUEST_CODE_WRITE_PERMISSION -> {
                        selectImage()
                    }

                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CONSTANTS.REQUEST_CODE_WRITE_PERMISSION -> {
                pickFromGallery()
            }
            CONSTANTS.REQUEST_CODE_CAMERA_PERMISSION -> {
                pickFromCamera()
            }
        }
    }

    override fun onStarted() {
        showProgress()
    }

    override fun onSuccess() {
        hideProgress()
    }

    override fun onError(msg: String) {
        hideProgress()
    }

}

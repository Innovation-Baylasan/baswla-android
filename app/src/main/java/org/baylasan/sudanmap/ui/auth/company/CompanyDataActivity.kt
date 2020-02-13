package org.baylasan.sudanmap.ui.auth.company

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toFile
import androidx.lifecycle.Observer
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.activity_company_data.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.common.*
import org.baylasan.sudanmap.data.entity.model.Category
import org.baylasan.sudanmap.data.user.model.RegisterCompanyRequest
import org.baylasan.sudanmap.data.user.model.RegisterRequest
import org.baylasan.sudanmap.domain.LocationViewModel
import org.baylasan.sudanmap.ui.LocationPickerActivity
import org.baylasan.sudanmap.ui.addentity.CategoryEntityAdapter
import org.baylasan.sudanmap.ui.auth.signup.DataEvent
import org.baylasan.sudanmap.ui.auth.signup.ErrorEvent
import org.baylasan.sudanmap.ui.auth.signup.LoadingEvent
import org.baylasan.sudanmap.ui.layers.MapLayersViewModel
import org.baylasan.sudanmap.ui.main.MainActivity
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File


class CompanyDataActivity : AppCompatActivity() {
    private var imageSelected = -1

    private val picasso by inject<Picasso>()
    private val viewModel by viewModel<MapLayersViewModel>()
    private val locationViewModel by viewModel<LocationViewModel>()
    private val completeRegisterViewModel by viewModel<CompleteRegisterViewModel>()
    private var snackBar: Snackbar? = null
    private lateinit var googleMap: GoogleMap
    private var selectedCategory: Category? = null
    private var selectedLocation: LatLng? = null
    private var selectedAvatar: File? = null
    private var selectedCover: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_data)
        avatar.setImageResource(R.drawable.ic_camera)
        viewModel.loadCategories()
        viewModel.events.observe(this, Observer {
            if (it is UiState.Loading || it is UiState.Empty) {
                snackBar?.dismiss()
            } else if (it is UiState.Success) {
                snackBar?.dismiss()
                val categories = it.data
                val categoryEntityAdapter = CategoryEntityAdapter(this, categories)
                categorySpinner.setAdapter(categoryEntityAdapter)
                categorySpinner.setOnItemClickListener { _, _, position, _ ->
                    val category = categories[position]
                    categorySpinner.setText(category.name, false)
                    selectedCategory = category
                    categorySpinner.clearError()
                }
            } else {
                snackBar =
                    Snackbar.make(
                        layout,
                        getString(R.string.failed_to_load_categories),
                        Snackbar.LENGTH_INDEFINITE
                    )
                        .setAction(R.string.retry) {
                            viewModel.loadCategories()
                        }
                snackBar?.show()
            }
        })
        val supportMapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        supportMapFragment.getMapAsync { googleMap ->
            googleMap.uiSettings.disableAll()
            this.googleMap = googleMap
            val marker =
                googleMap.addMarker(MarkerOptions().position(googleMap.cameraPosition.target))
            googleMap.setOnCameraMoveListener {
                marker.position = googleMap.cameraPosition.target
            }
            pickLocationLayout.setOnClickListener {
                startActivityForResult(Intent(this, LocationPickerActivity::class.java), 12)

            }
            locationViewModel.getLocationUpdates().observe(this, Observer { location ->
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location.toLatLng(), 12f))
            })
        }

        pickAvatarButton.setOnClickListener {
            imageSelected = 1
            pickAndCropImage()

        }

        pickCoverImageButton.setOnClickListener {
            imageSelected = 2
            pickAndCropImage()
        }
        completeRegister.setOnClickListener {
            val description = companyDescription.text.toString()
            if (selectedCategory == null) {
                toast(R.string.select_category)
                categorySpinner.error = ""
                return@setOnClickListener
            }
            if (description.isEmpty()) {
                companyDescription.error = getString(R.string.descritption_field_required)
                return@setOnClickListener
            }
            if (selectedLocation == null) {
                toast(R.string.location_not_selected)
                return@setOnClickListener
            }
            val registerRequest = intent?.getParcelableExtra<RegisterRequest>("registerData")
            completeRegisterViewModel.registerCompany(
                RegisterCompanyRequest(
                    email = registerRequest!!.email,
                    cover = selectedCover,
                    avatar = selectedAvatar,
                    name = registerRequest.name,
                    categoryId = selectedCategory!!.id,
                    description = description,
                    location = "${selectedLocation!!.latitude},${selectedLocation!!.longitude}",
                    password = registerRequest.password,
                    passwordConfirmation = registerRequest.passwordConfirmation,
                    userType = "company"


                )
            )
            completeRegisterViewModel.events.observe(this, Observer { event ->
                when (event) {
                    is DataEvent -> {
                        completeRegister.hideProgress("Done.")
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                    is LoadingEvent -> {

                        completeRegister.showProgress {
                            buttonText = "Completing registration..."
                            progressColor
                        }
                    }
                    is ErrorEvent -> {
                        completeRegister.hideProgress("Failed.")
                        toast(event.errorMessage)
                    }


                }
            })
        }
    }

    private fun pickAndCropImage() {
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
            .setFixAspectRatio(true)
            .setAspectRatio(
                if (imageSelected == 2) 16 else 1,
                if (imageSelected == 2) 9 else 1
            )
            .setActivityTitle(
                if (imageSelected == 2)
                    getString(R.string.crop_cover_title)
                else
                    getString(R.string.crop_profile_title)
            )
            .start(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageFile = CropImage.getActivityResult(data).uri.toFile()
            if (imageSelected == 2) {
                selectedCover = imageFile
                picasso.load(imageFile).into(coverImage)
            } else {
                avatar.setImageResource(0)
                selectedAvatar = imageFile
                picasso.load(imageFile)
                    .transform(CropCircleTransformation())
                    .into(avatar)

            }

        }
        if (requestCode == 12 && resultCode == Activity.RESULT_OK) {
            val latLng = data?.getParcelableExtra<LatLng>("picked_location")
            if (latLng != null) {
                placeSelectionPlaceHolder.gone()
                selectedLocation = latLng
                googleMap.clear()
                googleMap.zoomToMyLocation(latLng.latitude, latLng.longitude, false)
                googleMap.addMarker(MarkerOptions().position(latLng))
            }
        }
    }

}



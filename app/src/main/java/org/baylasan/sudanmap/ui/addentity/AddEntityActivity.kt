package org.baylasan.sudanmap.ui.addentity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.lifecycle.Observer
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.activity_add_entity.*
import kotlinx.android.synthetic.main.content_add_entity.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.common.*
import org.baylasan.sudanmap.data.common.UnAuthorizedException
import org.baylasan.sudanmap.data.entity.model.AddEntityRequest
import org.baylasan.sudanmap.data.entity.model.Category
import org.baylasan.sudanmap.domain.LocationViewModel
import org.baylasan.sudanmap.ui.LocationPickerActivity
import org.baylasan.sudanmap.ui.layers.MapLayersViewModel
import org.baylasan.sudanmap.ui.view.AppBarChangedListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddEntityActivity : AppCompatActivity() {
    private val onStateChanged = object : AppBarChangedListener() {
        override fun onStateChanged(appBarLayout: AppBarLayout, state: State) {
            if (state == State.COLLAPSED) {
                toolbar.setBackgroundColor(
                    ContextCompat.getColor(
                        this@AddEntityActivity,
                        R.color.yellowAccent
                    )
                )
            } else {
                toolbar.setBackgroundColor(
                    ContextCompat.getColor(
                        this@AddEntityActivity,
                        android.R.color.transparent
                    )
                )
            }

        }
    }
    private val viewModel by viewModel<MapLayersViewModel>()
    private var snackbar: Snackbar? = null
    private var isAvatarClicked: Boolean = false
    private var selectedCoverImage: Uri? = null
    private var selectedAvatarImage: Uri? = null
    private var selectedLocation: LatLng? = null
    private var selectedCategory: Category? = null
    private lateinit var googleMap: GoogleMap

    private val picasso by inject<Picasso>()
    private val locationViewModel by viewModel<LocationViewModel>()
    private val addEntityViewModel by viewModel<AddEntityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_entity)
        bindProgressButton(submitEntityButton)
        appBar.addOnOffsetChangedListener(onStateChanged)
        viewModel.loadCategories()

        pickAvatarImageButton.setOnClickListener {
            isAvatarClicked = true
            CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setFixAspectRatio(true)
                .setAspectRatio(1, 1)

                .start(this)
        }
        pickCoverImageButton.setOnClickListener {
            isAvatarClicked = false
            CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setFixAspectRatio(true)
                .setAspectRatio(19, 6)
                .start(this)
        }
        submitEntityButton.setOnClickListener {
            val name = entityName.text.toString()
            val description = entityDescription.text.toString()
            if (name.isEmpty()) {
                entityName.error = getString(R.string.name_required)
                return@setOnClickListener
            }
            if (description.isEmpty()) {
                entityDescription.error = getString(R.string.descritption_field_required)
                return@setOnClickListener
            }
            if (selectedLocation == null) {
                toast(getString(R.string.location_not_selected))
                return@setOnClickListener
            }
            if (selectedCategory == null) {
                entityCategorySpinner.error = ""
                return@setOnClickListener
            }
            addEntityViewModel.add(
                AddEntityRequest(
                    category = selectedCategory!!.id,
                    name = name,
                    avatar = selectedAvatarImage?.toFile(),
                    cover = selectedCoverImage?.toFile(),
                    description = description,
                    locationLat = selectedLocation!!.latitude.toString(),
                    locationLng = selectedLocation!!.longitude.toString()

                )
            )
            observeAddState()
        }
        setupCategorySpinner()
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


    }

    private fun observeAddState() {
        addEntityViewModel.addState.observe(this, Observer {
            if (it is UiState.Success) {
                submitEntityButton.hideProgress("Done.")
                toast(getString(R.string.entity_added))
                setResult(Activity.RESULT_OK, Intent().putExtra("entity", it.data))
                finish()
            }
            if (it is UiState.Error) {
                if (it.throwable is UnAuthorizedException) {
                    expiredSession()
                } else {
                    submitEntityButton.hideProgress("Failed.")
                    toast(getString(R.string.failed_to_add_entitiy))
                }
            }
            if (it is UiState.Loading) {
                submitEntityButton.showProgress {
                    buttonText = "Adding new entity"
                    progressColor = Color.WHITE
                }

            }
        })
    }


    private fun setupCategorySpinner() {
        viewModel.events.observe(this, Observer {
            if (it is UiState.Loading || it is UiState.Empty) {
                snackbar?.dismiss()
            } else if (it is UiState.Success) {
                snackbar?.dismiss()
                val list = it.data
                entityCategorySpinner.setAdapter(CategoryEntityAdapter(this, list))
                entityCategorySpinner.setOnItemClickListener { parent, view, position, id ->
                    val category = list[position]
                    entityCategorySpinner.setText(category.name, false)
                    selectedCategory = category
                }
            } else {
                snackbar =
                    Snackbar.make(
                        addEntityLayout,
                        getString(R.string.failed_to_load_categories),
                        Snackbar.LENGTH_INDEFINITE
                    )
                        .setAction(R.string.retry) {
                            viewModel.loadCategories()
                        }
                snackbar?.show()
            }
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri: Uri = result.uri
                if (isAvatarClicked) {
                    selectedAvatarImage = resultUri
                    Log.d("MEGA", "selected avatar= $resultUri")
                    picasso.load(resultUri.toFile())
                        .transform(CropCircleTransformation())
                        .into(avatarImage)

                } else {
                    selectedCoverImage = resultUri
                    Log.d("MEGA", "selected cover= $resultUri")

                    picasso.load(resultUri.toFile()).into(coverImage)
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                toast(getString(R.string.failed_to_pick_image))

            }
        }
        if (requestCode == 12 && resultCode == Activity.RESULT_OK) {
            val latLng = data?.getParcelableExtra<LatLng>("picked_location")
            if (latLng != null) {
                this.selectedLocation = latLng
                placeSelectionPlaceHolder.gone()
                googleMap.clear()
                googleMap.zoomToMyLocation(latLng.latitude, latLng.longitude, false)
                googleMap.addMarker(MarkerOptions().position(latLng))
            }
        }
    }


}


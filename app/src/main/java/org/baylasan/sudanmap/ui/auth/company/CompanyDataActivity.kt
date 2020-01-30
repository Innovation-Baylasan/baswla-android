package org.baylasan.sudanmap.ui.auth.company

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toFile
import androidx.lifecycle.Observer
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
import org.baylasan.sudanmap.common.disableAll
import org.baylasan.sudanmap.common.gone
import org.baylasan.sudanmap.common.toLatLng
import org.baylasan.sudanmap.common.zoomToMyLocation
import org.baylasan.sudanmap.data.user.UserApi
import org.baylasan.sudanmap.domain.LocationViewModel
import org.baylasan.sudanmap.ui.LocationPickerActivity
import org.baylasan.sudanmap.ui.layers.DataEvent
import org.baylasan.sudanmap.ui.layers.EmptyEvent
import org.baylasan.sudanmap.ui.layers.LoadingEvent
import org.baylasan.sudanmap.ui.layers.MapLayersViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class CompanyDataActivity : AppCompatActivity() {
    var imageSelected = -1

    private val picasso by inject<Picasso>()
    private val viewModel by viewModel<MapLayersViewModel>()
    private var snackbar: Snackbar? = null
    private val locationViewModel by viewModel<LocationViewModel>()
    private lateinit var googleMap: GoogleMap
    private val userApi by inject<UserApi>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_data)
        button.setOnClickListener {

        }
        viewModel.loadCategories()
        viewModel.events.observe(this, Observer {
            if (it is LoadingEvent || it is EmptyEvent) {
                snackbar?.dismiss()
            } else if (it is DataEvent) {
                snackbar?.dismiss()
                categorySpinner.setAdapter(
                    ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_list_item_1,
                        it.categories.map { it.name })
                )
            } else {
                snackbar =
                    Snackbar.make(
                        layout,
                        getString(R.string.failed_to_load_categories),
                        Snackbar.LENGTH_INDEFINITE
                    )
                        .setAction(R.string.retry) {
                            viewModel.loadCategories()
                        }
                snackbar?.show()
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
            val uri = CropImage.getActivityResult(data).uri.toFile()
            if (imageSelected == 2) {
                picasso.load(uri).into(coverImage)
            } else {
                avatar.setImageResource(0)
                picasso.load(uri)
                    .transform(CropCircleTransformation())
                    .into(avatar)

            }

        }
        if (requestCode == 12 && resultCode == Activity.RESULT_OK) {
            val latLng = data?.getParcelableExtra<LatLng>("picked_location")
            if (latLng != null) {
                placeSelectionPlaceHolder.gone()
                googleMap.clear()
                googleMap.zoomToMyLocation(latLng.latitude, latLng.longitude, false)
                googleMap.addMarker(MarkerOptions().position(latLng))
            }
        }
    }

}



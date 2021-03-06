package org.baylasan.sudanmap.ui.addentity

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
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
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.activity_add_entity.*
import kotlinx.android.synthetic.main.content_add_entity.*
import kotlinx.android.synthetic.main.content_add_entity.view.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.common.*
import org.baylasan.sudanmap.data.common.AddEntityResponseException
import org.baylasan.sudanmap.data.common.UnAuthorizedException
import org.baylasan.sudanmap.data.entity.model.AddEntityRequest
import org.baylasan.sudanmap.data.entity.model.Category
import org.baylasan.sudanmap.data.entity.model.stringify
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
        setSupportActionBar(toolbar)
        bindProgressButton(submitEntityButton)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.yellowAccent)
        }
        appBar.addOnOffsetChangedListener(onStateChanged)
        viewModel.loadCategories()
        tagField.addTextChangedListener(
            afterTextChanged = {
                if (it != null) {
                    val entry = it.toString()
                    if (entry.length > 2) {
                        val lastChar = entry[it.length - 1]
                        if (lastChar == ',' || lastChar == '،') {
                            val txt = entry.replace(",", "").replace("،", "")
                            if (txt.isNotEmpty()) {
                                tagsGroup.addChipToGroup(txt)
                                tagField.clear()
                            }
                        }
                    }
                }
            }
        )
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
                    tags = tagsGroup.groupValues() ?: setOf(),
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
            if (it is UiState.Complete) {
                submitEntityButton.hideProgress(getString(R.string.done))
                showSuccessDialog()
            }
            if (it is UiState.Error) {
                submitEntityButton.hideProgress(getString(R.string.failed))
                val throwable = it.throwable
                when (throwable) {
                    is UnAuthorizedException -> {
                        expiredSession()
                    }
                    is AddEntityResponseException -> {
                        toast(throwable.addEntityResponseError.fields.stringify())
                    }
                    else -> {
                        toast(getString(R.string.failed_to_add_entitiy))
                    }
                }
            }
            if (it is UiState.Loading) {
                submitEntityButton.showProgress {
                    buttonText = getString(R.string.adding_new_entity)
                    progressColor = Color.WHITE
                }

            }
        })
    }

    private fun showSuccessDialog() {
        MaterialAlertDialogBuilder(this,R.style.Theme_MaterialComponents_Light_Dialog_Alert)
            .setTitle(R.string.entity_added)
            .setMessage(getString(R.string.entity_added_message))
            .setPositiveButton(android.R.string.ok){ dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
                sendOkResultAndFinishActivity()
            }.setOnDismissListener {
                sendOkResultAndFinishActivity()
            }
            .create()
            .show()
    }

    private fun sendOkResultAndFinishActivity() {
        setResult(Activity.RESULT_OK)
        finish()
    }


    private fun setupCategorySpinner() {
        viewModel.events.observe(this, Observer {
            if (it is UiState.Loading || it is UiState.Empty) {
                snackbar?.dismiss()
            } else if (it is UiState.Success) {
                snackbar?.dismiss()
                val list = it.data
                entityCategorySpinner.setAdapter(CategoryEntityAdapter(this, list))
                entityCategorySpinner.setOnItemClickListener { _, _, position, _ ->
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
                    picasso.load(resultUri.toFile())
                        .transform(CropCircleTransformation())
                        .into(avatarImage)

                } else {
                    selectedCoverImage = resultUri

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

    private fun ChipGroup.addChipToGroup(txt: String) {
        val chip = Chip(this@AddEntityActivity)
        chip.text = txt
        chip.isCloseIconVisible = true
        chip.setChipIconTintResource(android.R.color.white)

        chip.isClickable = false
        chip.isCheckable = false
        addView(chip as View)
        chip.setOnCloseIconClickListener { removeView(chip as View) }
    }

    private fun ChipGroup.groupValues(): Set<String>? {
        return tagsGroup.children.map { it as Chip }.map { it.text.toString() }.toSet()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)

    }
}


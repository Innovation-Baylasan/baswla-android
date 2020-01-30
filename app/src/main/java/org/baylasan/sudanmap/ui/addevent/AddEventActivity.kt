package org.baylasan.sudanmap.ui.addevent

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.lifecycle.Observer
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
import kotlinx.android.synthetic.main.activity_add_event.*
import kotlinx.android.synthetic.main.content_add_event.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.common.*
import org.baylasan.sudanmap.data.common.UnAuthorizedException
import org.baylasan.sudanmap.data.entity.model.Entity
import org.baylasan.sudanmap.data.event.model.AddEventRequest
import org.baylasan.sudanmap.domain.LocationViewModel
import org.baylasan.sudanmap.ui.LocationPickerActivity
import org.baylasan.sudanmap.ui.myentities.MyEntitiesViewModel
import org.baylasan.sudanmap.ui.view.AppBarChangedListener
import org.baylasan.sudanmap.ui.view.ProgressFragmentDialog
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*


class AddEventActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {
    private var applicationStartDateTime: Calendar? = null
    private var applicationEndDateTime: Calendar? = null
    private val picasso by inject<Picasso>()
    private var startDateClicked = -1
    private lateinit var googleMap: GoogleMap
    private var selectedLocation: LatLng? = null
    private var selectedCoverImage: Uri? = null
    private var entity: Entity? = null
    private val locationViewModel by viewModel<LocationViewModel>()
    private val entitiesViewModel by viewModel<MyEntitiesViewModel>()
    private val calendar = Calendar.getInstance()
    private var snackBar: Snackbar? = null
    private val progressFragmentDialog = ProgressFragmentDialog.newInstance().apply {
        isCancelable = false
    }

    private val addViewModel by viewModel<AddEventViewModel>()
    private val onStateChanged = object : AppBarChangedListener() {
        override fun onStateChanged(appBarLayout: AppBarLayout, state: State) {
            if (state == State.COLLAPSED) {
                toolbar.setBackgroundColor(
                    ContextCompat.getColor(
                        this@AddEventActivity,
                        R.color.yellowAccent
                    )
                )
            } else {
                toolbar.setBackgroundColor(
                    ContextCompat.getColor(
                        this@AddEventActivity,
                        android.R.color.transparent
                    )
                )
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)
        setSupportActionBar(toolbar)
        appBar.addOnOffsetChangedListener(onStateChanged)
        applicationStartButton.setOnClickListener {
            startDateClicked = 1
            showDatePicker()
        }
        applicationEndButton.setOnClickListener {
            startDateClicked = 2
            showDatePicker()
        }
        entitiesViewModel.loadMyEntities()
        entitiesViewModel.entitiesState.observe(this, Observer {
            if (it is UiState.Loading) {
                snackBar?.dismiss()
            }
            if (it is UiState.Success) {
                snackBar?.dismiss()

                val list = it.data
                val entityArrayAdapter = EntityArrayAdapter(this, list)
                eventAssignmentSpinner.setAdapter(entityArrayAdapter)
                eventAssignmentSpinner.setOnItemClickListener { parent, view, position, id ->
                    val selectedEntity = list[position]
                    eventAssignmentSpinner.setText(selectedEntity.name, false)
                    entity = selectedEntity

                }
            }
            if (it is UiState.Error) {
                snackBar = Snackbar.make(
                    addEventLayout,
                    R.string.failed_to_load_details,
                    Snackbar.LENGTH_INDEFINITE
                ).setAction(R.string.retry) {
                    entitiesViewModel.loadMyEntities()
                }
                snackBar?.show()

            }
        })

        pickCoverImageButton.setOnClickListener {
            CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setFixAspectRatio(true)
                .setAspectRatio(16, 9)
                .start(this)
        }
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

        submitEventButton.setOnClickListener {
            val eventName = eventNameTextField.text.toString()
            val eventDescription = eventDescriptionTextField.text.toString()
            val eventAddress = eventAddressTextField.text.toString()
            val seats = eventSeatsTextField.text.toString()
            val price = eventPriceTextField.text.toString()
            val registerLink = eventRegisterLinkTextField.text.toString()

            if (eventName.isEmpty()) {
                eventNameTextField.error = getString(R.string.event_name_required)
                return@setOnClickListener
            }
            if (eventDescription.isEmpty()) {
                eventDescriptionTextField.error = getString(R.string.event_description_required)
                return@setOnClickListener
            }
            if (eventAddress.isEmpty()) {
                eventAddressTextField.error = getString(R.string.event_address_required)
                return@setOnClickListener
            }

            if (seats.isEmpty()) {
                eventSeatsTextField.error = getString(R.string.number_of_seats_is_required)
                return@setOnClickListener
            }
            if (price.isEmpty()) {
                eventPriceTextField.error = getString(R.string.price_required)
                return@setOnClickListener
            }
            if (registerLink.isEmpty()) {
                eventRegisterLinkTextField.error = getString(R.string.register_link_required)
                return@setOnClickListener
            }
            if (applicationStartDateTime == null) {
                applicationStartButton.error = ""
                return@setOnClickListener
            }
            if (applicationEndDateTime == null) {
                applicationEndButton.error = ""
                return@setOnClickListener
            }
            if (entity == null) {
                eventAssignmentSpinner.error = ""
                return@setOnClickListener
            }
            if (selectedLocation == null) {
                toast(getString(R.string.event_location_required))
                return@setOnClickListener
            }

            addViewModel.submitEvent(
                AddEventRequest(
                    name = eventName,
                    description = eventDescription,
                    cover = selectedCoverImage?.toFile(),
                    registrationLink = registerLink,
                    startDateTime = applicationStartDateTime!!.timeInMillis,
                    endDateTime = applicationEndDateTime!!.timeInMillis,
                    entityId = entity!!.id,
                    locationLat = selectedLocation!!.latitude,
                    locationLng = selectedLocation!!.longitude

                )
            )

            addViewModel.addState.observe(this, Observer {
                if (it is UiState.Loading) {
                    progressFragmentDialog
                        .show(supportFragmentManager, "")
                }
                if (it is UiState.Complete) {
                    progressFragmentDialog.dismiss()
                    toast(getString(R.string.event_add_success))
                    finish()
                }
                if (it is UiState.Error) {
                    progressFragmentDialog.dismiss()
                    if (it.throwable is UnAuthorizedException) {
                        expiredSession()
                    } else {
                        toast(getString(R.string.failed_to_add_event))
                    }
                }
            })
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri: Uri = result.uri
                selectedCoverImage = resultUri
                picasso.load(resultUri.toFile()).into(eventCoverImageView)
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

    private fun showDatePicker() {

        DatePickerDialog(
            this,
            R.style.ThemeOverlay_MaterialComponents_Dialog_Alert,
            this,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    override fun onStop() {
        super.onStop()
        appBar.removeOnOffsetChangedListener(onStateChanged)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        if (startDateClicked == 1) {
            applicationStartDateTime = Calendar.getInstance()
            applicationStartDateTime?.set(Calendar.YEAR, year)
            applicationStartDateTime?.set(Calendar.MONTH, month)
            applicationStartDateTime?.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            applicationStartButton.error = null

            Log.d("MEGA", "application start date $applicationStartDateTime")
        } else {
            applicationEndDateTime = Calendar.getInstance()
            applicationEndDateTime?.set(Calendar.YEAR, year)
            applicationEndDateTime?.set(Calendar.MONTH, month)
            applicationEndDateTime?.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            applicationEndButton.error = null
            Log.d("MEGA", "application end date $applicationEndDateTime")

        }

        TimePickerDialog(
            this,
            R.style.ThemeOverlay_MaterialComponents_Dialog_Alert,

            this,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        ).show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        if (startDateClicked == 1) {
            applicationStartDateTime?.set(Calendar.HOUR_OF_DAY, hourOfDay)
            applicationStartDateTime?.set(Calendar.MINUTE, minute)
            val selectedDate = formatSelectedDate(applicationStartDateTime)
            applicationStartButton.text = selectedDate
            Log.d("MEGA", "application start time $applicationStartDateTime")

        } else {

            applicationEndDateTime?.set(Calendar.HOUR_OF_DAY, hourOfDay)
            applicationEndDateTime?.set(Calendar.MINUTE, minute)
            val selectedDate = formatSelectedDate(applicationEndDateTime)
            applicationEndButton.text = selectedDate
            Log.d("MEGA", "application start time $applicationEndDateTime")
        }

    }

    private fun formatSelectedDate(calendar: Calendar?): String {

        return SimpleDateFormat("yyyy-MM-dd HH:MM", Locale.getDefault())
            .format(calendar?.time)
    }
}


package org.baylasan.sudanmap.ui.addevent

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_add_event.*
import kotlinx.android.synthetic.main.content_add_event.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.ui.view.AppBarChangedListener
import java.util.*

class AddEventActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {
    private var applicationStartDateTime = Calendar.getInstance()
    private var applicationEndDateTime = Calendar.getInstance()
    /**
     *  clickedButton will be one if he clicked on start date and 2 if clicked on end date
     */
    private var clickedButton = -1
    private val calendar = Calendar.getInstance()
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)
        appBar.addOnOffsetChangedListener(onStateChanged)
        applicationStartButton.setOnClickListener {
            showDatePicker()
        }
        applicationEndButton.setOnClickListener {
            showDatePicker()
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

    }
}

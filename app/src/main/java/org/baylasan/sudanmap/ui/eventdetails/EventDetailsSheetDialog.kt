package org.baylasan.sudanmap.ui.eventdetails

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_event_details.*
import kotlinx.android.synthetic.main.fragment_event_details.view.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.data.event.model.Event
import org.baylasan.sudanmap.ui.main.entity.load

class EventDetailsSheetDialog : BottomSheetDialogFragment() {

    companion object {
        fun newInstance(event: Event): EventDetailsSheetDialog {
            val dialog = EventDetailsSheetDialog()
            val bundle = Bundle()
            bundle.putParcelable("event", event)
            dialog.arguments = bundle
            return dialog
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val window = dialog?.window
        window?.setDimAmount(0f)
        return LayoutInflater.from(activity)
            .inflate(R.layout.fragment_event_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val event = arguments?.getParcelable<Event>("event") as Event

        view.eventImage.load(event.eventPicture)
        view.eventName.text = event.eventName
        view.eventByUser.text = event.creator?.name
        view.eventDescription.text = event.description

        viewMoreButton.setOnClickListener {
            val intent = Intent(activity, EventDetailsActivity::class.java)
            intent.putExtra("event",event)
            startActivity(intent)
        }

    }
}
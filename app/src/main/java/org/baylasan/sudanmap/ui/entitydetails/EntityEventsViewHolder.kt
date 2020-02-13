package org.baylasan.sudanmap.ui.entitydetails

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_related_event.view.*

class EntityEventsViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
     val eventName: TextView = view.eventName
     val eventImage: ImageView = view.eventImage
     val eventPrice: AppCompatTextView = view.eventPrice
     val eventSeats: AppCompatTextView = view.eventSeats
}
package org.baylasan.sudanmap.ui.eventsearch

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_event.view.*

class EventViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val eventName: TextView =view.eventName
    val eventDescription: TextView =view.eventDescription
    val eventImage: ImageView =view.eventImage
    /*val eventPrice: TextView =view.eventPrice
    val eventSeats: TextView =view.eventSeats*/
}
package org.baylasan.sudanmap.ui.eventsearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.data.event.model.Event
import org.baylasan.sudanmap.ui.main.entity.load

class EventAdapter(
    private val list: List<Event>,
    private val onClick: (Event) -> Unit
) : RecyclerView.Adapter<EventViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.row_event,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = list[position]
        holder.eventName.text = event.name
        holder.eventDescription.text = event.description
        holder.eventImage.load(event.picture ?: "")
        holder.view.setOnClickListener {
            onClick.invoke(event)
        }

    }
/*
    fun removeItemAt(data: Int) {
        list.removeAt(data)
        notifyItemRemoved(data)
    }

    fun addItem(event: Event?) {
        if (event != null) {
            list.add(event)
            notifyItemInserted(list.size - 1)
        }
    }

    fun addAll(data: List<Event>) {
        list.addAll(data)
        notifyDataSetChanged()
    }*/

}


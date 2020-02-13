package org.baylasan.sudanmap.ui.entitydetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.data.event.model.Event
import org.baylasan.sudanmap.ui.main.entity.load

class EntityEventsAdapter(
    private val list: List<Event>,
    private val onClick: (Event) -> Unit
) : RecyclerView.Adapter<EntityEventsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntityEventsViewHolder {
        return EntityEventsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.row_related_event,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: EntityEventsViewHolder, position: Int) {
        val event = list[position]
        holder.view.setOnClickListener {
            onClick(event)
        }
        holder.eventImage.load(event.picture ?: "")
        holder.eventName.text = event.name

    }
}
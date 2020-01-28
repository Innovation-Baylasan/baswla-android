package org.baylasan.sudanmap.ui.main.entity

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_entity_filter.view.*

class EntityFilterViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val chipFilter: TextView = view.filterChip
}
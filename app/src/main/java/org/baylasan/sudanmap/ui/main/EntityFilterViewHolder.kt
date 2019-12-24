package org.baylasan.sudanmap.ui.main

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.row_entity_filter.view.*

class EntityFilterViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val chipFilter: Chip = view.filterChip
}
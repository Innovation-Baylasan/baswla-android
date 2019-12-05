package org.baylasan.sudanmap.ui.layers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.baylasan.sudanmap.R

class MapLayersAdapter : RecyclerView.Adapter<MapLayersViewHolder>() {
    val list =
        mutableListOf(
            Selectable(item = "item1"),
            Selectable(item = "item2"),
            Selectable(item = "item3"),
            Selectable(item = "item4"),
            Selectable(item = "item5"),
            Selectable(item = "item6"),
            Selectable(item = "item7")
        )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapLayersViewHolder {
        return MapLayersViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.row_map_layer,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MapLayersViewHolder, position: Int) {
        holder.apply {
            val selectable = list[adapterPosition]
            layerName.text = selectable.item
            itemView.setOnClickListener {
                selectable.toggle()
                notifyItemChanged(adapterPosition)
            }
            layerSelected.setColorFilter(

                ContextCompat.getColor(
                    layerSelected.context,
                    if (selectable.isSelected) R.color.colorAccent else android.R.color.darker_gray
                )
            )
            layerCard.strokeColor=  ContextCompat.getColor(
                layerSelected.context,
                if (selectable.isSelected) R.color.colorAccent else android.R.color.transparent
            )
        }
    }
}
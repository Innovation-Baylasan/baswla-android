package org.baylasan.sudanmap.ui.layers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.data.entity.model.Category
import org.baylasan.sudanmap.ui.main.loadCircle

class MapLayersAdapter : RecyclerView.Adapter<MapLayersViewHolder>() {
    val list = mutableListOf<Selectable<Category>>()

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
            val category = selectable.item
            layerName.text = category.name
            itemView.setOnClickListener {
                selectable.toggle()
                notifyItemChanged(adapterPosition)
            }
            layerImage.loadCircle("http://104.248.145.132/${category.icon}")
            layerSelected.setColorFilter(

                ContextCompat.getColor(
                    layerSelected.context,
                    if (selectable.isSelected) R.color.colorAccent else android.R.color.darker_gray
                )
            )
            layerCard.strokeColor = ContextCompat.getColor(
                layerSelected.context,
                if (selectable.isSelected) R.color.colorAccent else android.R.color.transparent
            )
        }
    }
}
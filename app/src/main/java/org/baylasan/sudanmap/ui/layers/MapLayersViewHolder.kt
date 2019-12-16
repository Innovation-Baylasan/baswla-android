package org.baylasan.sudanmap.ui.layers

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.row_map_layer.view.*

class MapLayersViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val layerImage: ImageView = view.layerImage
    val layerCard: MaterialCardView = view.layerCard
    val layerName: TextView = view.layerName
    val layerSelected: AppCompatImageView = view.layerSelected
}
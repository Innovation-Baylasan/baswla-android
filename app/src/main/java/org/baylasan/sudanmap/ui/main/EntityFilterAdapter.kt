package org.baylasan.sudanmap.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.domain.entity.model.Category
import org.xmlpull.v1.XmlPullParser

class EntityFilterAdapter(
    private val list: List<Category>,
    private val onClick: (Category) -> Unit
) :
    RecyclerView.Adapter<EntityFilterViewHolder>() {
    private var selectedCategory: Category? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntityFilterViewHolder =
        EntityFilterViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.row_entity_filter,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: EntityFilterViewHolder, position: Int) {
        val category = list[holder.adapterPosition]
        holder.chipFilter.text = category.name
        holder.chipFilter.isSelected = category == selectedCategory

        holder.chipFilter.setOnClickListener {
            selectedCategory = category
            onClick(category)


        }

    }

}
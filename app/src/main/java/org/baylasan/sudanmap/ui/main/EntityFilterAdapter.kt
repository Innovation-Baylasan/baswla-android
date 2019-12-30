package org.baylasan.sudanmap.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.data.entity.model.Category

class EntityFilterAdapter(
    private val context: Context,
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
        holder.chipFilter.background = ContextCompat.getDrawable(
            context, if (isSelected(category))
                R.drawable.selected_chip_background else
                R.drawable.not_selected_chip_background
        )
        holder.chipFilter.setTextColor(
            ContextCompat.getColor(
                context,
                if (isSelected(category)) R.color.white else R.color.black
            )
        )

        holder.chipFilter.setOnClickListener {
            selectedCategory = category
            onClick(category)
            notifyItemChanged(position)
            notifyDataSetChanged()

        }

    }

    private fun isSelected(category: Category) =
        category == selectedCategory

}
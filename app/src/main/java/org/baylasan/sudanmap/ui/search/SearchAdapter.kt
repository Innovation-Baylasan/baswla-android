package org.baylasan.sudanmap.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_search_result.view.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.domain.entity.model.Entity
import org.baylasan.sudanmap.ui.main.load

class SearchAdapter(private val list: List<Entity>,private val onClick:(Entity)->Unit) : RecyclerView.Adapter<SearchViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.row_search_result,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val entity = list[position]
        holder.entityAvatar.load(entity.avatar)
        holder.entityName.text = entity.name
        holder.itemView.setOnClickListener {
            onClick(entity)
        }
    }
}

class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val entityName: TextView = view.entityName
    val entityAvatar: ImageView = view.entityAvatar
}
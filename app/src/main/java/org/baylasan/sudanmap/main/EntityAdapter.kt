package org.baylasan.sudanmap.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_entitiy_list.view.*
import org.baylasan.sudanmap.R

class EntityListAdapter(val onClick: () -> Unit) : RecyclerView.Adapter<EntityListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntityListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.row_entitiy_list,
            parent,
            false
        )
        return EntityListViewHolder(view)
    }

    override fun getItemCount(): Int = 10

    override fun onBindViewHolder(holder: EntityListViewHolder, position: Int) {
        holder.entitiesRecyclerView.adapter = EntityAdapter()
        holder.entitiesRecyclerView.layoutManager =
            LinearLayoutManager(
                holder.entitiesRecyclerView.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        holder.showMoreButton.setOnClickListener {
            onClick.invoke()
        }
    }
}

class EntityListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val entitiesRecyclerView: RecyclerView = view.entitiesRecyclerView
    val showMoreButton = view.showMoreButton
}

class EntityAdapter : RecyclerView.Adapter<EntityViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntityViewHolder {
        return EntityViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.row_entity,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = 5

    override fun onBindViewHolder(holder: EntityViewHolder, position: Int) {

    }

}

class EntityViewHolder(view: View) : RecyclerView.ViewHolder(view)

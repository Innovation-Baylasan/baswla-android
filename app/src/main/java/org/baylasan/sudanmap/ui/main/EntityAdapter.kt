package org.baylasan.sudanmap.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.row_entitiy_list.view.*
import kotlinx.android.synthetic.main.row_entity.view.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.domain.entity.model.Entity

class EntityListAdapter(private val list: List<Entity>, val onClick: () -> Unit) :
    RecyclerView.Adapter<EntityListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntityListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.row_entity,
            parent,
            false
        )
        return EntityListViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: EntityListViewHolder, position: Int) {
        val currentEntity = list[position]
        holder.entitiesRecyclerView.adapter =
            EntityAdapter()
        holder.entitiesRecyclerView.layoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        )

        with(holder.itemView) {
            entityName.text = currentEntity.name
            entityDescription.text = currentEntity.description
            avatarImage.load(currentEntity.avatar)
            coverImage.load(currentEntity.cover)

        }


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
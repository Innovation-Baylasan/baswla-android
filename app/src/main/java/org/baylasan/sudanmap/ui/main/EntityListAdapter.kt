package org.baylasan.sudanmap.ui.main

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_entity.view.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.domain.entity.model.Entity

class EntitiesListAdapter(
    private val list: List<Entity>,
    private val onItemClick: OnItemClick
) :
    RecyclerView.Adapter<EntitiesListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.row_entity,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], onItemClick)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(
            entityDto: Entity,
            onItemClick: OnItemClick
        ) {
            Log.d("KLD", entityDto.toString())
            itemView.entityName.text = entityDto.name
            itemView.entityDescription.text = entityDto.description
/*            itemView.coverImage.load(entityDto.cover)
            itemView.avatarImage.load(entityDto.avatar)*/
            //TODO:
            itemView.setOnClickListener {
                onItemClick.onItemClick(entityDto)
            }
        }
    }

    interface OnItemClick {
        fun onItemClick(entityDto: Entity)
    }
}

fun ImageView.load(imageUrl: String) = Picasso.get().load(imageUrl).into(this)
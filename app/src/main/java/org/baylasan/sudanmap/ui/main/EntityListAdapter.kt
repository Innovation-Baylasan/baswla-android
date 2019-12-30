package org.baylasan.sudanmap.ui.main

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.row_entity.view.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.data.entity.model.EntityDto

class EntitiesListAdapter(
    private val list: List<EntityDto>,
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
            entityDtoDto: EntityDto,
            onItemClick: OnItemClick
        ) {
            Log.d("KLD", entityDtoDto.toString())
            itemView.entityName.text = entityDtoDto.name
            itemView.entityDescription.text = entityDtoDto.description
            itemView.coverImage.load(entityDtoDto.cover)
            itemView.avatarImage.loadCircle(entityDtoDto.avatar)
            itemView.setOnClickListener {
                onItemClick.onItemClick(entityDtoDto)
            }
        }
    }

    interface OnItemClick {
        fun onItemClick(entityDtoDto: EntityDto)
    }
}

// fun ImageView.load(imageUrl: String) = Picasso.get().load(imageUrl ).into(this)

fun ImageView.load(imageUrl: String){
    if (imageUrl.isEmpty()) {
        setImageResource(R.drawable.ic_launcher_foreground);
    } else{
        Picasso.get().load(imageUrl).into(this)
    }
}

fun ImageView.loadCircle(imageUrl: String){
    if (imageUrl.isEmpty()) {
        setImageResource(R.drawable.ic_launcher_foreground);
    } else{
        Picasso.get()
            .load(imageUrl)
            .transform(CropCircleTransformation()).into(this)
    }
}
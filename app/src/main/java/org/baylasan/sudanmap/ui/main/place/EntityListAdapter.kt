package org.baylasan.sudanmap.ui.main.place

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
import org.baylasan.sudanmap.data.entity.model.Entity

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
        val entity = list[position]
        holder.bind(entity, onItemClick)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(
            entity: Entity,
            onItemClick: OnItemClick
        ) {
            Log.d("KLD", entity.toString())
            itemView.entityName.text = entity.name
            itemView.entityDescription.text = entity.description
            itemView.coverImage.load(entity.cover)
            itemView.avatarImage.loadCircle(entity.avatar)
            itemView.setOnClickListener {
                onItemClick.onItemClick(entity)
            }
        }
    }

    interface OnItemClick {
        fun onItemClick(entity: Entity)
    }
}

// fun ImageView.load(imageUrl: String) = Picasso.get().load(imageUrl ).into(this)

fun ImageView.load(imageUrl: String){
    if (imageUrl.isEmpty()) {
        setImageResource(R.drawable.circle)
    } else{
        Picasso.get().load(imageUrl)
            .error(R.drawable.ic_icon)
            .placeholder(R.drawable.ic_icon)

            .into(this)
    }
}

fun ImageView.loadCircle(imageUrl: String){
    if (imageUrl.isEmpty()) {
        setImageResource(R.drawable.circle);
    } else{
        Picasso.get()
            .load(imageUrl)
            .error(R.drawable.ic_icon)
            .placeholder(R.drawable.ic_icon)
            .transform(CropCircleTransformation()).into(this)
    }
}
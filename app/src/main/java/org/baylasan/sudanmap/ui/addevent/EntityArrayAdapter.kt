package org.baylasan.sudanmap.ui.addevent

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.data.entity.model.Entity
import org.baylasan.sudanmap.ui.main.entity.load

class EntityArrayAdapter(
    context: Context,
    private val list: List<Entity>
) : ArrayAdapter<Entity>(context, 0, list) {
    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var listItem = convertView
        if (listItem == null) listItem =
            LayoutInflater.from(context).inflate(R.layout.entity_list_item, parent, false)
        val entity = list[position]
        val image = listItem?.findViewById<ImageView>(R.id.entityImage)
        val name = listItem?.findViewById<TextView>(R.id.entityName)
        val release = listItem?.findViewById<TextView>(R.id.entityCategory)
        name?.text = entity.name
        release?.text = entity.name
        image?.load(entity.category.iconPng)
        return listItem!!
    }

}
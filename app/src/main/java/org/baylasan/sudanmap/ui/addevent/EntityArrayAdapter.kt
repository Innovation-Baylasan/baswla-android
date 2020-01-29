package org.baylasan.sudanmap.ui.addevent

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.entity_list_item.view.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.data.entity.model.Entity
import org.baylasan.sudanmap.ui.main.entity.load

class EntityArrayAdapter(context: Context, private val list: List<Entity>) :
    ArrayAdapter<Entity>(context, 0, list) {
    override fun getCount(): Int {
        return list.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(R.layout.row_entitiy_list, parent, false)
        val entity = list[position]
        view.entityName.text = entity.name
        view.entityCategory.text = entity.category.name
        view.entityImage.load(entity.avatar)
        return convertView!!
    }


}
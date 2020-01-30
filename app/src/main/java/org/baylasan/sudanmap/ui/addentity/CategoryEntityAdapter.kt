package org.baylasan.sudanmap.ui.addentity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import org.baylasan.sudanmap.data.entity.model.Category

class CategoryEntityAdapter(context: Context, private val list: List<Category>) :
    ArrayAdapter<Category>(context, 0, list) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val category = list[position]
        var view = convertView
        if (view == null)
            view = LayoutInflater.from(context)
                .inflate(android.R.layout.simple_list_item_1, parent, false)

        val textView = view?.findViewById<TextView>(android.R.id.text1)
        textView?.text = category.name
        return view!!

    }

    override fun getCount(): Int {
        return list.size
    }
}
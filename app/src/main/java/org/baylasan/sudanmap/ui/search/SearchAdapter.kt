package org.baylasan.sudanmap.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.baylasan.sudanmap.R

class SearchAdapter : RecyclerView.Adapter<SearchViewHolder>() {
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
        return 20
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
    }
}

class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view)
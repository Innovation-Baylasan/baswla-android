package org.baylasan.sudanmap.ui.intro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.pager_item.view.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.domain.ViewPagerModel

class ViewPagerAdapter(private val viewList: ArrayList<ViewPagerModel>) :
    RecyclerView.Adapter<ViewPagerAdapter.ViewPagerVH>() {

    class ViewPagerVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(model: ViewPagerModel) {
            with(itemView) {
                lottieView.setAnimation(model.jsonFile)
                lottieView.speed = 1.3f

                headerText.text = model.header
                descriptionText.text = model.description
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerVH =
        ViewPagerVH(
            LayoutInflater.from(parent.context).inflate(
                R.layout.pager_item, parent, false
            )
        )

    override fun getItemCount() = viewList.size

    override fun onBindViewHolder(holder: ViewPagerVH, position: Int) {
        holder.bind(viewList[position])
    }
}



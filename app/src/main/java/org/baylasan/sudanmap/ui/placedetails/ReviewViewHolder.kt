package org.baylasan.sudanmap.ui.placedetails

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_review.view.*

class ReviewViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val reviewDate: TextView = view.reviewDate
    val reviewerName: TextView = view.reviewer
    val reviewerImage: ImageView = view.reviewerImage
    val reviewContent: TextView = view.reviewContent
}
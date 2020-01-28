package org.baylasan.sudanmap.ui.entitydetails

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.common.parseDate
import org.baylasan.sudanmap.data.entity.model.Review
import java.util.*

class ReviewAdapter(private val reviews: MutableList<Review> = mutableListOf()) :
    RecyclerView.Adapter<ReviewViewHolder>() {
    fun add(review: Review) {
        reviews.add(review)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        return ReviewViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.row_review,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = reviews.size


    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]
        holder.reviewContent.text = review.review
        val time = parseDate(review.createdAt)!!.time
        holder.reviewDate.text = DateUtils.getRelativeTimeSpanString(time, Date().time, DateUtils.MINUTE_IN_MILLIS)
    }

    fun addAll(reviews: List<Review>) {
        this.reviews.addAll(reviews)
        notifyDataSetChanged()
    }
}
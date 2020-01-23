package org.baylasan.sudanmap.ui.faq

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_faq.*
import kotlinx.android.synthetic.main.row_faq.view.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.common.gone
import org.baylasan.sudanmap.common.show

class FAQActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faq)
        backButton.setOnClickListener {
            finish()
        }
        faqRecyclerView.adapter = FAQAdapter()
        faqRecyclerView.layoutManager = LinearLayoutManager(this)
    }
}

class FAQViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val faqDescription = view.faqDescription
    val faqLayout = view.faqLayout
    val faqExpandIcon = view.faqExpandIcon
}

class FAQAdapter : RecyclerView.Adapter<FAQViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FAQViewHolder {
        return FAQViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.row_faq,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = 10

    override fun onBindViewHolder(holder: FAQViewHolder, position: Int) {
        holder.faqLayout.setOnClickListener {
            if (holder.faqDescription.isGone) {
                holder.faqDescription.show()
                holder.faqExpandIcon.rotation=180f
            } else {
                holder.faqDescription.gone()
                holder.faqExpandIcon.rotation=0f

            }

        }
    }

}

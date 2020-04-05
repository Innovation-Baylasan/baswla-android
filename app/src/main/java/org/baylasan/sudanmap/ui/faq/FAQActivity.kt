package org.baylasan.sudanmap.ui.faq

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.activity_faq.*
import kotlinx.android.synthetic.main.activity_faq.backButton
import kotlinx.android.synthetic.main.activity_faq.errorView
import kotlinx.android.synthetic.main.activity_faq.progressBar
import kotlinx.android.synthetic.main.activity_faq.retryButton
import kotlinx.android.synthetic.main.activity_privacy_policy.*
import kotlinx.android.synthetic.main.row_faq.view.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.common.UiState
import org.baylasan.sudanmap.common.gone
import org.baylasan.sudanmap.common.visible
import org.baylasan.sudanmap.domain.faq.model.Faq
import org.baylasan.sudanmap.domain.faq.model.Faqs
import org.koin.androidx.viewmodel.ext.android.viewModel

class FAQActivity : AppCompatActivity() {
    private val viewModel by viewModel<FaqViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faq)
        viewModel.loadFaqs()
        backButton.setOnClickListener {
            finish()
        }

        val faqAdapter = FAQAdapter()
        faqRecyclerView.adapter = faqAdapter
        faqRecyclerView.layoutManager = LinearLayoutManager(this)
        viewModel.uiState.observe(this, Observer {
            if (it is UiState.Loading) {
                progressBar.visible()
                errorView.gone()
                faqRecyclerView.gone()
            }
            if (it is UiState.Success) {
                progressBar.gone()
                errorView.gone()
                faqRecyclerView.visible()
                faqAdapter.addData(it.data)
            }
            if (it is UiState.Error) {
                progressBar.gone()
                faqRecyclerView.gone()
                errorView.visible()

                retryButton.setOnClickListener {
                    viewModel.loadFaqs()
                }
            }
        })


    }
}

class FAQViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val faqDescription: TextView = view.faqDescription
    val faqLayout: MaterialCardView = view.faqLayout
    val faqTitle: TextView = view.faqTitle
    val faqExpandIcon: ImageView = view.faqExpandIcon
}

class FAQAdapter : RecyclerView.Adapter<FAQViewHolder>() {
    private val list = mutableListOf<Faq>()

    fun addData(faqs: List<Faq>) {
        list.addAll(faqs)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FAQViewHolder {
        return FAQViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.row_faq,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: FAQViewHolder, position: Int) {
        val faq = list[position]
        holder.faqTitle.text = faq.title
        holder.faqDescription.text = faq.answer
        holder.faqDescription.movementMethod = LinkMovementMethod.getInstance()
        Linkify.addLinks(holder.faqDescription, Linkify.ALL)
        holder.faqLayout.setOnClickListener {
            if (holder.faqDescription.isGone) {
                holder.faqDescription.visible()
                holder.faqExpandIcon.animate().rotation(180f).start()
            } else {
                holder.faqDescription.gone()
                holder.faqExpandIcon.animate().rotation(0f).start()
            }

        }
    }

}

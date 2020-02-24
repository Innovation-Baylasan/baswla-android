package org.baylasan.sudanmap.ui.entitydetails

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.fragment_entity_details.*
import kotlinx.android.synthetic.main.fragment_entity_details.view.*
import kotlinx.android.synthetic.main.row_search_result.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.data.entity.model.Entity
import org.baylasan.sudanmap.data.entity.model.Tag
import org.baylasan.sudanmap.ui.main.entity.load
import org.baylasan.sudanmap.ui.main.entity.loadCircle


class EntityDetailsSheetDialog : BottomSheetDialogFragment() {
    private lateinit var activity: Activity

    companion object {
        @JvmStatic
        fun newInstance(entity: Entity): EntityDetailsSheetDialog {
            val dialog = EntityDetailsSheetDialog()
            val bundle = Bundle()
            bundle.putParcelable("entity", entity)
            dialog.arguments = bundle
            return dialog
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as Activity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val window: Window? = dialog!!.window
        window?.setDimAmount(0f)
        return LayoutInflater.from(activity)
            .inflate(R.layout.fragment_entity_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val entity = arguments?.getParcelable<Entity>("entity")
        if (entity != null) {
            addTagsToChipGroup(entity.tags)
            view.profileCoverImage.load(entity.cover)
            view.profileImage.loadCircle(entity.avatar)
            view.companyNameTxt.text = entity.name
            view.companyDescription.text = entity.description
            view.viewMoreButton.setOnClickListener {
                val profileIntent = Intent(activity, EntityDetailsActivity::class.java)
                profileIntent.putExtra("entity", entity)
                startActivity(profileIntent)
                dismiss()
            }
        } else {
            dismissAllowingStateLoss()
        }
    }

    private fun addTagsToChipGroup(tags: List<Tag>) {
        tags.forEach {
            val chip = Chip(activity)
            chip.text = it.label
            tagsGroup.addView(chip)
        }
    }

}
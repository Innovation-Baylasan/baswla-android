package org.baylasan.sudanmap.ui.placedetails

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_place_details.view.*

import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.data.entity.model.Entity
import org.baylasan.sudanmap.ui.main.place.load
import org.baylasan.sudanmap.ui.main.place.loadCircle


class PlaceDetailsSheetDialog : BottomSheetDialogFragment() {
    private lateinit var activity: Activity

    companion object {
        @JvmStatic
        fun newInstance(entity: Entity): PlaceDetailsSheetDialog {
            val dialog = PlaceDetailsSheetDialog()
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
            .inflate(R.layout.fragment_place_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val entity = arguments?.getParcelable<Entity>("entity")
        if (entity != null) {
            view.profileCoverImage.load(entity.cover)
            view.profileImage.loadCircle(entity.avatar)
            view.companyNameTxt.text = entity.name
            view.companyDescription.text = entity.description
            view.viewMoreButton.setOnClickListener {
                val profileIntent = Intent(activity, PlaceDetailsActivity::class.java)
                profileIntent.putExtra("entity", entity)
                startActivity(profileIntent)
                dismiss()
            }
        } else {
            dismissAllowingStateLoss()
        }
    }

}
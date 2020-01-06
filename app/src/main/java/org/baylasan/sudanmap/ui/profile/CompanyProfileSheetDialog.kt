package org.baylasan.sudanmap.ui.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.sheet_dialog_fragment_company_profile.view.*

import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.data.entity.model.Entity
import org.baylasan.sudanmap.ui.main.load
import org.baylasan.sudanmap.ui.main.loadCircle


class CompanyProfileSheetDialog : BottomSheetDialogFragment() {
    private lateinit var activity: Activity

    companion object {
        @JvmStatic
        fun newInstance(entity: Entity): CompanyProfileSheetDialog {
            val dialog = CompanyProfileSheetDialog()
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
            .inflate(R.layout.sheet_dialog_fragment_company_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val entity = arguments?.getParcelable<Entity>("entity")
        if (entity != null) {
            view.profileCoverImage.load(entity.cover)
            view.profileImage.loadCircle(entity.avatar)
            view.companyNameTxt.text = entity.name
            view.companyDescrition.text = entity.description
            view.viewMoreButton.setOnClickListener {
                val profileIntent = Intent(activity, CompanyProfileActivity::class.java)
                profileIntent.putExtra("entity", entity)
                startActivity(profileIntent)
                dismiss()
            }
        } else {
            dismissAllowingStateLoss()
        }
    }

}
package org.baylasan.sudanmap.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import org.baylasan.sudanmap.R

class ProgressFragmentDialog : DialogFragment() {
    companion object {
        fun newInstance(): ProgressFragmentDialog {
            return ProgressFragmentDialog()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return LayoutInflater.from(context)
            .inflate(R.layout.fragment_progress_dialog, container, false)
    }


}
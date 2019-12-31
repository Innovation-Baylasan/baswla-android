package org.baylasan.sudanmap.ui.searchfilter


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_search_filter.*
import org.baylasan.sudanmap.R


/**
 * A simple [Fragment] subclass.
 */
class SearchFilterFragment : Fragment(R.layout.fragment_search_filter) {
    private lateinit var activity: AppCompatActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as AppCompatActivity
    }

    companion object {
        @JvmStatic
        fun newInstance(): SearchFilterFragment {
            return SearchFilterFragment()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*    searchFilterToolbar.setNavigationOnClickListener {
                activity?.supportFragmentManager?.popBackStack()
            }

    */
        backButton.setOnClickListener {
            activity.supportFragmentManager.popBackStack()
        }
    }

}

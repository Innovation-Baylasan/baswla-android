package org.baylasan.sudanmap.ui.searchfilter


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_search_filter.*
import org.baylasan.sudanmap.R


/**
 * A simple [Fragment] subclass.
 */
class SearchFilterFragment : Fragment(R.layout.fragment_search_filter) {
    companion object {
        @JvmStatic
        fun newInstance(): SearchFilterFragment {
            return SearchFilterFragment()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchFilterToolbar.setNavigationOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

    }

}

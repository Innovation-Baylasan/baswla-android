package org.baylasan.sudanmap.ui.eventsearch


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_event_search.*
import org.baylasan.sudanmap.R


/**
 * A simple [Fragment] subclass.
 */
class EventSearchFragment : Fragment(R.layout.fragment_event_search) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchRecyclerView.adapter=EventAdapter(listOf())
        searchBackBtn.setOnClickListener {
            activity
                ?.supportFragmentManager
                ?.beginTransaction()
                ?.remove(this)
                ?.commit()
        }
    }

}

package org.baylasan.sudanmap.ui.eventsearch


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_event_search.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.common.UiState
import org.baylasan.sudanmap.common.gone
import org.baylasan.sudanmap.common.setEndDrawableOnTouchListener
import org.baylasan.sudanmap.common.visible
import org.baylasan.sudanmap.ui.eventdetails.EventDetailsActivity
import org.baylasan.sudanmap.ui.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit


class EventSearchFragment : Fragment(R.layout.fragment_event_search) {
    private val viewModel by viewModel<EventSearchViewModel>()
    private lateinit var activity: MainActivity
    private lateinit var disposable: Disposable

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        queryField.setEndDrawableOnTouchListener {
            queryField.setText("")
        }
        searchBackBtn.setOnClickListener {
            activity.supportFragmentManager.popBackStack()
        }

        disposable = queryField.textChanges()
            .skip(1)
            .filter { it.isNotBlank() || it.isNotEmpty() }
            .map(CharSequence::toString)
            .debounce(500, TimeUnit.MILLISECONDS)
            .throttleLast(200, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                viewModel.findEvents(it)
            }, {
                it.printStackTrace()
            })
        observeSearchState()
    }

    private fun observeSearchState() {
        viewModel.events.observe(viewLifecycleOwner, Observer { event ->
            when (event) {
                is UiState.Success -> {
                    searchRecyclerView.visible()
                    progressBar.gone()
                    emptyView.gone()
                    errorView.gone()
                    searchRecyclerView.adapter =
                        EventAdapter(list = event.data, onClick = {
                            val intent = Intent(activity, EventDetailsActivity::class.java)
                            intent.putExtra("event", it)
                            startActivity(intent)
                        })
                }
                is UiState.Loading -> {
                    progressBar.visible()
                    searchRecyclerView.gone()
                    emptyView.gone()
                    errorView.gone()

                }
                is UiState.Empty -> {
                    emptyView.visible()
                    progressBar.gone()
                    searchRecyclerView.gone()
                    errorView.gone()
                }
                is UiState.Error -> {
                    errorView.visible()
                    emptyView.gone()
                    searchRecyclerView.gone()
                    progressBar.gone()
                }
            }

        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onStop() {
        super.onStop()
        disposable.dispose()
    }


}

package org.baylasan.sudanmap.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_entity_list_dialog.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.data.WebAccess
import org.baylasan.sudanmap.domain.entity.model.EntityDto

// TODO: Customize parameter argument names
const val ARG_ITEM_COUNT = "item_count"

/**
 *
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * You can show this modal bottom sheet from your activity like this:
 * <pre>
 *    EntityDialogFragment.newInstance(30).show(supportFragmentManager, "dialog")
 * </pre>
 *
 * You activity (or fragment) needs to implement [EntityDialogFragment.Listener].
 */
class EntityDialogFragment : BottomSheetDialogFragment() {
    private val entities = ArrayList<EntityDto>()
    lateinit var adapter: EntityListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_entity_list_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        adapter = EntityListAdapter(entities) {
            /*     supportFragmentManager.beginTransaction()
                     .replace(
                         R.id.fragmentLayout,
                         SearchFragment.newInstance(),
                         "search"
                     )
                     .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                     .addToBackStack(null)
                     .commit()*/
        }
        list.layoutManager = GridLayoutManager(context, 2)
        list.adapter = adapter
        loadEntities()
    }



    fun loadEntities() {
        WebAccess.apiService.getEntities()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ this::handleResult }, { this::handleError })
    }

    fun handleResult(result: List<EntityDto>) {
        entities.addAll(result)
        adapter.notifyDataSetChanged()
    }

    fun handleError(error: Throwable) {

    }


    companion object {

        // TODO: Customize parameters
        fun newInstance(): EntityDialogFragment =
            EntityDialogFragment().apply {
            }

    }
}

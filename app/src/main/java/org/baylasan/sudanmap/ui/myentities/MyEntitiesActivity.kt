package org.baylasan.sudanmap.ui.myentities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.activity_my_entities.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.common.UiState
import org.baylasan.sudanmap.common.expiredSession
import org.baylasan.sudanmap.common.gone
import org.baylasan.sudanmap.common.show
import org.baylasan.sudanmap.data.common.UnAuthorizedException
import org.baylasan.sudanmap.data.entity.model.Entity
import org.baylasan.sudanmap.ui.addentity.AddEntityActivity
import org.baylasan.sudanmap.ui.main.entity.EntitiesListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyEntitiesActivity : AppCompatActivity(), EntitiesListAdapter.OnItemClick {
    private val viewModel by viewModel<MyEntitiesViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_entities)
        setSupportActionBar(toolbar)
        viewModel.loadMyEntities()
        refreshLayout.setOnRefreshListener {
            viewModel.loadMyEntities()
        }
        viewModel.entitiesState.observe(this, Observer {
            if (it is UiState.Loading) {
                emptyEntityLayout.gone()
                entitiesRecyclerView.gone()
                refreshLayout.isRefreshing = true
            }
            if (it is UiState.Success) {
                refreshLayout.isRefreshing = false
                if (it.data.isEmpty()) {
                    emptyEntityLayout.show()
                } else {
                    entitiesRecyclerView.show()

                    emptyEntityLayout.gone()
                    Log.d("MEGA", "DATA ${it.data}")
                    entitiesRecyclerView.adapter = EntitiesListAdapter(it.data, this)
                    entitiesRecyclerView.layoutManager =
                        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                }
            }
            if (it is UiState.Error) {
                if (it.throwable is UnAuthorizedException) {
                    expiredSession()
                } else {
                    refreshLayout.isRefreshing = false
                    emptyEntityLayout.gone()
                }
            }

        })

        addPlaceButton.setOnClickListener {
            startActivity(Intent(this, AddEntityActivity::class.java))
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)

    }

    override fun onItemClick(entity: Entity) {

    }
}

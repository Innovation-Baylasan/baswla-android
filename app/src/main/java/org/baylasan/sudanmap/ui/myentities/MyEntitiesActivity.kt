package org.baylasan.sudanmap.ui.myentities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.activity_my_entities.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.common.UiState
import org.baylasan.sudanmap.common.expiredSession
import org.baylasan.sudanmap.common.gone
import org.baylasan.sudanmap.common.visible
import org.baylasan.sudanmap.data.common.UnAuthorizedException
import org.baylasan.sudanmap.data.entity.model.Entity
import org.baylasan.sudanmap.ui.addentity.AddEntityActivity
import org.baylasan.sudanmap.ui.entitydetails.EntityDetailsActivity
import org.baylasan.sudanmap.ui.main.entity.EntitiesListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyEntitiesActivity : AppCompatActivity(), EntitiesListAdapter.OnItemClick {
    private val viewModel by viewModel<MyEntitiesViewModel>()
    private lateinit var entitiesListAdapter: EntitiesListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_entities)
        entitiesListAdapter = EntitiesListAdapter(mutableListOf(), this)
        entitiesRecyclerView.adapter =
            entitiesListAdapter
        entitiesRecyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
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
                    emptyEntityLayout.visible()
                } else {
                    entitiesRecyclerView.visible()
                    emptyEntityLayout.gone()
                    entitiesListAdapter.addAll(it.data)
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
            startActivityForResult(Intent(this, AddEntityActivity::class.java), 123)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)

    }

    override fun onItemClick(entity: Entity) {
        val intent = Intent(this, EntityDetailsActivity::class.java)
        intent.putExtra("entity", entity)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123 && resultCode == Activity.RESULT_OK) {
            if (data != null)
                entitiesListAdapter.addItem(
                    data.getParcelableExtra(
                        "entity"
                    )
                )
        }
    }
}

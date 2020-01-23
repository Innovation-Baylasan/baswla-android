package org.baylasan.sudanmap.ui.placedetails


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_place_details.*
import kotlinx.android.synthetic.main.content_place_details.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.common.*
import org.baylasan.sudanmap.data.entity.model.Entity
import org.baylasan.sudanmap.ui.main.UserProfileViewModel
import org.baylasan.sudanmap.ui.main.place.load
import org.baylasan.sudanmap.ui.main.place.loadCircle
import org.baylasan.sudanmap.ui.view.AppBarChangedListener
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlaceDetailsActivity : AppCompatActivity() {
    private val viewModel by viewModel<PlaceDetailsViewModel>()
    private var snackBar: Snackbar? = null
    lateinit var adapter: ReviewAdapter
    private val profileViewModel by viewModel<UserProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_details)
        val entity = intent?.getParcelableExtra<Entity>("entity") as Entity

        adapter = ReviewAdapter(mutableListOf())

        profileBackBtn.setOnClickListener {
            super.onBackPressed()
        }
        viewModel.followState.observe(this, Observer {
            if (it is UiState.Loading) {
                toggleFollowButton.disable()
            }
            if (it is UiState.Error) {

                toggleFollowButton.enable()
                toast("Failed to follow, try again")

            }
            if (it is UiState.Complete) {
                toggleFollowButton.enable()

                companyProfileFollowersNumTxt.text =
                    (companyProfileFollowersNumTxt.text.toString().toInt() + 1).toString()
            }
        })
        toggleFollowButton.setOnClickListener {
            //TODO handle if to follow or un-follow
            viewModel.follow(entity.id)
        }
        checkIfUserIsGuest()

        viewModel.entityDetailsState.observe(this, Observer {
            if (it is UiState.Success) {
                snackBar?.dismiss()
                loadingLayout.gone()
                val details = it.data
                profileToolBarTitleTxt.text = details.name
                companyNameTxt.text = details.name
                companyDescriptionTextView.text = details.description
                profileCoverImage.load(details.cover)
                profileImage.loadCircle(details.avatar)
                companyProfileFollowersNumTxt.text = details.followersCount.toString()
                reviewsRecyclerView.adapter = adapter
                adapter.addAll(details.reviews)
                reviewsRecyclerView.layoutManager = LinearLayoutManager(this)
            }
            if (it is UiState.Error) {
                loadingLayout.stopShimmerAnimation()
                snackBar = Snackbar.make(
                    coordinatorLayout,
                    getString(R.string.failed_to_load_details),
                    Snackbar.LENGTH_INDEFINITE
                ).setAction(R.string.retry) {
                    viewModel.getDetailsForId(entity.id)
                }
                snackBar?.show()
            }
            if (it is UiState.Loading) {
                loadingLayout.show()
                loadingLayout.startShimmerAnimation()
                snackBar?.dismiss()
            }
        })
        entity.let {
            viewModel.getDetailsForId(entity.id)
            profileToolBarTitleTxt.text = it.name
            companyNameTxt.text = it.name
            companyDescriptionTextView.text = it.description
            profileCoverImage.load(it.cover)
            profileImage.loadCircle(it.avatar)

        }
        viewModel.reviewState.observe(this, Observer {
            if (it is UiState.Success) {
                reviewField.clear()
                reviewField.enable()
                submitCommentButton.enable()
                adapter.add(it.data)

            }
            if (it is UiState.Loading) {
                reviewField.disable()
                submitCommentButton.disable()
            }
            if (it is UiState.Error) {
                reviewField.enable()
                submitCommentButton.enable()
                toast("Failed to add comment, try again.")

            }
        })
        submitCommentButton.setOnClickListener {
            val review = reviewField.asString()
            if (review.isEmpty()) {
                toast(getString(R.string.write_review_first))
                return@setOnClickListener
            }
            viewModel.review(review, entity.id)
            hideKeyboard()

        }

        appbar.addOnOffsetChangedListener(object : AppBarChangedListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout, state: State) {
                when (state) {
                    State.EXPANDED -> {
                        profileImage.show()
                        profileToolBarTitleTxt.text = ""
                        commentLayout.gone()
                    }
                    State.COLLAPSED -> {
                        profileImage.gone()
                        commentLayout.show()

                        profileToolBarTitleTxt.text = companyNameTxt.text
                    }
                }
            }

        })
    }

    private fun checkIfUserIsGuest() {
        profileViewModel.loadUser()
        profileViewModel.listenToUserProfile().observe(this, Observer {
            if (it == null) {
                toggleFollowButton.disable()
                commentLayout.disableChildern()
            } else {
                toggleFollowButton.enable()

                commentLayout.enableChildern()
            }
        })
    }


}


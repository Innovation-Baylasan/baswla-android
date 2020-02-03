package org.baylasan.sudanmap.ui.entitydetails


import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_entity_details.*
import kotlinx.android.synthetic.main.content_entity_details.*
import kotlinx.android.synthetic.main.rate_entity_dialog_layout.view.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.common.*
import org.baylasan.sudanmap.data.common.UnAuthorizedException
import org.baylasan.sudanmap.data.entity.model.Entity
import org.baylasan.sudanmap.ui.main.UserProfileViewModel
import org.baylasan.sudanmap.ui.main.entity.load
import org.baylasan.sudanmap.ui.main.entity.loadCircle
import org.baylasan.sudanmap.ui.view.AppBarChangedListener
import org.koin.androidx.viewmodel.ext.android.viewModel

/*TODO:(
*  handle when user enters one of his entity/event.
*  We should limit some of the interactions between him and his entity/event
*  like rate/follow and respectively show other interactions that normal user can not do.)
* */
class EntityDetailsActivity : AppCompatActivity() {
    private val viewModel by viewModel<EntityDetailsViewModel>()
    private var snackBar: Snackbar? = null
    lateinit var adapter: ReviewAdapter
    private val profileViewModel by viewModel<UserProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entity_details)
        val entity = intent?.getParcelableExtra("entity") as Entity

        adapter = ReviewAdapter(mutableListOf())
        rateNowButtton.setOnClickListener {
            val view =
                LayoutInflater.from(this).inflate(R.layout.rate_entity_dialog_layout, null, true)
            val ratingBar = view.ratingBar
            val submitRateButton = view.submitRateButton
            val cancelRateButton = view.cancelRateButton
            val alertDialog = AlertDialog.Builder(this)
                .setView(view)
                .create()
            alertDialog
                .show()
            submitRateButton.setOnClickListener {
                val rating = ratingBar.rating
                if (rating != 0f) {
                    viewModel.rate(entity.id, rating)
                    alertDialog.dismiss()
                }
            }
            cancelRateButton.setOnClickListener {
                alertDialog.dismiss()
            }
        }
        viewModel.rateState.observe(this, Observer {
            if (it is UiState.Loading) {
                rateNowButtton.disable()
            }
            if (it is UiState.Complete) {
                rateNowButtton.enable()
            }
            if (it is UiState.Error) {
                if (it.throwable is UnAuthorizedException) {
                    expiredSession()
                } else {
                    rateNowButtton.enable()
                    toast(getString(R.string.failed_to_rate_entity))
                }
            }
        })
        profileBackBtn.setOnClickListener {
            super.onBackPressed()
        }
        viewModel.followState.observe(this, Observer {
            if (it is UiState.Loading) {
                toggleFollowButton.disable()
            }
            if (it is UiState.Error) {
                if (it.throwable is UnAuthorizedException) {
                    expiredSession()
                } else {
                    toggleFollowButton.enable()
                    toast(getString(R.string.failed_to_follow))
                }
            }
            if (it is UiState.Complete) {
                toggleFollowButton.setImageResource(R.drawable.ic_bell)
                toggleFollowButton.enable()
                toast(getString(R.string.followed_successfully))
                companyProfileFollowersNumTxt.text =
                    (companyProfileFollowersNumTxt.text.toString().toInt() + 1).toString()
            }
        })
        viewModel.unFollowState.observe(this, Observer {
            if (it is UiState.Loading) {

                toggleFollowButton.disable()
            }
            if (it is UiState.Error) {
                if (it.throwable is UnAuthorizedException) {
                    expiredSession()
                } else {
                    toggleFollowButton.enable()
                    toast(getString(R.string.failed_to_follow))
                }
            }
            if (it is UiState.Complete) {
                toggleFollowButton.setImageResource(R.drawable.ic_bell_vib)

                toggleFollowButton.enable()
//                toast(getString(R.string.unfollowed_successfully))
                companyProfileFollowersNumTxt.text =
                    (companyProfileFollowersNumTxt.text.toString().toInt() - 1).toString()
            }
        })
        toggleFollowButton.setOnClickListener {
            viewModel.toggleFollow(entity.id)
        }
        checkIfUserIsGuest()
        reviewsRecyclerView.adapter = adapter
        reviewsRecyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.entityDetailsState.observe(this, Observer {
            if (it is UiState.Success) {
                snackBar?.dismiss()
                val details = it.data
                if (!profileViewModel.isThisMine(details.userId)) {
                    rateNowButtton.visible()
                    toggleFollowButton.visible()
                } else {
                    toggleFollowButton.gone()
                    rateNowButtton.gone()
                }
                toggleFollowButton.setImageResource(if (details.isFollowed) R.drawable.ic_bell else R.drawable.ic_bell_vib)
                companyNameTxt.text = details.name
                ratingBar2.rating = details.rating.toFloat()
                companyDescriptionTextView.text = details.description
                profileCoverImage.load(details.cover)
                profileImage.loadCircle(details.avatar)
                companyProfileReviewsNumTxt.text=details.reviewsCount.toString()

                companyProfileFollowersNumTxt.text = details.followersCount.toString()
                if (details.reviews.isNotEmpty()) {
                    loadingLayout.gone()
                    adapter.addAll(details.reviews)
                } else {
                    loadingLayout.gone()
                }


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
                loadingLayout.visible()
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
                loadingLayout.gone()

            }
            if (it is UiState.Loading) {
                reviewField.disable()
                submitCommentButton.disable()
            }
            if (it is UiState.Error) {
                if (it.throwable is UnAuthorizedException) {
                    expiredSession()
                }
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


    }

    private fun checkIfUserIsGuest() {
        profileViewModel.loadUser()
        profileViewModel.listenToUserProfile().observe(this, Observer {
            if (it == null) {
                commentLayout.gone()
                appbar.addOnOffsetChangedListener(object : AppBarChangedListener() {
                    override fun onStateChanged(appBarLayout: AppBarLayout, state: State) {
                        when (state) {
                            State.EXPANDED -> {
                                profileImage.visible()
                                profileToolBarTitleTxt.text = ""
                            }
                            State.COLLAPSED -> {
                                profileImage.gone()
                                profileToolBarTitleTxt.text = companyNameTxt.text
                            }
                        }
                    }

                })
            } else {
                appbar.addOnOffsetChangedListener(object : AppBarChangedListener() {
                    override fun onStateChanged(appBarLayout: AppBarLayout, state: State) {
                        when (state) {
                            State.EXPANDED -> {
                                profileImage.visible()
                                profileToolBarTitleTxt.text = ""
                                commentLayout.gone()
                            }
                            State.COLLAPSED -> {
                                profileImage.gone()
                                commentLayout.visible()
                                profileToolBarTitleTxt.text = companyNameTxt.text
                            }
                            State.IDLE -> {


                            }
                        }
                    }

                })
            }
        })
    }


}


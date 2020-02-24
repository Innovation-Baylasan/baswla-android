package org.baylasan.sudanmap.ui.entitydetails


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.util.Linkify
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_entity_details.*
import kotlinx.android.synthetic.main.content_entity_details.*
import kotlinx.android.synthetic.main.rate_entity_dialog_layout.view.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.common.*
import org.baylasan.sudanmap.data.common.UnAuthorizedException
import org.baylasan.sudanmap.data.entity.model.Entity
import org.baylasan.sudanmap.data.entity.model.Tag
import org.baylasan.sudanmap.ui.eventdetails.EventDetailsActivity
import org.baylasan.sudanmap.ui.main.UserProfileViewModel
import org.baylasan.sudanmap.ui.main.entity.load
import org.baylasan.sudanmap.ui.main.entity.loadCircle
import org.baylasan.sudanmap.ui.view.AppBarChangedListener
import org.koin.androidx.viewmodel.ext.android.viewModel


class EntityDetailsActivity : AppCompatActivity() {
    private val viewModel by viewModel<EntityDetailsViewModel>()
    private var snackBar: Snackbar? = null
    lateinit var adapter: ReviewAdapter
    private val profileViewModel by viewModel<UserProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entity_details)
        val entity = Gson().fromJson<Entity>(
            "{\n" +
                    "\"id\": 1,\n" +
                    "\"name\": \"Innovation Baylasan\",\n" +
                    "\"description\": \"شركة بيلسان الابتكار. مهتمون بتصميم و تجهيز و تفعيل البيئات الإبداعية. نساعدكم في تحويل مساحات العمل إلى بيئات إبداعية\",\n" +
                    "\"category\": {\n" +
                    "\"id\": 7,\n" +
                    "\"name\": \"R&D Centers\",\n" +
                    "\"icon\": \"/storage/icons/6A9WUoH9A7JCjMiQfxh2wrxyngonMxJepSaDWo6u.png\",\n" +
                    "\"icon_png\": \"/storage/markers/oBL7FbFkCa7563o5knfUQHMzXxEI1nphrmenCCbp.png\",\n" +
                    "\"created_at\": \"2020-02-15T14:03:28.000000Z\",\n" +
                    "\"updated_at\": \"2020-02-15T14:03:28.000000Z\"\n" +
                    "},\n" +
                    "\"avatar\": \"/storage/7/conversions/%2BzsKj1sDUHMUoGGSLoVAUwtoBaF2iZtzHVL1SkF%2BQOYMYkgrHGxOtcArssJXP8Xm9g754hELQ0AAAAASUVORK5CYII%3D-avatar.jpg\",\n" +
                    "\"cover\": \"/storage/8/conversions/AAAAAElFTkSuQmCC-cover.jpg\",\n" +
                    "\"tags\": [],\n" +
                    "\"rating\": \"0.00\",\n" +
                    "\"location\": {\n" +
                    "\"lat\": 15.60168778,\n" +
                    "\"long\": 32.50422591\n" +
                    "}\n" +
                    "}", Entity::class.java
        )
        viewModel.getEvents(entity.id)
        viewModel.getDetailsForId(entity.id)

        observeRateState()
        observeFollowState()
        observeUnFollowState()
        observeDetailsState(entity)
        observeReviewState()
        observeEventState(entity)

        toggleFollowButton.setOnClickListener {
            viewModel.toggleFollow(entity.id)
        }
        rateNowButtton.setOnClickListener {
            showRateDialog(entity)
        }
        profileBackBtn.setOnClickListener {
            super.onBackPressed()
        }
        appbar.addOnOffsetChangedListener(object : AppBarChangedListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout, state: State) {
                when (state) {
                    State.EXPANDED -> {
                        profileImage.showAlpha()
                        profileImageSmall.hideAlpha()
                        profileToolBarTitleTxt.hideAlpha()
                        companyNameTxt.showAlpha()
                    }
                    State.COLLAPSED -> {
                        profileImage.hideAlpha()
                        profileImageSmall.showAlpha()
                        profileToolBarTitleTxt.showAlpha()
                        companyNameTxt.hideAlpha()

                    }
                    State.IDLE -> {
                        //DOSE NOTHING.
                    }
                }
            }

        })

        adapter = ReviewAdapter(mutableListOf())
        reviewsRecyclerView.adapter = adapter
        reviewsRecyclerView.layoutManager = LinearLayoutManager(this)

        entity.let {
            profileToolBarTitleTxt.text = it.name
            companyNameTxt.text = it.name
            companyDescriptionTextView.text = it.description
            profileCoverImage.load(it.cover)
            profileImage.loadCircle(it.avatar)
            profileImageSmall.loadCircle(it.avatar)
            addTagsToChipGroup(it.tags)

        }
        submitCommentButton.setOnClickListener {
            submitReview(entity)

        }


    }

    private fun observeEventState(entity: Entity) {
        viewModel.eventState.observe(this, Observer {
            if (it is UiState.Loading) {
                eventErrorLayout.gone()
                eventRecyclerView.gone()
                emptyEventLayout.gone()
                eventLoadingLayout.visible()
            }
            if (it is UiState.Success) {
                if (it.data.isEmpty()) {
                    eventErrorLayout.gone()
                    emptyEventLayout.visible()
                    eventLoadingLayout.gone()
                    eventRecyclerView.gone()
                } else {
                    eventErrorLayout.gone()
                    emptyEventLayout.gone()
                    eventLoadingLayout.gone()
                    eventRecyclerView.visible()
                    val eventsAdapter = EntityEventsAdapter(it.data) { event ->
                        val intent = Intent(this, EventDetailsActivity::class.java)
                        intent.putExtra("event", event)
                        startActivity(intent)
                    }
                    eventRecyclerView.adapter = eventsAdapter
                    eventRecyclerView.layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                }
            }
            if (it is UiState.Error) {
                eventErrorLayout.visible()
                retryEventButton.setOnClickListener {
                    viewModel.getEvents(entity.id)
                }
                emptyEventLayout.gone()
                eventLoadingLayout.gone()
                eventRecyclerView.gone()
            }
        })
    }

    private fun submitReview(entity: Entity) {
        val review = reviewField.asString()
        if (review.isEmpty()) {
            toast(getString(R.string.write_review_first))
            return
        }
        viewModel.review(review, entity.id)
        hideKeyboard()
    }

    private fun showRateDialog(entity: Entity) {
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

    private fun observeReviewState() {
        viewModel.reviewState.observe(this, Observer {
            if (it is UiState.Success) {
                reviewField.clear()
                reviewField.enable()
                submitCommentButton.enable()
                loadingLayout.gone()
                emptyReviewsView.gone()
                adapter.add(it.data)

            }
            if (it is UiState.Loading) {
                reviewField.disable()
                submitCommentButton.disable()
            }
            if (it is UiState.Error) {
                if (it.throwable is UnAuthorizedException) {
                    expiredSession()
                } else {
                    reviewField.enable()
                    submitCommentButton.enable()
                    toast("Failed to add comment, try again.")
                }
            }
        })
    }

    private fun observeDetailsState(entity: Entity) {
        viewModel.entityDetailsState.observe(this, Observer {
            if (it is UiState.Success) {
                snackBar?.dismiss()
                val entityDetails = it.data
                if (profileViewModel.isThisMine(entityDetails.userId)) {

                    toggleFollowButton.gone()
                    rateNowButtton.gone()
                } else {
                    rateNowButtton.visible()
                    toggleFollowButton.visible()
                }
                toggleFollowButton.setImageResource(if (entityDetails.isFollowed) R.drawable.ic_bell else R.drawable.ic_bell_vib)
                companyNameTxt.text = entityDetails.name
                ratingBar2.rating = entityDetails.rating.toFloat()
                companyDescriptionTextView.text = entityDetails.description
                val details = entityDetails.details
                if (details != null && details.isNotEmpty()) {
                    Linkify.addLinks(companyDescriptionTextView, Linkify.ALL)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        companyDescriptionTextView.append("\n")
                        companyDescriptionTextView.append(
                            Html.fromHtml(
                                details,
                                Html.FROM_HTML_MODE_COMPACT
                            )
                        )
                    } else {
                        companyDescriptionTextView.append("\n")

                        companyDescriptionTextView.append(Html.fromHtml(details))

                    }
                }
                profileCoverImage.load(entityDetails.cover)
                profileImage.loadCircle(entityDetails.avatar)
                companyProfileReviewsNumTxt.text = entityDetails.reviewsCount.toString()
                addTagsToChipGroup(it.data.tags)
                commentLayout.visible()
                companyProfileFollowersNumTxt.text = entityDetails.followersCount.toString()
                if (entityDetails.reviews.isNotEmpty()) {
                    loadingLayout.gone()
                    adapter.addAll(entityDetails.reviews)
                    //                    moreCommentButton.visible()
                } else {
                    emptyReviewsView.visible()
                    loadingLayout.gone()
                    //                    moreCommentButton.gone()
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
    }

    private fun observeUnFollowState() {
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
    }

    private fun observeFollowState() {
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
    }

    private fun observeRateState() {
        viewModel.rateState.observe(this, Observer {
            if (it is UiState.Loading) {
                rateNowButtton.disable()
            }
            if (it is UiState.Success) {
                ratingBar2.rating = it.data.avgRate.toFloatOrNull() ?: 0f
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
    }

    private fun addTagsToChipGroup(tags: List<Tag>) {
        tagsGroup.removeAllViews()
        tags.forEach {
            val chip = Chip(this)
            chip.text = it.label
            tagsGroup.addView(chip)
        }
    }

}


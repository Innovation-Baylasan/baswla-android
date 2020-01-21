package org.baylasan.sudanmap.ui.placedetails


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.content_company_profile.*
import kotlinx.android.synthetic.main.activity_place_details.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.data.entity.model.Entity
import org.baylasan.sudanmap.ui.main.place.load
import org.baylasan.sudanmap.ui.main.place.loadCircle
import org.baylasan.sudanmap.ui.view.AppBarChangedListener
import org.baylasan.sudanmap.utils.gone
import org.baylasan.sudanmap.utils.show


class PlaceDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_details)

        profileBackBtn.setOnClickListener {
            super.onBackPressed()
        }

        val entity = intent?.getParcelableExtra<Entity>("entity")
        entity?.let {
            profileToolBarTitleTxt.text = it.name
            companyNameTxt.text = it.name
            companyDescrition.text = it.description
            profileCoverImage.load(it.cover)
            profileImage.loadCircle(it.avatar)

        }


        appbar.addOnOffsetChangedListener(object : AppBarChangedListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout, state: State) {
                if (state == State.EXPANDED) // toolbar.title = companyNameTxt.text
                {
                    profileImage.show()
                    profileToolBarTitleTxt.text = ""
                } else {
                    profileImage.gone()
                    profileToolBarTitleTxt.text = companyNameTxt.text
                }
            }

        })
    }


}


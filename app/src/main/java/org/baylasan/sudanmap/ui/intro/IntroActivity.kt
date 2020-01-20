package org.baylasan.sudanmap.ui.intro

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.activity_intro.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.domain.ViewPagerModel
import org.baylasan.sudanmap.domain.user.SessionManager
import org.baylasan.sudanmap.ui.auth.AuthActivity
import org.baylasan.sudanmap.ui.main.MainActivity
import org.koin.android.ext.android.inject

class IntroActivity : AppCompatActivity() {


    private var currentPage = 0
    private val sessionManager : SessionManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val introItems = arrayListOf(
            ViewPagerModel(
                jsonFile = R.raw.map_inter_actions, header = "Explore",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Nunc eget lorem dolor sed viverra ipsum."
            ), ViewPagerModel(
                jsonFile = R.raw.search, header = "Test",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Nunc eget lorem dolor sed viverra ipsum."
            ), ViewPagerModel(
                jsonFile = R.raw.third, header = "Explore",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Nunc eget lorem dolor sed viverra ipsum."
            )
        )
        val marginPageTransformer = MarginPageTransformer(50)

        val adapter = ViewPagerAdapter(introItems)
        introViewPager.adapter = adapter

        dotsIndicator.setViewPager(introViewPager)
        // introViewPager.adapter?.registerAdapterDataObserver(dotsIndicator.dataSetObserver)

        introViewPager.setPageTransformer(marginPageTransformer)

        /*    introViewPager.setPageTransformer { page, position ->
                page.apply {
                    translationY = Math.abs(position) * 500f
                    scaleX = 1f
                    scaleY = 1f
                }
            }*/

        nextBtn.setOnClickListener {
            if (currentPage == 2) {
                sessionManager.setIsFirstTime(false)
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            }
            currentPage += 1

            introViewPager.setCurrentItem(currentPage, true)


        }
        introViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                currentPage = position
                if (currentPage == introItems.lastIndex) {
                    //  introViewPager.setCurrentItem(currentPage, true)
                    //  currentPage =0
                    nextBtn.text = "Finish"
                } else {
                    nextBtn.text = "Next"
                }
            }

        })
    }
}

package org.baylasan.sudanmap.ui.intro

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.activity_intro.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.common.disable
import org.baylasan.sudanmap.common.enable
import org.baylasan.sudanmap.common.gone
import org.baylasan.sudanmap.common.visible
import org.baylasan.sudanmap.domain.ViewPagerModel
import org.baylasan.sudanmap.ui.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.abs

class IntroActivity : AppCompatActivity() {


    private var currentPage = 0
    private val introViewModel by viewModel<IntroViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        //TODO change intro data
        val introItems = arrayListOf(
            ViewPagerModel(
                jsonFile = R.raw.map_inter_actions,
                header = getString(R.string.page1_title),
                description = getString(R.string.page1_summary)
            ), ViewPagerModel(
                jsonFile = R.raw.search,
                header = getString(R.string.page2_title),
                description = getString(R.string.page2_summary)
            ), ViewPagerModel(
                jsonFile = R.raw.third,
                header = getString(R.string.page3_title),
                description = getString(R.string.page3_summary)
            ), ViewPagerModel(
                jsonFile = R.raw.four,
                header = getString(R.string.page4_title),
                description = getString(R.string.page4_summary)
            ), ViewPagerModel(
                jsonFile = R.raw.five,
                header = getString(R.string.page5_title),
                description = getString(R.string.page5_summary)
            )
        )
        val marginPageTransformer = MarginPageTransformer(100)

        val adapter = ViewPagerAdapter(introItems)
        introViewPager.adapter = adapter

        dotsIndicator.setViewPager(introViewPager)
        // introViewPager.adapter?.registerAdapterDataObserver(dotsIndicator.dataSetObserver)

        introViewPager.setPageTransformer(marginPageTransformer)

        introViewPager.setPageTransformer { page, position ->
            page.apply {
                translationY = abs(position) * 500f
                scaleX = 1f
                scaleY = 1f
            }
        }

        nextButton.setOnClickListener {
            if (currentPage == 4) {
                introViewModel.setFirstLaunchCompleted()
            }
            currentPage += 1

            introViewPager.setCurrentItem(currentPage, true)


        }
        introViewModel.event.observe(this, Observer {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        })
        skipButton.setOnClickListener {
            introViewModel.setFirstLaunchCompleted()
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
                    skipButton.gone()
                    nextButton.text = getString(R.string.finish)
                } else {
                    nextButton.text = getString(R.string.next)
                    skipButton.visible()
                }
            }
        })
    }
}

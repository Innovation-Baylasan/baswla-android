package org.baylasan.sudanmap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.activity_intro.*
import org.baylasan.sudanmap.adapter.ViewPagerAdapter
import org.baylasan.sudanmap.domain.ViewPagerModel

class IntroActivity : AppCompatActivity() {


    private var currentPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val introItems = arrayListOf<ViewPagerModel>(
            ViewPagerModel(
                jsonFile = R.raw.me_at_office, header = "Explore",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Nunc eget lorem dolor sed viverra ipsum."
            ), ViewPagerModel(
                jsonFile = R.raw.limit_of_transaction, header = "Test",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Nunc eget lorem dolor sed viverra ipsum."
            ), ViewPagerModel(
                jsonFile = R.raw.me_at_office, header = "Explore",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Nunc eget lorem dolor sed viverra ipsum."
            ), ViewPagerModel(
                jsonFile = R.raw.seo_search_ads, header = "Tesyt",
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
            if (nextBtn.text == "Skip") {
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
                    nextBtn.text = "Skip"
                }
            }

        })
    }
}

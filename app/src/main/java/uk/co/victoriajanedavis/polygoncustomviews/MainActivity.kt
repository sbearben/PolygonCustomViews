package uk.co.victoriajanedavis.polygoncustomviews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var pagerAdapter: MainActivityPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupPagerAdapter()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewPager.adapter = null
    }

    private fun setupPagerAdapter() {
        pagerAdapter = MainActivityPagerAdapter(this)
        viewPager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }
}

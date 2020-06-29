package uk.co.victoriajanedavis.polygoncustomviews

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.facebook.react.ReactInstanceManager
import com.facebook.react.ReactPackage
import com.facebook.react.common.LifecycleState
import com.facebook.react.shell.MainReactPackage
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var pagerAdapter: MainActivityPagerAdapter
    var instanceManager: ReactInstanceManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instanceManager = ReactInstanceManager.builder()
            .setApplication(application)
            .setCurrentActivity(this)
            .addPackages(ArrayList(
                listOf<ReactPackage>(
                    MainReactPackage()
                )
            ))
            .setUseDeveloperSupport(false)
            .setBundleAssetName("index.android.bundlejs")
            // .setJSMainModulePath("../../../../../../../src/index.js")
            .setInitialLifecycleState(LifecycleState.BEFORE_RESUME)
            .build()

        setContentView(R.layout.activity_main)
        setupPagerAdapter()
    }

    override fun onResume() {
        super.onResume()
        instanceManager?.onHostResume(this)
    }

    override fun onPause() {
        super.onPause()
        instanceManager?.onHostPause(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        instanceManager = null
        viewPager.adapter = null
    }

    private fun setupPagerAdapter() {
        pagerAdapter = MainActivityPagerAdapter(this)
        viewPager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }
}

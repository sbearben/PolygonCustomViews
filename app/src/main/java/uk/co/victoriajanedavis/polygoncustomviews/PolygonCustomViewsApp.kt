package uk.co.victoriajanedavis.polygoncustomviews

import android.app.Application
import com.facebook.react.PackageList
import com.facebook.react.ReactApplication
import com.facebook.react.ReactNativeHost
import com.facebook.react.ReactPackage
import com.facebook.soloader.SoLoader


class PolygonCustomViewsApp : Application(), ReactApplication {

    val mReactNativeHost: ReactNativeHost = object : ReactNativeHost(this) {
        override fun getUseDeveloperSupport(): Boolean {
            return false
        }
        override fun getPackages(): List<ReactPackage> {
            return PackageList(this).packages
        }

        override fun getBundleAssetName(): String? {
            return "index.android.bundlejs"
        }
    }


    override fun getReactNativeHost(): ReactNativeHost? {
        return mReactNativeHost
    }

    override fun onCreate() {
        super.onCreate()
        SoLoader.init(this, false)
        // ReactBridge.staticInit()
    }
}
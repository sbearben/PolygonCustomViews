package uk.co.victoriajanedavis.polygoncustomviews

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import uk.co.victoriajanedavis.polygoncustomviews.concentricPolygonDrawable.DrawableContainer
import uk.co.victoriajanedavis.polygoncustomviews.markerPathStamping.MarkerPathStampingView

class MainActivityPagerAdapter(
    private val context: Context
) : PagerAdapter() {

    private val layoutInflater = (context as Activity).layoutInflater

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = when(position) {
            0 -> layoutInflater.inflate(R.layout.view_polygon_picker, container, false)
            1 -> layoutInflater.inflate(R.layout.view_concentric_polygon, container, false)
            2 -> DrawableContainer(context)
            else -> MarkerPathStampingView(context)
        }

        container.addView(view, 0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView(view as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return NUMBER_TABS
    }

    override fun getPageTitle(position: Int): CharSequence? = when(position) {
        0 -> "Polygon Picker" //context.getString(R.string.friend_requests_pager_adapter_received)
        1 -> "Concentric Polygon View"
        2 -> "Concentric Polygon Drawable"
        else -> "Path Marker Drawable"
    }

    companion object {
        const val NUMBER_TABS = 4
    }
}
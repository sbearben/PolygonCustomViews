package uk.co.victoriajanedavis.polygoncustomviews.concentricPolygonDrawable

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.RelativeLayout

class DrawableContainer: RelativeLayout {
    constructor(context: Context): this(context, null)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int): super(context, attrs, defStyle)

    private val concenctricDrawable = ConcentricPolygonDrawable()

    init {
        // Instantiate an ImageView and define its properties
        val imageView = ImageView(context).apply {
            setImageDrawable(concenctricDrawable)
            adjustViewBounds = true
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        }
        addView(imageView, 0)

        startDrawableAnimation()
    }

    private fun startDrawableAnimation() {
        ObjectAnimator.ofFloat(concenctricDrawable, "dotProgress", 0f, 1f).apply {
            duration = 10000L
            interpolator = LinearInterpolator()
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART
        }.start()
    }
}
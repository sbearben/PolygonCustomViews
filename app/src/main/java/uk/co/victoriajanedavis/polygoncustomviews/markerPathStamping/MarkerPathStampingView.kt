package uk.co.victoriajanedavis.polygoncustomviews.markerPathStamping

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import uk.co.victoriajanedavis.polygoncustomviews.R
import uk.co.victoriajanedavis.polygoncustomviews.polygonPicker.PURPLE
import uk.co.victoriajanedavis.polygoncustomviews.polygonPicker.Polygon
import uk.co.victoriajanedavis.polygoncustomviews.polygonPicker.createPolygonPath
import kotlin.math.PI
import kotlin.math.min

internal class MarkerPathStampingView : View {
    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    private val marker: Drawable = resources.getDrawable(R.drawable.marker, null).apply {
        setBounds(-MARKER_SIZE/2, -MARKER_SIZE/2, MARKER_SIZE/2, MARKER_SIZE/2)
    }

    private var markerProgress = 0f
        set(value) {
            field = value.coerceIn(0f, 1f)
            invalidate()
        }

    private val pos = FloatArray(2)
    private val tan = FloatArray(2)

    private val polygon = Polygon(sides = 12, radius = 400f, color = PURPLE)

    private var diameter: Float = 0f
    private var centrePoint = Point(0,0)

    private var polygonPath = Path()
    private var pathMeasure = PathMeasure()

    private val polygonPaint by lazy(LazyThreadSafetyMode.NONE) { Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 16f
        color = PURPLE
        pathEffect = CornerPathEffect(0.05f*(diameter/2))
    }}

    init {
        ObjectAnimator.ofFloat(this, "markerProgress", 0f, 1f).apply {
            duration = 4000L
            interpolator = LinearInterpolator()
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART
        }.start()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minWidth: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = View.resolveSizeAndState(minWidth, widthMeasureSpec, 0)

        // Whatever the width ends up being, ask for a height that would let the polygon get as big as it can
        val minHeight: Int =  View.MeasureSpec.getSize(w) + (paddingBottom + paddingTop) - (paddingLeft + paddingRight)
        val h: Int = View.resolveSizeAndState(minHeight, heightMeasureSpec, 0)

        setMeasuredDimension(w, h)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        val xPad = paddingLeft + paddingRight
        val yPad = paddingTop + paddingBottom

        val width = w - xPad
        val height = h - yPad

        diameter = min(width, height).toFloat()
        centrePoint.x = w/2
        centrePoint.y = h/2

        recreatePolygonPath()
        updatePathMeasure()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPath(polygonPath, polygonPaint)

        pathMeasure.getPosTan(markerProgress * pathMeasure.length, pos, tan)

        canvas.translate(pos[0], pos[1])

        val angle = Math.atan2(tan[1].toDouble(), tan[0].toDouble())
        canvas.rotate(Math.toDegrees(angle).toFloat())

        marker.draw(canvas)
    }

    private fun recreatePolygonPath() {
        polygonPath = createPolygonPath(
            center = centrePoint,
            sides = polygon.sides,
            radius = min(polygon.radius, diameter/2),
            startingAngle = (PI/polygon.sides).toFloat()
        )
    }

    private fun updatePathMeasure() {
        pathMeasure.setPath(polygonPath, false)
    }

    companion object {
        private const val MARKER_SIZE = 100
    }
}
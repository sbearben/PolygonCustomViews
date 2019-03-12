package uk.co.victoriajanedavis.polygoncustomviews.polygonPicker

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.math.min

internal class PolygonView : View {
    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    internal var minSides = DEFAULT_MIN_SIDES
        set(value) {
            field = value.coerceIn(MIN_SIDES, maxSides-1)
            if(sides < field) sides = field
        }
    internal var maxSides = DEFAULT_MAX_SIDES
        set(value) {
            field = value.coerceIn(minSides+1, MAX_SIDES)
            if(sides > field) sides = field
        }

    internal var sides = minSides
        set(value) {
            field = value.coerceIn(minSides..maxSides)
            recreatePolygonPath()
            updatePaintPathEffect()
            //if(notCurrentlyRounding()) setPaintEffectToTraceEffect()
            invalidate() // invalidate the view after any change to its properties that might change its appearance
            //requestLayout() // request a new layout when a property change might affect the size or shape of the view
        }

    internal var roundedPercent: Float = 0f
        set(value) {
            field = value.coerceIn(0f, 1f)
            setPaintEffectToCornerPathEffect()
            invalidate()
        }

    internal var tracePercent: Float = 1f
        set(value) {
            field = value.coerceIn(0f, 1f)
            setPaintEffectToTraceEffect()
            invalidate()
        }

    private var diameter: Float = 0f
    private var centrePoint = Point(0,0)
    private var polygonPath = Path()

    private val polygonPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 16f
        color = PURPLE
        //pathEffect = CornerPathEffect(roundedPercent*(diameter/2))
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //Log.d("PolygonView", "onMeasure()")
        // Try for a width based on our minimum
        val minWidth: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        // resolveSizeAndState returns a View.MeasureSpec value (is a compound int)
        val w: Int = View.resolveSizeAndState(minWidth, widthMeasureSpec, 0)

        // Whatever the width ends up being, ask for a height that would let the polygon get as big as it can
        val minHeight: Int =  View.MeasureSpec.getSize(w) + (paddingBottom + paddingTop) - (paddingLeft + paddingRight)
        val h: Int = View.resolveSizeAndState(minHeight, heightMeasureSpec, 0)

        setMeasuredDimension(w, h)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        //Log.d("PolygonView", "onSizeChanged()")
        val xPad = paddingLeft + paddingRight
        val yPad = paddingTop + paddingBottom

        val width = w - xPad
        val height = h - yPad

        diameter = min(width, height).toFloat()
        centrePoint.x = w/2
        centrePoint.y = h/2

        recreatePolygonPath()
        //setPaintEffectToCornerPathEffect()
    }

    override fun onDraw(canvas: Canvas) {
        //Log.d("PolygonView", "onDraw()")
        canvas.apply {
            drawPath(polygonPath, polygonPaint)
        }
    }

    private fun recreatePolygonPath() {
        polygonPath = createPolygonPath(
            center = centrePoint,
            sides = sides,
            radius = diameter / 2
        )
    }

    private fun updatePaintPathEffect() {
        if(notCurrentlyRounding() && !traceEffectIsMaxed()) setPaintEffectToTraceEffect()
        else if(notCurrentlyRounding()) polygonPaint.pathEffect = null
    }

    private fun setPaintEffectToCornerPathEffect() {
        polygonPaint.pathEffect = CornerPathEffect(roundedPercent*(diameter/2))
    }

    private fun notCurrentlyRounding(): Boolean {
        return (roundedPercent*100).toInt() == PolygonPicker.ROUNDING_SEEKBAR_RESET_VALUE
        //return (tracePercent*100).toInt() < 100
    }

    private fun setPaintEffectToTraceEffect() {
        polygonPaint.pathEffect =
                createTracePathEffectFromPath(
                    polygonPath,
                    1 - tracePercent
                )
    }

    private fun traceEffectIsMaxed(): Boolean {
        return (tracePercent*100).toInt() == 100
    }

    companion object {
        internal const val MIN_SIDES = 3
        internal const val MAX_SIDES = 100

        internal const val DEFAULT_MIN_SIDES = 3
        internal const val DEFAULT_MAX_SIDES = 15
    }
}
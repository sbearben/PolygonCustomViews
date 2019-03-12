package uk.co.victoriajanedavis.polygoncustomviews.concentricPolygonView

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import uk.co.victoriajanedavis.polygoncustomviews.polygonPicker.Polygon
import uk.co.victoriajanedavis.polygoncustomviews.polygonPicker.createPolygonPath
import uk.co.victoriajanedavis.polygoncustomviews.polygonPicker.createTracePathEffectFromPath2
import kotlin.math.PI

internal class ConcentricPolygonView : View {
    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    init {
        setOnClickListener { if (!animator.isStarted) animator.start() }
    }

    private val animator = ObjectAnimator.ofFloat(this, "tracePercent", 0f, 1f).apply {
        duration = 4000L
        interpolator = LinearInterpolator()
        repeatCount = 0
        repeatMode = ValueAnimator.RESTART
    }

    private val polygons = listOf(
        Polygon(sides = 3, radius = 77f, color = 0xffe84c65.toInt()),
        Polygon(sides = 4, radius = 88f, color = 0xffe79442.toInt()),
        Polygon(sides = 5, radius = 104f, color = 0xffefefbb.toInt()),
        Polygon(sides = 6, radius = 123f, color = 0xff9cd757.toInt()),
        Polygon(sides = 7, radius = 147f, color = 0xff4ace4b.toInt()),
        Polygon(sides = 8, radius = 175f, color = 0xff31ce81.toInt()),
        Polygon(sides = 9, radius = 203f, color = 0xff57dde6.toInt()),
        Polygon(sides = 10, radius = 231f, color = 0xff317ee2.toInt()),
        Polygon(sides = 11, radius = 259f, color = 0xff3a3ce1.toInt()),
        Polygon(sides = 12, radius = 287f, color = 0xff9e67e7.toInt()),
        Polygon(sides = 13, radius = 315f, color = 0xffce52ce.toInt()),
        Polygon(sides = 14, radius = 343f, color = 0xffe84c65.toInt()),
        Polygon(sides = 15, radius = 371f, color = 0xffd54e58.toInt())
    )

    private val polygonPaths: MutableList<Path> = mutableListOf()

    private val polygonPaints: List<Paint> = List(polygons.size) { _ ->
        Paint(ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 4f
        }
    }

    private var tracePercent: Float = 1f
        set(value) {
            field = value.coerceIn(0f, 1f)
            updateTraceEffect()
            invalidate()
        }

    private var centrePoint = Point(0,0)


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minWidth: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val suggestedWidthSpec: Int = View.resolveSizeAndState(minWidth, widthMeasureSpec, 0)

        val minHeight: Int =
            View.MeasureSpec.getSize(suggestedWidthSpec) + (paddingBottom + paddingTop) - (paddingLeft + paddingRight)
        val suggestedHeightSpec: Int = View.resolveSizeAndState(minHeight, heightMeasureSpec, 0)

        setMeasuredDimension(suggestedWidthSpec, suggestedHeightSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        centrePoint.x = w/2
        centrePoint.y = h/2

        recreatePolygonPaths()
    }

    override fun onDraw(canvas: Canvas) {
        for(i in 0 until polygons.size) {
            polygonPaints[i].color = polygons[i].color
            canvas.drawPath(polygonPaths[i], polygonPaints[i])
        }
    }

    private fun recreatePolygonPaths() {
        polygonPaths.clear()

        polygons.forEach { polygon ->
            polygonPaths.add(createPolygonPath(
                center = centrePoint,
                sides = polygon.sides,
                radius = polygon.radius,
                startingAngle = (PI/2).toFloat() + (PI/polygon.sides).toFloat() //(PI*(2+polygons[i].sides)/(2*polygons[i].sides)).toFloat()
            ))
        }
    }

    private fun updateTraceEffect() {
        polygonPaints.forEachIndexed { index, paint ->
            paint.pathEffect = createTracePathEffectFromPath2(
                polygonPaths[index],
                1 - tracePercent
            )
        }
    }
}
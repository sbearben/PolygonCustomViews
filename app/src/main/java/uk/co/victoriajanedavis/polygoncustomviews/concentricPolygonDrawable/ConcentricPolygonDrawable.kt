package uk.co.victoriajanedavis.polygoncustomviews.concentricPolygonDrawable

import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.drawable.Drawable

internal class ConcentricPolygonDrawable : Drawable() {

    private val polygons = listOf(
        Polygon(sides = 3, radius = 90f, color = 0xffe84c65.toInt(), laps = 14),
        Polygon(sides = 4, radius = 106f, color = 0xffe79442.toInt(), laps = 13),
        Polygon(sides = 5, radius = 128f, color = 0xffefefbb.toInt(), laps = 12),
        Polygon(sides = 6, radius = 148f, color = 0xff9cd757.toInt(), laps = 11),
        Polygon(sides = 7, radius = 172f, color = 0xff4ace4b.toInt(), laps = 10),
        Polygon(sides = 8, radius = 196f, color = 0xff31ce81.toInt(), laps = 9),
        Polygon(sides = 9, radius = 220f, color = 0xff57dde6.toInt(), laps = 8),
        Polygon(sides = 10, radius = 244f, color = 0xff317ee2.toInt(), laps = 7),
        Polygon(sides = 11, radius = 268f, color = 0xff3a3ce1.toInt(), laps = 6),
        Polygon(sides = 12, radius = 292f, color = 0xff9e67e7.toInt(), laps = 5),
        Polygon(sides = 13, radius = 314f, color = 0xffce52ce.toInt(), laps = 4),
        Polygon(sides = 14, radius = 338f, color = 0xffe84c65.toInt(), laps = 3),
        Polygon(sides = 15, radius = 362f, color = 0xffd54e58.toInt(), laps = 2)
    )

    private var progress: Float = 1f
        set(value) {
            field = value.coerceIn(0f, 1f)
            callback?.invalidateDrawable(this)
        }

    private var dotProgress = 0f
        set(value) {
            field = value.coerceIn(0f, 1f)
            callback?.invalidateDrawable(this)
        }

    private val cornerEffect = CornerPathEffect(8f)

    private val linePaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 4f
        pathEffect = cornerEffect
    }

    private val dotPaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = 0xff0e0d0e.toInt()
        style = Paint.Style.FILL
    }

    private val dotPath = Path().apply {
        addCircle(0f, 0f, 8f, Path.Direction.CW)
    }

    private var centrePoint = Point(0,0)

    override fun onBoundsChange(bounds: Rect) {
        centrePoint.x = bounds.centerX()
        centrePoint.y = bounds.centerY()
    }

    override fun draw(canvas: Canvas) {
        polygons.forEach { polygon ->
            linePaint.color = polygon.color
            if (progress < 1f) {
                val progressEffect =
                    createDashPathEffectFromPolygon(
                        polygon,
                        progress
                    )
                linePaint.pathEffect = ComposePathEffect(progressEffect, cornerEffect)
            }
            canvas.drawPath(polygon.path, linePaint)
        }

        // loop separately to ensure the dots are on top
        polygons.forEach { polygon ->
            dotPaint.pathEffect =
                    createPathDashPathEffectFromPolygonAndDotPath(
                        polygon,
                        dotPath,
                        dotProgress
                    )
            canvas.drawPath(polygon.path, dotPaint)
        }
    }

    override fun setAlpha(alpha: Int) {
        linePaint.alpha = alpha
        dotPaint.alpha = alpha
    }

    override fun getOpacity() = PixelFormat.TRANSLUCENT

    override fun setColorFilter(colorFilter: ColorFilter) {
        linePaint.colorFilter = colorFilter
        dotPaint.colorFilter = colorFilter
    }

    override fun getIntrinsicWidth(): Int {
        return width
    }

    override fun getIntrinsicHeight(): Int {
        return height
    }

    companion object {
        private const val width = 1080
        private const val height = 1080
        internal const val cx = (width / 2).toFloat()
        internal const val cy = (height / 2).toFloat()
        //private val pathMeasure = PathMeasure()
    }
}
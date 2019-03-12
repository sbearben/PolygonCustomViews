package uk.co.victoriajanedavis.polygoncustomviews.concentricPolygonDrawable

import android.graphics.Path
import android.graphics.PathMeasure
import uk.co.victoriajanedavis.polygoncustomviews.concentricPolygonDrawable.ConcentricPolygonDrawable.Companion.cx
import uk.co.victoriajanedavis.polygoncustomviews.concentricPolygonDrawable.ConcentricPolygonDrawable.Companion.cy
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class Polygon(
    val sides: Int,
    val color: Int,
    radius: Float,
    val laps: Int
) {
    val path = createPath(sides, radius)

    val length: Float by lazy(LazyThreadSafetyMode.NONE) {
        with(PathMeasure()) {
            setPath(path, false)
            length
        }
    }

    val initialPhase by lazy(LazyThreadSafetyMode.NONE) {
        (1f - (1f / (2 * sides))) * length
    }

    private fun createPath(sides: Int, radius: Float): Path {
        return Path().apply {
            val angle = 2.0 * PI / sides
            val startingAngle = PI/2f + Math.toRadians(360.0/(2*sides))  //(PI/2).toFloat() + (PI/sides).toFloat()

            moveTo(
                cx + (radius * cos(startingAngle)).toFloat(),
                cy + (radius * sin(startingAngle)).toFloat()
            )
            for(i in 1 until sides) {
                lineTo(
                    cx + (radius * cos(angle*i + startingAngle)).toFloat(),
                    cy + (radius * sin(angle*i + startingAngle)).toFloat()
                )
            }
            close()
        }
    }
}
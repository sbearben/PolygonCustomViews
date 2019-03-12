package uk.co.victoriajanedavis.polygoncustomviews.polygonPicker

import android.graphics.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


private const val STARTING_RADIANS = (-PI/2).toFloat()

// startingAngle is in radians
fun createPolygonPath(center: Point, sides: Int, radius: Float, startingAngle: Float = STARTING_RADIANS): Path {
    return Path().apply {
        val angle = 2.0 * PI / sides

        moveTo(
            center.x + (radius * cos(startingAngle)).toFloat(),
            center.y + (radius * sin(startingAngle)).toFloat()
        )

        for(i in 1..sides) {
            lineTo(
                center.x + (radius * cos(angle*i + startingAngle)).toFloat(),
                center.y + (radius * sin(angle*i + startingAngle)).toFloat()
            )
        }
        close()
    }
}

fun createTracePathEffectFromPath(originalPath: Path, blankPercent: Float): PathEffect {
    val measure = PathMeasure(originalPath, false)
    val fullLength = measure.length

    return DashPathEffect(floatArrayOf(fullLength, fullLength), blankPercent*fullLength)
}

fun createTracePathEffectFromPath2(originalPath: Path, blankPercent: Float): PathEffect {
    val measure = PathMeasure(originalPath, false)
    val fullLength = measure.length

    return DashPathEffect(floatArrayOf(blankPercent*fullLength, blankPercent*fullLength), fullLength)
}




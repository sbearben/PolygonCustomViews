package uk.co.victoriajanedavis.polygoncustomviews.concentricPolygonDrawable

import android.graphics.*


fun createDashPathEffectFromPolygon(polygon: Polygon, progressPercent: Float): PathEffect {
    return DashPathEffect(
        floatArrayOf(0f, (1f - progressPercent) * polygon.length, progressPercent * polygon.length, 0f),
        polygon.initialPhase
    )
}

fun createPathDashPathEffectFromPolygonAndDotPath(polygon: Polygon, dotPath: Path, dotProgress: Float): PathEffect {
    val phase = polygon.initialPhase + dotProgress * polygon.length * polygon.laps
    return PathDashPathEffect(dotPath, polygon.length, phase, PathDashPathEffect.Style.TRANSLATE)
}
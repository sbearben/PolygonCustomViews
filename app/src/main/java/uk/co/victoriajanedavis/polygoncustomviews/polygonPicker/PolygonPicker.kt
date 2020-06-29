package uk.co.victoriajanedavis.polygoncustomviews.polygonPicker

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.RelativeLayout
import android.widget.SeekBar
import com.facebook.react.ReactRootView
import kotlinx.android.synthetic.main.polygon_picker.view.*
import uk.co.victoriajanedavis.polygoncustomviews.MainActivity
import uk.co.victoriajanedavis.polygoncustomviews.PolygonCustomViewsApp
import uk.co.victoriajanedavis.polygoncustomviews.R

class PolygonPicker : RelativeLayout {
    constructor(context: Context): this(context, null)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int): super(context, attrs, defStyle) {
        (context as Activity).layoutInflater
            .inflate(R.layout.polygon_picker, this, true)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PolygonPicker,
            0, 0).apply {
            try {
                val minSides = getInteger(
                    R.styleable.PolygonPicker_minSides,
                    PolygonView.DEFAULT_MIN_SIDES
                )
                val maxSides = getInteger(
                    R.styleable.PolygonPicker_maxSides,
                    PolygonView.DEFAULT_MAX_SIDES
                )

                assert(maxSides > minSides) { "\'maxSides\' must be greater than or equal to \'minSides\'" }
                assert(minSides in PolygonView.MIN_SIDES..PolygonView.MAX_SIDES) { "\'minSides\' must be at least 3" }
                assert(maxSides in PolygonView.MIN_SIDES..PolygonView.MAX_SIDES) { "\'maxSides\' must be less than or equal to 100" }

                polygonView.minSides = minSides
                polygonView.maxSides = maxSides

                sidesTitledSeekBar.max = polygonView.maxSides - polygonView.minSides
                roundingTitledSeekBar.max = 50
                traceTitledSeekBar.progress = traceTitledSeekBar.max
            } finally {
                recycle()
            }
        }

        sidesTitledSeekBar.setOnSeekBarChangeListener(sidesSeekBarChangeListener)
        roundingTitledSeekBar.setOnSeekBarChangeListener(roundingSeekBarChangeListener)
        traceTitledSeekBar.setOnSeekBarChangeListener(traceSeekBarChangeListener)

        val reactView = ReactRootView(context)
        (context as MainActivity)?.let {
            reactView.startReactApplication(it.instanceManager, "Widget")
        }
        addView(reactView)

        /*
        val reactView = ReactRootView(context)
        (context.application as PolygonCustomViewsApp).let {
            Log.d("PolygonRNRootView", "mainActivity is in scope")
            reactView.startReactApplication(it.mReactNativeHost.reactInstanceManager, "Widget")
        }
        Log.d("PolygonRNRootView", "trying to add root view")
        addView(reactView)
        */
    }

    private val sidesSeekBarChangeListener by lazy {
        object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                polygonView.sides = progress + polygonView.minSides
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        }
    }

    private val roundingSeekBarChangeListener by lazy {
        object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                polygonView.roundedPercent = (progress.toFloat())/seekBar.max
                traceTitledSeekBar.progress = traceTitledSeekBar.max
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        }
    }

    private val traceSeekBarChangeListener by lazy {
        object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                polygonView.tracePercent = (progress.toFloat())/seekBar.max
                roundingTitledSeekBar.progress = ROUNDING_SEEKBAR_RESET_VALUE

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        }
    }

    companion object {
        const val ROUNDING_SEEKBAR_RESET_VALUE = 0
    }
}
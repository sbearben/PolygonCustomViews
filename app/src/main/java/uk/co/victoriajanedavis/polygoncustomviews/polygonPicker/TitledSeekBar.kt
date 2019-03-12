package uk.co.victoriajanedavis.polygoncustomviews.polygonPicker

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import android.widget.SeekBar
import kotlinx.android.synthetic.main.titled_seekbar.view.*
import uk.co.victoriajanedavis.polygoncustomviews.R

class TitledSeekBar : RelativeLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    var max: Int
        get() = seekBar.max
        set(value) { seekBar.max = value }

    var progress: Int
        get() = seekBar.progress
        set(value) {
            if(value in 0..max) seekBar.progress = value
        }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int): super(context, attrs, defStyle) {
        (context as Activity).layoutInflater
            .inflate(R.layout.titled_seekbar, this, true)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.TitledSeekBar,
            0, 0).apply {
            try {
                val title = getString(R.styleable.TitledSeekBar_title) ?: DEFAULT_TITLE
                val showTitle = getBoolean(
                    R.styleable.TitledSeekBar_showTitle,
                    DEFAULT_SHOW_TITLE
                )

                titleTextView.text = title
                titleTextView.visibility = if(showTitle) View.VISIBLE else View.GONE
            } finally {
                recycle()
            }
        }
    }

    fun setOnSeekBarChangeListener(l: SeekBar.OnSeekBarChangeListener) {
        seekBar.setOnSeekBarChangeListener(l)
    }

    companion object {
        private const val DEFAULT_SHOW_TITLE = true
        private const val DEFAULT_TITLE = "Title"
    }
}
package com.github.kiolk.chemistrytests.ui.customviews

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.RelativeLayout
import com.github.kiolk.chemistrytests.R

class RoundCornerLayout : RelativeLayout {
    private var maskBitmap: Bitmap? = null
    private var paint: Paint? = null
    private var cornerRadius: Float = 0.toFloat()

    constructor(context: Context) : super(context) {
        init(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context, attrs, defStyle)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyle: Int) {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)

        setWillNotDraw(false)
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        if (maskBitmap == null) {
            // This corner radius assumes the image width == height and you want it to be circular
            // Otherwise, customize the radius as needed
            cornerRadius = (canvas.width / 20).toFloat()
            maskBitmap = createMask(canvas.width, canvas.height)
        }

        canvas.drawBitmap(maskBitmap!!, 0f, 0f, paint)
    }

    private fun createMask(width: Int, height: Int): Bitmap {
        val mask = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(mask)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

//        paint.color = Color.WHITE // TODO set your background color as needed

        //try get actual background color
        val typedValue = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(R.attr.backgroundColor, typedValue, true)
        paint.color = typedValue.data

        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        canvas.drawRoundRect(RectF(0f, 0f, width.toFloat(), height.toFloat()), cornerRadius, cornerRadius, paint)

        return mask
    }
}
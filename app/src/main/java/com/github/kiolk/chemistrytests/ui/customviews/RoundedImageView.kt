package com.github.kiolk.chemistrytests.ui.customviews

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.PorterDuff.Mode
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.utils.SomeDrawable

class RoundedImageView : ImageView {

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    var mBitmap: Bitmap? = null

    override fun setImageBitmap(bm: Bitmap?) {
//        super.setImageBitmap(bm)
        mBitmap = bm
        visibility = if (mBitmap == null) {
            View.INVISIBLE
        } else {
            View.VISIBLE
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var drawable: Drawable = drawable ?: return
        if (height == 0 || width == 0) {
            return
        }
//        val bit: Bitmap = (drawable as BitmapDrawable).bitmap
//       drawable = SomeDrawable()
//        visibility = if(mBitmap == null){
//            View.INVISIBLE
//        }else{
//            View.VISIBLE
//        }
        val bit: Bitmap = mBitmap ?: (context?.resources?.getDrawable(R.drawable.empty) as BitmapDrawable).bitmap //drawable as BitmapDrawable).bitmap
        val bitmap = bit.copy(Bitmap.Config.ARGB_8888, true)

        val bitWidth: Int = bitmap.width
        val bitHeight: Int = bitmap.height
        val rad = Math.min(bitHeight, bitWidth)
        val croppedBitmap: Bitmap = getCroppedBitmap(bitmap, rad)
        canvas?.drawBitmap(croppedBitmap, 0F, 0F, null)
//        background = context.resources.getDrawable(R.drawable.area_select_answer)
//        setImageBitmap(null)
    }

    private fun getCroppedBitmap(bitmap: Bitmap, radius: Int): Bitmap {
        val tmpBitmap: Bitmap

        if (bitmap.height != radius || bitmap.width != radius) {
            val smallest: Float = Math.min(bitmap.height, bitmap.width).toFloat()
            val factor: Float = smallest / radius
            tmpBitmap = Bitmap.createScaledBitmap(bitmap, (bitmap.width / factor).toInt(),
                    (bitmap.height / factor).toInt(), false)
        } else {
            tmpBitmap = bitmap
        }

        val outputBitmap: Bitmap = Bitmap.createBitmap(radius, radius, Bitmap.Config.ARGB_8888)
        val canvas: Canvas = Canvas(outputBitmap)

        val color: String = "#45AA66"
        val paint: Paint = Paint()
        val rect: Rect = Rect(0, 0, radius, radius)

        paint.isAntiAlias = true
        paint.isFilterBitmap = true
        paint.isDither = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = Color.parseColor(color)
        canvas.drawCircle((radius / 2).toFloat(), (radius / 2).toFloat(), radius / 2 + 0.2F, paint)
        paint.xfermode = PorterDuffXfermode(Mode.SRC_IN)
        canvas.drawBitmap(tmpBitmap, rect, rect, paint)


        return outputBitmap
    }
}

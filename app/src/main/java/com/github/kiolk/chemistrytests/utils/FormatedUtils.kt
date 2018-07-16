package com.github.kiolk.chemistrytests.utils

//import com.github.kiolk.chemistrytests.data.models.DEFAULT_RATIO
//import com.github.kiolk.chemistrytests.data.models.INCREACE_INDEX
//import com.github.kiolk.chemistrytests.data.models.RATIO_INDEX
import android.graphics.drawable.BitmapDrawable
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.widget.TextView
import com.github.kiolk.chemistrytests.utils.Constants.PHOTO_TAG
import com.github.kiolk.chemistrytests.utils.FormatedConstants.DEFAULT_RATIO
import com.github.kiolk.chemistrytests.utils.FormatedConstants.INCREACE_INDEX
import com.github.kiolk.chemistrytests.utils.FormatedConstants.RATIO_INDEX
import isPresentDrawable
import kiolk.com.github.pen.Pen
import toHtml

object FormatedConstants{
    val RATIO_INDEX: Float = 2F
    val INCREACE_INDEX: Float = 3F
    val DEFAULT_RATIO: Float = 1F
}


fun setFormattedText(view: TextView, text: String, photoUrl: String?) {
    val context = view.context
    if (photoUrl == null || !isPresentDrawable(text)) {
        view.text = Html.fromHtml(toHtml(text))
    } else {
        Pen.getInstance().getImageFromUrl(photoUrl).getBitmapDirect { pBitmapFromLoader ->
            val spannableBuilder = SpannableStringBuilder("")
            val photoHeight = pBitmapFromLoader?.height
            val photoWidth = pBitmapFromLoader?.width
            var ratio: Float = DEFAULT_RATIO

            if (photoHeight != null) {
                ratio = photoWidth?.div(photoHeight.toFloat()) ?: DEFAULT_RATIO
            }
            val padding = (view.paddingLeft + view.paddingRight).times(1)
            var height : Float = view.lineHeight.times(INCREACE_INDEX)
            var width = ratio.times(height)

            if (ratio > RATIO_INDEX) {
                width = view.width.toFloat() - padding
                height = width.div(ratio)
            }

            val drawable = BitmapDrawable(context.resources, pBitmapFromLoader)
            drawable.setBounds(0, 0, width.toInt(), height.toInt())
            val readyString = toHtml(text)
            val spannable = Html.fromHtml(readyString)
            val start = spannable.indexOf(PHOTO_TAG)
            spannableBuilder.append(Html.fromHtml(readyString))
            spannableBuilder.setSpan(ImageSpan(drawable), start, start.plus(PHOTO_TAG.length), Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            view.setText(spannableBuilder, TextView.BufferType.SPANNABLE)
        }
    }
}
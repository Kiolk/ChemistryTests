package com.github.kiolk.chemistrytests.data

import PHOTO_TAG
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.widget.TextView
import kiolk.com.github.pen.GetBitmapCallback
import kiolk.com.github.pen.Pen
import toHtml
import java.util.*

val RATIO_INDEX: Int = 2
val INCREACE_INDEX: Int = 3
val DEFAULT_RATIO: Int = 1

interface Question {

    fun checkAnswer(userAnswer: String): Boolean

}

class CloseQuestion(var questionEn: String = "",
                    var photoUrl: String? = null,
                    var option1: Option = Option(),
                    var option2: Option = Option(),
                    var option3: Option = Option(),
                    var option4: Option = Option(),
                    var option5: Option = Option(),
                    var answerNumber: Int = 1,
                    var questionType: Int = Question.SINGLE_CHOICE,
                    var tags: List<String>? = null,
                    var questionCost: Float = 1.0F,
                    var language: String = "ru") : Question {

    object Question {
        val SINGLE_CHOICE: Int = 0
        val MULTIPLE_CHOICE: Int = 1
    }

    private var answer = getOptions()[answerNumber - 1]

    fun getAnswer() = answer

    override fun checkAnswer(userAnswer: String) = userAnswer == answer.optionPhotoUtl ?: answer.text

    fun getOptions() = arrayListOf(option1, option2, option3, option4)

    fun getRandomOptions() = randomSort(getOptions())
}

fun <T> randomSort(collection: List<T>): List<T> {
    val listOfNumbers: MutableList<Int> = mutableListOf(Random().nextInt(collection.size))

    while (listOfNumbers.size != collection.size) {
        val random = Random().nextInt(collection.size)
        if (!listOfNumbers.contains(random)) {
            listOfNumbers.add(random)
        }
    }

    val resultListOption = mutableListOf<T>()
    listOfNumbers.forEach({ resultListOption.add(collection[it]) })

    return resultListOption
}

class OpenQuestion(var question: String = "",
                   var answer: String = "",
                   var tags: List<String>? = null,
                   var questionCost: Float = 1.0F) : Question {

    override fun checkAnswer(userAnswer: String) = userAnswer == answer
}

fun setFormattedText(view: TextView, text: String, photoUrl: String?) {
    val context = view.context
    if (photoUrl == null) {
        view.text = toHtml(text)
    } else {
        Pen.getInstance().getImageFromUrl(photoUrl).getBitmapDirect { pBitmapFromLoader ->
            val spannableBuilder = SpannableStringBuilder("")
            val photoHeight = pBitmapFromLoader?.height
            val photoWidth = pBitmapFromLoader?.width
            var ratio: Int = DEFAULT_RATIO

            if (photoHeight != null) {
                ratio = photoWidth?.div(photoHeight) ?: DEFAULT_RATIO
            }

            var height = view.lineHeight.times(INCREACE_INDEX)
            var width = ratio.times(height)

            if (ratio > RATIO_INDEX) {
                width = view.width
                height = width.div(ratio)
            }

            val drawable = BitmapDrawable(context.resources, pBitmapFromLoader)
            drawable.setBounds(0, 0, width, height)
            val readyString = toHtml(text)
            val spannable = Html.fromHtml(readyString)
            val start = spannable.indexOf(PHOTO_TAG)
            spannableBuilder.append(Html.fromHtml(readyString))
            spannableBuilder.setSpan(ImageSpan(drawable), start, start.plus(PHOTO_TAG.length), Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            view.setText(spannableBuilder, TextView.BufferType.SPANNABLE)
        }
    }
}





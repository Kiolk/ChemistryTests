package com.github.kiolk.chemistrytests.utils

import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.github.kiolk.chemistrytests.R

object SlideAnimationUtil {

    val FASTER: Long = 500
    val SLOWLY: Long = 1500
    val NORMAL: Long = 1000

    fun slideInFromLeft(pContext: Context, pView: View, pSlideAnimationListener: SlideAnimationListener?, pDuration: Long) {
        runSimpleAnimation(pContext, pView, R.anim.slide_from_left, pSlideAnimationListener, pDuration)
    }

    fun slideOutToLeft(pContext: Context, pView: View, pSlideAnimationListener: SlideAnimationListener?, pDuration: Long) {
        runSimpleAnimation(pContext, pView, R.anim.slide_to_left, pSlideAnimationListener, pDuration)
    }

    fun slideInFromRight(pContext: Context, pView: View, pSlideAnimationListener: SlideAnimationListener?, pDuration: Long) {
        runSimpleAnimation(pContext, pView, R.anim.slide_from_right, pSlideAnimationListener, pDuration)
    }

    fun slideOutToRight(pContext: Context, pView: View, pSlideAnimationListener: SlideAnimationListener?, pDuration: Long) {
        runSimpleAnimation(pContext, pView, R.anim.slide_to_right, pSlideAnimationListener, pDuration)
    }

    fun slideInToTop(pContext: Context, pView: View, pSlideAnimationListener: SlideAnimationListener?, pDuration: Long) {
        runSimpleAnimation(pContext, pView, R.anim.slide_to_top, pSlideAnimationListener, pDuration)
    }

    fun slideOutToTop(pContext: Context, pView: View, pSlideAnimationListener: SlideAnimationListener?, pDuration: Long) {
        runSimpleAnimation(pContext, pView, R.anim.slide_from_top, pSlideAnimationListener, pDuration)
    }

    fun slideInFromTop(pContext: Context, pView: View, pSlideAnimationListener: SlideAnimationListener?, pDuration: Long) {
        runSimpleAnimation(pContext, pView, R.anim.slide_to_bottom, pSlideAnimationListener, pDuration)
    }

    fun slideOutFromTop(pContext: Context, pView: View, pSlideAnimationListener: SlideAnimationListener?, pDuration: Long) {
        runSimpleAnimation(pContext, pView, R.anim.slide_from_bottom, pSlideAnimationListener, pDuration)
    }

    private fun runSimpleAnimation(pContext: Context, pView: View,
                                   pAnimResources: Int,
                                   pListener: SlideAnimationListener?,
                                   pDuration: Long) {
        val animation = AnimationUtils.loadAnimation(pContext, pAnimResources)

        if (pListener != null) {
            val animationListener = object : Animation.AnimationListener {

                override fun onAnimationStart(pAnimation: Animation) {}

                override fun onAnimationEnd(pAnimation: Animation) {
                    pListener.animationEnd()
                }

                override fun onAnimationRepeat(pAnimation: Animation) {}
            }
            animation.setAnimationListener(animationListener)
        }

        animation.duration = pDuration
        pView.startAnimation(animation)
    }

    interface SlideAnimationListener {

        fun animationEnd()
    }
}
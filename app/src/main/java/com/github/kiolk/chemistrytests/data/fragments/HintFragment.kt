package com.github.kiolk.chemistrytests.data.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.adapters.HintDottedPagerAdapter
import com.github.kiolk.chemistrytests.data.adapters.InfinityPagerAdapter
import com.github.kiolk.chemistrytests.data.models.Hint
import com.github.kiolk.chemistrytests.data.models.setFormattedText
import com.github.kiolk.chemistrytests.ui.activities.UserTouchListener

class HintFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_hint, null)
        return view ?: super.onCreateView(inflater, container, savedInstanceState)
    }

    fun showHint(hint: List<Hint>?) {
        if (hint != null) {
            view?.findViewById<TextView>(R.id.hint_fragment_text_view)?.let { setFormattedText(it, hint[0].text, hint[0].images?.get(0)) }
            val viewPager: ViewPager? = view?.findViewById(R.id.hint_view_pager)
            val adapter: HintDottedPagerAdapter? = context?.let { HintDottedPagerAdapter(it, hint) }
            viewPager?.adapter = adapter
            val dottedIndicatorLayout: LinearLayout? = view?.findViewById(R.id.dot_indicator_linear_layout)
            val dotCount: Int = adapter?.count ?: 0
            attachRoundIndicators(context, dottedIndicatorLayout, viewPager, dotCount)
//            val imageViewDots: List<ImageView> = List<ImageView>(dotCount) {
//                val image: ImageView = ImageView(context)
//                image.setImageDrawable(context?.resources?.getDrawable(R.drawable.non_active_dot))
//                val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
//                params.setMargins(8, 8, 8, 8)
//                params.gravity = Gravity.CENTER
//                image.layoutParams = params
//                dottedIndicatorLayout?.addView(image)
//                image
//            }
//            imageViewDots[0].setImageDrawable(context?.resources?.getDrawable(R.drawable.active_dot))
//            val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
//            params.setMargins(4, 4, 4, 4)
//            imageViewDots[0].layoutParams = params
//            params.gravity = Gravity.CENTER
//            viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
//                override fun onPageScrollStateChanged(state: Int) {
//                }
//
//                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//
//                }
//
//                override fun onPageSelected(position: Int) {
//                    val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
//                    imageViewDots.forEach {
//                        it.setImageDrawable(context?.resources?.getDrawable(R.drawable.non_active_dot))
//                        params.setMargins(8, 8, 8, 8)
//                        params.gravity = Gravity.CENTER
//                        it.layoutParams = params
//                    }
//                    imageViewDots[position].setImageDrawable(context?.resources?.getDrawable(R.drawable.active_dot))
//                    params.setMargins(4, 4, 4, 4)
//                    params.gravity = Gravity.CENTER
//                    imageViewDots[position].layoutParams = params
//                }
//            })

        }
    }


}

    fun attachRoundIndicators(context : Context?, dottedIndicatorLayout: LinearLayout?, viewPager: ViewPager?, dotCount: Int, listener : UserTouchListener? = null) {
        val imageViewDots: List<ImageView> = List<ImageView>(dotCount) {
            val image: ImageView = ImageView(context)
            image.setImageDrawable(context?.resources?.getDrawable(R.drawable.non_active_dot))
            val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.setMargins(8, 8, 8, 8)
            params.gravity = Gravity.CENTER
            image.layoutParams = params
            dottedIndicatorLayout?.addView(image)
            image
        }
        imageViewDots[0].setImageDrawable(context?.resources?.getDrawable(R.drawable.active_dot))
        val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        params.setMargins(4, 4, 4, 4)
        imageViewDots[0].layoutParams = params
        params.gravity = Gravity.CENTER
        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                var realPosition = position
                if(viewPager.adapter is InfinityPagerAdapter){
                    val infAdapter = viewPager.adapter as InfinityPagerAdapter
//                    realPosition = position % infAdapter.realCount()
                    realPosition = viewPager.currentItem
                }
                val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                imageViewDots.forEach {
                    it.setImageDrawable(context?.resources?.getDrawable(R.drawable.non_active_dot))
                    params.setMargins(8, 8, 8, 8)
                    params.gravity = Gravity.CENTER
                    it.layoutParams = params
                }
                listener?.userTouch(realPosition)
                imageViewDots[realPosition].setImageDrawable(context?.resources?.getDrawable(R.drawable.active_dot))
                params.setMargins(4, 4, 4, 4)
                params.gravity = Gravity.CENTER
                imageViewDots[realPosition].layoutParams = params
            }
        })
}
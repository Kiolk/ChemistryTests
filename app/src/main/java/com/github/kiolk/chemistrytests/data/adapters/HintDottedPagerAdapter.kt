package com.github.kiolk.chemistrytests.data.adapters

import android.app.Service
import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.models.Hint
import com.github.kiolk.chemistrytests.data.models.setFormattedText

class HintDottedPagerAdapter(var contex : Context, var hintList : List<Hint>) : PagerAdapter(){
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
    return view == `object`
    }

    override fun getCount(): Int {
        return hintList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = contex.getSystemService(Service.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view : View = layoutInflater.inflate(R.layout.layout_item_hint, null)
        val hintView : TextView = view.findViewById(R.id.hint_fragment_text_view)
        val imageUrl : String? = hintList[position].images?.get(0)
        setFormattedText(hintView, hintList[position].text, imageUrl)

        val viewPager : ViewPager = container as ViewPager
        viewPager.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val viewPager : ViewPager = container as ViewPager
        val view : View = `object` as View
        viewPager.removeView(view)
    }
}
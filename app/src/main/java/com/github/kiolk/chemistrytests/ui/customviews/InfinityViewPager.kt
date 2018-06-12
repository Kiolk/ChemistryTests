package com.github.kiolk.chemistrytests.ui.customviews

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import com.github.kiolk.chemistrytests.data.adapters.InfinityPagerAdapter


class InfinityViewPager(context: Context, set : AttributeSet? = null) : ViewPager(context, set) {

    override fun setAdapter(adapter: PagerAdapter?) {
        super.setAdapter(adapter)
        setCurrentItem(0)
    }

    override fun setCurrentItem(item: Int) {
//        super.setCurrentItem(item)
        setCurrentItem(item, false)
    }

//    override fun setCurrentItem(item: Int, smoothScroll: Boolean) {
//        if(adapter?.count == 0){
//            super.setCurrentItem(item, smoothScroll)
//            return
//        }
//        val count : Int = adapter?.count ?: 1
//        val real : Int = item % count
//        val itemVirtual = getOffsetAmount() + (item % count)
//        super.setCurrentItem(itemVirtual, smoothScroll)
//    }

    override fun setCurrentItem(item: Int, smoothScroll: Boolean) {
        var item = item
        if (adapter?.getCount() == 0) {
            super.setCurrentItem(item, smoothScroll)
            return
        }
        item = getOffsetAmount() + item % adapter?.getCount()!!
        super.setCurrentItem(item, smoothScroll)
    }

    override fun getCurrentItem(): Int {
        if(adapter?.count == 0){
            return super.getCurrentItem()
        }
        if(adapter is InfinityPagerAdapter){
            val position = super.getCurrentItem()
            val infinityAdapter = adapter as InfinityPagerAdapter
            return (position % infinityAdapter.realCount())
        }else{
            return super.getCurrentItem()
        }
    }

    private fun getOffsetAmount() : Int {
        if(adapter?.count == 0){
            return 0
        }
        if(adapter is InfinityPagerAdapter){
            val adapter = adapter as InfinityPagerAdapter
            return adapter.realCount() * 100
        }else{
            return 0
        }
    }

}

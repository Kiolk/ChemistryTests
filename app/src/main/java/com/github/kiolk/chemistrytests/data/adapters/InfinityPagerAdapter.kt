package com.github.kiolk.chemistrytests.data.adapters

import android.database.DataSetObserver
import android.os.Parcelable
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup

class InfinityPagerAdapter(var pageAdapter : PagerAdapter) : PagerAdapter(){

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return pageAdapter.isViewFromObject(view,  `object`)
    }

    override fun getCount(): Int {
        return if(realCount() == 0){
            0
        }else{
            Int.MAX_VALUE
        }
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val virtualPoint = position % realCount()

        return pageAdapter.instantiateItem(container, virtualPoint)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val virtualPoint = position % realCount()
        pageAdapter.destroyItem(container, virtualPoint, `object`)
    }

    fun realCount(): Int {
       return pageAdapter.count
    }

    override fun finishUpdate(container: ViewGroup) {
        pageAdapter.finishUpdate(container)
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {
        pageAdapter.restoreState(state, loader)
    }

    override fun saveState(): Parcelable? {
        return pageAdapter.saveState()
    }

    override fun startUpdate(container: ViewGroup) {
        pageAdapter.startUpdate(container)
    }

    override fun getPageWidth(position: Int): Float {
        return pageAdapter.getPageWidth(position)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val virtualPoint = position % realCount()
        return super.getPageTitle(virtualPoint)
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        pageAdapter.setPrimaryItem(container, position, `object`)
    }

    override fun unregisterDataSetObserver(observer: DataSetObserver) {
        pageAdapter.unregisterDataSetObserver(observer)
    }

    override fun registerDataSetObserver(observer: DataSetObserver) {
        pageAdapter.registerDataSetObserver(observer)
    }

    override fun notifyDataSetChanged() {
        pageAdapter.notifyDataSetChanged()
    }

    override fun getItemPosition(`object`: Any): Int {
        return pageAdapter.getItemPosition(`object`)
    }
}
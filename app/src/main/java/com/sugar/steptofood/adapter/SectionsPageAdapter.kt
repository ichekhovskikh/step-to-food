package com.sugar.steptofood.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter

class SectionsPageAdapter(private val mFragmentManager: FragmentManager) : FragmentPagerAdapter(mFragmentManager) {

    private val fragments = ArrayList<Fragment>()
    private val fragmentTitles = ArrayList<String>()

    fun addFragment(fragment: Fragment, title: String) {
        fragments.add(fragment)
        fragmentTitles.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitles[position]
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    fun replace(currentFragment: Fragment, newFragment: Fragment) {
        val position = fragments.indexOf(currentFragment)
        replace(position, newFragment)
    }

    fun replace(position: Int, newFragment: Fragment) {
        val transaction = mFragmentManager.beginTransaction()
        transaction.remove(fragments[position]).commitNow()
        fragments[position] = newFragment
        notifyDataSetChanged()
    }

    override fun getItemPosition(obj: Any): Int {
        return PagerAdapter.POSITION_NONE
    }
}
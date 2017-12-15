package vn.asiantech.way.ui.profileanko

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import java.util.*

/**
 * Copyright © 2017 AsianTech inc.
 * Created by vinh.huynh on 12/15/17.
 */
class MyViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    private val mFragmentList = ArrayList<Fragment>()
    private val mmFragmentTitleList = ArrayList<String>()

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    fun addFrag(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mmFragmentTitleList.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mmFragmentTitleList[position]
    }
}

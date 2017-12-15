package vn.asiantech.way.ui.profile;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright © 2017 AsianTech inc.
 * Created by vinh.huynh on 12/15/17.
 */
class MyViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mmFragmentTitleList = new ArrayList<>();

    public MyViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFrag(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mmFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mmFragmentTitleList.get(position);
    }
}

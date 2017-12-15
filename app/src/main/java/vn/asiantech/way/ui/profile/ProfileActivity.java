package vn.asiantech.way.ui.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import vn.asiantech.way.R;

/**
 * Copyright © 2017 AsianTech inc.
 * Created by vinh.huynh on 12/15/17.
 */
public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        TabLayout tabLayout = findViewById(R.id.htab_tabs);
        ViewPager viewPager = findViewById(R.id.htab_viewpager);
        tabLayout.setupWithViewPager(viewPager);

        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new DummyFragment(), "FIRST");
        adapter.addFrag(new DummyFragment(), "SECOND");
        adapter.addFrag(new DummyFragment(), "THRID");
        viewPager.setAdapter(adapter);
    }
}